package com.tokopedia.mediauploader.common.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mediauploader.common.VideoMetaDataExtractor
import com.tokopedia.mediauploader.common.data.entity.UploaderTracker
import com.tokopedia.mediauploader.common.data.store.base.CacheDataStoreImpl
import com.tokopedia.mediauploader.common.data.store.util.Serializer
import com.tokopedia.mediauploader.tracker.TrackerCacheDataStore
import com.tokopedia.mediauploader.tracker.TrackerCacheDataStoreImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object MediaUploaderCommonModule {

    @Provides
    @MediaUploaderQualifier
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideGson() = Gson()

    @Provides
    @MediaUploaderQualifier
    fun provideTrackerCacheDataStore(
        @ApplicationContext context: Context,
        metaDataExtractor: VideoMetaDataExtractor
    ): TrackerCacheDataStore {
        return TrackerCacheDataStoreImpl(
            metaDataExtractor,
            object : CacheDataStoreImpl<UploaderTracker>(context) {
                override fun default(cache: UploaderTracker.() -> Unit) = UploaderTracker().apply(cache)
                override fun read(string: String) = Serializer.read<UploaderTracker>(string)
                override fun write(data: UploaderTracker) = Serializer.write(data)
            }
        )
    }
}
