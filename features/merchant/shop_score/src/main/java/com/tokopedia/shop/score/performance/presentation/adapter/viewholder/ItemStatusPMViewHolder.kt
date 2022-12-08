package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.databinding.ItemStatusPowerMerchantBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPMBenefitAdapter
import com.tokopedia.shop.score.performance.presentation.adapter.ItemStatusPowerMerchantListener
import com.tokopedia.shop.score.performance.presentation.model.ItemParentBenefitUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusPMUiModel
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding


class ItemStatusPMViewHolder(
    view: View,
    private val itemStatusPowerMerchantListener: ItemStatusPowerMerchantListener
) : AbstractViewHolder<ItemStatusPMUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_status_power_merchant
    }

    private val binding: ItemStatusPowerMerchantBinding? by viewBinding()
    private val benefitAdapter by lazy { ItemPMBenefitAdapter() }

    override fun bind(element: ItemStatusPMUiModel) {
        setupIconClickListener()
        setupItemPowerMerchant(element)
        setupBackgroundColor()
        setupBenefits()
    }

    private fun setupBenefits() {
        binding?.run {
            setupBenefitItems()
            rvSsPmSectionBenefits.layoutManager = LinearLayoutManager(root.context)
            rvSsPmSectionBenefits.adapter = benefitAdapter
        }
    }

    private fun setupBenefitItems() {
        val items = listOf(
            ItemParentBenefitUiModel(
                iconUrl = ShopScoreConstant.PM_PRO_BENEFIT_URL_2,
                titleResources = R.string.title_item_rm_section_pm_benefit_4
            ),
            ItemParentBenefitUiModel(
                iconUrl = ShopScoreConstant.PM_PRO_BENEFIT_URL_3,
                titleResources = R.string.title_item_rm_section_pm_benefit_5
            )
        )
        benefitAdapter.setBenefits(items)
    }

    private fun setupBackgroundColor() {
        binding?.run {
            bgContainerPmStatus.showWithCondition(!root.context.isDarkMode())
        }
    }

    private fun setupItemPowerMerchant(element: ItemStatusPMUiModel?) {
        binding?.run {
            tvPmReputationValue.text = getString(R.string.title_pm_value)
            element?.descPM?.let {
                tvDescContentPmSection.setTextMakeHyperlink(getString(it)) {
                    itemStatusPowerMerchantListener.onItemClickedGotoPMPro()
                }
            }
            tvSsPmSectionSeeAllBenefits.setOnClickListener {
                itemStatusPowerMerchantListener.onItemClickedSeeAllBenefits()
            }
            iconSsPmSectionSeeAllBenefits.setOnClickListener {
                itemStatusPowerMerchantListener.onItemClickedSeeAllBenefits()
            }
        }
    }

    private fun setupIconClickListener() {
        binding?.run {
            icPmReputationRight.setOnClickListener {
                itemStatusPowerMerchantListener.onItemClickedGoToPMActivation()
            }
            potentialPowerMerchantWidget.setOnClickListener {
                itemStatusPowerMerchantListener.onItemClickedGoToPMActivation()
            }
            if (icPmReputationRight.isVisible) {
                itemStatusPowerMerchantListener.onImpressHeaderPowerMerchantSection()
            }
        }
    }
}
