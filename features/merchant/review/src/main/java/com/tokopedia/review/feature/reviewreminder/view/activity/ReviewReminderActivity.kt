package com.tokopedia.review.feature.reviewreminder.view.activity

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.header.HeaderUnify
import com.tokopedia.review.R
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.GlobalMainTabSelectedListener
import com.tokopedia.review.feature.reviewreminder.view.adapter.ReviewReminderPagerAdapter
import com.tokopedia.unifycomponents.TabsUnify

class ReviewReminderActivity : BaseActivity() {

    private var header: HeaderUnify? = null
    private var tabs: TabsUnify? = null
    private var pager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }
    }

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