package com.tokopedia.logisticseller.di.returntoshipper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticseller.ui.returntoshipper.viewmodel.ReturnToShipperViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReturnToShipperViewModelModule {
    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(ReturnToShipperViewModel::class)
    internal abstract fun provideReturnToShipperViewModel(viewModel: ReturnToShipperViewModel): ViewModel
}
