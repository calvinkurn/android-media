package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.util.convertCheckMaximumStockLimit
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.databinding.ItemCampaignStockReservedEventInfoBinding
import com.tokopedia.utils.view.binding.viewBinding

class ReservedEventInfoViewHolder(itemView: View?,
                                  private val onAccordionStateChange: (Int) -> Unit): AbstractViewHolder<ReservedEventInfoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_reserved_event_info
    }

    private val binding by viewBinding<ItemCampaignStockReservedEventInfoBinding>()

    override fun bind(element: ReservedEventInfoUiModel) {
        binding?.run {
            labelCampaignStockEventInfo.text = element.eventType
            tvCampaignStockEventName.text = element.eventName
            tvCampaignStockEventCount.text = element.stock.convertCheckMaximumStockLimit(itemView.context)
            tvCampaignStockEventDescription.text = element.eventDesc
            dividerCampaignStockEventInfo.showWithCondition(!(element.isLastEvent && element.isVariant))
            if (element.isVariant) {
                accordionCampaignStock.run {
                    setEventVariantInfo(
                            element.products,
                            element.isAccordionOpened
                    )
                    setOnActionClickListener {
                        element.isAccordionOpened = it
                        onAccordionStateChange(adapterPosition)
                        ProductManageTracking.eventClickPreviewVariantProduct()
                    }
                }
            }
            else {
                accordionCampaignStock.gone()
            }
        }
    }
}