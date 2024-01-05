package com.tokopedia.topads.dashboard.recommendation.views.activities

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.views.fragments.GroupSettingsFragment
import com.tokopedia.topads.dashboard.recommendation.views.fragments.PotentialProductFragment

class ProductRecommendationActivity : BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent>, RoutingCallback{

    override fun getNewFragment(): Fragment = PotentialProductFragment.createInstance()

    override fun getLayoutRes(): Int {
        return R.layout.activity_topads_product_recommendation
    }

    override fun getParentViewResourceID(): Int {
        return R.id.fragment_container
    }

    override fun getComponent(): TopAdsDashboardComponent {
        return DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        ).build()
    }

    override fun routeToGroupSettings() {
        addFragment(GroupSettingsFragment.createInstance())
    }

    private fun addFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                com.tokopedia.abstraction.R.anim.slide_in_right,
                com.tokopedia.abstraction.R.anim.slide_out_left,
                com.tokopedia.abstraction.R.anim.slide_in_left,
                com.tokopedia.abstraction.R.anim.slide_out_right
            )
            .replace(R.id.fragment_container,fragment)
            .addToBackStack(TAG)
            .commit()
    }

    companion object {
        const val TAG = "PRODUCT_RECOMMENDATIONS"
    }
}

interface RoutingCallback{
    fun routeToGroupSettings()
}
