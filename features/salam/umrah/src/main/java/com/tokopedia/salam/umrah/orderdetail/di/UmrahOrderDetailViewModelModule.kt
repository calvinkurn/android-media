package com.tokopedia.salam.umrah.orderdetail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel.UmrahOrderDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 08/10/2019
 */
@Module
abstract class UmrahOrderDetailViewModelModule {

    @UmrahOrderDetailScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(UmrahOrderDetailViewModel::class)
    internal abstract fun umrahOrderViewModel(viewModel: UmrahOrderDetailViewModel): ViewModel

}