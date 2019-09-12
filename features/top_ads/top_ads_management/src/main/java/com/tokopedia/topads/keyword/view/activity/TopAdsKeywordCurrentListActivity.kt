package com.tokopedia.topads.keyword.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.TopAdsComponentInstance
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordCurrentListFragment

class TopAdsKeywordCurrentListActivity: BaseSimpleActivity(), HasComponent<TopAdsComponent> {
    var isPositive = true
    var groupId = "-1"

    companion object {
        const val GROUP_ID_PARAM = "group_id"
        const val IS_POSITIVE = "is_positive"

        fun start(activity: Activity, isPositive: Boolean, groupId: String, fragment: Fragment? = null){
            val intent = createIntent(activity, isPositive, groupId)
            if (fragment != null) {
                fragment.startActivity(intent)
            } else {
                activity.startActivity(intent)
            }
        }

        private fun createIntent(activity: Activity, isPositive: Boolean, groupId: String): Intent {
            return Intent(activity, TopAdsKeywordCurrentListActivity::class.java).apply {
                putExtra(IS_POSITIVE, isPositive)
                putExtra(GROUP_ID_PARAM, groupId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        intent?.run {
            isPositive = getBooleanExtra(IS_POSITIVE, false)
            groupId = getStringExtra(GROUP_ID_PARAM)
        }
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment = TopAdsKeywordCurrentListFragment.createInstance(isPositive, groupId)

    override fun getComponent() = TopAdsComponentInstance.getComponent(application)

}