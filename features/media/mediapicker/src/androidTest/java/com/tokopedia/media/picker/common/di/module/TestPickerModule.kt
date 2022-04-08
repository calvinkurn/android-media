package com.tokopedia.media.picker.common.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.picker.common.data.repository.TestAlbumRepository
import com.tokopedia.media.picker.common.data.repository.TestMediaRepository
import com.tokopedia.media.picker.data.repository.AlbumRepository
import com.tokopedia.media.picker.data.repository.MediaRepository
import dagger.Module
import dagger.Provides

@Module
class TestPickerModule {

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

    // -- mock repository -- //

    @Provides
    @ActivityScope
    fun provideTestAlbumRepository() = TestAlbumRepository()

    @Provides
    @ActivityScope
    fun provideTestMediaRepository() = TestMediaRepository()

}