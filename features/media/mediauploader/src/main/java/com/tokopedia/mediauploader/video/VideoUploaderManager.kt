package com.tokopedia.mediauploader.video

import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.common.data.consts.*
import com.tokopedia.mediauploader.common.internal.SourcePolicyManager
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.isMaxFileSize
import com.tokopedia.mediauploader.common.util.mbToBytes
import com.tokopedia.mediauploader.video.data.params.VideoCompressionParam
import com.tokopedia.mediauploader.video.domain.GetVideoPolicyUseCase
import com.tokopedia.mediauploader.video.domain.SetVideoCompressionUseCase
import java.io.File
import javax.inject.Inject

class VideoUploaderManager @Inject constructor(
    private val policyManager: SourcePolicyManager,
    private val policyUseCase: GetVideoPolicyUseCase,
    private val videoCompression: SetVideoCompressionUseCase,
    private val simpleUploaderManager: SimpleUploaderManager,
    private val largeUploaderManager: LargeUploaderManager
) : UploaderManager {

    private var isSimpleUpload = true

    suspend operator fun invoke(
        file: File,
        sourceId: String,
        loader: ProgressUploader?,
        withTranscode: Boolean,
        shouldCompress: Boolean
    ): UploadResult {
        if (sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)

        // hit uploader policy
        val policy = policyUseCase(sourceId)
        policyManager.set(policy)

        // compress video if needed
        val filePath = if (shouldCompress) {
            val param = VideoCompressionParam(
                sourceId = sourceId,
                videoPath = file.path,
                bitrate = 1_000_000,
                resolution = 540
            )

            videoCompression.progressUploader = loader
            videoCompression(param)
        } else {
            file.path
        }

        // return the upload result
        return policy.videoPolicy?.let { videoPolicy ->
            val maxSizeOfSimpleUpload = videoPolicy.thresholdSizeOfVideo()
            val maxFileSize = videoPolicy.maxFileSize

            when {
                !file.exists() -> {
                    UploadResult.Error(FILE_NOT_FOUND)
                }
                file.isMaxFileSize(maxFileSize) -> {
                    UploadResult.Error(maxFileSizeMessage(maxFileSize))
                }
                !allowedExt(filePath, videoPolicy.extension) -> {
                    UploadResult.Error(formatNotAllowedMessage(videoPolicy.extension))
                }
                else -> {
                    isSimpleUpload = file.length() <= maxSizeOfSimpleUpload.mbToBytes()

                    setProgressUploader(loader)

                    if (!isSimpleUpload) {
                        largeUploaderManager(file, sourceId, withTranscode)
                    } else {
                        simpleUploaderManager(file, sourceId, withTranscode)
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

}
