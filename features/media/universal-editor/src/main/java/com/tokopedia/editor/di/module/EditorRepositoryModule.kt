package com.tokopedia.editor.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.editor.data.repository.ImageFlattenRepository
import com.tokopedia.editor.data.repository.ImageFlattenRepositoryImpl
import com.tokopedia.editor.data.repository.ImageSaveRepository
import com.tokopedia.editor.data.repository.ImageSaveRepositoryImpl
import com.tokopedia.editor.data.repository.NavigationToolRepository
import com.tokopedia.editor.data.repository.NavigationToolRepositoryImpl
import com.tokopedia.editor.data.repository.VideoExtractMetadataRepository
import com.tokopedia.editor.data.repository.VideoExtractMetadataRepositoryImpl
import com.tokopedia.editor.data.repository.VideoFlattenRepository
import com.tokopedia.editor.data.repository.VideoFlattenRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class EditorRepositoryModule {

    @Binds
    @ActivityScope
    internal abstract fun bindNavigationToolRepository(
        impl: NavigationToolRepositoryImpl
    ): NavigationToolRepository

    @Binds
    @ActivityScope
    internal abstract fun bindImageSaveRepository(
        impl: ImageSaveRepositoryImpl
    ): ImageSaveRepository

    @Binds
    @ActivityScope
    internal abstract fun bindVideoFlattenRepository(
        impl: VideoFlattenRepositoryImpl
    ): VideoFlattenRepository

    @Binds
    @ActivityScope
    internal abstract fun bindImageFlattenRepository(
        impl: ImageFlattenRepositoryImpl
    ): ImageFlattenRepository

    @Binds
    @ActivityScope
    internal abstract fun bindVideoExtractMetadataRepository(
        impl: VideoExtractMetadataRepositoryImpl
    ): VideoExtractMetadataRepository
}
