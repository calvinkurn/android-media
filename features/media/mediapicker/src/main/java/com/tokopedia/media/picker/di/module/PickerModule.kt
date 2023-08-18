package com.tokopedia.media.picker.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.picker.data.MediaQueryDataSource
import com.tokopedia.media.picker.data.MediaQueryDataSourceImpl
import com.tokopedia.media.picker.data.repository.*
import com.tokopedia.media.picker.ui.publisher.PickerEventBus
import com.tokopedia.media.picker.ui.publisher.PickerEventBusImpl
import com.tokopedia.media.picker.utils.internal.NetworkStateManager
import com.tokopedia.media.picker.utils.internal.NetworkStateManagerImpl
import com.tokopedia.media.picker.utils.internal.ResourceManager
import com.tokopedia.media.picker.utils.internal.ResourceManagerImpl
import dagger.Binds
import dagger.Module

@Module
abstract class PickerModule {

    @Binds
    @ActivityScope
    abstract fun providePickerEventBus(source: PickerEventBusImpl) : PickerEventBus

    @Binds
    @ActivityScope
    abstract fun provideMediaQueryDataSource(source: MediaQueryDataSourceImpl) : MediaQueryDataSource

    @Binds
    @ActivityScope
    abstract fun provideBucketAlbumRepository(repository: BucketAlbumRepositoryImpl): BucketAlbumRepository

    @Binds
    @ActivityScope
    abstract fun provideMediaFileRepository(repository: MediaFileRepositoryImpl): MediaFileRepository

    @Binds
    @ActivityScope
    abstract fun provideDeviceInfoRepository(repository: DeviceInfoRepositoryImpl): DeviceInfoRepository

    @Binds
    @ActivityScope
    abstract fun provideCreateMediaRepository(repository: CreateMediaRepositoryImpl): CreateMediaRepository

    @Binds
    @ActivityScope
    abstract fun provideBitmapConverterRepository(repository: BitmapConverterRepositoryImpl): BitmapConverterRepository

    @Binds
    @ActivityScope
    abstract fun provideNetworkStateManager(manager: NetworkStateManagerImpl): NetworkStateManager

    @Binds
    @ActivityScope
    abstract fun provideResourceManager(manager: ResourceManagerImpl): ResourceManager
}
