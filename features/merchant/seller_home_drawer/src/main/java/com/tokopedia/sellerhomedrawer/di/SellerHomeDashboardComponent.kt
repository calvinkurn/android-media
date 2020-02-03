package com.tokopedia.sellerhomedrawer.di

import com.tokopedia.sellerhomedrawer.di.module.SellerHomeDashboardModule
import com.tokopedia.sellerhomedrawer.drawer.SellerDrawerPresenterActivity
import com.tokopedia.sellerhomedrawer.presentation.view.dashboard.SellerDashboardActivity
import dagger.Component

@SellerHomeDashboardScope
@Component(modules = [SellerHomeDashboardModule::class])
interface SellerHomeDashboardComponent {

    fun inject(sellerDashboardActivity: SellerDashboardActivity)

    fun inject(sellerDrawerPresenterActivity: SellerDrawerPresenterActivity)
}