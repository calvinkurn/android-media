package com.tokopedia.media.picker.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.picker.data.repository.*
import com.tokopedia.media.picker.di.scope.PickerScope
import dagger.Module
import dagger.Provides

@Module
object PickerModule {

    @Provides
    @PickerScope
    fun provideDeviceInfoRepository(): DeviceInfoRepository {
        return DeviceInfoRepositoryImpl()
    }

    @Provides
    @PickerScope
    fun provideAlbumRepository(
        @ApplicationContext context: Context
    ): AlbumRepository {
        return AlbumRepositoryImpl(context)
    }

    @Provides
    @PickerScope
    fun provideMediaRepository(
        @ApplicationContext context: Context
    ): MediaRepository {
        return MediaRepositoryImpl(context)
    }

}