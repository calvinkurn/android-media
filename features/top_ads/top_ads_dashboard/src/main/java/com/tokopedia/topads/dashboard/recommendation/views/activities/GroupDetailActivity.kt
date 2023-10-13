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
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.GROUP_DETAIL_BUNDLE_KEY
import com.tokopedia.topads.dashboard.recommendation.views.fragments.GroupDetailFragment

class GroupDetailActivity : BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent> {

    private var headerToolbar: HeaderUnify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        headerToolbar = findViewById(R.id.headerGroupDetailsActivity)

        headerToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun getNewFragment(): Fragment =
        GroupDetailFragment.createInstance(intent.getBundleExtra(GROUP_DETAIL_BUNDLE_KEY))

    override fun getLayoutRes(): Int {
        return R.layout.activity_topads_group_details
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
