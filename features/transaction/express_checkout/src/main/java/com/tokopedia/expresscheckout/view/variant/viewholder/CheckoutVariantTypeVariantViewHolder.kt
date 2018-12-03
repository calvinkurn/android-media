package com.tokopedia.expresscheckout.view.variant.viewholder

import android.view.View
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.adapter.VariantOptionAdapter
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantTypeVariantViewModel
import kotlinx.android.synthetic.main.item_variant_detail_product_page.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantTypeVariantViewHolder(val view: View) : AbstractViewHolder<CheckoutVariantTypeVariantViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_variant_detail_product_page
    }

    override fun bind(element: CheckoutVariantTypeVariantViewModel?) {
        if (element != null) {
            itemView.tv_variant_name.text = element.variantName
            itemView.tv_variant_value.text = element.variantSelectedValua
            val chipsLayoutManager = ChipsLayoutManager.newBuilder(itemView.context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
            val variantOptionAdapter = VariantOptionAdapter(element.variantOptions)
            itemView.rv_variant_options.isNestedScrollingEnabled = false
            itemView.rv_variant_options.layoutManager = chipsLayoutManager
            itemView.rv_variant_options.adapter = variantOptionAdapter
        }
    }

}