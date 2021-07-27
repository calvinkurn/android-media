package com.tokopedia.editshipping.di.customproductlogistic

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import dagger.Binds
import dagger.Module

@Module
abstract class CustomProductLogisticViewModelModule{

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}