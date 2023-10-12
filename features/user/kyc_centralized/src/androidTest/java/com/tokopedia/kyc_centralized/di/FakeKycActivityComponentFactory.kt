package com.tokopedia.kyc_centralized.di

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kyc_centralized.data.UserSessionStub
import com.tokopedia.kyc_centralized.data.network.KycUploadApi
import com.tokopedia.kyc_centralized.fakes.FakeKycUploadApi
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class FakeKycActivityComponentFactory(context: Context) : ActivityComponentFactory() {

    val kycApi = FakeKycUploadApi()

    val userSession = UserSessionStub(context)

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
            .userIdentificationCommonModule(object : UserIdentificationCommonModule() {
                override fun provideUserSessionInterface(context: Context): UserSessionInterface {
                    return userSession
                }
            })
            .build()
    }
}
