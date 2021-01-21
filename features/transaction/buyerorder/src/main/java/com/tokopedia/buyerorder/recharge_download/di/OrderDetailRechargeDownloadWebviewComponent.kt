package com.tokopedia.buyerorder.recharge_download.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

/**
 * @author by furqan on 21/01/2021
 */
@OrderDetailRechargeDownloadWebviewScope
@Component(modules = [OrderDetailRechargeDownloadWebviewModule::class],
        dependencies = [BaseAppComponent::class])
interface OrderDetailRechargeDownloadWebviewComponent {
}