package com.tokopedia.play.broadcaster.di.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.play.broadcaster.view.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PlayBroadcastSetupViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PlayBroadcastPrepareViewModel::class)
    abstract fun getPlayPrepareViewModel(viewModel: PlayBroadcastPrepareViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayCoverSetupViewModel::class)
    abstract fun getPlayBroadcastCoverTitleViewModel(viewModel: PlayCoverSetupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DataStoreViewModel::class)
    abstract fun getDataStoreViewModel(viewModel: DataStoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BroadcastScheduleViewModel::class)
    abstract fun getSetupBroadcastScheduleViewModel(viewModel: BroadcastScheduleViewModel): ViewModel
}