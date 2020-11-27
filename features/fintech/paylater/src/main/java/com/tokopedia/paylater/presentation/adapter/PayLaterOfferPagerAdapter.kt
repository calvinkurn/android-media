package com.tokopedia.paylater.presentation.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PayLaterOfferPagerAdapter(val context: Context, fm: FragmentManager, behaviour: Int, val list: ArrayList<Fragment>): FragmentStatePagerAdapter(fm,behaviour) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

}