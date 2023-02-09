package com.tokopedia.affiliate.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.unifyprinciples.Typography

class AffiliateEducationSearchAdapter(
        fa: FragmentManager,
        lifecycle: Lifecycle,
        var context: Context?,
        private var fragmentList: ArrayList<Fragment>
) : FragmentStateAdapter(fa,lifecycle) {
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun setOnSelectView(tab : TabLayout.Tab) {
        val customView: Typography? = tab.customView as? Typography
        customView?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
    }

    fun setUnSelectView(tab : TabLayout.Tab) {
        val customView: Typography? = tab.customView as? Typography
        customView?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
    }
}
