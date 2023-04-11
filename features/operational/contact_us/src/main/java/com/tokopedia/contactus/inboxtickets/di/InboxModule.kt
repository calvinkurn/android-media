package com.tokopedia.contactus.inboxtickets.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.contactus.inboxtickets.data.UploadImageResponse
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ImageUploaderModule::class])
class InboxModule(private val context: Context) {

    @Provides
    @ActivityScope
    fun provideContext(): Context {
        return context
    }

    @ActivityScope
    @Provides
    fun provideSharedPreference(context: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    fun provideUserSession(): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideUploadImageUseCase(
        @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
        @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
        @ImageUploaderQualifier gson: Gson,
        @ImageUploaderQualifier userSession: UserSessionInterface,
        @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils
    ):
        UploadImageUseCase<UploadImageResponse> {
        return UploadImageUseCase(
            uploadImageRepository,
            generateHostRepository,
            gson,
            userSession,
            UploadImageResponse::class.java,
            imageUploaderUtils
        )
    }
}
