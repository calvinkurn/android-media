package com.tokopedia.topchat.revamp.di

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.topchat.uploadimage.data.pojo.TopChatImageUploadPojo
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author : Steven 29/11/18
 */

@Module(includes = arrayOf(ImageUploaderModule::class))
class ChatModule {


    @ChatScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }


    @ChatScope
    @Provides
    fun provideResource(@ApplicationContext context: Context): Resources {
        return context.resources
    }


    @Provides
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: com.tokopedia.abstraction.common.data.model.session.UserSession,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<TopChatImageUploadPojo> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession, TopChatImageUploadPojo::class.java, imageUploaderUtils)
    }
}