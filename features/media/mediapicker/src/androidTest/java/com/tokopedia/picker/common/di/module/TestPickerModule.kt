package com.tokopedia.picker.common.di.module

import com.tokopedia.picker.data.repository.AlbumRepository
import com.tokopedia.picker.data.repository.MediaRepository
import com.tokopedia.picker.di.scope.PickerScope
import com.tokopedia.picker.common.data.repository.TestAlbumRepository
import com.tokopedia.picker.common.data.repository.TestMediaRepository
import dagger.Module
import dagger.Provides

@Module
class TestPickerModule {

    @Provides
    @PickerScope
    fun provideAlbumRepository(
        stub: TestAlbumRepository
    ): AlbumRepository = stub

    @Provides
    @PickerScope
    fun provideMediaRepository(
        stub: TestMediaRepository
    ): MediaRepository = stub

    // -- mock repository -- //

    @Provides
    @PickerScope
    fun provideTestAlbumRepository() = TestAlbumRepository()

    @Provides
    @PickerScope
    fun provideTestMediaRepository() = TestMediaRepository()

}