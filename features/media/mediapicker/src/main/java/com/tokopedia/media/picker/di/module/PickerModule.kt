package com.tokopedia.media.picker.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.picker.data.FeatureToggleManager
import com.tokopedia.media.picker.data.FeatureToggleManagerImpl
import com.tokopedia.media.picker.data.MediaQueryDataSource
import com.tokopedia.media.picker.data.MediaQueryDataSourceImpl
import com.tokopedia.media.picker.data.repository.*
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.cache.PickerParamCacheManager
import dagger.Binds
import dagger.Module

@Module
abstract class PickerModule {

    @Binds
    @ActivityScope
    abstract fun provideFeatureToggleManager(source: FeatureToggleManagerImpl) : FeatureToggleManager

    @Binds
    @ActivityScope
    abstract fun providePickerCacheManager(source: PickerParamCacheManager) : PickerCacheManager

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
}
