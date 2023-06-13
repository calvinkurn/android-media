package com.tokopedia.manageaddress.ui.manageaddress

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by irpan on 08/06/22.
 */
class ManageAddressViewPagerAdapter(fragmentActivity: Fragment, pages: List<Pair<String, Fragment>>) :
    FragmentStateAdapter(fragmentActivity) {

    private var _pages: List<Pair<String, Fragment>> = listOf()

    init {
        _pages = pages
    }

    override fun createFragment(position: Int): Fragment {
        return _pages.getOrNull(position)?.second ?: ManageAddressFragment()
    }

    override fun getItemCount(): Int = _pages.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(pages: List<Pair<String, Fragment>>) {
        _pages = pages
        this.notifyDataSetChanged()
    }

    /**
     * handle differential of fragment so its allow fragment to recreate for search
     */
    override fun getItemId(position: Int): Long {
        return _pages.getOrNull(position)?.second.hashCode().toLong()
    }
}
