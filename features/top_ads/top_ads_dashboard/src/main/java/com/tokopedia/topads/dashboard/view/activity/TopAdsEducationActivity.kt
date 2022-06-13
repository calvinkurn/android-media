package com.tokopedia.topads.dashboard.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.header.HeaderUnify
import com.tokopedia.topads.dashboard.R
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topads.dashboard.view.fragment.education.TopAdsEducationFragment


class TopAdsEducationActivity : BaseSimpleActivity() {

    private lateinit var headerToolbar: HeaderUnify

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        headerToolbar = findViewById(R.id.headerEducationActivity)

        headerToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_topads_dashboard_education
    }

    override fun getNewFragment(): Fragment {
        return TopAdsEducationFragment.createInstance()
    }

    override fun getParentViewResourceID(): Int {
        return R.id.fragment_container
    }

    fun addFragment(fragment: Fragment) {
        val fm = supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, fragment.javaClass.name)
            .addToBackStack(null)
        fm.commit()
    }
}