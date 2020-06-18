package com.tokopedia.play.broadcaster.di.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastCoverSetupViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlaySearchSuggestionsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PlayBroadcastSetupViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PlayEtalasePickerViewModel::class)
    abstract fun getPlayEtalasePickerViewModel(viewModel: PlayEtalasePickerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayBroadcastCoverSetupViewModel::class)
    abstract fun getPlayBroadcastCoverTitleViewModel(viewModel: PlayBroadcastCoverSetupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlaySearchSuggestionsViewModel::class)
    abstract fun getPlaySearchSuggestionsViewModel(viewModel: PlaySearchSuggestionsViewModel): ViewModel

}