package com.tokopedia.mediauploader.video

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.BaseParam
import com.tokopedia.mediauploader.BaseParam.Companion.copy
import com.tokopedia.mediauploader.BaseUploaderParam
import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.VideoParam
import com.tokopedia.mediauploader.analytics.MediaUploaderAnalytics
import com.tokopedia.mediauploader.common.cache.SourcePolicyManager
import com.tokopedia.mediauploader.common.data.consts.SOURCE_NOT_FOUND
import com.tokopedia.mediauploader.common.data.consts.UNKNOWN_ERROR
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
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

    override suspend fun upload(param: BaseUploaderParam): UploadResult {
        val base = (param as VideoParam).base as BaseParam

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

        val (isValid, message) = VideoUploaderValidator(filePath, policy.videoPolicy)
        if (isValid.not()) return UploadResult.Error(message)

        val maxSizeOfSimpleUpload = policy.videoPolicy
            ?.thresholdSizeOfVideo()
            ?.mbToBytes() ?: 0

        isSimpleUpload = filePath.length() <= maxSizeOfSimpleUpload
        setProgressUploader(base.progress)

        // start upload time tracker before do uploads
        analytics.setStartUploadTime(trackerCacheKey, System.currentTimeMillis())

        val newParam = param.copy(base = updateFileSource(base, filePath))
        val upload = if (!isSimpleUpload) largeUploaderManager(newParam) else simpleUploaderManager(newParam)

        return upload.also {
            analytics.setEndUploadTime(trackerCacheKey, System.currentTimeMillis()) // set the end upload time tracker
            analytics.sendEvent(base.sourceId, base.file, param.ableToRetry, isSimpleUpload) // send tracker to thanos
        }
    }

    suspend fun abortUpload(sourceId: String, file: File, abort: suspend () -> Unit) {
        if (isSimpleUpload) error("the upload abort behavior only supported on large video file")

        largeUploaderManager.abortUpload(sourceId, file) {
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

        return if (shouldCompress && isCompressionEnabled) {
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

    private fun updateFileSource(baseParam: BaseParam, newCompressFile: File): BaseParam{
        return if (baseParam.file.equals(newCompressFile.path)) {
            baseParam
        } else {
            baseParam.copy(newCompressFile)
        }
    }
}
