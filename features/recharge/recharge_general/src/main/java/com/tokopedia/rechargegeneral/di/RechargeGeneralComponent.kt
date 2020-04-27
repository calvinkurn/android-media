package com.tokopedia.rechargegeneral.di

import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.rechargegeneral.presentation.fragment.RechargeGeneralFragment
import com.tokopedia.rechargegeneral.presentation.fragment.RechargeGeneralPromoListFragment
import com.tokopedia.rechargegeneral.presentation.fragment.RechargeGeneralRecentTransactionFragment
import dagger.Component

@RechargeGeneralScope
@Component(modules = [RechargeGeneralModule::class, RechargeGeneralViewModelModule::class], dependencies = [CommonTopupBillsComponent::class])
interface RechargeGeneralComponent {

    fun inject(rechargeGeneralFragment: RechargeGeneralFragment)

    fun inject(rechargeGeneralRecentTransactionFragment: RechargeGeneralRecentTransactionFragment)

    fun inject(rechargeGeneralPromoListFragment: RechargeGeneralPromoListFragment)

}