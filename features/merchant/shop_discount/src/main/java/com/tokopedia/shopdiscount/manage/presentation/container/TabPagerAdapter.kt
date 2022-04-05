package com.tokopedia.shopdiscount.manage.presentation.container

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<Pair<String, Fragment>>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int) = fragments[position].second
}
