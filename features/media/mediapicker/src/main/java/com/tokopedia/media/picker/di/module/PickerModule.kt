package com.tokopedia.media.picker.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.picker.data.repository.*
import dagger.Module
import dagger.Provides

@Module
object PickerModule {

    @Provides
    @ActivityScope
    fun provideDeviceInfoRepository(): DeviceInfoRepository {
        return DeviceInfoRepositoryImpl()
    }

    @Provides
    @ActivityScope
    fun provideAlbumRepository(
        @ApplicationContext context: Context
    ): AlbumRepository {
        return AlbumRepositoryImpl(context)
    }

    @Provides
    @ActivityScope
    fun provideMediaRepository(
        @ApplicationContext context: Context
    ): MediaRepository {
        return MediaRepositoryImpl(context)
    }

}