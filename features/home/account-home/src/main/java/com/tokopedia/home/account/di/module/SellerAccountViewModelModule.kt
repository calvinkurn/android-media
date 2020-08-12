package com.tokopedia.home.account.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.home.account.di.scope.SellerAccountScope
import com.tokopedia.home.account.revamp.viewmodel.SellerAccountViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SellerAccountViewModelModule {

    @Binds
    @SellerAccountScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SellerAccountViewModel::class)
    abstract fun sellerAccountViewModel(viewModel: SellerAccountViewModel): ViewModel
}