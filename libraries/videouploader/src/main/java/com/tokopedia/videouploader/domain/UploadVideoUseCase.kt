package com.tokopedia.videouploader.domain

import com.google.gson.Gson
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.videouploader.data.UploadVideoApi
import com.tokopedia.videouploader.domain.model.VideoUploadDomainModel
import com.tokopedia.videouploader.domain.pojo.GenerateTokenPojo
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import java.io.File

/**
 * @author by nisie on 13/03/19.
 */
class UploadVideoUseCase<T>(val uploadVideoApi: UploadVideoApi,
                            val gson: Gson,
                            val videoUploadResultModel: Class<T>) :
        UseCase<VideoUploadDomainModel<T>>() {


    override fun createObservable(requestParams: RequestParams):
            Observable<VideoUploadDomainModel<T>> {
        return uploadVideoApi.generateToken(getGenerateTokenParams(requestParams))
                .flatMap { generateTokenPojo ->
                    uploadVideoApi.uploadVideo(getVideoToken(generateTokenPojo),
                            getUploadVideoParams(requestParams))
                            .map { response ->
                                val videoUploadDomainModel = VideoUploadDomainModel<T>(videoUploadResultModel)
                                val dataResultImageUpload = gson.fromJson(response, videoUploadDomainModel.type)
                                videoUploadDomainModel
                            }
                }
    }

    private fun getVideoToken(generateTokenPojo: GenerateTokenPojo?): String {
        return generateTokenPojo?.token ?: ""
    }

    private fun getUploadVideoParams(requestParams: RequestParams): Map<String, RequestBody> {
        val params = HashMap<String, RequestBody>()
        val videoFile = File(requestParams.getString(PARAM_VIDEO_PATH))

        val videoRequestBody: RequestBody = RequestBody.create(MediaType.parse("file"), videoFile)
        params[PARAM_VIDEO_FILE] = videoRequestBody
        return params
    }

    private fun getGenerateTokenParams(requestParams: RequestParams): Map<String, Any> {
        val params = RequestParams.create()
        params.putString("user_id", "2590134")
        params.putString("session", "Bearer 0wq4NzT3SpSAyVoEKPRtmA")
        return params.parameters
    }

    companion object {
        const val PARAM_VIDEO_FILE = "file"
        const val PARAM_VIDEO_PATH = "video_path"
    }
}