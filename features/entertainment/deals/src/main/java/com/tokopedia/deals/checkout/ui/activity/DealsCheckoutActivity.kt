package com.tokopedia.deals.checkout.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.deals.DealsComponentInstance
import com.tokopedia.deals.checkout.di.DaggerDealsCheckoutComponent
import com.tokopedia.deals.checkout.di.DealsCheckoutComponent
import com.tokopedia.deals.checkout.ui.DealsCheckoutCallbacks
import com.tokopedia.deals.checkout.ui.fragment.DealsCheckoutFragment
import com.tokopedia.deals.checkout.ui.fragment.DealsCheckoutLocationsFragment
import com.tokopedia.deals.pdp.data.Outlet
import com.tokopedia.deals.R.anim as animDeals
import com.tokopedia.abstraction.R.id as idAbstraction

class DealsCheckoutActivity: BaseSimpleActivity(), HasComponent<DealsCheckoutComponent>, DealsCheckoutCallbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

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

    override fun onShowAllLocation(outlets: List<Outlet>) {
        supportFragmentManager.commit {
            setCustomAnimations(
                animDeals.deals_slide_in_up,
                animDeals.deals_slide_in_down,
                animDeals.deals_slide_out_down,
                animDeals.deals_slide_out_up
            )
            add(idAbstraction.parent_view, DealsCheckoutLocationsFragment.createInstance(outlets))
            addToBackStack(null)
        }
    }

    companion object {
        const val EXTRA_DEAL_DETAIL = "EXTRA_DEAL_DETAIL"
        const val EXTRA_DEAL_VERIFY = "EXTRA_DEAL_VERIFY"
    }
}
