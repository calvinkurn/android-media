package com.tokopedia.buy_more_get_more.minicart.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.buy_more_get_more.minicart.presentation.viewmodel.BmgmMiniCartDetailViewModel
import com.tokopedia.buy_more_get_more.minicart.presentation.viewmodel.BmgmMiniCartViewModel
import com.tokopedia.buy_more_get_more.minicart.presentation.viewmodel.MiniCartEditorViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(MiniCartEditorViewModel::class)
    internal abstract fun provideMiniCartEditorViewModel(viewModel: MiniCartEditorViewModel): ViewModel
}