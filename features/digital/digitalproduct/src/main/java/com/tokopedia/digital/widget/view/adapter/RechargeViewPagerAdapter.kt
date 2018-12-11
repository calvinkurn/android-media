package com.tokopedia.digital.widget.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import com.tokopedia.digital.product.view.compoundview.DigitalWrapContentViewPager
import com.tokopedia.digital.widget.view.fragment.WidgetAllStyleRechargeFragment
import com.tokopedia.digital.widget.view.model.category.Category

/**
 * Created by Rizky on 19/11/18.
 */
class RechargeViewPagerAdapter(fm: FragmentManager?, private val categoryList: MutableList<Category>) : FragmentStatePagerAdapter(fm) {

    private var currentPosition = -1

    override fun getItem(position: Int): Fragment {
        val category = categoryList[position]
        return WidgetAllStyleRechargeFragment.newInstance(category, position)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        if (position != currentPosition) {
            val fragment = `object` as Fragment
            val pager = container as DigitalWrapContentViewPager
            if (fragment.view != null) {
                currentPosition = position
                pager.measureCurrentView(fragment.view)
            }
        }
    }

    fun addFragments(categoryList: List<Category>) {
        this.categoryList.clear()
        this.categoryList.addAll(categoryList)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return categoryList.size
    }

}