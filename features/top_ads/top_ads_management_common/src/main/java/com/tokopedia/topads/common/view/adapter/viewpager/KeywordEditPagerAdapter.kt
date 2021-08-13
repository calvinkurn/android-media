package com.tokopedia.topads.common.view.adapter.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by Pika on 16/4/20.
 */
class KeywordEditPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {

    val bundle = Bundle()
    var list: ArrayList<Fragment> = arrayListOf()

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    fun setData(list: ArrayList<Fragment>) {
        this.list = list
        notifyDataSetChanged()

    }

}