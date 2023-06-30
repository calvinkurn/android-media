package com.tokopedia.mediauploader.video

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.UploaderManager
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
import com.tokopedia.mediauploader.video.domain.GetVideoPolicyUseCase
import com.tokopedia.mediauploader.video.domain.SetVideoCompressionUseCase
import java.io.File
import javax.inject.Inject

class VideoUploaderManager @Inject constructor(
    @UploaderQualifier private val policyManager: SourcePolicyManager,
    private val policyUseCase: GetVideoPolicyUseCase,
    private val videoCompression: SetVideoCompressionUseCase,
    private val simpleUploaderManager: SimpleUploaderManager,
    private val largeUploaderManager: LargeUploaderManager,
    private val analytics: MediaUploaderAnalytics
) : UploaderManager {

    private var isSimpleUpload = true

    suspend operator fun invoke(
        file: File,
        sourceId: String,
        loader: ProgressUploader?,
        withTranscode: Boolean,
        shouldCompress: Boolean,
        isRetry: Boolean
    ): UploadResult {
        if (sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)

        // hit uploader policy
        val policy = policyUseCase(sourceId)
        policyManager.set(policy)

        // init tracker
        val trackerCacheKey = analytics.key(sourceId, file.path)
        analytics.setOriginalVideoInfo(trackerCacheKey, file)

        // compress video if needed
        val filePath = compressVideo(
            originalFile = file,
            sourceId = sourceId,
            shouldCompress = shouldCompress,
            policy = policy.videoPolicy,
            loader = loader
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
                    setProgressUploader(loader)

                    // start upload time tracker
                    analytics.setStartUploadTime(trackerCacheKey, System.currentTimeMillis())

                    val upload = if (!isSimpleUpload) {
                        largeUploaderManager(filePath, sourceId, withTranscode, isRetry)
                    } else {
                        simpleUploaderManager(filePath, sourceId, withTranscode)
                    }

                    upload.also {
                        // set the end upload time tracker
                        analytics.setEndUploadTime(trackerCacheKey, System.currentTimeMillis())

                        // send tracker to thanos
                        analytics.sendEvent(sourceId, file, isRetry, isSimpleUpload)
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
