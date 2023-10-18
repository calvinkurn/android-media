package com.tokopedia.review.feature.reviewreminder.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.header.HeaderUnify
import com.tokopedia.review.R
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.GlobalMainTabSelectedListener
import com.tokopedia.review.feature.reviewreminder.view.adapter.ReviewReminderPagerAdapter
import com.tokopedia.unifycomponents.TabsUnify

class ReviewReminderActivity : BaseSimpleActivity() {

    private var header: HeaderUnify? = null
    private var tabs: TabsUnify? = null
    private var pager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_review_reminder)
        setupStatusBar()
        initView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getNewFragment(): Fragment? = null

    private fun initView() {
        header = findViewById(R.id.header_review_reminder)
        tabs = findViewById(R.id.tabs_review_reminder)
        pager = findViewById(R.id.pager_review_reminder)

        setupHeader()
        setupTabsPager()
    }

    private fun setupHeader() {
        setSupportActionBar(header)
        header?.title = getString(R.string.review_reminder_title)
    }

    private fun setupTabsPager() {
        tabs?.addNewTab(getString(R.string.review_reminder_tab_title_1))
        tabs?.addNewTab(getString(R.string.review_reminder_tab_title_2))

        tabs?.getUnifyTabLayout()?.addOnTabSelectedListener(GlobalMainTabSelectedListener(pager, this))
        pager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs?.getUnifyTabLayout()))

        val reviewReminderPagerAdapter = ReviewReminderPagerAdapter(supportFragmentManager, tabs?.getUnifyTabLayout())
        pager?.offscreenPageLimit = reviewReminderPagerAdapter.count
        pager?.adapter = reviewReminderPagerAdapter
    }
}