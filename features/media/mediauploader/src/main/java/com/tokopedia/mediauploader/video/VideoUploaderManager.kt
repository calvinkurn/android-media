package com.tokopedia.mediauploader.video

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.BaseParam
import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.VideoParam
import com.tokopedia.mediauploader.common.FeatureToggleUploader
import com.tokopedia.mediauploader.common.data.consts.*
import com.tokopedia.mediauploader.common.cache.SourcePolicyManager
import com.tokopedia.mediauploader.analytics.MediaUploaderAnalytics
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.isMaxFileSize
import com.tokopedia.mediauploader.common.util.mbToBytes
import com.tokopedia.mediauploader.video.data.entity.VideoPolicy
import com.tokopedia.mediauploader.video.data.params.VideoCompressionParam
import com.tokopedia.mediauploader.video.domain.SetVideoCompressionUseCase
import java.io.File
import javax.inject.Inject

class VideoUploaderManager @Inject constructor(
    @UploaderQualifier private val policyManager: SourcePolicyManager,
    private val videoCompression: SetVideoCompressionUseCase,
    private val simpleUploaderManager: SimpleUploaderManager,
    private val largeUploaderManager: LargeUploaderManager,
    private val analytics: MediaUploaderAnalytics
) : UploaderManager {

    private var isSimpleUpload = true

    suspend operator fun invoke(param: VideoParam): UploadResult {
        val base = param.base as BaseParam

        if (base.sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)
        val policy = policyManager.get() ?: return UploadResult.Error(UNKNOWN_ERROR)

        // init tracker
        val trackerCacheKey = analytics.key(base.sourceId, base.file.path)
        analytics.setOriginalVideoInfo(trackerCacheKey, base.file)

        // compress video if needed
        val filePath = compressVideo(
            originalFile = base.file,
            sourceId = base.sourceId,
            shouldCompress = param.shouldCompress,
            policy = policy.videoPolicy,
            loader = base.progress
        )

        // return the upload result
        return policy.videoPolicy?.let { videoPolicy ->
            val maxSizeOfSimpleUpload = videoPolicy.thresholdSizeOfVideo()
            val maxFileSize = videoPolicy.maxFileSize

            when {
                !filePath.exists() -> {
                    UploadResult.Error(FILE_NOT_FOUND)
                }
                filePath.isMaxFileSize(maxFileSize) -> {
                    UploadResult.Error(maxFileSizeMessage(maxFileSize))
                }
                !allowedExt(filePath.path, videoPolicy.extension) -> {
                    UploadResult.Error(formatNotAllowedMessage(videoPolicy.extension))
                }
                else -> {
                    isSimpleUpload = filePath.length() <= maxSizeOfSimpleUpload.mbToBytes()
                    setProgressUploader(base.progress)

                    // start upload time tracker
                    analytics.setStartUploadTime(trackerCacheKey, System.currentTimeMillis())

                    val upload = if (!isSimpleUpload) {
                        largeUploaderManager(param)
                    } else {
                        simpleUploaderManager(param)
                    }

                    upload.also {
                        // set the end upload time tracker
                        analytics.setEndUploadTime(trackerCacheKey, System.currentTimeMillis())

                        // send tracker to thanos
                        analytics.sendEvent(base.sourceId, base.file, param.ableToRetry, isSimpleUpload)
                    }
                }
            }
        } ?: UploadResult.Error(UNKNOWN_ERROR)
    }

    suspend fun abortUpload(sourceId: String, fileName: String, abort: suspend () -> Unit) {
        if (isSimpleUpload) error("the upload abort behavior only supported on large video file")

        largeUploaderManager.abortUpload(sourceId, fileName) {
            abort()
        }
    }

    override fun setProgressUploader(progress: ProgressUploader?) {
        if (isSimpleUpload) {
            simpleUploaderManager.setProgressCallback(progress)
        } else {
            largeUploaderManager.setProgressCallback(progress)
        }
    }

    private suspend fun compressVideo(
        sourceId: String,
        originalFile: File,
        shouldCompress: Boolean,
        policy: VideoPolicy?,
        loader: ProgressUploader?
    ): File {
        // check if the policy exist, otherwise return the original file path
        val compressionPolicy = policy?.videoCompression ?: return originalFile

        // the toggle comes from policy
        val isCompressionEnabled = compressionPolicy.isCompressionEnabled

        // feature toggle using ab-test
        val isCompressionRemoteConfigEnabled = FeatureToggleUploader.isCompressionEnabled()

        return if (shouldCompress && isCompressionEnabled && isCompressionRemoteConfigEnabled) {
            val originalFileSizeInMb = originalFile.length() / (1024 * 1024)

            if (originalFileSizeInMb < compressionPolicy.thresholdInMb.orZero()) {
                return originalFile
            }

            val param = VideoCompressionParam(
                sourceId = sourceId,
                videoPath = originalFile.path,
                bitrate = compressionPolicy.maxBitrateBps,
                resolution = compressionPolicy.maxResolution
            )

            videoCompression.progressUploader = loader
            File(videoCompression(param))
        } else {
            originalFile
        }
    }

}
