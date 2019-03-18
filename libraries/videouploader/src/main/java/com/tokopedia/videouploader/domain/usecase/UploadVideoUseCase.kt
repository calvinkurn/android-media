package com.tokopedia.videouploader.domain.usecase

import android.util.Log
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
                            val generateVideoTokenUseCase : GenerateVideoTokenUseCase) :
        UseCase<VideoUploadDomainModel<T>>() {


    override fun createObservable(requestParams: RequestParams):
            Observable<VideoUploadDomainModel<T>> {
        try {

            return generateVideoTokenUseCase.getExecuteObservable()
                    .flatMap { graphqlResponse ->
                        uploadVideoApi.uploadVideo(getVideoToken(graphqlResponse),
                                getUploadVideoParams(requestParams))
                                .map { response ->
                                    val videoUploadDomainModel = VideoUploadDomainModel<T>(videoUploadResultModel)
                                    val dataResultImageUpload = gson.fromJson(response, videoUploadDomainModel.type)
                                    videoUploadDomainModel
                                }
                    }.onErrorReturn { t -> throw t }
        }catch (e : Exception){
            e.printStackTrace()
            throw e
        }
    }

    private fun getVideoToken(graphqlResponse: GraphqlResponse): String {
        val pojo = graphqlResponse.getData<GenerateTokenPojo>(GenerateTokenPojo::class.java)
        return pojo?.token ?: ""
    }

    private fun getVideoToken(generateTokenPojo: GenerateTokenPojo?): String {
        return generateTokenPojo?.token ?: ""
    }

    private fun getUploadVideoParams(requestParams: RequestParams): MultipartBody.Part{

        val videoFile = File(requestParams.getString(PARAM_VIDEO_PATH,""))

        Log.d("NISNIS", videoFile.toString())
        val videoRequestBody: RequestBody = RequestBody.create(MediaType.parse("video/*"), videoFile)
        val videoMultipart : MultipartBody.Part = MultipartBody.Part.createFormData(PARAM_VIDEO_FILE,
                videoFile.getName(), videoRequestBody)
        return videoMultipart
    }

    private fun getGenerateTokenParams(requestParams: RequestParams): HashMap<String, String> {
        val params = RequestParams.create()
        params.putString("user_id", requestParams.getString("user_id", ""))
        params.putString("session", requestParams.getString("session" , ""))
        return params.paramsAllValueInString
    }

    companion object {
        const val PARAM_VIDEO_FILE = "file"
        const val PARAM_VIDEO_PATH = "video_path"

        fun createParam(userId : String, session : String, filePath : String) : RequestParams{
            val params = RequestParams.create()
            params.putString("user_id", userId)
            params.putString("session", session)
            params.putString(PARAM_VIDEO_PATH, filePath)
            return params
        }
    }
}