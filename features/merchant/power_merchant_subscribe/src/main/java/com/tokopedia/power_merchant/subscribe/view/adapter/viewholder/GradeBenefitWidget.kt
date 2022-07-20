package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
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
    private val listener: Listener,
    private val powerMerchantTracking: PowerMerchantTracking
) : AbstractViewHolder<WidgetGradeBenefitUiModel>(itemView) {

    companion object {
        private const val SATURATION_INACTIVE = 0.0f
        private const val SATURATION_ACTIVE = 1f
        val RES_LAYOUT = R.layout.widget_pm_grade_benefit
    }

    private var highestHeight = Int.ZERO
    private val binding: WidgetPmGradeBenefitBinding? by viewBinding()

    override fun bind(element: WidgetGradeBenefitUiModel) {
        setupView(element)
        setupTabLayout(element)
        setupPagerView(element)
        selectDefaultTab(element)
    }

    private fun setupView(element: WidgetGradeBenefitUiModel) = binding?.run {
        tvPmLearMorePowerMerchant.setOnClickListener {
            powerMerchantTracking.sendEventClickLearnMorePM(element.shopScore.toString())
            RouteManager.route(root.context, element.ctaAppLink)
        }
        tvPmLearMorePowerMerchant.addOnImpressionListener(element.impressHolder){
            powerMerchantTracking.sendEventImpressUpliftPmPro(element.shopScore.toString())
        }
    }

    private fun selectDefaultTab(element: WidgetGradeBenefitUiModel) = binding?.run {
        val selected = element.benefitPages.indexOfFirst { it.isTabActive }
        if (selected != RecyclerView.NO_POSITION) {
            rvPmGradeBenefitPager.scrollToPosition(selected)
            tabPmGradeBenefit.tabLayout.getTabAt(selected)?.select()
        }
    }

    private fun setupTabLayout(element: WidgetGradeBenefitUiModel) {
        binding?.tabPmGradeBenefit?.run {
            tabLayout.removeAllTabs()
            try {
                val activeTabIndex = element.benefitPages.indexOfLast { it.isTabActive }
                element.benefitPages.forEachIndexed { i, page ->
                    addNewTab(page.tabLabel)
                    getUnifyTabLayout().getTabAt(i)?.run {
                        setIconUnify(page.tabResIcon)
                        if (i == activeTabIndex) {
                            setUnifyTabIconColorFilter(this.customView, SATURATION_ACTIVE)
                        } else {
                            setUnifyTabIconColorFilter(this.customView, SATURATION_INACTIVE)
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
            tabLayout.tabRippleColor = ColorStateList.valueOf(Color.TRANSPARENT)
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    setOnTabSelected(tabLayout.selectedTabPosition)

                    for (i in Int.ZERO..getUnifyTabLayout().tabCount.minus(Int.ONE)) {
                        val view = getUnifyTabLayout().getTabAt(i)
                        if (i == tabLayout.selectedTabPosition) {
                            setUnifyTabIconColorFilter(view?.customView, SATURATION_ACTIVE)
                        } else {
                            setUnifyTabIconColorFilter(view?.customView, SATURATION_INACTIVE)
                        }
                    }

                    sendTrackerOnTabSelected(element.benefitPages, tabLayout.selectedTabPosition)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
            post {
                val selectedTabPosition = element.benefitPages.indexOfLast { it.isTabActive }
                if (selectedTabPosition != RecyclerView.NO_POSITION) {
                    tabLayout.getTabAt(selectedTabPosition)?.select()
                }
            }
        }
    }

    private fun sendTrackerOnTabSelected(pages: List<PMGradeWithBenefitsUiModel>, position: Int) {
        val tabLabel = pages.getOrNull(position)?.tabLabel.orEmpty()
        powerMerchantTracking.sendEventClickTabPowerMerchantPro(tabLabel)
    }

    private fun setUnifyTabIconColorFilter(view: View?, saturation: Float) {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(saturation)
        val colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
        view?.findViewById<IconUnify>(com.tokopedia.unifycomponents.R.id.tab_item_icon_unify_id)?.colorFilter =
            colorMatrixColorFilter
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
                    val position = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if (position != RecyclerView.NO_POSITION && element.benefitPages.size > Int.ONE) {
                        mLayoutManager.findViewByPosition(position)?.let { view ->
                            refreshTableHeight(view)
                        }
                    }
                }
            })
        }
    }

    private fun refreshTableHeight(view: View) {
        view.post {
            binding?.run {
                val wMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                val hMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(Int.ZERO, View.MeasureSpec.UNSPECIFIED)
                view.measure(wMeasureSpec, hMeasureSpec)

                if (rvPmGradeBenefitPager.layoutParams?.height != view.measuredHeight) {
                    rvPmGradeBenefitPager.layoutParams =
                        (rvPmGradeBenefitPager.layoutParams as? ConstraintLayout.LayoutParams)
                            ?.also { lp ->
                                if (view.measuredHeight > highestHeight) {
                                    highestHeight = view.measuredHeight
                                    lp.height = view.measuredHeight
                                }
                            }
                }
            }
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