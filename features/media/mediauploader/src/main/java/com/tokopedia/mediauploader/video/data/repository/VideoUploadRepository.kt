package com.tokopedia.mediauploader.video.data.repository

import com.tokopedia.mediauploader.common.MediaUploaderUrl
import com.tokopedia.mediauploader.common.data.repository.BaseRequestRepository
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.util.network.requestBody
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.LargeUploader
import com.tokopedia.mediauploader.video.data.entity.SimpleUploader
import com.tokopedia.mediauploader.video.data.entity.Transcoding
import com.tokopedia.mediauploader.video.data.params.ChunkCheckerParam
import com.tokopedia.mediauploader.video.data.params.ChunkUploadParam
import com.tokopedia.mediauploader.video.data.params.InitParam
import com.tokopedia.mediauploader.video.data.params.SimpleUploadParam
import retrofit2.HttpException
import javax.inject.Inject

class VideoUploadRepository @Inject constructor(
    private val services: VideoUploadServices,
    private val url: MediaUploaderUrl
) : BaseRequestRepository() {

    suspend fun initUpload(params: InitParam): LargeUploader {
        return try {
            services.initLargeUpload(
                url = url.largeInit(),
                fileName = params.fileName,
                sourceId = params.sourceId
            )
        } catch (t: HttpException) {
            val reqId = getRequestId(t)

            LargeUploader(
                success = false,
                requestId = reqId
            )
        }
    }

    suspend fun simpleUpload(params: SimpleUploadParam, loader: ProgressUploader?): SimpleUploader {
        val (sourceId, file, timeOut) = params

        return try {
            services.simpleUpload(
                url = url.simpleVodUpload(sourceId),
                videoFile = params.fileBody(loader),
                fileName = file.name.requestBody(),
                timeOut = timeOut
            )
        } catch (t: HttpException) {
            val reqId = getRequestId(t)

            SimpleUploader(
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

    suspend fun shouldTranscodeSucceed(uploadId: String): Transcoding {
        return try {
            val url = url.hasLargeTranscodeStatus(uploadId)
            services.checkTranscodingStatus(url)
        } catch (t: HttpException) {
            val reqId = getRequestId(t)

            Transcoding(
                requestId = reqId
            )
        }
    }

    suspend fun completeUpload(uploadId: String): LargeUploader {
        return try {
            services.completeLargeUpload(
                url.largeComplete(),
                uploadId
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
