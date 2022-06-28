package com.tokopedia.media.editor.data

import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface EditorNetworkServices {

    @Multipart
    @POST(PATH_REMOVE_BACKGROUND)
    fun removeBackground(
        @Part file: MultipartBody.Part
    ): Flow<ResponseBody>

    companion object {
        private const val PATH_REMOVE_BACKGROUND = "remove-background/"
    }

}