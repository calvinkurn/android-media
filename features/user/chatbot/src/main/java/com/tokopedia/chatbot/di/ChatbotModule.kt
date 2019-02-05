package com.tokopedia.chatbot.di

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.chatbot.data.imageupload.ChatbotUploadImagePojo
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author by nisie on 12/12/18.
 */
@ChatbotScope
@Module(includes = arrayOf(ImageUploaderModule::class))
class ChatbotModule {

    @ChatbotScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ChatbotScope
    @Provides
    fun provideResource(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @ChatbotScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @ChatbotScope
    @Provides
    fun provideChatTkpdAuthInterceptor(@ApplicationContext context: Context,
                                       networkRouter: NetworkRouter,
                                       userSessionInterface: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ChatbotScope
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter,
                                      userSession: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @Provides
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: com.tokopedia.abstraction.common.data.model.session.UserSession,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils):
            UploadImageUseCase<ChatbotUploadImagePojo> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession,
                ChatbotUploadImagePojo::class.java, imageUploaderUtils)
    }
}