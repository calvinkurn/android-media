package com.tokopedia.picker.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.picker.data.repository.AlbumRepository
import com.tokopedia.picker.data.repository.AlbumRepositoryImpl
import com.tokopedia.picker.data.repository.MediaRepository
import com.tokopedia.picker.data.repository.MediaRepositoryImpl
import com.tokopedia.picker.di.scope.PickerScope
import dagger.Module
import dagger.Provides

@Module
class PickerModule {

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