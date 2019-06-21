package com.tokopedia.browse.categoryNavigation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.analytics.CategoryAnalytics
import com.tokopedia.browse.categoryNavigation.fragments.CategoryLevelTwoFragment
import com.tokopedia.browse.categoryNavigation.fragments.CategorylevelOneFragment
import com.tokopedia.browse.categoryNavigation.fragments.Listener
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.APP_CATEGORY_BROWSE_V1
import kotlinx.android.synthetic.main.activity_category_browse.*
import kotlinx.android.synthetic.main.activity_category_browse.empty_view
import kotlinx.android.synthetic.main.empty_category_view.*


open class BaseCategoryBrowseActivity : BaseSimpleActivity(), CategoryChangeListener {


    private var masterFragment = Fragment()
    private var slaveFragment = Fragment()

    private var autocompleteParam = ""

    private var deepLinkCategoryName: String? = null

    private var TOOLBAR_NAME = "Belanja"


    companion object {
        @JvmStatic
        fun newIntent(context: Context): Intent {
            return Intent(context, BaseCategoryBrowseActivity::class.java)
        }

        fun isNewCategoryEnabled(context: Context): Boolean {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            return remoteConfig.getBoolean(APP_CATEGORY_BROWSE_V1, true)
        }
    }

    open fun getCategoryLaunchSource(): String {
        return "Belanja/Category"
    }


    object DeepLinkIntents {

        @DeepLink(ApplinkConst.CATEGORY_BELANJA)
        @JvmStatic
        fun getCtegoryBrowseIntent(context: Context): Intent {
            return newIntent(context)
        }

    }


    override fun getNewFragment(): Fragment {
        return CategoryLevelTwoFragment.newInstance()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar(TOOLBAR_NAME)
    }

    private fun setupToolbar(toolbarTitle: String) {
        toolbar_top.contentInsetStartWithNavigation = 0
        setSupportActionBar(toolbar_top)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_action_back))

        val titleStr = SpannableStringBuilder(toolbarTitle)
        titleStr.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, toolbarTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        supportActionBar!!.title = titleStr
    }

    override fun setupFragment(savedInstance: Bundle?) {
        initView()
        inflateFragment()
    }

    private fun initView() {
        tv_button_retry.setOnClickListener {
            empty_view.visibility = View.GONE
            (slaveFragment as CategoryLevelTwoFragment).startShimmer(true)
            (masterFragment as CategorylevelOneFragment).reloadData()
        }
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

    override fun getLayoutRes(): Int {
        return R.layout.activity_category_browse
    }

    override fun onCategoryChanged(id: String, categoryName: String, applink: String?) {
        empty_view.visibility = View.GONE
        (slaveFragment as Listener).refreshView(id, categoryName, applink)
    }

    override fun onError() {
        (slaveFragment as CategoryLevelTwoFragment).startShimmer(false)
        empty_view.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            onSearchClicked()
            CategoryAnalytics.createInstance().eventSearchBarClick(this)
            return true
        } else if (item.itemId == R.id.home) {
            CategoryAnalytics.createInstance().eventBackButtonClick(this)
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onSearchClicked() {
        RouteManager.route(this, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE_WITH_SHOPPING_NAV_SOURCE, autocompleteParam)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuItem = menu.findItem(R.id.action_search)
        menuItem.setIcon(R.drawable.ic_browse_search)

        return super.onPrepareOptionsMenu(menu)
    }

}


interface CategoryChangeListener {
    fun onCategoryChanged(id: String, categoryName: String, applink: String?)
    fun onError()
}
