package com.tokopedia.shop.settings.common.di

import android.content.Context
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.gm.common.constant.GMCommonUrl
import com.tokopedia.gm.common.data.interceptor.PowerMerchantSubscribeInterceptor
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi
import com.tokopedia.gm.common.di.GmCommonModule
import com.tokopedia.gm.common.di.GmCommonQualifier
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.shop.settings.basicinfo.data.UploadShopEditImageModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * @author by furqan on 21/03/18.
 */

@Module(includes = [ImageUploaderModule::class])
class ShopSettingsModule {

    @Provides
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: UserSessionInterface,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<UploadShopEditImageModel> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession, UploadShopEditImageModel::class.java, imageUploaderUtils)
    }

    @Provides
    @ShopSettingsScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @GmCommonQualifier
    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools())
    }

    @GmCommonQualifier
    @Provides
    fun provideOkHttpClient(@GmCommonQualifier chuckInterceptor: ChuckInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            tkpdAuthInterceptor: TkpdAuthInterceptor,
                            powerMerchantSubscribeInterceptor: PowerMerchantSubscribeInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .addInterceptor(HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java))
            .addInterceptor(tkpdAuthInterceptor)
            .addInterceptor(powerMerchantSubscribeInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                .addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @GmCommonQualifier
    @Provides
    fun provideVoteRetrofit(retrofitBuilder: Retrofit.Builder,
                            @GmCommonQualifier okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder.baseUrl(GMCommonUrl.BASE_URL).client(okHttpClient).build()
    }

    @Provides
    fun provideGMCommonApi(@GmCommonQualifier retrofit: Retrofit): GMCommonApi {
        return retrofit.create(GMCommonApi::class.java)
    }
}
