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

    fun setOnSelectView(tab : TabLayout.Tab?) {
        tab?.let {
            val customView: Typography? = tab.customView  as Typography?
            customView?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
        }
    }

    fun setUnSelectView(tab : TabLayout.Tab?) {
        tab?.let {
            val customView: Typography? = tab.customView  as Typography?
            customView?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_44))
        }
    }
}
