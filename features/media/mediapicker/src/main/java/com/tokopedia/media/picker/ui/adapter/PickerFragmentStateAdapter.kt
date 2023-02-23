// This suppress is acceptable since this adapter only have less than 5 fragments actively
@file:Suppress("NotifyDataSetChanged")

package com.tokopedia.media.picker.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PickerFragmentStateAdapter constructor(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    var fragments = mutableListOf<Fragment>()
        private set

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }

    fun addSingleFragment(fragment: Fragment) {
        fragments.clear()
        addFragment(fragment)
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyDataSetChanged()
    }

    fun addFragments(mFragments: List<Fragment>) {
        fragments.clear()
        fragments.addAll(mFragments)
        notifyDataSetChanged()
    }

    fun removeFragment(position: Int) {
        fragments.removeAt(position)
        notifyDataSetChanged()
    }
}
