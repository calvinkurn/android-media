package com.tokopedia.tokopedianow.repurchase.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView
import com.tokopedia.tokopedianow.R
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactWishlistButtonView
import com.tokopedia.productcard.compact.productcard.presentation.listener.ProductCardCompactViewListener
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductGridCardBinding
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

class RepurchaseProductViewHolder(
    itemView: View,
    private val listener: RepurchaseProductCardListener? = null,
    private val similarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener? = null,
    private val productCardCompactListener: ProductCardCompactView.ProductCardCompactListener? = null,
): AbstractViewHolder<RepurchaseProductUiModel>(itemView), ProductCardCompactWishlistButtonView.WishlistButtonListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_grid_card
    }

    private var binding: ItemTokopedianowProductGridCardBinding? by viewBinding()

    override fun bind(item: RepurchaseProductUiModel) {
        binding?.productCard?.apply {
            bind(
                model = item.productCardModel,
                listener = createProductListener(item)
            )

            setOnClickListener {
                goToProductDetail(item)
                listener?.onClickProduct(item)
            }

            addOnImpressionListener(item) {
                listener?.onProductImpressed(item)
            }
        }
    }

    override fun bind(item: RepurchaseProductUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && item != null) {
            binding?.productCard?.bind(
                model = item.productCardModel,
                listener = createProductListener(item)
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
        listener?.onWishlistButtonClicked(
            productId = productId,
            isWishlistSelected = isWishlistSelected,
            descriptionToaster = descriptionToaster,
            ctaToaster = ctaToaster,
            type = type,
            ctaClickListener = ctaClickListener
        )
    }

    private fun createProductListener(
        item: RepurchaseProductUiModel
    ) = ProductCardCompactViewListener(
        onQuantityChangedListener = { quantity ->
            listener?.onAddToCartNonVariant(item, quantity)
        },
        clickAddVariantListener = {
            listener?.onAddToCartVariant(item)
        },
        wishlistButtonListener = this@RepurchaseProductViewHolder,
        productCardCompactListener = productCardCompactListener,
        similarProductTrackerListener = similarProductTrackerListener
    )

    private fun goToProductDetail(item: RepurchaseProductUiModel) {
        RouteManager.route(
            itemView.context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            item.productCardModel.productId
        )
    }

    interface RepurchaseProductCardListener {
        fun onClickProduct(
            item: RepurchaseProductUiModel
        )

        fun onAddToCartVariant(
            item: RepurchaseProductUiModel
        )

        fun onAddToCartNonVariant(
            item: RepurchaseProductUiModel,
            quantity: Int
        )

        fun onProductImpressed(
            item: RepurchaseProductUiModel
        )

        fun onClickSimilarProduct()

        fun onWishlistButtonClicked(
            productId: String,
            isWishlistSelected: Boolean,
            descriptionToaster: String,
            ctaToaster: String,
            type: Int,
            ctaClickListener: (() -> Unit)?
        )
    }
}
