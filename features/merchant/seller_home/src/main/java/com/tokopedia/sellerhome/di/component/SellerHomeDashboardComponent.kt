package com.tokopedia.sellerhome.di.component

import com.tokopedia.sellerhome.di.module.SellerHomeDrawerModule
import com.tokopedia.sellerhome.di.module.SellerHomeModule
import com.tokopedia.sellerhome.view.home.SellerHomeActivity
import dagger.Component

@Component(modules = [SellerHomeDrawerModule::class, SellerHomeModule::class])
interface SellerHomeDashboardComponent {

    fun inject(sellerHomeActivity: SellerHomeActivity)
}