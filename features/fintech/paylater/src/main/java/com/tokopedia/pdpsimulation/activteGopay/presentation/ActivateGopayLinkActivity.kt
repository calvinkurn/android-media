package com.tokopedia.pdpsimulation.activteGopay.presentation

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.pdpsimulation.activteGopay.bottomsheet.GopayLinkBenefitBottomSheet

class ActivateGopayLinkActivity: BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return GopayLinkBenefitBottomSheet()
    }
}