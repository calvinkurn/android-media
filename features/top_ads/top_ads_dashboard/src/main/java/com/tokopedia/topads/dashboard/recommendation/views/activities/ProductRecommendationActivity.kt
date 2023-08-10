package com.tokopedia.topads.dashboard.recommendation.views.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.header.HeaderUnify
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.views.fragments.PotentialProductFragment

class ProductRecommendationActivity : BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent> {

    private var headerUnify : HeaderUnify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        headerUnify = findViewById(R.id.headerPotentialProductActivity)

        headerUnify?.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    fun setUpHeader(title: String){
        headerUnify?.title = title
    }

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
}
