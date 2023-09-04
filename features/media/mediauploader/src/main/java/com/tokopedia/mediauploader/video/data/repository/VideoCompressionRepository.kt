package com.tokopedia.mediauploader.video.data.repository

import com.tokopedia.mediauploader.common.compressor.data.Configuration
import com.tokopedia.mediauploader.common.data.store.datastore.AnalyticsCacheDataStore
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.mediauploader.common.logger.logCompressionError
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.video.data.entity.VideoInfo
import com.tokopedia.mediauploader.video.data.params.VideoCompressionParam
import com.tokopedia.mediauploader.video.internal.VideoCompressor
import com.tokopedia.mediauploader.video.internal.VideoMetaDataExtractor
import java.io.File
import javax.inject.Inject
import kotlin.math.min
import kotlin.math.round

interface VideoCompressionRepository {
    suspend operator fun invoke(
        progressUploader: ProgressUploader?,
        param: VideoCompressionParam
    ): String
}

class VideoCompressionRepositoryImpl @Inject constructor(
    @UploaderQualifier private val compressor: VideoCompressor,
    @UploaderQualifier private val metadata: VideoMetaDataExtractor,
    @UploaderQualifier private val cacheAnalytics: AnalyticsCacheDataStore
) : VideoCompressionRepository {

    override suspend operator fun invoke(
        progressUploader: ProgressUploader?,
        param: VideoCompressionParam
    ): String {
        val (sourceId, originalPath, bitrate, resolution) = param

        // start track the compress time
        val startCompressionTime = System.currentTimeMillis()

        // if the same video already compressed, return the compressedVideoPath (if exist)
        val key = cacheAnalytics.key(sourceId, originalPath)
        val cache = cacheAnalytics.getData(key)

        // return the compressed file path if user has compressed before and file is exist
        if (cache != null &&
            cache.isCompressedFileExist() &&
            File(cache.compressedVideoPath).exists() &&

            // this validation is necessary due the compression there's a
            // edge case compressing the file is successful but the file is broken
            File(cache.compressedVideoPath).length() > 0
        ) return cache.compressedVideoPath

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

            // get minimum bitrate and convert it from Bps into Mbps
            val mBitrate = min(param.bitrate, videoOriginalInfo.bitrate)

            val config = Configuration(
                videoBitrate = mBitrate,
                videoHeight = (generateVideoSize.height.toDouble() * 2) / 2,
                videoWidth = (generateVideoSize.width.toDouble() * 2) / 2
            )

            val compression = compressor.compress(
                path = originalPath,
                configuration = config,
                progressUploader = progressUploader
            )

            if (compression.success) {
                val endCompressionTime = System.currentTimeMillis()
                val compressedPath = compression.path ?: originalPath

                // extract the metadata of compressed video file
                val compressedMetadataInfo = metadata.extract(compressedPath)

                // update the tracker for the compression step
                cacheAnalytics.setCompressionInfo(key) {
                    compressedVideoPath = compressedPath
                    startCompressedTime = startCompressionTime
                    endCompressedTime = endCompressionTime
                    videoCompressedSize = File(compressedPath).length().toString()
                    compressedVideoMetadata = compressedMetadataInfo
                }

                return compressedPath
            } else {
                logCompressionError(cache, compression.failureMessage)

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
            var width = round(info.width * scale).toInt()
            var height = round(info.height * scale).toInt()

            // since the MediaCodec android framework doesn't support an odd number,
            // let's force the size into even number.
            if (width % 2 == 1) width--
            if (height % 2 == 1) height--

            VideoInfo(
                width = width,
                height = height,
                bitrate = info.bitrate
            )
        } else {
            info
        }
    }

}
