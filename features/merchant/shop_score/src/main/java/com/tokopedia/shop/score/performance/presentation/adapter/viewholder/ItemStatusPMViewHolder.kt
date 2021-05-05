package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.performance.presentation.adapter.ItemStatusPowerMerchantListener
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusPMUiModel
import kotlinx.android.synthetic.main.item_status_power_merchant.view.*

class ItemStatusPMViewHolder(view: View,
                             private val itemStatusPowerMerchantListener: ItemStatusPowerMerchantListener) : AbstractViewHolder<ItemStatusPMUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_status_power_merchant
    }

    private val impressHolderPowerMerchantHeader = ImpressHolder()

    override fun bind(element: ItemStatusPMUiModel?) {
        with(itemView) {
            potentialPowerMerchantWidget?.addOnImpressionListener(impressHolderPowerMerchantHeader) {
                itemStatusPowerMerchantListener.onImpressHeaderPowerMerchantSection()
            }
        }
        setupIconClickListener()
        setupItemPowerMerchant(element)
    }

    private fun setupItemPowerMerchant(element: ItemStatusPMUiModel?) {
        with(itemView) {
            potentialPowerMerchantWidget?.background = ContextCompat.getDrawable(context, R.drawable.bg_header_bronze)
            tv_pm_reputation_value?.text = getString(R.string.title_pm_value)
            element?.descPM?.let {
                tv_desc_content_pm_section?.setTextMakeHyperlink(it) {
                    itemStatusPowerMerchantListener.onItemClickedGotoPMPro()
                }
            }
            tv_title_content_pm_section?.showWithCondition(element?.isNewSeller == false)
        }
    }

    private fun setupIconClickListener() {
        with(itemView) {
            ic_pm_reputation_right?.setOnClickListener {
                itemStatusPowerMerchantListener.onItemClickedGoToPMActivation()
            }
        }
    }
}