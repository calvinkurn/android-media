package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
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
        binding?.apply {
            bgContainerPmStatus.showWithCondition(!root.context.isDarkMode())
        }
    }

    private fun setupItemPowerMerchant(element: ItemStatusPMUiModel?) {
        binding?.apply {
            tvPmReputationValue.text = getString(R.string.title_pm_value)
            element?.descPM?.let {
                tvDescContentPmSection.setTextMakeHyperlink(it) {
                    itemStatusPowerMerchantListener.onItemClickedGotoPMPro()
                }
                if (element.isNewSellerProjection) {
                    tvDescContentPmSection.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                        )
                    )
                } else {
                    tvDescContentPmSection.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N700_96
                        )
                    )
                }
            }
            tvTitleContentPmSection.showWithCondition(element?.isNewSellerProjection == false)
        }
    }

    private fun setupIconClickListener() {
        binding?.apply {
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