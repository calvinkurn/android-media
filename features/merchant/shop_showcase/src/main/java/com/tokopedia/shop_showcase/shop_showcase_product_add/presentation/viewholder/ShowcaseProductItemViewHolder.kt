package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.util.getCurrencyFormatted
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

/**
 * @author by Rafli Syam on 2020-03-09
 */

class ShowcaseProductItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val parentItemProductCardContainer: LinearLayout? by lazy {
        itemView.findViewById(R.id.parent_item_product_card)
    }

    val productCheckBox: CheckboxUnify? by lazy {
        itemView.findViewById(R.id.card_checkbox)
    }

    private val productCard: ProductCardListView by lazy {
        itemView.findViewById(R.id.product_card)
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
        productCheckBox?.isChecked = element.ishighlighted
    }

    private fun skipCheckboxAnimation() {
        // temporary solution to avoid unify checkbox intermittent animation when scrolling recyclerview
        productCheckBox?.let {
            val checkboxDrawable = CompoundButtonCompat.getButtonDrawable(it)
            if(checkboxDrawable != null) {
                val checkboxAnimatedVectorDrawableCompat = checkboxDrawable as AnimatedVectorDrawableCompat
                checkboxAnimatedVectorDrawableCompat.stop()
            }
        }
    }

}