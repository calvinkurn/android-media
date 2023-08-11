package com.tokopedia.feedplus.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.feedcomponent.presentation.viewmodel.FeedProductItemInfoViewModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedPostViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
@Module
abstract class FeedMainViewModelModule {

    @FeedMainScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

//    @Binds
//    @IntoMap
//    @ViewModelKey(FeedMainViewModel::class)
//    internal abstract fun feedMainViewModel(viewModel: FeedMainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedProductItemInfoViewModel::class)
    abstract fun feedProductItemInfoViewModel(viewModel: FeedProductItemInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedPostViewModel::class)
    internal abstract fun feedPostViewModel(viewModel: FeedPostViewModel): ViewModel
}
