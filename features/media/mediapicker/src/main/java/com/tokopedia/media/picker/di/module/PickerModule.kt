package com.tokopedia.media.picker.di.module

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.picker.data.FeatureToggleManager
import com.tokopedia.media.picker.data.FeatureToggleManagerImpl
import com.tokopedia.media.picker.data.loader.LoaderDataSource
import com.tokopedia.media.picker.data.loader.LoaderDataSourceImpl
import com.tokopedia.media.picker.data.repository.*
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.cache.PickerParamCacheManager
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object PickerModule {

    @Provides
    @ActivityScope
    fun provideFeatureToggleManager(): FeatureToggleManager {
        return FeatureToggleManagerImpl()
    }

    @Provides
    fun providePickerCacheManager(
        @ApplicationContext context: Context,
        gson: Gson
    ): PickerCacheManager {
        return PickerParamCacheManager(context, gson)
    }

    @Provides
    @ActivityScope
    fun provideUserSession(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ActivityScope
    fun provideLoaderDataSource(
        @ApplicationContext context: Context,
        cacheManager: PickerCacheManager
    ) : LoaderDataSource {
        return LoaderDataSourceImpl(context, cacheManager)
    }

    @Provides
    @ActivityScope
    fun provideDeviceInfoRepository(
        dispatcher: CoroutineDispatchers
    ) = DeviceInfoRepository(dispatcher)

    @Provides
    @ActivityScope
    fun provideAlbumRepository(
        loaderDataSource: LoaderDataSource,
        dispatcher: CoroutineDispatchers
    ) = AlbumRepository(
        loaderDataSource,
        dispatcher
    )

    @Provides
    @ActivityScope
    fun provideMediaRepository(
        loaderDataSource: LoaderDataSource,
        dispatcher: CoroutineDispatchers
    ): MediaRepository {
        return MediaRepository(
            loaderDataSource,
            dispatcher
        )
    }

    @Provides
    @ActivityScope
    fun provideCreateMediaRepository(): CreateMediaRepository {
        return CreateMediaRepositoryImpl()
    }

    @Provides
    @ActivityScope
    fun provideBitmapConverterRepository(
        @ApplicationContext context: Context
    ): BitmapConverterRepository {
        return BitmapConverterRepositoryImpl(context)
    }

}
