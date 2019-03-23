package com.tokopedia.expresscheckout.view.variant.viewholder

import android.view.View
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.adapter.VariantOptionAdapter
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_NOT_SELECTED
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_SELECTED
import com.tokopedia.expresscheckout.view.variant.viewmodel.TypeVariantViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_variant_detail_product_page.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class TypeVariantViewHolder(val view: View, val listener: CheckoutVariantActionListener) : AbstractViewHolder<TypeVariantViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_variant_detail_product_page
    }

    override fun bind(element: TypeVariantViewModel?) {
        if (element != null) {
            val checkoutVariantProductViewModel = listener.onBindVariantGetProductViewModel()
            if (checkoutVariantProductViewModel != null && checkoutVariantProductViewModel.selectedVariantOptionsIdMap.isNotEmpty()) {
                for ((key, value) in checkoutVariantProductViewModel.selectedVariantOptionsIdMap) {
                    if (key == element.variantId) {
                        for (option: OptionVariantViewModel in element.variantOptions) {
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

            val variantName = "${element.variantName} : "
            itemView.tv_variant_name.text = variantName
            itemView.tv_variant_value.text = element.variantSelectedValue
            val chipsLayoutManager = ChipsLayoutManager.newBuilder(itemView.context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
            val variantOptionAdapter = VariantOptionAdapter(element.variantOptions, listener)
            itemView.rv_variant_options.isNestedScrollingEnabled = false
            itemView.rv_variant_options.layoutManager = chipsLayoutManager
            itemView.rv_variant_options.adapter = variantOptionAdapter

            if (element.variantGuideline.isEmpty()) {
                itemView.tv_variant_guideline.hide()
            } else {
                itemView.tv_variant_guideline.setOnClickListener {
                    listener.onVariantGuidelineClick(element.variantGuideline)
                }
                itemView.tv_variant_guideline.visible()
            }

            listener.onBindVariantUpdateProductViewModel()
        }
    }

}