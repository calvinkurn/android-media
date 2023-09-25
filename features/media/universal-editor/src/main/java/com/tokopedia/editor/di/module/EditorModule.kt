package com.tokopedia.editor.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.editor.analytics.image.placement.ImagePlacementAnalytics
import com.tokopedia.editor.analytics.image.placement.ImagePlacementAnalyticsImpl
import com.tokopedia.editor.analytics.input.text.InputTextAnalytics
import com.tokopedia.editor.analytics.input.text.InputTextAnalyticsImpl
import com.tokopedia.editor.analytics.main.editor.MainEditorAnalytics
import com.tokopedia.editor.analytics.main.editor.MainEditorAnalyticsImpl
import com.tokopedia.editor.ui.main.EditorParamFetcher
import com.tokopedia.editor.ui.main.EditorParamFetcherImpl
import com.tokopedia.editor.util.provider.ColorProvider
import com.tokopedia.editor.util.provider.ColorProviderImpl
import com.tokopedia.editor.util.provider.ResourceProvider
import com.tokopedia.editor.util.provider.ResourceProviderImpl
import dagger.Binds
import dagger.Module

@Module
abstract class EditorModule {

    @Binds
    @ActivityScope
    internal abstract fun bindEditorParamFetcher(impl: EditorParamFetcherImpl): EditorParamFetcher

    @Binds
    @ActivityScope
    internal abstract fun bindColorProvider(impl: ColorProviderImpl): ColorProvider

    @Binds
    @ActivityScope
    internal abstract fun bindResourceProvider(impl: ResourceProviderImpl): ResourceProvider

    @Binds
    @ActivityScope
    internal abstract fun bindMainEditorAnalyticsProvider(impl: MainEditorAnalyticsImpl): MainEditorAnalytics

    @Binds
    @ActivityScope
    internal abstract fun bindInputTextAnalyticsProvider(impl: InputTextAnalyticsImpl): InputTextAnalytics

    @Binds
    @ActivityScope
    internal abstract fun bindImagePlacementAnalyticsProvider(impl: ImagePlacementAnalyticsImpl): ImagePlacementAnalytics
}
