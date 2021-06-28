package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.LabelGroupDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.LabelGroupVariantDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.NonVariantATCDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.VariantATCDataView

class ProductItemViewHolder(
        itemView: View,
        private val productItemListener: ProductItemListener,
): AbstractViewHolder<ProductItemDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_product
    }

    private val productCard: ProductCardGridView? =
            itemView.findViewById(R.id.tokomartSearchCategoryProductCard)

    override fun bind(element: ProductItemDataView?) {
        element ?: return

        productCard?.setProductModel(
                ProductCardModel(
                        productImageUrl = element.imageUrl300,
                        productName = element.name,
                        formattedPrice = element.price,
                        slashedPrice = element.originalPrice,
                        discountPercentage = element.discountPercentageString,
                        countSoldRating = element.ratingAverage,
                        labelGroupList = element.labelGroupDataViewList.mapToLabelGroup(),
                        labelGroupVariantList = element.labelGroupVariantDataViewList.mapToLabelGroupVariant(),
                        variant = element.variantATC?.mapToVariant(),
                        nonVariant = element.nonVariantATC?.mapToNonVariant(),
                )
        )

        productCard?.setImageProductViewHintListener(element, object: ViewHintListener {
            override fun onViewHint() {
                productItemListener.onProductImpressed(element)
            }
        })

        productCard?.setOnClickListener {
            productItemListener.onProductClick(element)
        }

        productCard?.setAddVariantClickListener {
            productItemListener.onProductChooseVariantClicked(element)
        }

        productCard?.setAddToCartNonVariantClickListener(object: ProductCardGridView.ATCNonVariantListener {
            override fun onQuantityChanged(quantity: Int) {
                productItemListener.onProductNonVariantQuantityChanged(element, quantity)
            }
        })
    }

    private fun List<LabelGroupDataView>.mapToLabelGroup() = map {
        ProductCardModel.LabelGroup(
                position = it.position,
                title = it.title,
                type = it.type,
                imageUrl = it.url,
        )
    }

    private fun List<LabelGroupVariantDataView>.mapToLabelGroupVariant() = map {
        ProductCardModel.LabelGroupVariant(
                type = it.type,
                typeVariant = it.typeVariant,
                title = it.title,
                hexColor = it.hexColor,
        )
    }

    private fun VariantATCDataView.mapToVariant(): ProductCardModel.Variant {
        return ProductCardModel.Variant(
                quantity = this.quantity
        )
    }

    private fun NonVariantATCDataView.mapToNonVariant(): ProductCardModel.NonVariant {
        return ProductCardModel.NonVariant(
                quantity = this.quantity,
                maxQuantity = this.maxQuantity,
                minQuantity = this.minQuantity,
        )
    }
}