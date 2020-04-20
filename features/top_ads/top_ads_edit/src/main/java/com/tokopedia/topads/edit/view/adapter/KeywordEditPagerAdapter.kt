package com.tokopedia.topads.edit.view.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.topads.edit.view.fragment.edit.EditKeywordsFragment
import com.tokopedia.topads.edit.view.fragment.edit.EditNegativeKeywordsFragment

/**
 * Created by Pika on 16/4/20.
 */
class KeywordEditPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {

    private val POSITIVE = 0
    private val NEGATIVE = 1
    val bundle = Bundle()
    var list:ArrayList<Fragment> = arrayListOf()

    override fun getItem(position: Int): Fragment {
        when (position) {
            POSITIVE -> return list[0]
            NEGATIVE -> return list[1]
        }
        return list[0]
    }

    override fun getCount(): Int {
        return 2
    }

    fun setData(list:ArrayList<Fragment>){
        this.list = list
        notifyDataSetChanged()

    }

}