package com.tokopedia.media.picker.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.picker.analytics.PickerAnalytics
import com.tokopedia.media.picker.analytics.camera.CameraAnalytics
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalytics
import com.tokopedia.media.picker.common.analytics.TestCameraAnalytics
import com.tokopedia.media.picker.common.analytics.TestGalleryAnalytics
import com.tokopedia.media.picker.common.analytics.TestPickerAnalytics
import com.tokopedia.media.picker.common.data.repository.TestAlbumRepository
import com.tokopedia.media.picker.common.data.repository.TestDeviceInfoRepository
import com.tokopedia.media.picker.common.data.repository.TestMediaRepository
import com.tokopedia.media.picker.data.loader.LoaderDataSource
import com.tokopedia.media.picker.data.loader.LoaderDataSourceImpl
import com.tokopedia.media.picker.data.repository.AlbumRepository
import com.tokopedia.media.picker.data.repository.DeviceInfoRepository
import com.tokopedia.media.picker.data.repository.MediaRepository
import com.tokopedia.picker.common.cache.PickerCacheManager
import dagger.Module
import dagger.Provides

@Module
object TestPickerModule {

    @Provides
    @ActivityScope
    fun provideTestCameraAnalytics(): CameraAnalytics {
        return TestCameraAnalytics()
    }

    @Provides
    @ActivityScope
    fun provideTestGalleryAnalytics(): GalleryAnalytics {
        return TestGalleryAnalytics()
    }

    @Provides
    @ActivityScope
    fun providePickerAnalytics(
        cameraAnalytics: CameraAnalytics,
        galleryAnalytics: GalleryAnalytics
    ): PickerAnalytics {
        return TestPickerAnalytics(
            cameraAnalytics,
            galleryAnalytics
        )
    }

    @Provides
    @ActivityScope
    fun provideLoaderDataSource(
        @ApplicationContext context: Context,
        cacheManager: PickerCacheManager
    ) : LoaderDataSource {
        return LoaderDataSourceImpl(context, cacheManager)
    }

    // -- stub test repository -- //

    @Provides
    @ActivityScope
    fun provideTestAlbumRepository(
        loaderDataSource: LoaderDataSource,
        dispatcher: CoroutineDispatchers
    ) = TestAlbumRepository(loaderDataSource, dispatcher)

    @Provides
    @ActivityScope
    fun provideTestMediaRepository(
        loaderDataSource: LoaderDataSource,
        dispatcher: CoroutineDispatchers
    ) = TestMediaRepository(loaderDataSource, dispatcher)

    @Provides
    @ActivityScope
    fun provideTestDeviceInfoRepository(
        dispatcher: CoroutineDispatchers
    ) = TestDeviceInfoRepository(dispatcher)

    // -- stub real repository -- //

    @Provides
    @ActivityScope
    fun provideAlbumRepository(
        stub: TestAlbumRepository
    ): AlbumRepository = stub

    @Provides
    @ActivityScope
    fun provideMediaRepository(
        stub: TestMediaRepository
    ): MediaRepository = stub

    @Provides
    @ActivityScope
    fun provideDeviceInfoRepository(
        stub: TestDeviceInfoRepository
    ): DeviceInfoRepository = stub

}
