package com.tokopedia.dg_transaction.testing.recharge_pdp_emoney.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.dg_transaction.testing.di.DaggerStubEmoneyPdpComponent
import com.tokopedia.dg_transaction.testing.di.util.StubCommonTopupBillsComponentInstance
import com.tokopedia.dg_transaction.testing.recharge_pdp_emoney.presentation.fragment.EmoneyPdpFragmentStub
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.presentation.activity.EmoneyPdpActivity
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpFragment

class EmoneyPdpActivityStub: EmoneyPdpActivity() {

    override fun getComponent(): EmoneyPdpComponent {
        val component = DaggerStubEmoneyPdpComponent.builder()
            .stubCommonTopupBillsComponent(
                StubCommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application))
            .build()
        component.inject(this)
        return component
    }

    override fun getNewFragment(): Fragment {
        passData?.let {
            val fragment = EmoneyPdpFragmentStub()
            val bundle = Bundle()
            bundle.putParcelable(EmoneyPdpFragment.EXTRA_PARAM_DIGITAL_CATEGORY_DETAIL_PASS_DATA, it)
            fragment.arguments = bundle
            return fragment
        }
        return EmoneyPdpFragmentStub()
    }
}