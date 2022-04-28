package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.databinding.WidgetPmGradeBenefitBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.GradeBenefitPagerAdapter
import com.tokopedia.power_merchant.subscribe.view.model.WidgetGradeBenefitUiModel
import com.tokopedia.unifycomponents.setIconUnify
import com.tokopedia.utils.view.binding.viewBinding
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class GradeBenefitWidget(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<WidgetGradeBenefitUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_grade_benefit
    }

    private val binding: WidgetPmGradeBenefitBinding? by viewBinding()

    override fun bind(element: WidgetGradeBenefitUiModel) {
        setupView(element)
        setupTabLayout(element)
        setupPagerView(element)
        selectDefaultTab(element)
    }

    private fun setupView(element: WidgetGradeBenefitUiModel) = binding?.run {
        tvPmLearMorePowerMerchant.setOnClickListener {
            RouteManager.route(root.context, element.ctaAppLink)
        }
    }

    private fun selectDefaultTab(element: WidgetGradeBenefitUiModel) = binding?.run {
        val selected = element.benefitPages.indexOfFirst { it.isActive }
        if (selected != RecyclerView.NO_POSITION) {
            rvPmGradeBenefitPager.scrollToPosition(selected)
            tabPmGradeBenefit.tabLayout.getTabAt(selected)?.select()
        }
    }

    private fun setupTabLayout(element: WidgetGradeBenefitUiModel) {
        binding?.tabPmGradeBenefit?.run {
            tabLayout.removeAllTabs()
            try {
                getTabList(element.benefitPages).filterNotNull()
                    .forEachIndexed { i, pair ->
                        addNewTab(pair.first)
                        getUnifyTabLayout().getTabAt(i)?.setIconUnify(pair.second)
                    }
            } catch (e: Exception) {
                Timber.e(e)
            }
            tabLayout.tabRippleColor = ColorStateList.valueOf(Color.TRANSPARENT)
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    setOnTabSelected(tabLayout.selectedTabPosition)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
            post {
                val selectedTabPosition = element.benefitPages.indexOfFirst { it.isActive }
                if (selectedTabPosition != RecyclerView.NO_POSITION) {
                    tabLayout.getTabAt(selectedTabPosition)?.select()
                }
            }
        }
    }

    private fun getTabList(benefitPages: List<PMGradeWithBenefitsUiModel>): List<Pair<String, Int>?> {
        return benefitPages.mapIndexed { i, _ ->
            return@mapIndexed when (i) {
                Constant.POWER_MERCHANT_TAB_INDEX -> {
                    Pair(getString(R.string.pm_power_merchant), IconUnify.BADGE_PM_FILLED)
                }
                Constant.PM_PRO_ADVANCED_TAB_INDEX -> {
                    Pair(getString(R.string.pm_pro_advanced), IconUnify.BADGE_PMPRO_FILLED)
                }
                Constant.PM_PRO_EXPERT_TAB_INDEX -> {
                    Pair(getString(R.string.pm_pro_expert), IconUnify.BADGE_PMPRO_FILLED)
                }
                Constant.PM_PRO_ULTIMATE_TAB_INDEX -> {
                    Pair(getString(R.string.pm_pro_ultimate), IconUnify.BADGE_PMPRO_FILLED)
                }
                else -> null
            }
        }
    }

    private fun setOnTabSelected(position: Int) = binding?.run {
        rvPmGradeBenefitPager.smoothScrollToPosition(position)
    }

    private fun setupPagerView(element: WidgetGradeBenefitUiModel) {
        binding?.rvPmGradeBenefitPager?.run {
            val mLayoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
                override fun canScrollVertically(): Boolean = false
            }
            val pagerAdapter = GradeBenefitPagerAdapter(
                element, listener::showShopLevelInfoBottomSheet
            )
            layoutManager = mLayoutManager
            adapter = pagerAdapter

            try {
                PagerSnapHelper().attachToRecyclerView(this)
            } catch (e: IllegalStateException) {
                Timber.e(e)
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        selectTab(mLayoutManager)
                    }
                }
            })
        }
    }

    private fun selectTab(mLayoutManager: LinearLayoutManager) = binding?.run {
        val selectedPage = mLayoutManager.findFirstVisibleItemPosition()
        if (selectedPage != RecyclerView.NO_POSITION) {
            tabPmGradeBenefit.tabLayout.getTabAt(selectedPage)?.select()
        }
    }

    interface Listener {
        fun showShopLevelInfoBottomSheet() {}
    }
}