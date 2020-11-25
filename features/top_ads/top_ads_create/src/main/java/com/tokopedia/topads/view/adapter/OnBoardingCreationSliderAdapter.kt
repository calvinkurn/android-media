package com.tokopedia.topads.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by Pika on 29/5/20.
 */
class OnBoardingCreationSliderAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {

    private lateinit var listItem: ArrayList<Fragment>
    override fun getItem(position: Int): Fragment {
        return listItem[position]
    }

    override fun getCount(): Int {
        return listItem.size
    }

    fun setData(list: ArrayList<Fragment>) {
        listItem = list

    }

}