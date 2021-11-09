package com.tokopedia.mediauploader.video

import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.common.data.consts.*
import com.tokopedia.mediauploader.common.data.mapper.PolicyMapper
import com.tokopedia.mediauploader.common.state.ProgressCallback
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.fileExtension
import com.tokopedia.mediauploader.common.util.isMaxFileSize
import com.tokopedia.mediauploader.common.util.mbToBytes
import com.tokopedia.mediauploader.video.domain.GetVideoPolicyUseCase
import java.io.File
import javax.inject.Inject

class VideoUploaderManager @Inject constructor(
    private val policyUseCase: GetVideoPolicyUseCase,
    private val simpleUploaderManager: SimpleUploaderManager,
    private val largeUploaderManager: LargeUploaderManager
) : UploaderManager {

    private var isSimpleUpload = true

    suspend operator fun invoke(file: File, sourceId: String, withTranscode: Boolean): UploadResult {
        if (sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)

        val filePath = file.path
        val policyData = policyUseCase(sourceId)

        val sourcePolicy = PolicyMapper.map(policyData.dataPolicy)
        val videoPolicy = sourcePolicy.videoPolicy

        if (videoPolicy != null) {
            val extensions = videoPolicy.extension.split(",")
            val maxSizeOfSimpleUpload = videoPolicy.thresholdSizeOfVideo()
            val maxFileSize = videoPolicy.maxFileSize

            return when {
                !file.exists() -> {
                    UploadResult.Error(FILE_NOT_FOUND)
                }
                !extensions.contains(filePath.fileExtension()) -> {
                    UploadResult.Error(formatNotAllowedMessage(videoPolicy.extension))
                }
                file.isMaxFileSize(maxFileSize) -> {
                    UploadResult.Error(maxFileSizeMessage(maxFileSize))
                }
                else -> {
                    isSimpleUpload = file.length() <= maxSizeOfSimpleUpload.mbToBytes()

                    if (!isSimpleUpload) {
                        largeUploaderManager(file, sourceId, sourcePolicy, withTranscode)
                    } else {
                        simpleUploaderManager(file, sourceId, sourcePolicy, withTranscode)
                    }
                }
            }
        } else {
            return UploadResult.Error(UNKNOWN_ERROR)
        }
    }

    suspend fun abortUpload(abort: () -> Unit) {
        if (isSimpleUpload) return

        largeUploaderManager.abortUpload {
            abort()
        }
    }

    override fun setProgressUploader(progress: ProgressCallback?) {
        if (isSimpleUpload) {
            // for now, the progress loader only supported for simple uploader
            simpleUploaderManager.setProgressCallback(progress)
        } else {
            largeUploaderManager.setProgressCallback(progress)
        }
    }

}