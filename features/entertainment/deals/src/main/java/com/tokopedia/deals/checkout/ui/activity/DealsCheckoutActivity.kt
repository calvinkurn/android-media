package com.tokopedia.deals.checkout.ui.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.deals.DealsComponentInstance
import com.tokopedia.deals.checkout.di.DaggerDealsCheckoutComponent
import com.tokopedia.deals.checkout.di.DealsCheckoutComponent
import com.tokopedia.deals.checkout.ui.fragment.DealsCheckoutFragment

class DealsCheckoutActivity: BaseSimpleActivity(), HasComponent<DealsCheckoutComponent> {

    override fun getNewFragment(): Fragment {
        return DealsCheckoutFragment.createInstance(
            intent.getParcelableExtra(EXTRA_DEAL_DETAIL),
            intent.getParcelableExtra(EXTRA_DEAL_VERIFY)
        )
    }

    override fun getComponent(): DealsCheckoutComponent {
        return DaggerDealsCheckoutComponent.builder()
            .dealsComponent(DealsComponentInstance.getDealsComponent(application, this))
            .build()
    }

    companion object {
        const val EXTRA_DEAL_DETAIL = "EXTRA_DEAL_DETAIL"
        const val EXTRA_DEAL_VERIFY = "EXTRA_DEAL_VERIFY"
    }
}
