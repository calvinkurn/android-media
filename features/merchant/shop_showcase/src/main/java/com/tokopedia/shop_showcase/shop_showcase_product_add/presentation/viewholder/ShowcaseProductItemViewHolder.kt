package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewholder

import android.view.View
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.util.getCurrencyFormatted
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import kotlinx.android.synthetic.main.item_product_card_horizontal.view.*

/**
 * @author by Rafli Syam on 2020-03-09
 */

class ShowcaseProductItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val productCard: ProductCardListView by lazy {
        itemView.findViewById<ProductCardListView>(R.id.product_card)
    }

    fun bind(element: BaseShowcaseProduct) {
        if (element is ShowcaseProduct) {
            renderCardState(element)
            productCard.setProductModel(ProductCardModel(
                    productImageUrl = element.productImageUrl,
                    productName = element.productName,
                    formattedPrice = element.productPrice.getCurrencyFormatted(),
                    ratingString = element.ratingStarAvg.toString(),
                    reviewCount = element.totalReview
            ))
            skipCheckboxAnimation()
        }
    }

    fun renderCardState(element: ShowcaseProduct) {
        itemView.card_checkbox.isChecked = element.ishighlighted
    }

    private fun skipCheckboxAnimation() {
        // temporary solution to avoid unify checkbox intermittent animation when scrolling recyclerview
        val checkboxDrawable = CompoundButtonCompat.getButtonDrawable(itemView.card_checkbox)
        if(checkboxDrawable != null) {
            val checkboxAnimatedVectorDrawableCompat = checkboxDrawable as AnimatedVectorDrawableCompat
            checkboxAnimatedVectorDrawableCompat.stop()
        }
    }

}