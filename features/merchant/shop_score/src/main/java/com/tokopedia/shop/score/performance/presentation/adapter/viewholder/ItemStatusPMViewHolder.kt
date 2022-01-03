package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.databinding.ItemStatusPowerMerchantBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemStatusPowerMerchantListener
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

    override fun bind(element: ItemStatusPMUiModel?) {
        setupIconClickListener()
        setupItemPowerMerchant(element)
        setupBackgroundColor()
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