package com.tokopedia.recharge_pdp_emoney.di

import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.recharge_pdp_emoney.presentation.activity.EmoneyPdpActivity
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpFragment
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpPromoListFragment
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpRecentTransactionFragment
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpScope
import dagger.Component

/**
 * @author by jessica on 29/03/21
 */

@EmoneyPdpScope
@Component(modules = [EmoneyPdpModule::class, EmoneyPdpViewModelModule::class],
        dependencies = [CommonTopupBillsComponent::class])
interface EmoneyPdpComponent {
    fun inject(emoneyPdpActivity: EmoneyPdpActivity)
    fun inject(emoneyPdpFragment: EmoneyPdpFragment)
    fun inject(emoneyPdpPromoListFragment: EmoneyPdpPromoListFragment)
    fun inject(emoneyPdpRecentTransactionFragment: EmoneyPdpRecentTransactionFragment)
}