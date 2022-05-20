package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.databinding.WidgetPmPotentialBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.PotentialAdapter
import com.tokopedia.power_merchant.subscribe.view.model.PotentialItemUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPotentialUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class PotentialWidget(
    itemView: View,
    private val listener: Listener,
    private val powerMerchantTracking: PowerMerchantTracking
) : AbstractViewHolder<WidgetPotentialUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_potential
    }

    private val binding: WidgetPmPotentialBinding? by viewBinding()
    private val benefitList by getBenefitList()

    override fun bind(element: WidgetPotentialUiModel) {
        binding?.run {
            setupRecyclerView()

            tvPmPotentialDescription.text = root.context.getString(
                R.string.pm_registration_potential_description
            ).parseAsHtml()

            tvPmPotentialCtaCategory.text = root.context.getString(
                R.string.pm_service_fee_by_category
            ).parseAsHtml()
            tvPmPotentialCtaCategory.setOnClickListener {
                powerMerchantTracking.sendEventClickSeeCategory(element.shopScore.toString())
                listener.showServiceFeeByCategory()
            }

            showBackground()
        }
    }

    private fun showBackground() {
        try {
            binding?.viewPmPotentialBg?.setBackgroundResource(R.drawable.bg_pm_registration_potential_benefit)
        } catch (e: Exception) {
            binding?.viewPmPotentialBg?.gone()
        }
    }

    private fun setupRecyclerView() {
        binding?.run {
            rvPmPotential.layoutManager = object : LinearLayoutManager(
                root.context, HORIZONTAL, false
            ) {
                override fun canScrollVertically(): Boolean = false
            }

            containerPmPotentialBenefit.post {
                val itemWidth = containerPmPotentialBenefit.measuredWidth / benefitList.size
                rvPmPotential.adapter = PotentialAdapter(benefitList, itemWidth)
            }
        }
    }

    private fun getBenefitList(): Lazy<List<PotentialItemUiModel>> {
        return lazy {
            listOf(
                PotentialItemUiModel(
                    imgUrl = PMConstant.Images.PM_POTENTIAL_BENEFIT_01,
                    description = getString(R.string.pm_potential_benefit_01)
                ),
                PotentialItemUiModel(
                    imgUrl = PMConstant.Images.PM_POTENTIAL_BENEFIT_02,
                    description = getString(R.string.pm_potential_benefit_02)
                ),
                PotentialItemUiModel(
                    imgUrl = PMConstant.Images.PM_POTENTIAL_BENEFIT_03,
                    description = getString(R.string.pm_potential_benefit_03)
                )
            )
        }
    }

    interface Listener {
        fun showServiceFeeByCategory()
    }
}