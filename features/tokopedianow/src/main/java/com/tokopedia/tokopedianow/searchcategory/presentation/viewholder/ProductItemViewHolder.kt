package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductGridCardBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.utils.view.binding.viewBinding

class ProductItemViewHolder(
        itemView: View,
        private val listener: ProductItemListener,
): AbstractViewHolder<ProductItemDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_grid_card
    }

    private var binding: ItemTokopedianowProductGridCardBinding? by viewBinding()

    override fun bind(element: ProductItemDataView?) {
        element ?: return

        binding?.productCard?.apply {
            setData(
                model = element.productCardModel
            )
            setOnClickListener {
                listener.onProductClick(
                    productItemDataView = element
                )
            }
            setOnClickQuantityEditorListener { quantity ->
                listener.onProductNonVariantQuantityChanged(
                    productItemDataView = element,
                    quantity = quantity
                )
            }
            setOnClickQuantityEditorVariantListener {
                listener.onProductChooseVariantClicked(element)
            }
            addOnImpressionListener(element) {
                listener.onProductImpressed(
                    productItemDataView = element
                )
            }
        }
    }
}
