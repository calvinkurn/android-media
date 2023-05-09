package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView.ProductCardCompactListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactWishlistButtonView
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductGridCardBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.utils.view.binding.viewBinding

class ProductItemViewHolder(
    itemView: View,
    private val listener: ProductItemListener,
    private val productCardCompactListener: ProductCardCompactListener? = null,
    private val productCardCompactSimilarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener,
): AbstractViewHolder<ProductItemDataView>(itemView), ProductCardCompactWishlistButtonView.WishlistButtonListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_grid_card
    }

    private var binding: ItemTokopedianowProductGridCardBinding? by viewBinding()

    override fun bind(element: ProductItemDataView) {
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
                listener.onProductChooseVariantClicked(
                    productItemDataView = element
                )
            }
            setWishlistButtonListener(
                wishlistButtonListener = this@ProductItemViewHolder
            )
            setSimilarProductTrackerListener(
                productCardCompactSimilarProductTrackerListener = productCardCompactSimilarProductTrackerListener
            )
            setListener(
                productCardCompactListener = productCardCompactListener
            )
            setOnBlockAddToCartListener {
                listener.onProductCardAddToCartBlocked()
            }
            addOnImpressionListener(element) {
                listener.onProductImpressed(
                    productItemDataView = element
                )
            }
        }
    }

    override fun bind(element: ProductItemDataView?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && element != null) {
            binding?.productCard?.setData(
                model = element.productCardModel
            )
        }
    }

    override fun onWishlistButtonClicked(
        productId: String,
        isWishlistSelected: Boolean,
        descriptionToaster: String,
        ctaToaster: String,
        type: Int,
        ctaClickListener: (() -> Unit)?
    ) {
        listener.onWishlistButtonClicked(
            productId = productId,
            isWishlistSelected = isWishlistSelected,
            descriptionToaster = descriptionToaster,
            ctaToaster = ctaToaster,
            type = type,
            ctaClickListener = ctaClickListener
        )
    }
}
