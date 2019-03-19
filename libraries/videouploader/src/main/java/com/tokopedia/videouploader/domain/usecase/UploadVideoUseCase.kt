package com.tokopedia.videouploader.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.videouploader.data.UploadVideoApi
import com.tokopedia.videouploader.domain.model.VideoUploadDomainModel
import com.tokopedia.videouploader.domain.pojo.GenerateTokenPojo
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.Observable
import java.io.File

/**
 * @author by nisie on 13/03/19.
 */
class UploadVideoUseCase<T>(val uploadVideoApi: UploadVideoApi,
                            val gson: Gson,
                            val videoUploadResultModel: Class<T>,
                            val generateVideoTokenUseCase: GenerateVideoTokenUseCase) :
        UseCase<VideoUploadDomainModel<T>>() {


    override fun createObservable(requestParams: RequestParams):
            Observable<VideoUploadDomainModel<T>> {
        return generateVideoTokenUseCase.getExecuteObservable()
                .flatMap { graphqlResponse ->
                    uploadVideoApi.uploadVideo(getVideoToken(graphqlResponse),
                            getUploadVideoParams(requestParams))
                            .map { response ->
                                val videoUploadDomainModel = VideoUploadDomainModel(videoUploadResultModel)
                                val dataResultVideoUpload = gson.fromJson(response,
                                        videoUploadDomainModel.type)
                                videoUploadDomainModel.dataResultVideoUpload = dataResultVideoUpload
                                videoUploadDomainModel
                            }
                }
    }

    private fun getVideoToken(graphqlResponse: GraphqlResponse): String {
        val pojo = graphqlResponse.getData<GenerateTokenPojo>(GenerateTokenPojo::class.java)
        return pojo?.topLiveVideoToken?.token ?: ""
    }

    private fun getUploadVideoParams(requestParams: RequestParams): MultipartBody.Part {

        val videoFile = File(requestParams.getString(PARAM_VIDEO_PATH, ""))

        val videoRequestBody: RequestBody = RequestBody.create(MediaType.parse("video/*"), videoFile)
        val videoMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(PARAM_VIDEO_FILE,
                videoFile.getName(), videoRequestBody)
        return videoMultipart
    }

    companion object {
        const val PARAM_VIDEO_FILE = "file"
        const val PARAM_VIDEO_PATH = "video_path"

        fun createParam(filePath: String): RequestParams {
            val params = RequestParams.create()
            params.putString(PARAM_VIDEO_PATH, filePath)
            return params
        }
    }
}