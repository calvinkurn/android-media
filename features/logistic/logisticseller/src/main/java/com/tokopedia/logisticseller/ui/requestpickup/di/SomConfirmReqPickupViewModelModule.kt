package com.tokopedia.logisticseller.ui.requestpickup.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticseller.ui.requestpickup.presentation.viewmodel.RequestPickupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by fwidjaja on 2019-09-30.
 */

@Module
abstract class SomConfirmReqPickupViewModelModule {
    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(RequestPickupViewModel::class)
    internal abstract fun somConfirmRequestPickupViewModel(viewModel: RequestPickupViewModel): ViewModel
}
