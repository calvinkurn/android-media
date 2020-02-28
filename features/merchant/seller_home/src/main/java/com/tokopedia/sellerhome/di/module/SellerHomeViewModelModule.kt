package com.tokopedia.sellerhome.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeActivityViewModel
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

@SellerHomeScope
@Module
abstract class SellerHomeViewModelModule {

    @Binds
    @SellerHomeScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SellerHomeViewModel::class)
    abstract fun sellerHomeViewModel(sellerHomeViewModel: SellerHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SellerHomeActivityViewModel::class)
    abstract fun sellerHomeActivityViewModel(viewModel: SellerHomeActivityViewModel): ViewModel
}