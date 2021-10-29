package com.tokopedia.mediauploader.video

import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.common.data.consts.CHUNK_UPLOAD
import com.tokopedia.mediauploader.common.data.consts.UPLOAD_ABORT
import com.tokopedia.mediauploader.common.state.ProgressCallback
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.slice
import com.tokopedia.mediauploader.video.data.params.ChunkCheckerParam
import com.tokopedia.mediauploader.video.data.params.ChunkUploadParam
import com.tokopedia.mediauploader.video.data.params.InitParam
import com.tokopedia.mediauploader.video.domain.*
import java.io.File
import javax.inject.Inject
import kotlin.math.ceil

class LargeUploaderManager @Inject constructor(
    private val initUseCase: InitVideoUploaderUseCase,
    private val checkerUseCase: GetChunkCheckerUseCase,
    private val uploaderUseCase: GetChunkUploaderUseCase,
    private val completeUseCase: SetCompleteUploaderUseCase,
    private val abortUseCase: SetAbortUploaderUseCase
) : UploaderManager {

    private var hasInit = false
    private var partNumber = 1

    private var sourceId: String = ""
    private var currentUploadId = ""
    private var chunkSize: Int = 0

    suspend operator fun invoke(file: File, sourceId: String): UploadResult {
        this.chunkSize = ceil(file.length() / SIZE_PER_CHUNK.toDouble()).toInt()
        this.sourceId = sourceId

        initUpload(sourceId, file.name)

        for (part in 1..chunkSize) {
            // bypass if it's already uploaded by part number
            if (part < partNumber) continue

            // upload the chunk
            file.slice(part, SIZE_PER_CHUNK)?.let {
                if (!chunkUpload(file.name, it)) {
                    return UploadResult.Error(CHUNK_UPLOAD)
                }
            }
        }

        val resultVideoUrl = completeUpload()

        return if (resultVideoUrl.isNotEmpty()) {
            UploadResult.Success(videoUrl = resultVideoUrl)
        } else {
            UploadResult.Error(UPLOAD_ABORT)
        }
    }

    suspend fun abortUpload(abort: () -> Unit) {
        val abortUseCase = abortUseCase(currentUploadId)

        if (abortUseCase.isSuccess()) {
            abort()
        }
    }

    private suspend fun initUpload(sourceId: String, fileName: String) {
        if (hasInit) return

        val init = initUseCase(InitParam(
            sourceId = sourceId,
            fileName = fileName
        ))

        if (init.isSuccess()) {
            currentUploadId = init.uploadId()
            hasInit = true
        } else {
            // re-call if init didn't success
            initUpload(sourceId, fileName)
        }
    }

    private suspend fun chunkUpload(
        fileName: String,
        byteArray: ByteArray,
        maxRetryCount: Int = 3
    ): Boolean {
        val uploader = uploaderUseCase(ChunkUploadParam(
            sourceId = sourceId,
            uploadId = currentUploadId,
            partNumber = partNumber.toString(),
            fileName = fileName,
            byteArray = byteArray,
            timeOut = "999"
        ))

        return if (uploader.isSuccess()) {
            isChunkCorrect(fileName)
        } else {
            // failed to upload a existing part number, retry it 3 times!
            if (maxRetryCount > 0) {
                chunkUpload(fileName, byteArray, maxRetryCount - 1)
            } else {
                false
            }
        }
    }

    private suspend fun isChunkCorrect(fileName: String): Boolean {
        val checker = checkerUseCase(ChunkCheckerParam(
            uploadId = currentUploadId,
            partNumber = partNumber.toString(),
            fileName = fileName
        ))

        if (checker.isPartSuccess()) {
            partNumber++
        }

        return checker.isPartSuccess()
    }

    private suspend fun completeUpload(): String {
        if (partNumber >= chunkSize) {
            val complete = completeUseCase(currentUploadId)
            if (complete.isSuccess()) {
                resetUpload()

                return complete.videoUrl()
            }
        }

        return ""
    }

    override fun setProgressUploader(progress: ProgressCallback?) {
        // TODO
    }

    private fun resetUpload() {
        currentUploadId = ""
        hasInit = false
    }

    companion object {
        const val SIZE_PER_CHUNK = 1024 * 1024 * 10 // 10 mb
    }

}