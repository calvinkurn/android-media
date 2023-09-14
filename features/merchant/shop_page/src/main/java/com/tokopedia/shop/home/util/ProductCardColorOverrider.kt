package com.tokopedia.shop.home.util

import androidx.core.content.ContextCompat
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.R as productcardR
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.shop.R as shopR

class ProductCardColorOverrider {

    fun forceLightModeColor(productCard: ProductCardGridView?) {
        productCard?.let {
            val cardView = productCard.findViewById<CardUnify2>(productcardR.id.cardViewProductCard)
            cardView.setCardUnifyBackgroundColor(ContextCompat.getColor(cardView.context, shopR.color.dms_static_white))

            val productName = productCard.findViewById<Typography>(productcardR.id.textViewProductName)
            productName.setTextColor(ContextCompat.getColor(productName.context, shopR.color.dms_static_Unify_Unify_NN950_96_light))

            val productPrice = productCard.findViewById<Typography>(productcardR.id.textViewPrice)
            productPrice.setTextColor(ContextCompat.getColor(productName.context, shopR.color.dms_static_Unify_Unify_NN950_96_light))
        }
    }

    fun forceLightModeColor(productCard: ProductCardListView?) {
        productCard?.let {
            val cardView = productCard.findViewById<CardUnify2>(productcardR.id.cardViewProductCard)
            cardView.setCardUnifyBackgroundColor(ContextCompat.getColor(cardView.context, shopR.color.dms_static_white))

            val productName = productCard.findViewById<Typography>(productcardR.id.textViewProductName)
            productName.setTextColor(ContextCompat.getColor(productName.context, shopR.color.dms_static_Unify_Unify_NN950_96_light))

            val productPrice = productCard.findViewById<Typography>(productcardR.id.textViewPrice)
            productPrice.setTextColor(ContextCompat.getColor(productName.context, shopR.color.dms_static_Unify_Unify_NN950_96_light))
        }
    }

}
