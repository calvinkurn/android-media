package com.tokopedia.buyerorder.recharge.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.buyerorder.recharge.presentation.fragment.RechargeOrderDetailFragment
import dagger.Component

/**
 * @author by furqan on 28/10/2021
 */
@RechargeOrderDetailScope
@Component(modules = [RechargeOrderDetailModule::class, RechargeOrderDetailViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface RechargeOrderDetailComponent {
    fun inject(rechargeOrderDetailFragment: RechargeOrderDetailFragment)
}