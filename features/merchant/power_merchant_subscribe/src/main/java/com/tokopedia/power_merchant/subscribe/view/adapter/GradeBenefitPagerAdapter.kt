package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmGradeBenefitPagerBinding
import com.tokopedia.power_merchant.subscribe.view.model.WidgetGradeBenefitUiModel

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class GradeBenefitPagerAdapter(
    private val data: WidgetGradeBenefitUiModel,
    private val shopLevelInfoClicked: () -> Unit
) : RecyclerView.Adapter<GradeBenefitPagerAdapter.GradeBenefitPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeBenefitPagerViewHolder {
        val binding = ItemPmGradeBenefitPagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GradeBenefitPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GradeBenefitPagerViewHolder, position: Int) {
        val page = data.benefitPages[position]
        holder.bind(page)
    }

    override fun getItemCount(): Int = data.benefitPages.size

    inner class GradeBenefitPagerViewHolder(private val binding: ItemPmGradeBenefitPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(page: PMGradeWithBenefitsUiModel) {
            setupBenefitListView(page)
            setupTickerAndTermsView(page)
        }

        private fun setupTickerAndTermsView(page: PMGradeWithBenefitsUiModel) {
            if (page.isTabActive) {
                if (page is PMGradeWithBenefitsUiModel.PM) { //PM
                    binding.viewPmTargetAchievement.gone()
                    binding.tickerPmAchievementInfo.gone()
                    binding.dividerPmGradeBenefit.gone()
                } else { //PM Pro
                    showTargetAchievement()
                }
            } else {
                if (page is PMGradeWithBenefitsUiModel.PMProAdvance && data.currentShopLevel <= PMConstant.ShopLevel.TWO) {
                    showTargetAchievement()
                } else {
                    showTickerBasedOnLevel(page)
                }
            }
        }

        private fun showTickerBasedOnLevel(page: PMGradeWithBenefitsUiModel) {
            when (data.currentShopLevel) {
                PMConstant.ShopLevel.ONE -> {
                    val message = when (page.gradeName) {
                        Constant.PM_PRO_ADVANCED -> itemView.context.getString(
                            R.string.pm_grade_benefit_ticker_pm_pro_not_eligible,
                            PMConstant.ShopLevel.TWO,
                            Constant.PM_PRO_ADVANCED
                        )
                        Constant.PM_PRO_EXPERT -> itemView.context.getString(
                            R.string.pm_grade_benefit_ticker_pm_pro_not_eligible,
                            PMConstant.ShopLevel.THREE,
                            Constant.PM_PRO_EXPERT
                        )
                        Constant.PM_PRO_ULTIMATE -> itemView.context.getString(
                            R.string.pm_grade_benefit_ticker_pm_pro_not_eligible,
                            PMConstant.ShopLevel.FOUR,
                            Constant.PM_PRO_ULTIMATE
                        )
                        else -> String.EMPTY
                    }
                    showTicker(message)
                }
                PMConstant.ShopLevel.TWO -> {
                    val message = when (page.gradeName) {
                        Constant.POWER_MERCHANT -> itemView.context.getString(
                            R.string.pm_grade_benefit_ticker_pm_passed, Constant.PM_PRO_ADVANCED
                        )
                        Constant.PM_PRO_EXPERT -> itemView.context.getString(
                            R.string.pm_grade_benefit_ticker_pm_pro_not_eligible,
                            PMConstant.ShopLevel.THREE,
                            Constant.PM_PRO_EXPERT
                        )
                        Constant.PM_PRO_ULTIMATE -> itemView.context.getString(
                            R.string.pm_grade_benefit_ticker_pm_pro_not_eligible,
                            PMConstant.ShopLevel.FOUR,
                            Constant.PM_PRO_ULTIMATE
                        )
                        else -> String.EMPTY
                    }
                    showTicker(message)
                }
                PMConstant.ShopLevel.THREE -> {
                    val message = when (page.gradeName) {
                        Constant.POWER_MERCHANT, Constant.PM_PRO_ADVANCED -> itemView.context.getString(
                            R.string.pm_grade_benefit_ticker_pm_passed, Constant.PM_PRO_EXPERT
                        )
                        Constant.PM_PRO_ULTIMATE -> itemView.context.getString(
                            R.string.pm_grade_benefit_ticker_pm_pro_not_eligible,
                            PMConstant.ShopLevel.FOUR,
                            Constant.PM_PRO_ULTIMATE
                        )
                        else -> String.EMPTY
                    }
                    showTicker(message)
                }
                PMConstant.ShopLevel.FOUR -> {
                    val message = itemView.context.getString(
                        R.string.pm_grade_benefit_ticker_pm_passed, Constant.PM_PRO_ULTIMATE
                    )
                    showTicker(message)
                }
                else -> {
                    hideTicker()
                }
            }
        }

        private fun showTargetAchievement() {
            binding.run {
                tickerPmAchievementInfo.gone()
                viewPmTargetAchievement.visible()
                viewPmTargetAchievement.showInfo(
                    completedOrder = data.currentCompletedOrder,
                    netIncome = data.currentIncome,
                    shopLevel = data.currentShopLevel,
                    shopLevelInfoClicked = {
                        this@GradeBenefitPagerAdapter.shopLevelInfoClicked()
                    }
                )
            }
        }

        private fun showTicker(message: String) {
            binding.run {
                viewPmTargetAchievement.gone()
                dividerPmGradeBenefit.visible()
                tickerPmAchievementInfo.visible()
                tickerPmAchievementInfo.setTextDescription(message)
            }
        }

        private fun hideTicker() {
            binding.run {
                dividerPmGradeBenefit.gone()
                tickerPmAchievementInfo.gone()
                viewPmTargetAchievement.gone()
            }
        }

        private fun setupBenefitListView(page: PMGradeWithBenefitsUiModel) {
            with(binding.rvPmGradeBenefitItem) {
                val benefitAdapter = GradeBenefitAdapter(page.benefitList)
                layoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean = false
                }
                adapter = benefitAdapter
            }
        }
    }
}