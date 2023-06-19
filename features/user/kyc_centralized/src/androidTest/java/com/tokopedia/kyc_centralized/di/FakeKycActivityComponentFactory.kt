package com.tokopedia.kyc_centralized.di

import android.app.Activity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kyc_centralized.data.network.KycUploadApi
import com.tokopedia.kyc_centralized.fakes.FakeKycUploadApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class FakeKycActivityComponentFactory : ActivityComponentFactory() {

    val kycApi = FakeKycUploadApi()

    override fun createActivityComponent(activity: Activity): UserIdentificationCommonComponent {
        return DaggerUserIdentificationCommonComponent.builder()
            .baseAppComponent((activity.application as BaseMainApplication).baseAppComponent)
            .kycUploadImageModule(object : KycUploadImageModule() {
                override fun provideApi(
                    okHttpClient: OkHttpClient,
                    retrofitBuilder: Retrofit.Builder
                ): KycUploadApi {
                    return kycApi
                }
            })
            .build()
    }
}
