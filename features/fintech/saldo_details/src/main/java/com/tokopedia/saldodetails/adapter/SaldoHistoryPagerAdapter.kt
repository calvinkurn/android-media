package com.tokopedia.saldodetails.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tokopedia.saldodetails.view.ui.SaldoHistoryTabItem
import java.util.*

class SaldoHistoryPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    private val items: MutableList<SaldoHistoryTabItem>

    init {
        this.items = ArrayList()
    }

    fun setItems(items: List<SaldoHistoryTabItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return items[position].title
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Fragment {
        return items[position].fragment
    }
}
