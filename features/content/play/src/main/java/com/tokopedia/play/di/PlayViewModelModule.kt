package com.tokopedia.play.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.play.view.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by jegul on 03/12/19
 */
@Module
abstract class PlayViewModelModule {

    @Binds
    @PlayScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PlayViewModel::class)
    abstract fun getPlayViewModel(viewModel: PlayViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayInteractionViewModel::class)
    abstract fun getPlayInteractionViewModel(viewModel: PlayInteractionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayBottomSheetViewModel::class)
    abstract fun getPlayVariantViewModel(viewModel: PlayBottomSheetViewModel): ViewModel
}