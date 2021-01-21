package com.tokopedia.buyerorder.recharge_download.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.buyerorder.recharge_download.presentation.viewmodel.OrderDetailRechargeDownloadWebviewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 21/01/2021
 */
@Module
abstract class OrderDetailRechargeDownloadWebviewViewModelModule {

    @OrderDetailRechargeDownloadWebviewScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @OrderDetailRechargeDownloadWebviewScope
    @Binds
    @IntoMap
    @ViewModelKey(OrderDetailRechargeDownloadWebviewViewModel::class)
    abstract fun orderDetailRechargeDownloadWebviewViewModel(viewModel: OrderDetailRechargeDownloadWebviewViewModel): ViewModel

}