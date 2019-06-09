package com.tokopedia.browse.categoryNavigation.view

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.fragments.CategoryLevelTwoFragment
import com.tokopedia.browse.categoryNavigation.fragments.CategorylevelOneFragment
import com.tokopedia.browse.categoryNavigation.fragments.Listener
import kotlinx.android.synthetic.main.activity_category_browse.*


open class CategoryBrowseActivity : BaseSimpleActivity(), CategoryChangeListener {

    private var masterFragment = Fragment()
    private var slaveFragment = Fragment()

    private var autocompleteParam = ""
    private val toolBarName = "Belanja"


    override fun getNewFragment(): Fragment {
        return CategorylevelOneFragment.newInstance()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        setupActionBarHomeIndicatorIcon()
    }

    private fun setupActionBarHomeIndicatorIcon() {
        if (supportActionBar != null && isShowCloseButton) {
            supportActionBar!!.setHomeAsUpIndicator(ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_close_default))
        }
    }

    private fun setupToolbar() {
        toolbar_top.contentInsetStartWithNavigation = 0
        setSupportActionBar(toolbar_top)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_action_back))


        val title = toolBarName
        val titleStr = SpannableStringBuilder(title)
        titleStr.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar!!.title = titleStr
    }

    override fun setupFragment(savedInstance: Bundle?) {
        inflateFragment()
    }

    override fun inflateFragment() {
        masterFragment = CategorylevelOneFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.master_view, masterFragment, tagFragment)
                .commit()

        slaveFragment = CategoryLevelTwoFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.slave_view, slaveFragment, tagFragment)
                .commit()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_category_browse
    }

    override fun onCategoryChanged(id: String, categoryName: String) {
        (slaveFragment as Listener).refreshView(id, categoryName)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            onSearchClicked()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onSearchClicked() {
        RouteManager.route(this, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE_WITH_NAVSOURCE, autocompleteParam)
    }

}


interface CategoryChangeListener {
    fun onCategoryChanged(id: String, categoryName: String)
}
