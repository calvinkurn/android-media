package com.tokopedia.shop_showcase.shop_showcase_add.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.util.getCurrencyFormatted
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.listener.ShopShowcasePreviewListener
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import kotlinx.android.synthetic.main.item_product_card_horizontal.view.*

class ShowcaseProductPreviewViewHolder(itemView: View, private val previewListener: ShopShowcasePreviewListener): RecyclerView.ViewHolder(itemView) {

    private val productCard: ProductCardListView by lazy {
        itemView.findViewById<ProductCardListView>(R.id.product_card)
    }

    fun bind(element: BaseShowcaseProduct) {
        if (element is ShowcaseProduct) {
            productCard.apply {
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
            itemView.card_checkbox.gone()
        }
    }

}