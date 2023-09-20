package com.tokopedia.scp_rewards.detail.presentation.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tokopedia.scp_rewards.detail.domain.model.CouponList

class BonusPagerAdapter(
        private val couponList: List<CouponList>,
        fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return couponList.size
    }

    override fun getItem(position: Int): Fragment {
        val couponData = couponList[position]
        return CouponListFragment.newInstance(
                pageStatus = couponData.status,
                list = couponData.list)
    }
}
