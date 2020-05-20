package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewholder

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.util.getCurrencyFormatted
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import kotlinx.android.synthetic.main.item_add_product_showcase_grid.view.*
import kotlinx.android.synthetic.main.item_product_card_horizontal.view.*

/**
 * @author by Rafli Syam on 2020-03-09
 */

class ShowcaseProductItemViewHolder(itemView: View, private val context: Context): RecyclerView.ViewHolder(itemView) {

    private val productCard: ProductCardListView by lazy {
        itemView.findViewById<ProductCardListView>(R.id.product_card)
    }

    fun bind(element: BaseShowcaseProduct) {
        if (element is ShowcaseProduct) {
            productCard.setProductModel(ProductCardModel(
                    productImageUrl = element.productImageUrl,
                    productName = element.productName,
                    formattedPrice = element.productPrice.getCurrencyFormatted(),
                    ratingString = "4.5",
                    reviewCount = 2
            ))
        }
    }

    fun renderCardState(element: ShowcaseProduct) {
        itemView.card_checkbox.isChecked = element.ishighlighted
    }

}