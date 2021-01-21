package com.tokopedia.buyerorder.recharge_download.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.buyerorder.recharge_download.presentation.fragment.OrderDetailRechargeDownloadWebviewFragment
import dagger.Component

/**
 * @author by furqan on 21/01/2021
 */
@OrderDetailRechargeDownloadWebviewScope
@Component(modules = [OrderDetailRechargeDownloadWebviewModule::class,
    OrderDetailRechargeDownloadWebviewViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface OrderDetailRechargeDownloadWebviewComponent {
    fun inject(orderDetailRechargeDownloadWebviewFragment: OrderDetailRechargeDownloadWebviewFragment)
}