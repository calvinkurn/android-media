package com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductUiModel

class RepurchaseProductViewHolder(
    itemView: View,
    private val listener: RepurchaseProductCardListener? = null
): AbstractViewHolder<RepurchaseProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_grid_card
    }

    private val productCard: ProductCardGridView? by lazy {
        itemView.findViewById(R.id.tokoNowGridProductCard)
    }

    override fun bind(item: RepurchaseProductUiModel) {
//        productCard?.setProductModel(
//            ProductCardModel(
//                productImageUrl = data.imageUrl300,
//                productName = data.name,
//                formattedPrice = data.price,
//                slashedPrice = data.originalPrice,
//                discountPercentage = data.discountPercentageString,
//                countSoldRating = data.ratingAverage,
//                labelGroupList = data.labelGroupDataViewList,
//                labelGroupVariantList = data.labelGroupVariantDataViewList,
//                variant = data.variantATC,
//                nonVariant = data.nonVariantATC,
//            )
//        )

        productCard?.setProductModel(item.productCard)

        productCard?.setImageProductViewHintListener(item, object: ViewHintListener {
            override fun onViewHint() {
                listener?.onProductImpressed(item)
            }
        })

        productCard?.setOnClickListener {
            listener?.onClickProduct(item)
        }

        productCard?.setAddVariantClickListener {
            listener?.onAddToCartVariant(item)
        }

        productCard?.setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
            override fun onQuantityChanged(quantity: Int) {
                listener?.onAddToCartNonVariant(item, quantity)
            }
        })
    }

    interface RepurchaseProductCardListener {
        fun onClickProduct(item: RepurchaseProductUiModel)
        fun onAddToCartVariant(item: RepurchaseProductUiModel)
        fun onAddToCartNonVariant(item: RepurchaseProductUiModel, quantity: Int)
        fun onProductImpressed(item: RepurchaseProductUiModel)
    }
}
