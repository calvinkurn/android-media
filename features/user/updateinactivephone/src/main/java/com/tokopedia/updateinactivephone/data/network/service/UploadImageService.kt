package com.tokopedia.updateinactivephone.data.network.service

import com.tokopedia.core.network.retrofit.services.AuthService
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneURL
import com.tokopedia.updateinactivephone.data.network.api.UploadImageApi

import javax.inject.Inject

import retrofit2.Retrofit

class UploadImageService @Inject constructor() : AuthService<UploadImageApi>() {

    override fun initApiService(retrofit: Retrofit) {
        api = retrofit.create(UploadImageApi::class.java)
    }

    override fun getBaseUrl(): String {
        return UpdateInactivePhoneURL.ACCOUNTS_DOMAIN
    }

    override fun getApi(): UploadImageApi {
        return api
    }
}