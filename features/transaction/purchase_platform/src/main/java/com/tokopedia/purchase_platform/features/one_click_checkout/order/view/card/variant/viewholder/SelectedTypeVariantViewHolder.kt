package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.variant.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.variant.adapter.SelectedVariantOptionAdapter
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.SelectedTypeVariantUiModel
import kotlinx.android.synthetic.main.item_variant_detail_product_page.view.*

class SelectedTypeVariantViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_selected_variant_detail_product_page
    }

    fun bind(element: SelectedTypeVariantUiModel?) {
        if (element != null) {
//            val variantName = "${element.variantName} : "
//            itemView.tv_variant_name.text = variantName
//            itemView.tv_variant_value.text = element.variantSelectedValue
//            val chipsLayoutManager = ChipsLayoutManager.newBuilder(itemView.context)
//                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
//                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
//                    .build()
            val variantOptionAdapter = SelectedVariantOptionAdapter(element.selectedList)
            itemView.rv_variant_options.isNestedScrollingEnabled = true
            itemView.rv_variant_options.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            itemView.rv_variant_options.adapter = variantOptionAdapter

//            if (element.variantGuideline.isEmpty()) {
//                itemView.tv_variant_guideline.hide()
//            } else {
//                itemView.tv_variant_guideline.visible()
//            }

        }
    }
}