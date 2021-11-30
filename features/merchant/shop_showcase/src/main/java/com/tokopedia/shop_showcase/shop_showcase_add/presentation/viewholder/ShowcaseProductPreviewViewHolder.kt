package com.tokopedia.shop_showcase.shop_showcase_add.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_showcase.common.util.getCurrencyFormatted
import com.tokopedia.shop_showcase.databinding.ItemProductCardHorizontalBinding
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.listener.ShopShowcasePreviewListener
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class ShowcaseProductPreviewViewHolder(
        itemViewBinding: ItemProductCardHorizontalBinding,
        private val previewListener: ShopShowcasePreviewListener
): RecyclerView.ViewHolder(itemViewBinding.root) {

    private var itemProductCard: ProductCardListView? = null
    private var itemCardCheckbox: CheckboxUnify? = null

    init {
        itemViewBinding.apply {
            itemProductCard = productCard
            itemCardCheckbox = cardCheckbox
        }
    }

    fun bind(element: BaseShowcaseProduct) {
        if (element is ShowcaseProduct) {
            itemProductCard?.apply {
                // set product card model
                setProductModel(ProductCardModel(
                        productImageUrl = element.productImageUrl,
                        productName = element.productName,
                        formattedPrice = element.productPrice.getCurrencyFormatted(),
                        ratingString = element.ratingStarAvg.toString(),
                        reviewCount = element.totalReview,
                        hasDeleteProductButton = element.isCloseable
                ))

                // set delete button onClick listener
                setDeleteProductOnClickListener {
                    previewListener.deleteSelectedProduct(adapterPosition)
                }
            }

            // hide checkbox in preview page
            itemCardCheckbox?.gone()
        }
    }

}