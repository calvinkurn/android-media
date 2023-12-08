package com.tokopedia.creation.common.upload.di.worker

import android.app.NotificationManager
import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics
import com.tokopedia.createpost.common.di.qualifier.SubmitPostCoroutineScope
import com.tokopedia.creation.common.upload.di.common.CreationUploadDataModule
import com.tokopedia.creation.common.upload.util.logger.CreationUploadLogger
import com.tokopedia.creation.common.upload.util.logger.CreationUploadLoggerImpl
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
 * Created By : Jonathan Darwin on September 18, 2023
 */
@Module(
    includes = [
        CreationUploadDataModule::class,
        VideoUploaderModule::class
    ]
)
object CreationUploadWorkerModule {

    /** Need to use this because
     * MediaUploaderModule is not using graphqlRepository
     * from AppModule @ApplicationContext
     * */
    @Provides
    fun provideGraphQLRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideCreatePostAnalytics(userSessionInterface: UserSessionInterface): CreatePostAnalytics {
        return CreatePostAnalytics(userSessionInterface)
    }

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    fun provideUploadVideoUseCase(
        @VideoUploaderQualifier uploadVideoApi: UploadVideoApi,
        @VideoUploaderQualifier gson: Gson,
        generateVideoTokenUseCase: GenerateVideoTokenUseCase
    ):
        UploadVideoUseCase<DefaultUploadVideoResponse> {
        return UploadVideoUseCase(uploadVideoApi, gson, DefaultUploadVideoResponse::class.java, generateVideoTokenUseCase)
    }

    @Provides
    @SubmitPostCoroutineScope
    fun provideSubmitPostCoroutineScope(
        dispatchers: CoroutineDispatchers
    ): CoroutineScope {
        return CoroutineScope(dispatchers.io)
    }

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}
