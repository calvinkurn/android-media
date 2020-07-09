package com.tokopedia.product.manage.feature.campaignstock.ui.customview.variantaccordion

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import kotlinx.android.synthetic.main.item_campaign_stock_variant_action.view.*

class VariantProductStockActionViewHolder(itemView: View?,
                                          private val onActionClicked: (Boolean) -> Unit): AbstractViewHolder<VariantProductStockActionUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_variant_action

        private const val CHEVRON_CLOSED_ROTATION = 0f
        private const val CHEVRON_OPENED_ROTATION = 180f
    }

    override fun bind(element: VariantProductStockActionUiModel) {
        with(itemView) {
            iv_campaign_stock_chevron?.rotation =
                    if (element.isAccordionOpened) {
                        CHEVRON_OPENED_ROTATION
                    } else {
                        CHEVRON_CLOSED_ROTATION
                    }
            setOnClickListener {
                element.isAccordionOpened = !element.isAccordionOpened
                onActionClicked(element.isAccordionOpened)
            }
        }
    }
}