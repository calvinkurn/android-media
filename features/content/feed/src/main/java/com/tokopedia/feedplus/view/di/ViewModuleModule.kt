package com.tokopedia.feedplus.view.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.feedplus.view.presenter.FeedOnboardingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by yoasfs on 2019-09-18
 */

@Module
@FeedPlusScope
abstract class ViewModelModule {

    @FeedPlusScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FeedOnboardingViewModel::class)
    internal abstract fun feedOnboardingViewModel(viewModel: FeedOnboardingViewModel): ViewModel

}