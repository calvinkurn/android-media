package com.tokopedia.mediauploader.video.data.repository

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.abedelazizshe.lightcompressorlibrary.CompressionProgressListener
import com.abedelazizshe.lightcompressorlibrary.compressor.Compressor
import com.abedelazizshe.lightcompressorlibrary.config.Configuration
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.loader.utils.toUri
import com.tokopedia.mediauploader.common.internal.CacheCompressionModel
import com.tokopedia.mediauploader.common.internal.VideoCompressionCacheManager
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.video.data.params.VideoCompressionParam
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import kotlin.math.min
import kotlin.math.round

interface VideoCompressionRepository {
    suspend fun compress(
        progressUploader: ProgressUploader?,
        param: VideoCompressionParam
    ): String
}

internal class VideoCompressionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cacheManager: VideoCompressionCacheManager
) : VideoCompressionRepository {

    override suspend fun compress(
        progressUploader: ProgressUploader?,
        param: VideoCompressionParam
    ): String {
        val (sourceId, originalPath, bitrate, resolution) = param

        // start track the compress time
        val startBeforeCompressionTime = System.currentTimeMillis()

        val cache = cacheManager.get(sourceId, originalPath)

        // if the same video already compressed, return the compressedVideoPath (if exist)
        if (cache != null && cache.isCompressedFileExist()) return cache.compressedVideoPath

        // get the video info of originalVideoPath, return the originalPath if couldn't get the video info
        val videoOriginalInfo = getOriginalVideoInfo(originalPath) ?: return originalPath

        // get the minimum value compare the width and height of the original video resolution
        // this minimum value will use for determine whether the compression will do or not
        val minVideoRes = min(videoOriginalInfo.width, videoOriginalInfo.height)

        // this validation comes from PRD, where:
        // 1. whether the width or height of original video should be higher than the threshold value from policy
        // 2. the bitrate of original video also should be higher than the threshold value from policy
        if (minVideoRes > resolution || videoOriginalInfo.bitrate > bitrate) {

            // generate the output video size based on original video info and expected minim resolution from policy
            val generateVideoSize = generateVideoSize(videoOriginalInfo, resolution)
            val cacheFile = compressedVideoPath(originalPath)

            val compression = Compressor
                .compressVideo(
                    index = 0,
                    context = context,
                    srcUri = Uri.parse(originalPath),
                    streamableFile = null,
                    destination = cacheFile.path,
                    configuration = Configuration(
                        videoBitrateInMbps = param.bitrate / 1000000,
                        videoHeight = generateVideoSize.height.toDouble(),
                        videoWidth = generateVideoSize.width.toDouble()
                    ),
                    listener = object : CompressionProgressListener {
                        override fun onProgressCancelled(index: Int) {}

                        override fun onProgressChanged(index: Int, percent: Float) {
                            progressUploader?.onProgress(percent.toInt(), ProgressType.Compression)
                        }
                    }
                )

            if (compression.success) {
                val endCompressionTime = System.currentTimeMillis()
                val compressedPath = compression.path ?: originalPath

                cacheManager.set(
                    sourceId = sourceId,
                    data = CacheCompressionModel(
                        originalVideoPath = originalPath,
                        compressedVideoPath = compressedPath,
                        compressedTime = endCompressionTime - startBeforeCompressionTime,
                        videoOriginalSize = File(originalPath).length().toString(),
                        videoCompressedSize = File(compressedPath).length().toString()
                    )
                )

                return compressedPath
            } else {
                // failed to compress, return the originalPath
                return originalPath
            }
        } else {
            return originalPath
        }
    }

    /**
     * This video size generator will calculate based on the video-info of original video file and
     * threshold value of resolution from policy. This is smart calculation cause the generator
     * will find the nearest value of minimum resolution value from original video file and will
     * produces the scale value based on the minimum resolution value and the threshold value.
     *
     * <b>Sample Usage</b>
     *
     * ```
     * val resolutionThreshold = 540 // px
     * val info = getVideoInfo(videoPath)
     * val newSize = generateVideoSize(info, resolutionThreshold)
     *
     * println(newSize.width)
     * println(newSize.height)
     * ```
     *
     * @param info video meta data info
     * @param thresholdRes resolution threshold from upload-pedia policy
     * @return [VideoInfo] a new width and height value
     */
    private fun generateVideoSize(info: VideoInfo, thresholdRes: Int): VideoInfo {
        val minVideoRes = min((info.width).toFloat(), (info.height).toFloat())

        return if (minVideoRes > thresholdRes) {
            val scale = thresholdRes / minVideoRes

            VideoInfo(
                width = round(info.width * scale).toInt(),
                height = round(info.height * scale).toInt(),
                bitrate = info.bitrate
            )
        } else {
            info
        }
    }

    /**
     * This method will extract out the video info based on file path. The method use the MetadataRetriever's API
     * to gathering the video file. The method will cater the width, height, and bitrate of the video.
     *
     * The return value is nullable which somehow the metadataRetriever failed to extract the info.
     *
     * @param path
     * @return [VideoInfo]
     */
    private fun getOriginalVideoInfo(path: String): VideoInfo? {
        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(context, path.toUri())
        } catch (exception: IllegalArgumentException) {
            Timber.d("VID-Compression: $exception")
            return null
        }

        return VideoInfo(
            width = getVideoWidth(retriever),
            height = getVideoHeight(retriever),
            bitrate = getVideoBitrate(retriever)
        )
    }

    private fun getVideoBitrate(retriever: MediaMetadataRetriever): Int {
        return retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_BITRATE
        )?.toInt() ?: 0
    }

    private fun getVideoWidth(retriever: MediaMetadataRetriever): Int {
        return retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH
        )?.toInt() ?: 0
    }

    private fun getVideoHeight(retriever: MediaMetadataRetriever): Int {
        return retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT
        )?.toInt() ?: 0
    }

    private fun compressedVideoPath(originalPath: String): File {
        val currentTime = System.currentTimeMillis()

        // internal app storage
        val internalAppCacheDir = FileUtil
            .getTokopediaInternalDirectory(ImageProcessingUtil.DEFAULT_DIRECTORY)
            .absolutePath

        // get the file name from original path
        val fileName = File(originalPath).nameWithoutExtension

        return File(
            internalAppCacheDir,
            "compressed_${fileName}_${currentTime}.mp4"
        )
    }

    data class VideoInfo(
        val width: Int = 0,
        val height: Int = 0,
        val bitrate: Int = 0
    )
}
