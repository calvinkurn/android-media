package com.tokopedia.recharge_pdp_emoney.presentation.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent

/**
 * @author by jessica on 31/03/21
 */
class EmoneyPdpRecentTransactionFragment : BaseDaggerFragment() {
    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(EmoneyPdpComponent::class.java).inject(this)
    }
}