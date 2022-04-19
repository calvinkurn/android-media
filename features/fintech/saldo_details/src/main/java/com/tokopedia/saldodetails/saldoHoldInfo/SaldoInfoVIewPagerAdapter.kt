package com.tokopedia.saldodetails.saldoHoldInfo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tokopedia.saldodetails.commom.design.SaldoHistoryTabItem

class SaldoInfoVIewPagerAdapter(fm: FragmentManager, var arrayList: ArrayList<SaldoHistoryTabItem>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return arrayList[position].fragment
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return arrayList[position].title
    }
}