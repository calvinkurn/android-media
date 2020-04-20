package com.tokopedia.topads.edit.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.topads.edit.view.fragment.edit.EditGroupAdFragment
import com.tokopedia.topads.edit.view.fragment.edit.BaseEditKeywordFragment
import com.tokopedia.topads.edit.view.fragment.edit.EditProductFragment


class TopAdsEditPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {

    private val PRODUK = 0
    private val KATAKUNCI = 1
    private val ATUR = 2
    private val PAGE_SIZE = 3
    var list: ArrayList<Fragment> = arrayListOf()

    var titles = arrayOf(
            "Produk", "Kata Kunci", "Atur"
    )

    override fun getItem(position: Int): Fragment {
        return list[position]

    }

    override fun getCount(): Int {
        return PAGE_SIZE
    }

    override fun getPageTitle(position: Int): CharSequence? {

        when (position) {
            PRODUK -> return titles[0]
            KATAKUNCI -> return titles[1]
            ATUR -> return titles[2]
        }
        return titles[2]
    }

    fun setData(list: ArrayList<Fragment>) {
        this.list = list
        notifyDataSetChanged()

    }

}