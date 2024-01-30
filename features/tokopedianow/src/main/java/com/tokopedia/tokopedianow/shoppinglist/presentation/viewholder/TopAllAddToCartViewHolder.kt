package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowTopAllAddToCartBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.TopAllAddToCartUiModel
import com.tokopedia.utils.view.binding.viewBinding

class TopAllAddToCartViewHolder(
    itemView: View
): AbstractViewHolder<TopAllAddToCartUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_top_all_add_to_cart
    }

    private var binding: ItemTokopedianowTopAllAddToCartBinding? by viewBinding()

    override fun bind(element: TopAllAddToCartUiModel) {
        binding?.apply {
            tpAllPrice.text = element.allPrice
            tpSelectedProductCounter.text = element.selectedProductCounter
            ubAtc.setOnClickListener {

            }
        }
    }

}
