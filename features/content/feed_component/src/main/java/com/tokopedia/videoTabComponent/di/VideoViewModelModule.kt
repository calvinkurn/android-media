package com.tokopedia.videoTabComponent.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.videoTabComponent.viewmodel.PlayFeedVideoTabViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by yoasfs on 2019-09-18
 */

@Module
abstract class VideoViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PlayFeedVideoTabViewModel::class)
    internal abstract fun playFeedVideoTabViewModel(viewModel: PlayFeedVideoTabViewModel): ViewModel



}