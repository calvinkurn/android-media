package com.tokopedia.mediauploader.common.compressor

import android.content.Context
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.mediauploader.common.compressor.data.Configuration
import com.tokopedia.mediauploader.common.compressor.data.Result
import com.tokopedia.mediauploader.common.compressor.video.InputSurface
import com.tokopedia.mediauploader.common.compressor.video.OutputSurface
import com.tokopedia.mediauploader.common.compressor.video.mp4.MP4Builder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.ByteBuffer

object Compressor {

    private const val VIDEO_MIME_TYPE_PREFIX = "video/"
    private const val AUDIO_MIME_TYPE_PREFIX = "audio/"

    // H.264 Advanced Video Coding
    private const val DEFAULT_MIME_TYPE = "video/avc"

    private const val MEDIACODEC_TIMEOUT_INTERVAL = 100L
    private const val DEFAULT_VIDEO_TRACK_INDEX = -5

    private const val ROTATION_DEGREE_270 = 270
    private const val ROTATION_DEGREE_180 = 180
    private const val ROTATION_DEGREE_90 = 90

    private const val BITRATE_BPS = 1_000_000
    private const val DURATION_SEC = 1000
    private const val MAX_AUDIO_BUFFER_THRESHOLD = 64
    private const val MAX_AUDIO_BUFFER_SIZE = 1024

    private var isRunning = true

    suspend fun compressVideo(
        context: Context,
        srcUri: Uri,
        destination: String,
        configuration: Configuration,
        listener: CompressionProgressListener,
    ): Result = withContext(Dispatchers.Default) {
        val extractor = MediaExtractor()

        // Retrieve the source's metadata to be used as input to generate new values for compression
        val metadata = MediaMetadataRetriever()

        try {
            metadata.setDataSource(context, srcUri)
        } catch (exception: IllegalArgumentException) {
            return@withContext Result(
                success = false,
                failureMessage = "${exception.message}"
            )
        }

        runCatching {
            extractor.setDataSource(context, srcUri, null)
        }

        val rotationData = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
        val bitrateData = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
        val durationData = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

        if (rotationData.isNullOrEmpty() || bitrateData.isNullOrEmpty() || durationData.isNullOrEmpty()) {
            return@withContext Result(
                success = false,
                failureMessage = "Failed to extract video meta-data, please try again"
            )
        }

        var rotation = rotationData.toInt()

        // Convert to millis
        val duration = durationData.toLong() * DURATION_SEC

        // Handle new bitrate value
        val newBitrate = configuration.videoBitrate ?: BITRATE_BPS

        // Handle new width and height values
        var (newWidth, newHeight) = Pair(
            configuration.videoWidth?.toInt(),
            configuration.videoHeight?.toInt()
        )

        // Handle rotation values and swapping height and width if needed
        rotation = when (rotation) {
            ROTATION_DEGREE_90, ROTATION_DEGREE_270 -> {
                val tempHeight = newHeight
                newHeight = newWidth
                newWidth = tempHeight
                Int.ZERO
            }

            ROTATION_DEGREE_180 -> Int.ZERO
            else -> rotation
        }

        val info = CompressorInfo(
            newWidth!!,
            newHeight!!,
            destination,
            newBitrate,
            duration,
            rotation
        )

        return@withContext start(info, extractor, listener)
    }

    @Suppress("DEPRECATION")
    private fun start(
        info: CompressorInfo,
        extractor: MediaExtractor,
        listener: CompressionProgressListener,
    ): Result {
        if (info.newWidth.isMoreThanZero() && info.newHeight.isMoreThanZero()) {
            val cacheFile = File(info.destination)

            try {
                // MediaCodec accesses encoder and decoder components and processes the new video
                //input to generate a compressed/smaller size video
                val bufferInfo = MediaCodec.BufferInfo()

                // Setup mp4 movie
                val movie = CompressionUtils.setUpMP4Movie(info.rotation, cacheFile)

                // MediaMuxer outputs MP4 in this app
                val mediaMuxer = MP4Builder().createMovie(movie)

                // Start with video track
                val videoIndex = CompressionUtils.findTrack(extractor, VIDEO_MIME_TYPE_PREFIX)

                extractor.selectTrack(videoIndex)
                extractor.seekTo(0, MediaExtractor.SEEK_TO_PREVIOUS_SYNC)

                val inputFormat = extractor.getTrackFormat(videoIndex)

                val outputFormat = MediaFormat.createVideoFormat(
                    DEFAULT_MIME_TYPE,
                    info.newWidth,
                    info.newHeight
                )

                // Set output format
                CompressionUtils.setOutputFileParameters(inputFormat, outputFormat, info.newBitrate)

                val decoder: MediaCodec
                val encoder = prepareEncoder(outputFormat, CompressionUtils.hasQTI())

                val inputSurface: InputSurface
                val outputSurface: OutputSurface

                try {
                    var inputDone = false
                    var outputDone = false

                    var videoTrackIndex = DEFAULT_VIDEO_TRACK_INDEX

                    inputSurface = InputSurface(encoder.createInputSurface())
                    inputSurface.makeCurrent()

                    // Move to executing state
                    encoder.start()

                    outputSurface = OutputSurface()

                    decoder = prepareDecoder(inputFormat, outputSurface)

                    // Move to executing state
                    decoder.start()

                    while (!outputDone) {
                        if (!inputDone) {
                            val index = extractor.sampleTrackIndex

                            if (index == videoIndex) {
                                val inputBufferIndex = decoder.dequeueInputBuffer(
                                    MEDIACODEC_TIMEOUT_INTERVAL
                                )

                                if (inputBufferIndex >= 0) {
                                    val inputBuffer = decoder.getInputBuffer(inputBufferIndex)
                                    val chunkSize = extractor.readSampleData(inputBuffer!!, 0)

                                    when {
                                        chunkSize.isLessThanZero() -> {
                                            decoder.safeQueueInputBuffer(
                                                bufferIndex = inputBufferIndex,
                                                flags = MediaCodec.BUFFER_FLAG_END_OF_STREAM
                                            )

                                            inputDone = true
                                        }
                                        else -> {
                                            decoder.safeQueueInputBuffer(
                                                bufferIndex = inputBufferIndex,
                                                size = chunkSize,
                                                presentationTime = extractor.sampleTime
                                            )

                                            extractor.advance()
                                        }
                                    }
                                }

                            } else if (index == -1) { // end of file
                                val inputBufferIndex =
                                    decoder.dequeueInputBuffer(MEDIACODEC_TIMEOUT_INTERVAL)

                                if (inputBufferIndex >= 0) {
                                    decoder.safeQueueInputBuffer(
                                        bufferIndex = inputBufferIndex,
                                        flags = MediaCodec.BUFFER_FLAG_END_OF_STREAM
                                    )

                                    inputDone = true
                                }
                            }
                        }

                        var decoderOutputAvailable = true

                        loop@ while (decoderOutputAvailable) {
                            if (!isRunning) {
                                dispose(
                                    videoIndex,
                                    decoder,
                                    encoder,
                                    inputSurface,
                                    outputSurface,
                                    extractor
                                )

                                return Result(
                                    success = false,
                                    failureMessage = "The compression has stopped!"
                                )
                            }

                            // Encoder
                            val encoderStatus = encoder.dequeueOutputBuffer(
                                bufferInfo,
                                MEDIACODEC_TIMEOUT_INTERVAL
                            )

                            when {
                                encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER -> Unit
                                encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED -> Unit
                                encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {
                                    val newFormat = encoder.outputFormat

                                    if (videoTrackIndex == DEFAULT_VIDEO_TRACK_INDEX) {
                                        videoTrackIndex = mediaMuxer.addTrack(newFormat, false)
                                    }
                                }

                                encoderStatus.isLessThanZero() -> {
                                    throw RuntimeException("unexpected result from encoder.dequeueOutputBuffer: $encoderStatus")
                                }

                                else -> {
                                    val encodedData = encoder.getOutputBuffer(encoderStatus)
                                        ?: throw RuntimeException("encoderOutputBuffer $encoderStatus was null")

                                    if (bufferInfo.atLeastNotEmpty()) {
                                        if ((bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG) == 0) {
                                            mediaMuxer.writeSampleData(
                                                videoTrackIndex,
                                                encodedData, bufferInfo, false
                                            )
                                        }

                                    }

                                    outputDone = bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0
                                    encoder.releaseOutputBuffer(encoderStatus, false)
                                }
                            }

                            if (encoderStatus != MediaCodec.INFO_TRY_AGAIN_LATER) continue@loop

                            //Decoder
                            val decoderStatus = decoder.dequeueOutputBuffer(bufferInfo, MEDIACODEC_TIMEOUT_INTERVAL)

                            when {
                                decoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER -> {
                                    decoderOutputAvailable = false
                                }

                                // ignore this status
                                decoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED -> {}
                                decoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {}

                                decoderStatus.isLessThanZero() -> {
                                    throw RuntimeException("unexpected result from decoder.dequeueOutputBuffer: $decoderStatus")
                                }

                                else -> {
                                    val doRender = bufferInfo.isNotZero()
                                    decoder.releaseOutputBuffer(decoderStatus, doRender)

                                    if (doRender) {
                                        var errorWait = false

                                        try {
                                            outputSurface.awaitNewImage()
                                        } catch (e: Exception) {
                                            errorWait = true
                                        }

                                        if (!errorWait) {
                                            outputSurface.drawImage()

                                            inputSurface.setPresentationTime(
                                                bufferInfo.presentationTimeAsSec()
                                            )

                                            listener.onProgressChanged(
                                                bufferInfo.presentationTimeUs.toFloat() / info.duration.toFloat() * 100
                                            )

                                            inputSurface.swapBuffers()
                                        }
                                    }

                                    if ((bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                                        decoderOutputAvailable = false
                                        encoder.signalEndOfInputStream()
                                    }
                                }
                            }
                        }
                    }

                } catch (exception: Exception) {
                    return Result(success = false, failureMessage = exception.message)
                }

                dispose(
                    videoIndex,
                    decoder,
                    encoder,
                    inputSurface,
                    outputSurface,
                    extractor
                )

                // process the audio
                processAudio(
                    muxer = mediaMuxer,
                    bufferInfo = bufferInfo,
                    extractor
                )

                // release after editing succeed
                extractor.release()

                try {
                    mediaMuxer.finishMovie()
                } catch (ignored: Exception) {}

            } catch (ignored: Exception) {}

            return Result(
                success = true,
                failureMessage = null,
                size = cacheFile.length(),
                path = cacheFile.path
            )
        }

        return Result(
            success = false,
            failureMessage = "Something went wrong, please try again"
        )
    }

    private fun processAudio(muxer: MP4Builder, bufferInfo: MediaCodec.BufferInfo, extractor: MediaExtractor) {
        val audioIndex = CompressionUtils.findTrack(extractor, AUDIO_MIME_TYPE_PREFIX)

        if (audioIndex >= 0) {
            extractor.selectTrack(audioIndex)

            val audioFormat = extractor.getTrackFormat(audioIndex)
            val muxerTrackIndex = muxer.addTrack(audioFormat, true)

            var maxBufferSize = audioFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE)
            if (maxBufferSize <= 0) maxBufferSize = MAX_AUDIO_BUFFER_THRESHOLD * MAX_AUDIO_BUFFER_SIZE

            var buffer = ByteBuffer.allocateDirect(maxBufferSize)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val size = extractor.sampleSize

                if (size > maxBufferSize) {
                    maxBufferSize = (size + MAX_AUDIO_BUFFER_SIZE).toInt()
                    buffer = ByteBuffer.allocateDirect(maxBufferSize)
                }
            }

            var inputDone = false
            extractor.seekTo(0, MediaExtractor.SEEK_TO_PREVIOUS_SYNC)

            while (!inputDone) {
                val index = extractor.sampleTrackIndex
                if (index == audioIndex) {
                    bufferInfo.size = extractor.readSampleData(buffer, 0)

                    if (bufferInfo.size >= 0) {
                        bufferInfo.apply {
                            presentationTimeUs = extractor.sampleTime
                            offset = 0
                            flags = MediaCodec.BUFFER_FLAG_KEY_FRAME
                        }
                        muxer.writeSampleData(muxerTrackIndex, buffer, bufferInfo, true)
                        extractor.advance()

                    } else {
                        bufferInfo.size = 0
                        inputDone = true
                    }
                } else if (index.isLessThanZero()) {
                    inputDone = true
                }
            }

            extractor.unselectTrack(audioIndex)
        }
    }

    private fun prepareEncoder(outputFormat: MediaFormat, hasQTI: Boolean): MediaCodec {
        val encoder = if (hasQTI) {
            MediaCodec.createByCodecName("c2.android.avc.encoder")
        } else {
            MediaCodec.createEncoderByType(DEFAULT_MIME_TYPE)
        }
        encoder.configure(
            outputFormat, null, null,
            MediaCodec.CONFIGURE_FLAG_ENCODE
        )

        return encoder
    }

    private fun prepareDecoder(inputFormat: MediaFormat, outputSurface: OutputSurface): MediaCodec {
        val decoder = MediaCodec.createDecoderByType(inputFormat.getString(MediaFormat.KEY_MIME)!!)
        decoder.configure(inputFormat, outputSurface.getSurface(), null, 0)

        return decoder
    }

    private fun dispose(
        videoIndex: Int,
        decoder: MediaCodec,
        encoder: MediaCodec,
        inputSurface: InputSurface,
        outputSurface: OutputSurface,
        extractor: MediaExtractor
    ) {
        extractor.unselectTrack(videoIndex)

        decoder.stop()
        decoder.release()

        encoder.stop()
        encoder.release()

        inputSurface.release()
        outputSurface.release()
    }

    data class CompressorInfo(
        val newWidth: Int,
        val newHeight: Int,
        val destination: String,
        val newBitrate: Int,
        val duration: Long,
        val rotation: Int
    )
}
