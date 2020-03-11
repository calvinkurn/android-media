package com.tokopedia.sellerhome.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.centralizedpromo.di.CentralizedPromoViewModelModule
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.settings.view.viewmodel.OtherMenuViewModel
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeActivityViewModel
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeViewModel
import com.tokopedia.sellerhome.view.viewmodel.SharedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

@SellerHomeScope
@Module(includes = [CentralizedPromoViewModelModule::class])
abstract class SellerHomeViewModelModule {

    @Binds
    @SellerHomeScope
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SellerHomeViewModel::class)
    abstract fun provideSellerHomeViewModel(sellerHomeViewModel: SellerHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SellerHomeActivityViewModel::class)
    abstract fun provideSellerHomeActivityViewModel(viewModel: SellerHomeActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SharedViewModel::class)
    abstract fun provideSharedViewModel(viewModel: SharedViewModel): ViewModel
    abstract fun sellerHomeViewModel(sellerHomeViewModel: SellerHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OtherMenuViewModel::class)
    abstract fun otherSettingViewModel(otherMenuViewModel: OtherMenuViewModel): ViewModel
}