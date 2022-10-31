package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductGridCardBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.mapper.mapToLabelGroup
import com.tokopedia.tokopedianow.searchcategory.presentation.mapper.mapToLabelGroupVariant
import com.tokopedia.tokopedianow.searchcategory.presentation.mapper.mapToNonVariant
import com.tokopedia.tokopedianow.searchcategory.presentation.mapper.mapToVariant
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.utils.view.binding.viewBinding

class ProductItemViewHolder(
        itemView: View,
        private val productItemListener: ProductItemListener,
): AbstractViewHolder<ProductItemDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_grid_card
    }

    private var binding: ItemTokopedianowProductGridCardBinding? by viewBinding()

    override fun bind(element: ProductItemDataView?) {
        element ?: return

        binding?.productCard?.apply {
            setData(element.productCardModel)

//            setImageProductViewHintListener(element, object: ViewHintListener {
//                override fun onViewHint() {
//                    productItemListener.onProductImpressed(element)
//                }
//            })
//
//            setOnClickListener {
//                productItemListener.onProductClick(element)
//            }
//
//            setAddVariantClickListener {
//                productItemListener.onProductChooseVariantClicked(element)
//            }
//
//            setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
//                override fun onQuantityChanged(quantity: Int) {
//                    productItemListener.onProductNonVariantQuantityChanged(element, quantity)
//                }
//            })
        }
    }

    override fun onViewRecycled() {
//        binding?.tokoNowGridProductCard?.recycle()
    }
}
