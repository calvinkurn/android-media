package com.tokopedia.media.picker.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.common.data.repository.TestAlbumRepository
import com.tokopedia.media.picker.common.data.repository.TestDeviceInfoRepository
import com.tokopedia.media.picker.common.data.repository.TestMediaRepository
import com.tokopedia.media.picker.data.loader.LoaderDataSource
import com.tokopedia.media.picker.data.loader.LoaderDataSourceImpl
import com.tokopedia.media.picker.data.repository.AlbumRepository
import com.tokopedia.media.picker.data.repository.DeviceInfoRepository
import com.tokopedia.media.picker.data.repository.MediaRepository
import dagger.Module
import dagger.Provides

@Module
class TestPickerModule {

    @Provides
    @ActivityScope
    fun provideLoaderDataSource(
        @ApplicationContext context: Context,
        cacheManager: ParamCacheManager
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