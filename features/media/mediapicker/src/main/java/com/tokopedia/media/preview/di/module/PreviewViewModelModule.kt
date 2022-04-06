package com.tokopedia.media.preview.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.media.preview.di.scope.PreviewScope
import com.tokopedia.media.preview.ui.activity.PreviewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PreviewViewModelModule {

    @Binds
    @PreviewScope
    internal abstract fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @PreviewScope
    @ViewModelKey(PreviewViewModel::class)
    internal abstract fun getPreviewViewModel(
        viewModel: PreviewViewModel
    ): ViewModel

}