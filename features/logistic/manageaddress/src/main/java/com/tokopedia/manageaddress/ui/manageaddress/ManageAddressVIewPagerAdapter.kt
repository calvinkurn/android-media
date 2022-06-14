package com.tokopedia.manageaddress.ui.manageaddress

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by irpan on 08/06/22.
 */
class ManageAddressVIewPagerAdapter(fragmentActivity: FragmentActivity, private val pages: List<Pair<String, Fragment>>) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return pages[position].second
    }

    override fun getItemCount(): Int = pages.size

}