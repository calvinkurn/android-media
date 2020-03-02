package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OptionVariantUiModel.Companion.STATE_NOT_SELECTED
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OptionVariantUiModel.Companion.STATE_SELECTED
import kotlinx.android.synthetic.main.item_variant_detail_product_page.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class TypeVariantViewHolder(view: View, val listener: CheckoutVariantActionListener) : RecyclerView.ViewHolder(view){

    companion object {
        val LAYOUT = R.layout.item_variant_detail_product_page
    }

    fun bind(element: TypeVariantUiModel?) {
        if (element != null) {
            val checkoutVariantProductViewModel = listener.onBindVariantGetProductViewModel()
            if (checkoutVariantProductViewModel.selectedVariantOptionsIdMap.isNotEmpty()) {
                for ((key, value) in checkoutVariantProductViewModel.selectedVariantOptionsIdMap) {
                    if (key == element.variantId) {
                        for (option: OptionVariantUiModel in element.variantOptions) {
                            if (option.optionId == value) {
                                option.currentState = STATE_SELECTED
                                element.variantSelectedValue = option.variantName
                            } else {
                                option.currentState = STATE_NOT_SELECTED
                            }
                        }
                        break
                    }
                }
            }

//            val variantName = "${element.variantName} : "
//            itemView.tv_variant_name.text = variantName
//            itemView.tv_variant_value.text = element.variantSelectedValue
//            val chipsLayoutManager = ChipsLayoutManager.newBuilder(itemView.context)
//                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
//                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
//                    .build()
            val chipsLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val variantOptionAdapter = VariantOptionAdapter(element.variantOptions, listener)
            itemView.rv_variant_options.isNestedScrollingEnabled = false
            itemView.rv_variant_options.layoutManager = chipsLayoutManager
            itemView.rv_variant_options.adapter = variantOptionAdapter

//            if (element.variantGuideline.isEmpty()) {
//                itemView.tv_variant_guideline.hide()
//            } else {
//                itemView.tv_variant_guideline.setOnClickListener {
//                    listener.onVariantGuidelineClick(element.variantGuideline)
//                }
//                itemView.tv_variant_guideline.visible()
//            }

//            listener.onBindVariantUpdateProductViewModel()
        }
    }

}