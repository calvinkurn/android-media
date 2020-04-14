package com.tokopedia.kategori.view.activity

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kategori.analytics.CategoryAnalytics.Companion.categoryAnalytics
import com.tokopedia.kategori.view.fragments.CategoryLevelTwoFragment
import com.tokopedia.kategori.view.fragments.CategoryLevelOneFragment
import com.tokopedia.kategori.view.fragments.Listener
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kategori.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.activity_category_browse.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class CategoryBrowseActivity : BaseSimpleActivity(), CategoryChangeListener, ActivityStateListener {

    private val trackingQueue: TrackingQueue by lazy {
        TrackingQueue(this)
    }
    private lateinit var masterFragment: Fragment
    private lateinit var slaveFragment: Fragment
    private var deepLinkCategoryName: String = "0"
    private var toolbarName = "Kategori"
    private lateinit var globalError: GlobalError
    private val EXTRA_CATEGORY_NAME = "CATEGORY_NAME"

    override fun getScreenName(): String = getString(R.string.belanja_screen_name)

    override fun getNewFragment(): Fragment {
        return CategoryLevelTwoFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        uri?.getQueryParameter(EXTRA_CATEGORY_NAME)?.let {
            deepLinkCategoryName = it
        }
        super.onCreate(savedInstanceState)
        setupToolbar(toolbarName)
    }

    override fun onPause() {
        super.onPause()
        getActivityTrackingQueue().sendAll()
    }

    private fun setupToolbar(toolbarTitle: String) {
        toolbar_top.contentInsetStartWithNavigation = 0
        val titleStr = SpannableStringBuilder(toolbarTitle)
        titleStr.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, toolbarTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        setSupportActionBar(toolbar_top)

        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_action_back))
            it.title = titleStr
        }
    }

    override fun setupFragment(savedInstance: Bundle?) {
        initView()
        inflateFragment()
    }

    private fun initView() {
        globalError = findViewById(R.id.global_error)
    }

    override fun inflateFragment() {
        slaveFragment = CategoryLevelTwoFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.slave_view, slaveFragment, tagFragment)
                .commit()


        masterFragment = CategoryLevelOneFragment.newInstance(deepLinkCategoryName)
        supportFragmentManager.beginTransaction()
                .replace(R.id.master_view, masterFragment, tagFragment)
                .commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is CategoryLevelOneFragment) {
            fragment.activityStateListener = this
        } else if (fragment is CategoryLevelTwoFragment) {
            fragment.activityStateListener = this
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_category_browse
    }

    override fun onCategoryChanged(id: String, categoryName: String, applink: String?) {
        (slaveFragment as Listener).refreshView(id, categoryName, applink)
    }

    override fun onError(e: Throwable) {
        slave_view.hide()
        master_view.hide()

        if (e is UnknownHostException
                || e is SocketTimeoutException) {
            globalError.setType(GlobalError.NO_CONNECTION)
        } else {
            globalError.setType(GlobalError.SERVER_ERROR)
        }

        globalError.show()

        globalError.setOnClickListener {
            slave_view.show()
            master_view.show()
            globalError.hide()
            (masterFragment as CategoryLevelOneFragment).reloadData()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            categoryAnalytics.eventBackButtonClick()
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getActivityTrackingQueue(): TrackingQueue {
        return trackingQueue
    }
}

interface ActivityStateListener {
    fun getActivityTrackingQueue(): TrackingQueue
}

interface CategoryChangeListener {
    fun onCategoryChanged(id: String, categoryName: String, applink: String?)
    fun onError(e: Throwable)
}
