package com.tokopedia.dg_transaction.testing.recharge_pdp_emoney.presentation.activity

import com.tokopedia.dg_transaction.testing.di.DaggerStubEmoneyPdpComponent
import com.tokopedia.dg_transaction.testing.di.util.StubCommonTopupBillsComponentInstance
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.presentation.activity.EmoneyPdpActivity

class EmoneyPdpActivityStub: EmoneyPdpActivity() {

    override fun getComponent(): EmoneyPdpComponent {
        val component = DaggerStubEmoneyPdpComponent.builder()
            .stubCommonTopupBillsComponent(
                StubCommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application))
            .build()
        component.inject(this)
        return component
    }
}