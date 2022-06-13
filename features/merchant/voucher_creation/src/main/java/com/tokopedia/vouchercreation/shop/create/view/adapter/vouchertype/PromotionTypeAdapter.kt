package com.tokopedia.vouchercreation.shop.create.view.adapter.vouchertype

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment

class PromotionTypeAdapter(fragment: Fragment,
                           private val fragmentList: List<BaseListFragment<*,*>>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}