package com.tokopedia.media.editor.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.editor.analytics.editordetail.EditorDetailAnalytics
import com.tokopedia.media.editor.analytics.editordetail.EditorDetailAnalyticsImpl
import com.tokopedia.media.editor.analytics.editorhome.EditorHomeAnalytics
import com.tokopedia.media.editor.analytics.editorhome.EditorHomeAnalyticsImpl
import com.tokopedia.media.editor.data.repository.*
import com.tokopedia.picker.common.cache.EditorAddLogoCacheManager
import com.tokopedia.picker.common.cache.EditorAddLogoCacheManagerImpl
import com.tokopedia.picker.common.cache.EditorCacheManager
import com.tokopedia.picker.common.cache.EditorParamCacheManager
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.cache.PickerParamCacheManager
import dagger.Binds
import dagger.Module

@Module
abstract class EditorModule {

    @Binds
    @ActivityScope
    internal abstract fun provideEditorPickerCacheManager(
        manager: EditorParamCacheManager
    ): EditorCacheManager

    @Binds
    @ActivityScope
    internal abstract fun providePickerPickerCacheManager(
        manager: PickerParamCacheManager
    ): PickerCacheManager

    @Binds
    @ActivityScope
    internal abstract fun provideEditorAddLogoCacheManager(
        manager: EditorAddLogoCacheManagerImpl
    ): EditorAddLogoCacheManager

    @Binds
    @ActivityScope
    internal abstract fun provideBitmapRepository(
        repository: BitmapConverterRepositoryImpl
    ): BitmapConverterRepository

    @Binds
    @ActivityScope
    internal abstract fun provideEditorRepository(
        repository: RemoveBackgroundRepositoryImpl
    ): RemoveBackgroundRepository

    @Binds
    @ActivityScope
    internal abstract fun provideColorFilterRepository(
        repository: ColorFilterRepositoryImpl
    ): ColorFilterRepository

    @Binds
    @ActivityScope
    internal abstract fun provideContrastFilterRepository(
        repository: ContrastFilterRepositoryImpl
    ): ContrastFilterRepository

    @Binds
    @ActivityScope
    internal abstract fun provideWatermarkFilterRepository(
        repository: WatermarkFilterRepositoryImpl
    ): WatermarkFilterRepository

    @Binds
    @ActivityScope
    internal abstract fun provideRotateFilterRepository(
        repository: RotateFilterRepositoryImpl
    ): RotateFilterRepository

    @Binds
    @ActivityScope
    internal abstract fun provideAddLogoFilterRepository(
        repository: AddLogoFilterRepositoryImpl
    ): AddLogoFilterRepository

    @Binds
    @ActivityScope
    internal abstract fun provideSaveImageRepository(
        repository: SaveImageRepositoryImpl
    ): SaveImageRepository

    @Binds
    @ActivityScope
    internal abstract fun provideSaveEditorHomeAnalytics(
        analytics: EditorHomeAnalyticsImpl
    ): EditorHomeAnalytics

    @Binds
    @ActivityScope
    internal abstract fun provideSaveEditorDetailAnalytics(
        analytics: EditorDetailAnalyticsImpl
    ): EditorDetailAnalytics

}
