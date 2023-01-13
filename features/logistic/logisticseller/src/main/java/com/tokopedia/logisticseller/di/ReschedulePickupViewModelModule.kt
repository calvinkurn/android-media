package com.tokopedia.logisticseller.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticseller.ui.reschedulepickup.ReschedulePickupComposeViewModel
import com.tokopedia.logisticseller.ui.reschedulepickup.ReschedulePickupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReschedulePickupViewModelModule {
    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(ReschedulePickupViewModel::class)
    internal abstract fun provideReschedulePickupViewModel(viewModel: ReschedulePickupViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(ReschedulePickupComposeViewModel::class)
    internal abstract fun provideReschedulePickupComposeViewModel(viewModel: ReschedulePickupComposeViewModel): ViewModel
}
