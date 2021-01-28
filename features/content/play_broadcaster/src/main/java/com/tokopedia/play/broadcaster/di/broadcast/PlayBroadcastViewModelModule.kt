package com.tokopedia.play.broadcaster.di.broadcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.play.broadcaster.view.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by jegul on 20/05/20
 */
@Module
abstract class PlayBroadcastViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PlayBroadcastPrepareViewModel::class)
    abstract fun getPlayPrepareViewModel(viewModel: PlayBroadcastPrepareViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayBroadcastViewModel::class)
    abstract fun getPlayBroadcastViewModel(viewModel: PlayBroadcastViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayBroadcastSummaryViewModel::class)
    abstract fun getPlaySummaryViewModel(viewModel: PlayBroadcastSummaryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayEtalasePickerViewModel::class)
    abstract fun getPlayEtalasePickerViewModel(viewModel: PlayEtalasePickerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlaySearchSuggestionsViewModel::class)
    abstract fun getPlaySearchSuggestionsViewModel(viewModel: PlaySearchSuggestionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BroadcastScheduleViewModel::class)
    abstract fun getSetupBroadcastScheduleViewModel(viewModel: BroadcastScheduleViewModel): ViewModel
}