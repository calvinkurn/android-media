package com.tokopedia.media.picker.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.analytics.PickerAnalytics
import com.tokopedia.media.picker.analytics.camera.CameraAnalytics
import com.tokopedia.media.picker.analytics.camera.CameraAnalyticsImpl
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalytics
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalyticsImpl
import com.tokopedia.media.picker.data.loader.LoaderDataSource
import com.tokopedia.media.picker.data.loader.LoaderDataSourceImpl
import com.tokopedia.media.picker.data.repository.AlbumRepository
import com.tokopedia.media.picker.data.repository.DeviceInfoRepository
import com.tokopedia.media.picker.data.repository.MediaRepository
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
    fun providePickerAnalytics(
        cameraAnalytics: CameraAnalytics,
        galleryAnalytics: GalleryAnalytics
    ) = PickerAnalytics(
        cameraAnalytics,
        galleryAnalytics
    )

    @Provides
    @ActivityScope
    fun provideGalleryAnalytics(
        userSession: UserSessionInterface,
        paramCacheManager: ParamCacheManager
    ): GalleryAnalytics {
        return GalleryAnalyticsImpl(userSession, paramCacheManager)
    }

    @Provides
    @ActivityScope
    fun provideCameraAnalytics(
        userSession: UserSessionInterface,
        paramCacheManager: ParamCacheManager
    ): CameraAnalytics {
        return CameraAnalyticsImpl(userSession, paramCacheManager)
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

}