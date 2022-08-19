package com.tokopedia.centralizedpromoold.di

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.centralizedpromoold.view.viewmodel.CentralizedPromoViewModelOld
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CentralizedPromoViewModelModuleOld {
    @Binds
    @IntoMap
    @ViewModelKey(CentralizedPromoViewModelOld::class)
    abstract fun centralizedPromoViewModel(centralizedPromoViewModelOld: CentralizedPromoViewModelOld): ViewModel
}