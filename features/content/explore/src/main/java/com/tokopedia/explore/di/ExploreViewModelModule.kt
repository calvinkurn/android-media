package com.tokopedia.explore.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.explore.view.viewmodel.HashtagLandingPageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ExploreViewModelModule {

    @ExploreScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HashtagLandingPageViewModel::class)
    internal abstract fun hashtagLandingPageViewModel(viewModel: HashtagLandingPageViewModel): ViewModel

}