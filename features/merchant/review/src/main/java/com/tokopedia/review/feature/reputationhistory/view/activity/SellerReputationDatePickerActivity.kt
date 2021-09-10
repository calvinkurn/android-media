package com.tokopedia.review.feature.reputationhistory.view.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.datepicker.range.view.activity.DatePickerActivity
import com.tokopedia.datepicker.range.view.adapter.DatePickerTabPagerAdapter
import java.util.*

/**
 * Created by nathan on 7/11/17.
 */
class SellerReputationDatePickerActivity : DatePickerActivity() {

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        tabLayout?.visibility = View.GONE
    }

    override fun getViewPagerAdapter(): PagerAdapter {
        val fragmentList: MutableList<Fragment> = ArrayList()
        fragmentList.add(datePickerCustomFragment)
        return DatePickerTabPagerAdapter(this, supportFragmentManager, fragmentList)
    }

    override fun getPageLimit(): Int {
        return OFFSCREEN_PAGE_LIMIT
    }

    companion object {
        const val OFFSCREEN_PAGE_LIMIT = 1
    }
}