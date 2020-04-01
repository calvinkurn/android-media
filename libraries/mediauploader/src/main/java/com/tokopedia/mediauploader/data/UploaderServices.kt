package com.tokopedia.mediauploader.data

import com.tokopedia.mediauploader.data.entity.MediaUploader
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface UploaderServices {

    @POST
    @Multipart
    suspend fun uploadFile(
            /*
            * get url from data policy
            * */
            @Url urlToUpload: String,

            /*
            * file_upload:
            * media blob (a file) to upload
            * */
            @Part fileUpload: MultipartBody.Part
    ) : MediaUploader

}