package com.tokopedia.shop_nib.data.service

import com.tokopedia.shop_nib.data.response.UploadFileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface UploadFileService {
    @Multipart
    @POST("/seller/nib")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ): UploadFileResponse
}
