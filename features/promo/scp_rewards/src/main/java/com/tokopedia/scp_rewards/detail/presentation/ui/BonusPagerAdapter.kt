package com.tokopedia.scp_rewards.detail.presentation.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tokopedia.scp_rewards.detail.domain.model.TabData
import com.tokopedia.scp_rewards_widgets.model.FilterModel

class BonusPagerAdapter(
    private val tabList: List<TabData>,
    private val filters: List<FilterModel>?,
    fragmentManager: FragmentManager,
    private val onCouponListCallBack: CouponListFragment.OnCouponListCallBack
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return tabList.size
    }

    override fun getItem(position: Int): Fragment {
        val couponData = tabList[position]
        return CouponListFragment.newInstance(
            pageStatus = couponData.status,
            medaliSlug = couponData.medaliSlug,
            list = couponData.list,
            filters = filters,
            onCouponListCallBack = onCouponListCallBack
        )
    }
}
