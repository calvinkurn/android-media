package com.tokopedia.editor.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
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
}
