package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.PMProURL
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemStatusPowerMerchantProBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemStatusPowerMerchantProListener
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusPMProUiModel
import com.tokopedia.utils.view.binding.viewBinding


class ItemStatusPMProViewHolder(
    view: View,
    private val itemStatusPowerMerchantProListener: ItemStatusPowerMerchantProListener
) : AbstractViewHolder<ItemStatusPMProUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_status_power_merchant_pro
    }

    private val binding: ItemStatusPowerMerchantProBinding? by viewBinding()

    override fun bind(element: ItemStatusPMProUiModel?) {
        setupIconClickListener()
        setupItemPowerMerchantPro()
    }

    private fun setupItemPowerMerchantPro() {
        binding?.run {
            icPmProBadgeCurrentStatus.loadImage(PMProURL.ICON_URL)
            tvPmProReputationValue.text = getString(R.string.title_pm_pro_value)
            tvDescContentPmProSection.text = getString(R.string.desc_content_pm_pro_section)
        }
    }

    private fun setupIconClickListener() {
        binding?.run {
            icPmProReputationRight.setOnClickListener {
                itemStatusPowerMerchantProListener.onItemClickedGoToPMProActivation()
            }
            potentialPowerMerchantProWidget.setOnClickListener {
                itemStatusPowerMerchantProListener.onItemClickedGoToPMProActivation()
            }
        }
    }
}