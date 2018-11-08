package com.tokopedia.profile.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.kol.feature.following_list.view.activity.KolFollowingListActivity
import com.tokopedia.kol.feature.following_list.view.fragment.KolFollowingListFragment
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingListEmptyListener
import com.tokopedia.profile.ProfileModuleRouter
import com.tokopedia.profile.R
import com.tokopedia.profile.view.adapter.FollowingListTabAdapter
import com.tokopedia.profile.view.viewmodel.FollowingListTabItem

/**
 * @author by milhamj on 30/10/18.
 */
class FollowingListActivity : BaseTabActivity(), KolFollowingListEmptyListener {

    private var userId: String = ZERO
    private var pagerAdapter: FollowingListTabAdapter? = null
    private var isKolShown = true

    companion object {
        const val EXTRA_PARAM_USER_ID = "user_id"
        const val ZERO = "0"
        const val PAGE_COUNT = 2

        fun createIntent(context: Context?, userId: String): Intent {
            val intent = Intent(context, FollowingListActivity::class.java)
            intent.putExtra(EXTRA_PARAM_USER_ID, userId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            userId = savedInstanceState.getString(EXTRA_PARAM_USER_ID, ZERO)
        } else {
            userId = intent.getStringExtra(EXTRA_PARAM_USER_ID) ?: ZERO
        }
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(EXTRA_PARAM_USER_ID, userId)
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.visibility = View.GONE
    }

    override fun getViewPagerAdapter(): PagerAdapter {
        val tabList = ArrayList<FollowingListTabItem>()
        tabList.add(getKolFollowingTabItem())
        tabList.add(getFavoritedShopTabItem())

        pagerAdapter = FollowingListTabAdapter(supportFragmentManager)
        pagerAdapter!!.setItemList(tabList)
        return pagerAdapter!!
    }

    override fun getPageLimit(): Int {
        return PAGE_COUNT
    }

    private fun getKolFollowingTabItem(): FollowingListTabItem {
        val bundle = Bundle()
        bundle.putInt(KolFollowingListActivity.ARGS_USER_ID, userId.toInt())
        return FollowingListTabItem(
                getString(R.string.fl_toppers_title),
                KolFollowingListFragment.createInstance(bundle)
        )
    }

    private fun getFavoritedShopTabItem(): FollowingListTabItem {
        val fragment = (application as ProfileModuleRouter).getFavoritedShopFragment(userId)
        return FollowingListTabItem(
                getString(R.string.fl_shop_title),
                fragment
        )
    }

    override fun onFollowingEmpty() {
        if (isKolShown) {
            val tabList = ArrayList<FollowingListTabItem>()
            tabList.add(getFavoritedShopTabItem())

            pagerAdapter!!.setItemList(tabList)
            isKolShown = false
        }
        tabLayout.visibility = View.GONE
    }

    override fun onFollowingNotEmpty() {
        tabLayout.visibility = View.VISIBLE
    }
}