package com.tokopedia.editor.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.editor.ui.main.EditorParamFetcher
import com.tokopedia.editor.ui.main.EditorParamFetcherImpl
import dagger.Binds
import dagger.Module

@Module
abstract class EditorModule {

    @Binds
    @ActivityScope
    internal abstract fun bindEditorParamFetcher(impl: EditorParamFetcherImpl): EditorParamFetcher
}
