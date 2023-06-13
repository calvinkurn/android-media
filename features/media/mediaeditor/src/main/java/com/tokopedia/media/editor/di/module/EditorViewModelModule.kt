package com.tokopedia.media.editor.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorViewModel
import com.tokopedia.media.editor.ui.activity.main.EditorViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EditorViewModelModule {

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(DetailEditorViewModel::class)
    internal abstract fun getDetailEditorViewModel(viewModel: DetailEditorViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(EditorViewModel::class)
    internal abstract fun getEditorViewModel(viewModel: EditorViewModel): ViewModel

}