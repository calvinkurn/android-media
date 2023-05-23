package com.tokopedia.mediauploader.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.common.VideoMetaDataExtractor
import com.tokopedia.mediauploader.common.VideoMetaDataExtractorImpl
import com.tokopedia.mediauploader.tracker.TrackerCacheDataStore
import com.tokopedia.mediauploader.tracker.TrackerCacheDataStoreImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object MediaUploaderDebugModule {

    @Provides
    @MediaUploaderTestScope
    fun provideGson() = Gson()

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

    @Provides
    @MediaUploaderTestScope
    fun provideVideoMetaDataExtractor(
        @ApplicationContext context: Context
    ): VideoMetaDataExtractor {
        return VideoMetaDataExtractorImpl(context)
    }

    @Provides
    @MediaUploaderTestScope
    fun provideTrackerCacheDataSource(
        @ApplicationContext context: Context,
        metaDataExtractor: VideoMetaDataExtractor,
        gson: Gson
    ): TrackerCacheDataStore {
        return TrackerCacheDataStoreImpl(context, metaDataExtractor, gson)
    }
}
