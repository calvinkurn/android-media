package com.tokopedia.recharge_pdp_emoney.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.di.DaggerEmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpFragment

class EmoneyPdpActivity : BaseSimpleActivity(), HasComponent<EmoneyPdpComponent> {
    override fun getNewFragment(): Fragment = EmoneyPdpFragment()

    override fun getComponent(): EmoneyPdpComponent {
        return DaggerEmoneyPdpComponent.builder()
                .commonTopupBillsComponent(CommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application))
                .build()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_emoney
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.emoney_toolbar
    }
}