package com.tokopedia.saldodetails.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tokopedia.saldodetails.view.ui.SaldoHistoryTabItem

class SaldoInfoVIewPagerAdapter(fm: FragmentManager, var arrayList: ArrayList<SaldoHistoryTabItem>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return arrayList[position].fragment
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (position == 0) {
            return "Saldo Penghasilan (2)"
        } else
            return "Saldo Refund (1)"
    }
}