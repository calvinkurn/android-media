package com.tokopedia.profile.following_list.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.PagerAdapter
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.profile.R
import com.tokopedia.profile.following_list.view.fragment.BaseFollowListFragment
import com.tokopedia.profile.following_list.view.fragment.ShopFollowingListFragment
import com.tokopedia.profile.following_list.view.fragment.UserFollowListFragment
import com.tokopedia.profile.following_list.view.listener.FollowingListEmptyListener
import com.tokopedia.profile.following_list.view.viewmodel.FollowingResultViewModel
import com.tokopedia.profile.following_list.view.viewmodel.FollowingViewModel
import com.tokopedia.profile.view.adapter.FollowingListTabAdapter
import com.tokopedia.profile.view.viewmodel.FollowingListTabItem

/**
 * @author by milhamj on 30/10/18.
 */
class FollowingListActivity : BaseTabActivity(), FollowingListEmptyListener {

    private var userId: String = ZERO
    private lateinit var pagerAdapter: FollowingListTabAdapter

    companion object {
        const val EXTRA_PARAM_USER_ID = "user_id"
        const val ZERO = "0"
        const val PAGE_COUNT = 2

        fun createIntent(context: Context, userId: String): Intent {
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
    }

    override fun getViewPagerAdapter(): PagerAdapter {
        val tabList = ArrayList<FollowingListTabItem>()
        tabList.add(getUserFollowingTabItem())
        tabList.add(getShopFollowingTabItem())

        pagerAdapter = FollowingListTabAdapter(supportFragmentManager)
        pagerAdapter.setItemList(tabList)
        return pagerAdapter
    }

    override fun getPageLimit(): Int {
        return PAGE_COUNT
    }

    private fun getUserFollowingTabItem(): FollowingListTabItem {
        val bundle = Bundle()
        bundle.putInt(EXTRA_PARAM_USER_ID, userId.toInt())
        return FollowingListTabItem(
                getString(R.string.fl_toppers_title),
                UserFollowListFragment.createInstance(userId.toInt(), BaseFollowListFragment.FollowListType.Following)
        )
    }

    private fun getShopFollowingTabItem(): FollowingListTabItem {
        val bundle = Bundle()
        bundle.putInt(EXTRA_PARAM_USER_ID, userId.toInt())
        return FollowingListTabItem(
                getString(R.string.fl_shop_title),
                ShopFollowingListFragment.createInstance(bundle)
        )
    }

    override fun onFollowingEmpty(javaClass: Class<out BaseFollowListFragment<FollowingViewModel, FollowingResultViewModel<*>>>) {
        synchronized(this) {
            if (::pagerAdapter.isInitialized) {
                pagerAdapter.removeByInstance(javaClass)
                pagerAdapter.notifyDataSetChanged()
                if (pagerAdapter.count == 0) tabLayout.visibility = View.GONE
            }
        }
    }

    override fun onFollowingNotEmpty() {
        tabLayout.visibility = View.VISIBLE
    }
}