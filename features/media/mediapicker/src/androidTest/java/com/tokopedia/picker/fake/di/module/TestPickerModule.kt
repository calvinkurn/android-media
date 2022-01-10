package com.tokopedia.picker.fake.di.module

import com.tokopedia.picker.data.repository.AlbumRepository
import com.tokopedia.picker.data.repository.MediaRepository
import com.tokopedia.picker.di.scope.PickerScope
import com.tokopedia.picker.fake.data.repository.TestAlbumRepository
import com.tokopedia.picker.fake.data.repository.TestMediaRepository
import dagger.Module
import dagger.Provides

@Module
class TestPickerModule {

    @Provides
    @PickerScope
    fun provideAlbumRepository(): AlbumRepository {
        return TestAlbumRepository()
    }

    @Provides
    @PickerScope
    fun provideMediaRepository(): MediaRepository {
        return TestMediaRepository()
    }

}