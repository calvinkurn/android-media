package com.tokopedia.top_ads_headline.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.top_ads_headline.R
import com.tokopedia.topads.common.constant.Constants
import com.tokopedia.topads.common.constant.Constants.TAB_POSITION
import com.tokopedia.topads.common.view.adapter.viewpager.TopAdsEditPagerAdapter
import kotlinx.android.synthetic.main.topads_edit_headline_activity.*

class EditFormHeadlineActivity : BaseActivity(){

    private lateinit var adapter: TopAdsEditPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topads_edit_headline_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
        renderTabAndViewPager()
    }

    private fun renderTabAndViewPager() {
        val bundle = intent.extras
        view_pager.adapter = getViewPagerAdapter()
        view_pager.offscreenPageLimit = 3
        tab_layout?.addNewTab(Constants.AD_CONTENT)
        tab_layout?.addNewTab(Constants.AD_AND_KEYWORD_COST)
        tab_layout?.addNewTab(Constants.OTHERS)
        tab_layout?.getUnifyTabLayout()?.getTabAt(bundle?.getInt(TAB_POSITION, 2) ?: 2)?.select()
        view_pager.currentItem = bundle?.getInt(TAB_POSITION, 2) ?: 2
    }

    private fun getViewPagerAdapter(): TopAdsEditPagerAdapter {
        val list: ArrayList<Fragment> = ArrayList()
        adapter = TopAdsEditPagerAdapter(arrayOf(Constants.AD_CONTENT, Constants.AD_AND_KEYWORD_COST, Constants.OTHERS), supportFragmentManager, 0)
        adapter.setData(list)
        return adapter
    }

}