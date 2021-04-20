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
import kotlinx.android.synthetic.main.item_campaign_stock_reserved_event_info.view.*

class ReservedEventInfoViewHolder(itemView: View?,
                                  private val onAccordionStateChange: (Int) -> Unit): AbstractViewHolder<ReservedEventInfoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_reserved_event_info
    }

    override fun bind(element: ReservedEventInfoUiModel) {
        with(itemView) {
            label_campaign_stock_event_info?.text = element.eventType
            tv_campaign_stock_event_name?.text = element.eventName
            tv_campaign_stock_event_count?.text = element.stock.convertCheckMaximumStockLimit(context)
            tv_campaign_stock_event_description?.text = element.eventDesc
            divider_campaign_stock_event_info?.showWithCondition(!(element.isLastEvent && element.isVariant))
            if (element.isVariant) {
                accordion_campaign_stock?.run {
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
                accordion_campaign_stock?.gone()
            }
        }
    }
}