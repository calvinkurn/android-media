package com.tokopedia.play.broadcaster.di

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
    @ViewModelKey(BroadcastScheduleViewModel::class)
    abstract fun getSetupBroadcastScheduleViewModel(viewModel: BroadcastScheduleViewModel): ViewModel
}