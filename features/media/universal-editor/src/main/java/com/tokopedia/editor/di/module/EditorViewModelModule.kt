package com.tokopedia.editor.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.editor.ui.main.MainEditorViewModel
import com.tokopedia.editor.ui.text.InputTextViewModel
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
    @ViewModelKey(MainEditorViewModel::class)
    internal abstract fun getMainEditorViewModel(viewModel: MainEditorViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(InputTextViewModel::class)
    internal abstract fun getInputTextViewModel(viewModel: InputTextViewModel): ViewModel

}
