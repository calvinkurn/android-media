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
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.kotlin.extensions.view.asCamelCase
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.view.adapter.GradeBenefitPagerAdapter
import com.tokopedia.power_merchant.subscribe.view.model.WidgetGradeBenefitUiModel
import kotlinx.android.synthetic.main.widget_pm_grade_benefit.view.*
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class GradeBenefitWidget(itemView: View) : AbstractViewHolder<WidgetGradeBenefitUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_grade_benefit
    }

    override fun bind(element: WidgetGradeBenefitUiModel) {
        setupTabLayout(element)
        setupPagerView(element)
        selectDefaultTab(element)
        setupView(element)
    }

    private fun setupView(element: WidgetGradeBenefitUiModel) = with(itemView) {
        tvPmLearMorePowerMerchant.setOnClickListener {
            RouteManager.route(context, Constant.Url.POWER_MERCHANT_EDU)
        }

        val isPmPro = element.selectedPmTireType == PMConstant.PMTierType.PM_PRO
        if (isPmPro) {
            tvPmGradeBenefitTitle.text = getString(R.string.pm_grade_benefit_widget_title_pm_pro)
            tvPmGradeBenefitDescription.visible()
        } else {
            tvPmGradeBenefitTitle.text = getString(R.string.pm_grade_benefit_widget_title_pm_pro)
            tvPmGradeBenefitDescription.gone()
        }
    }

    private fun selectDefaultTab(element: WidgetGradeBenefitUiModel) {
        val selected = element.benefitPages.indexOfFirst { it.isActive }
        if (selected != RecyclerView.NO_POSITION) {
            itemView.rvPmGradeBenefitPager.scrollToPosition(selected)
            itemView.tabPmGradeBenefit.tabLayout.tabRippleColor = ColorStateList.valueOf(Color.TRANSPARENT)
            itemView.tabPmGradeBenefit.tabLayout.getTabAt(selected)?.select()
        }
    }

    private fun setupTabLayout(element: WidgetGradeBenefitUiModel) {
        with(itemView.tabPmGradeBenefit) {
            val isSinglePage = element.benefitPages.size <= 1
            if (isSinglePage) {
                tabLayout.gone()
                return@with
            }

            tabLayout.visible()
            tabLayout.removeAllTabs()
            element.benefitPages.forEach {
                addNewTab(it.gradeName.asCamelCase())
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val selectedTabIndex = tabPmGradeBenefit.tabLayout.selectedTabPosition
                    setOnTabSelected(selectedTabIndex)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun setOnTabSelected(position: Int) = with(itemView) {
        rvPmGradeBenefitPager.smoothScrollToPosition(position)
    }

    private fun setupPagerView(element: WidgetGradeBenefitUiModel) {
        with(itemView.rvPmGradeBenefitPager) {
            val mLayoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
                override fun canScrollVertically(): Boolean = false
            }
            val pagerAdapter = GradeBenefitPagerAdapter(element.benefitPages)
            layoutManager = mLayoutManager
            adapter = pagerAdapter

            val isSinglePage = element.benefitPages.size <= 1
            if (!isSinglePage) {
                try {
                    PagerSnapHelper().attachToRecyclerView(this)
                } catch (e: IllegalStateException) {
                    Timber.e(e)
                }
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

    private fun selectTab(mLayoutManager: LinearLayoutManager) = with(itemView) {
        val selectedPage = mLayoutManager.findFirstVisibleItemPosition()
        if (selectedPage != RecyclerView.NO_POSITION) {
            tabPmGradeBenefit.tabLayout.getTabAt(selectedPage)?.select()
        }
    }
}