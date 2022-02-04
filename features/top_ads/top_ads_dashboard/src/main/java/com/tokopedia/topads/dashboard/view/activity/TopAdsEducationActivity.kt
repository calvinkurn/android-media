package com.tokopedia.topads.dashboard.view.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.header.HeaderUnify
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.TopadsEducationRvAdapter
import androidx.fragment.app.Fragment
import com.tokopedia.topads.dashboard.view.fragment.education.ListArticleTopAdsEducationFragment
import com.tokopedia.topads.dashboard.view.fragment.education.TopAdsEducationFragment


class TopAdsEducationActivity : BaseActivity() {

    private lateinit var headerToolbar: HeaderUnify

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topads_dashboard_education)

        headerToolbar = findViewById(R.id.headerEducationActivity)

        addFragment(TopAdsEducationFragment.createInstance(),false)

        headerToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun addFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val fm = supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, fragment.javaClass.name)
        if (addToBackStack) fm.addToBackStack(null)
        fm.commit()
    }
}