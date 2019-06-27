package com.tokopedia.groupchat.room.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * @author : Steven 12/02/19
 */
class FragmentPagerAdapter(fm: FragmentManager?,
                           var fragmentList : ArrayList<Fragment> ) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}