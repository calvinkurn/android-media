package com.tokopedia.sellerhomedrawer.di.component

import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import com.tokopedia.sellerhomedrawer.di.module.SellerHomeDashboardModule
import com.tokopedia.sellerhomedrawer.presentation.view.dashboard.SellerDashboardActivity
import com.tokopedia.sellerhomedrawer.presentation.view.drawer.SellerDrawerPresenterActivity
import dagger.Component

@SellerHomeDashboardScope
@Component(modules = [SellerHomeDashboardModule::class])
interface SellerHomeDashboardComponent {

    fun inject(sellerDashboardActivity: SellerDashboardActivity)

    fun inject(sellerDrawerPresenterActivity: SellerDrawerPresenterActivity)
}