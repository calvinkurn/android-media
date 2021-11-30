package com.tokopedia.home_account.linkaccount.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.linkaccount.viewmodel.LinkAccountViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module abstract class LinkAccountViewModelModule {

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(
            viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LinkAccountViewModel::class)
    internal abstract fun bindLinkAccountViewModel(
            viewModel: LinkAccountViewModel
    ): ViewModel

}