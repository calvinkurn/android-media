package com.tokopedia.centralizedpromo.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.centralizedpromo.di.scope.CentralizedPromoScope
import com.tokopedia.centralizedpromo.view.viewmodel.CentralizedPromoComposeViewModel
import com.tokopedia.centralizedpromo.view.viewmodel.CentralizedPromoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CentralizedPromoViewModelModule {
    @CentralizedPromoScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CentralizedPromoViewModel::class)
    abstract fun centralizedPromoViewModel(centralizedPromoViewModel: CentralizedPromoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CentralizedPromoComposeViewModel::class)
    abstract fun centralizedPromoComposeViewModel(centralizedPromoComposeViewModel: CentralizedPromoComposeViewModel): ViewModel
}
