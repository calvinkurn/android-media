package com.tokopedia.recharge_credit_card.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.recharge_credit_card.viewmodel.RechargeCCViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@RechargeCCScope
abstract class RechargeCCViewModelModule {

    @RechargeCCScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RechargeCCViewModel::class)
    internal abstract fun digitalCustomTelcoViewModel(customViewModel: RechargeCCViewModel): ViewModel

}