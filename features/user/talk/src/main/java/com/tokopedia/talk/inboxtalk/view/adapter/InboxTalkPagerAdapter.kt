package com.tokopedia.talk.inboxtalk.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.util.SparseArrayCompat
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.talk.inboxtalk.view.activity.InboxTalkActivity
import com.tokopedia.talk.inboxtalk.view.fragment.InboxTalkFragment

/**
 * @author by nisie on 8/27/18.
 */

class InboxTalkPagerAdapter(val fragmentManager: FragmentManager,
                            val titles: Array<String>) : FragmentStatePagerAdapter(fragmentManager) {

    private val registeredFragments = SparseArrayCompat<Fragment>()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> if (GlobalConfig.isSellerApp()) {
                InboxTalkFragment.newInstance(InboxTalkActivity.MY_PRODUCT)
            } else {
                InboxTalkFragment.newInstance(InboxTalkActivity.INBOX_ALL)
            }
            1 -> InboxTalkFragment.newInstance(InboxTalkActivity.MY_PRODUCT)
            2 -> InboxTalkFragment.newInstance(InboxTalkActivity.FOLLOWING)
            else -> Fragment()
        }
    }

    override fun getPageTitle(position: Int) = titles[position]

    override fun getCount() = titles.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o = super.instantiateItem(container, position)
        registeredFragments.put(position, o as Fragment)
        return o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): Fragment? {
        return registeredFragments.get(position)
    }

    fun getFragmentPosition(nav: String): Int {
        return when (nav) {
            InboxTalkActivity.INBOX_ALL -> 0
            InboxTalkActivity.MY_PRODUCT -> if (GlobalConfig.isSellerApp()) {
                1
            } else {
                0
            }
            InboxTalkActivity.FOLLOWING -> 2
            else -> 0
        }
    }
}