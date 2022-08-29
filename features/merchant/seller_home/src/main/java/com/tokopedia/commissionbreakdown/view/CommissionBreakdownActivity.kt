package com.tokopedia.commissionbreakdown.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.commissionbreakdown.di.component.CommissionBreakdownComponent
import com.tokopedia.commissionbreakdown.di.component.DaggerCommissionBreakdownComponent

/**
 * For navigating to this class

 */

class CommissionBreakdownActivity : BaseSimpleActivity(),
    HasComponent<CommissionBreakdownComponent> {

    companion object {
        private const val TAG = "COMMISSION_BREAKDOWN_FRAGMENT"
    }

    override fun getNewFragment(): Fragment {
        return CommissionBreakdownFragment.createInstance()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
    }

    override fun getTagFragment() = TAG

    override fun getScreenName() = null

    override fun getComponent(): CommissionBreakdownComponent {
        return DaggerCommissionBreakdownComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }
}
