package com.tokopedia.logisticseller.ui.confirmshipping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticseller.ui.confirmshipping.ui.ConfirmShippingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by fwidjaja on 2019-11-15.
 */
@Module
abstract class ConfirmShippingViewModelModule {
    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(ConfirmShippingViewModel::class)
    internal abstract fun somConfirmShippingViewModel(viewModel: ConfirmShippingViewModel): ViewModel
}
