package com.tokopedia.mediauploader.video.data.repository

import com.tokopedia.mediauploader.common.MediaUploaderUrl
import com.tokopedia.mediauploader.common.data.repository.BaseRequestRepository
import com.tokopedia.mediauploader.common.util.network.requestBody
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.LargeUploader
import com.tokopedia.mediauploader.video.data.params.ChunkCheckerParam
import com.tokopedia.mediauploader.video.data.params.ChunkUploadParam
import retrofit2.HttpException
import javax.inject.Inject

class LargeUploadRepository @Inject constructor(
    private val services: VideoUploadServices,
    private val url: MediaUploaderUrl
) : BaseRequestRepository() {

    suspend fun checkChunk(params: ChunkCheckerParam): LargeUploader {
        return try {
            services.chunkCheckerUpload(
                url = url.largePart(),
                fileName = params.fileName,
                uploadId = params.uploadId,
                partNumber = params.partNumber
            )
        } catch (t: HttpException) {
            val reqId = getRequestId(t)

            LargeUploader(
                success = false,
                requestId = reqId
            )
        }
    }

    suspend fun chunkUpload(params: ChunkUploadParam): LargeUploader {
        return try {
            services.uploadLargeUpload(
                url = url.largePart(),
                sourceId = params.sourceId.requestBody(),
                uploadId = params.uploadId.requestBody(),
                partNumber = params.partNumber.requestBody(),
                videoFile = params.fileBody(),
                timeOut = params.timeOut
            )
        } catch (t: HttpException) {
            val reqId = getRequestId(t)

            LargeUploader(
                success = false,
                requestId = reqId
            )
        }
    }
}
