package com.tokopedia.power_merchant.subscribe.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.power_merchant.subscribe.view.fragment.MembershipDetailFragment

/**
 * Created by @ilhamsuaib on 23/05/22.
 */

class MembershipViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = mutableListOf<MembershipDetailFragment>()
    private val pmGrades = mutableListOf<PMGradeWithBenefitsUiModel>()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun getPmGradeList(): List<PMGradeWithBenefitsUiModel> = pmGrades

    fun clearFragments() {
        fragments.clear()
        pmGrades.clear()
    }

    fun addFragment(fragment: MembershipDetailFragment, grade: PMGradeWithBenefitsUiModel) {
        fragments.add(fragment)
        pmGrades.add(grade)
    }
}