package com.tokopedia.kyc_centralized.di

import android.app.Activity
import android.content.SharedPreferences
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kyc_centralized.data.network.KycUploadApi
import com.tokopedia.kyc_centralized.util.KycSharedPreferenceInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class FakeKycActivityComponentFactory(val case: FakeKycUploadApi.Case = FakeKycUploadApi.Case.Success) :
    ActivityComponentFactory() {
    override fun createActivityComponent(activity: Activity): UserIdentificationCommonComponent {
        return DaggerUserIdentificationCommonComponent.builder()
            .baseAppComponent((activity.application as BaseMainApplication).baseAppComponent)
            .kycUploadImageModule(object : KycUploadImageModule() {
                override fun provideApi(
                    okHttpClient: OkHttpClient,
                    retrofitBuilder: Retrofit.Builder
                ): KycUploadApi {
                    return FakeKycUploadApi(case)
                }
            })
            .userIdentificationCommonModule(object : UserIdentificationCommonModule() {
                override fun provideGraphQlRepository(): GraphqlRepository {
                    return FakeGraphqlRepository()
                }
            })
            .build()
    }
}

class FakeKycActivityComponentFactorySimulateNullPref : ActivityComponentFactory() {
    override fun createActivityComponent(activity: Activity): UserIdentificationCommonComponent {
        return DaggerUserIdentificationCommonComponent.builder()
            .baseAppComponent((activity.application as BaseMainApplication).baseAppComponent)
            .kycUploadImageModule(object : KycUploadImageModule() {
                override fun provideApi(
                    okHttpClient: OkHttpClient,
                    retrofitBuilder: Retrofit.Builder
                ): KycUploadApi {
                    return FakeKycUploadApi()
                }
            })
            .userIdentificationCommonModule(object : UserIdentificationCommonModule() {
                override fun provideKycPrefInterface(pref: SharedPreferences): KycSharedPreferenceInterface {
                    return FakeKycPreferences()
                }
                override fun provideGraphQlRepository(): GraphqlRepository {
                    return FakeGraphqlRepository()
                }
            })
            .build()
    }
}