package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.variant.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.TypeVariantUiModel
import kotlinx.android.synthetic.main.item_selected_variant_option_detail_product_page.view.*

class SelectedOptionVariantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.item_selected_variant_option_detail_product_page
    }

    fun bind(element: TypeVariantUiModel) {
        val variantName = "${element.variantName} : "
        itemView.tv_variant_name.text = variantName
        itemView.tv_variant_value.text = element.variantSelectedValue
    }
}