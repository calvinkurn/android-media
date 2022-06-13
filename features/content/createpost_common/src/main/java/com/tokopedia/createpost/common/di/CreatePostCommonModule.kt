package com.tokopedia.createpost.common.di

import android.app.NotificationManager
import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics
import com.tokopedia.createpost.common.data.pojo.uploadimage.UploadImageResponse
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.twitter_share.TwitterManager
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videouploader.data.UploadVideoApi
import com.tokopedia.videouploader.di.VideoUploaderModule
import com.tokopedia.videouploader.di.VideoUploaderQualifier
import com.tokopedia.videouploader.domain.pojo.DefaultUploadVideoResponse
import com.tokopedia.videouploader.domain.usecase.GenerateVideoTokenUseCase
import com.tokopedia.videouploader.domain.usecase.UploadVideoUseCase
import dagger.Module
import dagger.Provides

/**
 * @author by milhamj on 9/26/18.
 */
@Module(includes = [ImageUploaderModule::class, VideoUploaderModule::class])
class CreatePostCommonModule(private val context: Context) {

    @Provides
    @ActivityContext
    fun provideActivityContext(): Context {
        return context
    }

    @Provides
    @ApplicationContext
    fun provideApplicationContext(): Context {
        return context.applicationContext
    }

    @Provides
    @CreatePostScope
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: UserSessionInterface,
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
    fun provideAffiliateAnalytics(userSessionInterface: UserSessionInterface): AffiliateAnalytics {
        return AffiliateAnalytics(userSessionInterface)
    }
    @Provides
    @CreatePostScope
    fun provideCreatePostAnalytics(userSessionInterface: UserSessionInterface): CreatePostAnalytics {
        return CreatePostAnalytics(userSessionInterface)
    }

    @Provides
    @CreatePostScope
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    @CreatePostScope
    fun provideUploadVideoUseCase(
            @VideoUploaderQualifier uploadVideoApi: UploadVideoApi,
            @VideoUploaderQualifier gson: Gson,
            generateVideoTokenUseCase: GenerateVideoTokenUseCase):
            UploadVideoUseCase<DefaultUploadVideoResponse> {
        return UploadVideoUseCase(uploadVideoApi, gson, DefaultUploadVideoResponse::class.java, generateVideoTokenUseCase)
    }

    @Provides
    @CreatePostScope
    fun provideGraphQlRepository(@ApplicationContext context: Context): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @CreatePostScope
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @Provides
    @CreatePostScope
    fun provideTwitterManager(userSession: UserSessionInterface): TwitterManager {
        return TwitterManager(userSession)
    }

    @Provides
    @CreatePostScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}
