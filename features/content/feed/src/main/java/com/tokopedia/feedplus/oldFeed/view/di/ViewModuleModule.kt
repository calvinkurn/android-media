package com.tokopedia.feedplus.oldFeed.view.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.feedcomponent.di.FeedComponentViewModelModule
import com.tokopedia.feedplus.oldFeed.view.presenter.FeedDetailViewModel
import com.tokopedia.feedplus.oldFeed.view.presenter.FeedViewModel
import com.tokopedia.videoTabComponent.viewmodel.PlayFeedVideoTabViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by yoasfs on 2019-09-18
 */

@Module(includes = [FeedComponentViewModelModule::class])
abstract class ViewModelModule {

    @FeedPlusScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    internal abstract fun feedViewModel(viewModel: FeedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedDetailViewModel::class)
    internal abstract fun feedDetailViewModel(viewModel: FeedDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayFeedVideoTabViewModel::class)
    internal abstract fun playFeedVideoTabViewModel(viewModel: PlayFeedVideoTabViewModel): ViewModel
}
