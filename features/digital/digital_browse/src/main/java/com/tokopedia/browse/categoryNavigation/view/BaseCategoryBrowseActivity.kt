package com.tokopedia.browse.categoryNavigation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.analytics.CategoryAnalytics
import com.tokopedia.browse.categoryNavigation.fragments.CategoryLevelTwoFragment
import com.tokopedia.browse.categoryNavigation.fragments.CategorylevelOneFragment
import com.tokopedia.browse.categoryNavigation.fragments.Listener
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.activity_category_browse.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseCategoryBrowseActivity : BaseSimpleActivity(), CategoryChangeListener, ActivityStateListener {

    private val trackingQueue: TrackingQueue by lazy {
        TrackingQueue(this)
    }
    private var masterFragment = Fragment()
    private var slaveFragment = Fragment()

    private var deepLinkCategoryName: String? = null

    private var TOOLBAR_NAME = "Kategori"
    private lateinit var globalError: GlobalError


    companion object {
        private const val EXTRA_CATEGORY_NAME = "CATEGORY_NAME"

        @JvmStatic
        fun newIntent(context: Context, categoryName: String): Intent {
            val intent = Intent(context, BaseCategoryBrowseActivity::class.java)
            val bundle = Bundle()
            bundle.putString(EXTRA_CATEGORY_NAME, categoryName)
            intent.putExtras(bundle)
            return intent
        }

        @JvmStatic
        fun newIntent(context: Context): Intent {
            return Intent(context, BaseCategoryBrowseActivity::class.java)
        }
    }

    override fun getScreenName() = getString(R.string.belanja_screen_name)

    open fun getCategoryLaunchSource(): String {
        return "Belanja/Category"
    }


    object DeepLinkIntents {
        lateinit var extras: Bundle

        @DeepLink(ApplinkConst.CATEGORY_BELANJA)
        @JvmStatic
        fun getCategoryBrowseIntent(context: Context, bundle: Bundle): Intent {
            extras = bundle
            return openBelanjaActivity(context)
        }

        private fun openBelanjaActivity(context: Context): Intent {
            val deepLinkCategoryName = extras.getString(EXTRA_CATEGORY_NAME, "0")
            return newIntent(context, deepLinkCategoryName)
        }
    }


    override fun getNewFragment(): Fragment {
        return CategoryLevelTwoFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        intent?.extras?.let {
            if (it.containsKey(EXTRA_CATEGORY_NAME)) {
                deepLinkCategoryName = intent?.extras?.getString(EXTRA_CATEGORY_NAME)
            }
        }
        super.onCreate(savedInstanceState)
        setupToolbar(TOOLBAR_NAME)
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


        masterFragment = CategorylevelOneFragment.newInstance(deepLinkCategoryName)
        supportFragmentManager.beginTransaction()
                .replace(R.id.master_view, masterFragment, tagFragment)
                .commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is CategorylevelOneFragment) {
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
            (masterFragment as CategorylevelOneFragment).reloadData()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
             CategoryAnalytics.createInstance().eventBackButtonClick()
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
