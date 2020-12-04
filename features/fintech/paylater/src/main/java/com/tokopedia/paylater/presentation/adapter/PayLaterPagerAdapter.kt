package com.tokopedia.paylater.presentation.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.paylater.R
import com.tokopedia.paylater.presentation.fragment.PayLaterOffersFragment
import com.tokopedia.paylater.presentation.fragment.SimulationFragment
import timber.log.Timber

class PayLaterPagerAdapter(val context: Context, fm: FragmentManager, behaviour: Int): FragmentStatePagerAdapter(fm, behaviour) {
    private val itemTabList: MutableList<Fragment> = mutableListOf()

    override fun getItem(position: Int): Fragment {
        return itemTabList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        Timber.d("PayLater Pager Adapter")
        return when(getItem(position)) {
            is SimulationFragment -> context.getString(R.string.paylater_title_simalisi)
            is PayLaterOffersFragment -> context.getString(R.string.paylater_title_detail)
            else -> null
        }
    }

    override fun getCount(): Int {
        return 2
    }

    fun getList() = itemTabList

    fun setList(item: List<Fragment>) {
        item.let {
            itemTabList.clear()
            itemTabList.addAll(item)
        }
    }
}