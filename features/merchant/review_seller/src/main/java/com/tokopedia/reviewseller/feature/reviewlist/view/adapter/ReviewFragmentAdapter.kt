package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.reviewseller.R
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.partial_review_tab_text.view.*
import java.util.ArrayList

class ReviewFragmentAdapter(fm: FragmentManager):
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private companion object {
        val TAB_LAYOUT = R.layout.partial_review_tab_text
        val ACTIVE_TAB_STATE_COLOR = R.color.Green_G500
        val INACTIVE_TAB_STATE_COLOR = R.color.Neutral_N200
    }

    private var itemList: MutableList<ReviewFragmentItem> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return itemList[position].fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return itemList[position].title
    }

    override fun getCount() = itemList.size

    fun setItemList(tabList: ArrayList<ReviewFragmentItem>) {
        itemList = tabList
        notifyDataSetChanged()
    }

    fun setupLayout(context: Context?, tabLayout: TabLayout) {
        for(i in 0 until tabLayout.tabCount) {
            val tabTypography = getCustomTabLayout(context)
            tabTypography?.text = itemList[i].title
            tabLayout.getTabAt(i)?.customView = tabTypography
        }
        handleSelectedTab(context, tabLayout.getTabAt(0), true)
    }

    fun handleSelectedTab(context: Context?, tab: TabLayout.Tab?, state: Boolean) {
        context?.let {
            tab?.customView?.apply {
                tab_title.setTextColor(ContextCompat.getColor(it, getStateColor(state)))
            }
        }
    }

    private fun getStateColor(isActive: Boolean): Int {
        return if(isActive) ACTIVE_TAB_STATE_COLOR else INACTIVE_TAB_STATE_COLOR
    }

    private fun getCustomTabLayout(context: Context?): Typography? {
        return LayoutInflater.from(context).inflate(TAB_LAYOUT, null) as Typography
    }

    data class ReviewFragmentItem (
            val title: String,
            val fragment: Fragment
    )
}