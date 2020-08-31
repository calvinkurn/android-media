package com.tokopedia.topads.edit.view.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by Pika on 16/4/20.
 */
class KeywordEditPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {

    private val POSITIVE = 0
    private val COUNT = 2
    val bundle = Bundle()
    var list: ArrayList<Fragment> = arrayListOf()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            POSITIVE -> list[0]
            else -> list[1]
        }
    }

    override fun getCount(): Int {
        return COUNT
    }

    fun setData(list: ArrayList<Fragment>) {
        this.list = list
        notifyDataSetChanged()

    }

}