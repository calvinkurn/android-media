package com.tokopedia.createpost.common.di

import android.app.NotificationManager
import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics
import com.tokopedia.createpost.common.di.qualifier.CreatePostCommonDispatchers
import com.tokopedia.createpost.common.di.qualifier.SubmitPostCoroutineScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
import kotlinx.coroutines.CoroutineScope

/**
 * @author by milhamj on 9/26/18.
 */
@Module(includes = [VideoUploaderModule::class])
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
    fun provideGraphQlRepository(): GraphqlRepository {
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

    @Provides
    @CreatePostScope
    @CreatePostCommonDispatchers
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }

    @Provides
    @CreatePostScope
    @SubmitPostCoroutineScope
    fun provideSubmitPostCoroutineScope(
        @CreatePostCommonDispatchers dispatchers: CoroutineDispatchers
    ): CoroutineScope {
        return CoroutineScope(dispatchers.io)
    }
}
