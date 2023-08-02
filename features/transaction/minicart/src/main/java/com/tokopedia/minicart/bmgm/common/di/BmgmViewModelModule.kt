package com.tokopedia.minicart.bmgm.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.minicart.bmgm.presentation.viewmodel.BmgmCartWidgetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

@Module
abstract class BmgmViewModelModule {

    @BmgmMiniCartScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BmgmCartWidgetViewModel::class)
    internal abstract fun provideViewModel(viewModel: BmgmCartWidgetViewModel): ViewModel
}