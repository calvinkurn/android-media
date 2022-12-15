package com.tokopedia.media.picker.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.data.loader.LoaderDataSource
import com.tokopedia.media.picker.data.loader.LoaderDataSourceImpl
import com.tokopedia.media.picker.data.repository.*
import com.tokopedia.media.picker.data.repository.CreateMediaRepositoryImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object PickerModule {

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
        cacheManager: ParamCacheManager
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
