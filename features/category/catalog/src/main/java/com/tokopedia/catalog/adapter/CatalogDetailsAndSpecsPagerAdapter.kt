package com.tokopedia.catalog.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.catalog.R
import com.tokopedia.unifyprinciples.Typography


class CatalogDetailsAndSpecsPagerAdapter(
        val fa: FragmentActivity,
        var context: Context?,
        var fragmentList: ArrayList<Fragment>
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
        // TODO ERROR. Runtime crash with Unify Colors
        customView?.setTextColor(Color.parseColor("#03AC0E"))
    }

    fun setUnSelectView(tabLayout: TabLayout, position: Int) {
        val tab = tabLayout.getTabAt(position)
        val customView: Typography? = tab!!.customView  as Typography
        customView?.setTextColor(fa.resources.getColor(R.color.catalog_N700_44))
    }
}
