package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.performance.presentation.adapter.ItemStatusPowerMerchantListener
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusPMUiModel
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.item_status_power_merchant.view.*

class ItemStatusPMViewHolder(
    view: View,
    private val itemStatusPowerMerchantListener: ItemStatusPowerMerchantListener
) : AbstractViewHolder<ItemStatusPMUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_status_power_merchant
    }

    override fun bind(element: ItemStatusPMUiModel?) {
        setupIconClickListener()
        setupItemPowerMerchant(element)
        setupBackgroundColor()
    }

    private fun setupBackgroundColor() {
        with(itemView) {
            bg_container_pm_status?.showWithCondition(!context.isDarkMode())
        }
    }

    private fun setupItemPowerMerchant(element: ItemStatusPMUiModel?) {
        with(itemView) {
            tv_pm_reputation_value?.text = getString(R.string.title_pm_value)
            element?.descPM?.let {
                tv_desc_content_pm_section?.setTextMakeHyperlink(it) {
                    itemStatusPowerMerchantListener.onItemClickedGotoPMPro()
                }
            }
        }
    }

    private fun setupIconClickListener() {
        with(itemView) {
            ic_pm_reputation_right?.setOnClickListener {
                itemStatusPowerMerchantListener.onItemClickedGoToPMActivation()
            }
            potentialPowerMerchantWidget?.setOnClickListener {
                itemStatusPowerMerchantListener.onItemClickedGoToPMActivation()
            }
            if (ic_pm_reputation_right?.isVisible == true) {
                itemStatusPowerMerchantListener.onImpressHeaderPowerMerchantSection()
            }
        }
    }
}