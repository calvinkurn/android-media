package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewholder

import android.widget.LinearLayout
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import androidx.viewbinding.ViewBinding
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_showcase.common.util.getCurrencyFormatted
import com.tokopedia.shop_showcase.databinding.ItemProductCardHorizontalBinding
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

/**
 * @author by Rafli Syam on 2020-03-09
 */

class ShowcaseProductItemViewHolder(itemViewBinding: ViewBinding): RecyclerView.ViewHolder(itemViewBinding.root) {

    var productCheckBox: CheckboxUnify? = null
    var productContainer: LinearLayout? = null
    private var productCardItemHorizontal: ProductCardListView? = null

    init {
        if (itemViewBinding is ItemProductCardHorizontalBinding) {
            itemViewBinding.apply {
                productCardItemHorizontal = productCard
                productCheckBox = cardCheckbox
                productContainer = parentItemProductCard
            }
        }
    }

    fun bind(element: BaseShowcaseProduct) {
        if (element is ShowcaseProduct) {
            renderCardState(element)
            productCardItemHorizontal?.setProductModel(ProductCardModel(
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
        productCheckBox?.let { checkbox ->
            val checkboxDrawable = CompoundButtonCompat.getButtonDrawable(checkbox)
            if(checkboxDrawable != null) {
                val checkboxAnimatedVectorDrawableCompat = checkboxDrawable as AnimatedVectorDrawableCompat
                checkboxAnimatedVectorDrawableCompat.stop()
            }
        }
    }

}