package com.tokopedia.recharge_pdp_emoney.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponentInstance
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpFragment

class EmoneyPdpActivity : BaseSimpleActivity(), HasComponent<EmoneyPdpComponent> {
    override fun getNewFragment(): Fragment = EmoneyPdpFragment()

    override fun getComponent(): EmoneyPdpComponent {
        return EmoneyPdpComponentInstance.getEmoneyPdpComponent(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.elevation = 0f
    }
}