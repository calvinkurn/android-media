package com.tokopedia.sellerhomedrawer.di.component

import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import com.tokopedia.sellerhomedrawer.di.module.ServiceModule
import com.tokopedia.sellerhomedrawer.domain.service.SellerDrawerGetNotificationService
import dagger.Component

@SellerHomeDashboardScope
@Component(modules = [ServiceModule::class])
interface ServiceComponent {

    fun inject(sellerDrawerGetNotificationService: SellerDrawerGetNotificationService)
}