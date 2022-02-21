package com.tokopedia.dg_transaction.testing.di

import com.tokopedia.dg_transaction.testing.PdpCheckoutThankyouJourneyTest
import com.tokopedia.dg_transaction.testing.recharge_pdp_emoney.presentation.activity.EmoneyPdpActivityStub
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpModule
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpScope
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpViewModelModule
import dagger.Component

@EmoneyPdpScope
@Component(
    modules = [EmoneyPdpModule::class, EmoneyPdpViewModelModule::class],
    dependencies = [StubCommonTopupBillsComponent::class]
)
interface StubEmoneyPdpComponent: EmoneyPdpComponent {
    fun inject(pdpCheckoutThankyouJourneyTest: PdpCheckoutThankyouJourneyTest)
    fun inject(emoneyPdpActivityStub: EmoneyPdpActivityStub)
}