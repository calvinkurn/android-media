package com.tokopedia.product.detail.postatc.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.detail.postatc.view.PostAtcViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PostAtcViewModelModule {
    @PostAtcScope
    @Binds
    internal abstract fun bindViewModelFatory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @PostAtcScope
    @Binds
    @IntoMap
    @ViewModelKey(PostAtcViewModel::class)
    internal abstract fun providePostAtcViewModel(viewModel: PostAtcViewModel): ViewModel
}
