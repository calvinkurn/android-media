package com.tokopedia.mediauploader.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.common.data.store.datastore.AnalyticsCacheDataStore
import com.tokopedia.mediauploader.video.internal.VideoMetaDataExtractor
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.mediauploader.data.repository.LogRepository
import com.tokopedia.mediauploader.data.repository.LogRepositoryImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object MediaUploaderDebugModule {

    @Provides
    @MediaUploaderTestScope
    fun provideLogRepository(
        @ApplicationContext context: Context,
        @UploaderQualifier videoMetaDataExtractor: VideoMetaDataExtractor,
        @UploaderQualifier trackerCacheStore: AnalyticsCacheDataStore,
    ): LogRepository {
        return LogRepositoryImpl(context, videoMetaDataExtractor, trackerCacheStore)
    }

    @Provides
    @MediaUploaderTestScope
    fun provideUserSession(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @MediaUploaderTestScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}
