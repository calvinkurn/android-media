package com.tokopedia.mediauploader.video.data.repository

import android.content.Context
import android.net.Uri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mediauploader.common.VideoMetaDataExtractor
import com.tokopedia.mediauploader.tracker.MediaUploaderTracker
import com.tokopedia.mediauploader.common.internal.compressor.CompressionProgressListener
import com.tokopedia.mediauploader.common.internal.compressor.Compressor
import com.tokopedia.mediauploader.common.internal.compressor.data.Configuration
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.video.data.entity.VideoInfo
import com.tokopedia.mediauploader.video.data.params.VideoCompressionParam
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File
import javax.inject.Inject
import kotlin.math.min
import kotlin.math.round

interface VideoCompressionRepository {
    suspend fun compress(progressUploader: ProgressUploader?, param: VideoCompressionParam): String
}

class VideoCompressionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val metadata: VideoMetaDataExtractor,
    private val tracker: MediaUploaderTracker
) : VideoCompressionRepository {

    override suspend fun compress(
        progressUploader: ProgressUploader?,
        param: VideoCompressionParam
    ): String {
        val (sourceId, originalPath, bitrate, resolution) = param

        // start track the compress time
        val startCompressionTime = System.currentTimeMillis()

        val cache = cacheManager.get(sourceId, originalPath)

        // if the same video already compressed, return the compressedVideoPath (if exist)
        if (cache != null && cache.isCompressedFileExist()) return cache.compressedVideoPath

        // get the video info of originalVideoPath, return the originalPath if couldn't get the video info
        val videoOriginalInfo = metadata.extract(originalPath) ?: return originalPath

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

            // get minimum bitrate and convert it from Bps into Mbps
            val mBitrate = min(param.bitrate, videoOriginalInfo.bitrate) / 1_000_000

            val config = Configuration(
                videoBitrateInMbps = mBitrate,
                videoHeight = generateVideoSize.height.toDouble(),
                videoWidth = generateVideoSize.width.toDouble()
            )

            val compression = Compressor.compressVideo(
                context = context,
                srcUri = Uri.parse(originalPath),
                destination = cacheFile.path,
                configuration = config,
                listener = object : CompressionProgressListener {
                    override fun onProgressChanged(percent: Float) {
                        progressUploader?.onProgress(percent.toInt(), ProgressType.Compression)
                    }
                }
            )

            if (compression.success) {
                val endCompressionTime = System.currentTimeMillis()
                val compressedPath = compression.path ?: originalPath

                // extract the metadata of compressed video file
                val compressedMetadataInfo = metadata.extract(compressedPath)

                // update the tracker for the compression step
                val key = tracker.key(sourceId, originalPath)
                tracker.setCompressionTracker(key) {
                    compressedVideoPath = compressedPath
                    startCompressedTime = startCompressionTime
                    endCompressedTime = endCompressionTime
                    videoCompressedSize = File(compressedPath).length().toString()
                    compressedVideoMetadata = compressedMetadataInfo
                }

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
     * val info = metadata.extract(videoPath)
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
}
