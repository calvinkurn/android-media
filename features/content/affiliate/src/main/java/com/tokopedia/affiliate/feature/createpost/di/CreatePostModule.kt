package com.tokopedia.affiliate.feature.createpost.di

import android.app.NotificationManager
import android.content.Context

import com.google.gson.Gson
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliate.analytics.AffiliateAnalytics
import com.tokopedia.affiliate.feature.createpost.data.pojo.uploadimage.UploadImageResponse
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.feature.createpost.view.presenter.CreatePostPresenter
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

/**
 * @author by milhamj on 9/26/18.
 */
@Module(includes = [ImageUploaderModule::class])
class CreatePostModule(private val context: Context) {

    @Provides
    @ApplicationContext
    internal fun provideApplicationContext(): Context {
        return context.applicationContext
    }

    @Provides
    @CreatePostScope
    internal fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: com.tokopedia.abstraction.common.data.model.session.UserSession,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<UploadImageResponse> {
        return UploadImageUseCase(
                uploadImageRepository,
                generateHostRepository,
                gson,
                userSession,
                UploadImageResponse::class.java,
                imageUploaderUtils
        )
    }

    @Provides
    @CreatePostScope
    internal fun providePresenter(createPostPresenter: CreatePostPresenter): CreatePostContract.Presenter {
        return createPostPresenter
    }

    @Provides
    @CreatePostScope
    internal fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @CreatePostScope
    internal fun provideAffiliateAnalytics(@ApplicationContext context: Context,
                                           userSessionInterface: UserSessionInterface): AffiliateAnalytics {
        return AffiliateAnalytics(context as AbstractionRouter, userSessionInterface)
    }

    @Provides
    @CreatePostScope
    internal fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}
