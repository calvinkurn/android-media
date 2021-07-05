package com.tokopedia.kol.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.kol.feature.video.view.viewmodel.FeedMediaPreviewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class KolViewModelModule {

    @KolScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FeedMediaPreviewViewModel::class)
    internal abstract fun feedMediaPreviewViewModel(viewModel: FeedMediaPreviewViewModel): ViewModel
}