package com.tokopedia.catalog.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.unifyprinciples.Typography

class CatalogDetailsAndSpecsPagerAdapter(
        fa: FragmentActivity,
        var context: Context?,
        private var fragmentList: ArrayList<Fragment>
) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun setOnSelectView(tabLayout: TabLayout, position: Int) {
        val tab = tabLayout.getTabAt(position)
        val customView: Typography? = tab!!.customView  as Typography
        customView?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
    }

    fun setUnSelectView(tabLayout: TabLayout, position: Int) {
        val tab = tabLayout.getTabAt(position)
        val customView: Typography? = tab!!.customView  as Typography
        customView?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
    }
}
