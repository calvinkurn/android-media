package com.tokopedia.centralizedpromo.di

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.centralizedpromo.view.viewmodel.CentralizedPromoViewModel
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CentralizedPromoViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CentralizedPromoViewModel::class)
    abstract fun centralizedPromoViewModel(centralizedPromoViewModel: CentralizedPromoViewModel): ViewModel
}