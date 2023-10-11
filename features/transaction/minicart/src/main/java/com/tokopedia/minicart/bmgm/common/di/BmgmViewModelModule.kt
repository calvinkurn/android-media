package com.tokopedia.minicart.bmgm.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.minicart.bmgm.presentation.viewmodel.BmgmMiniCartDetailViewModel
import com.tokopedia.minicart.bmgm.presentation.viewmodel.BmgmMiniCartViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

@Module
abstract class BmgmViewModelModule {

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BmgmMiniCartViewModel::class)
    internal abstract fun provideMiniCartViewModel(viewModel: BmgmMiniCartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BmgmMiniCartDetailViewModel::class)
    internal abstract fun provideMiniCartDetailViewModel(viewModel: BmgmMiniCartDetailViewModel): ViewModel
}