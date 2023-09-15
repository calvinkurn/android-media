package com.tokopedia.shop.home.util

import android.graphics.PorterDuff
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.R as productcardR
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.shop.R as shopR

class ProductCardColorOverrideManager {

    private val horizontalPaddingPx by lazy { 4.toPx() }
    private val verticalPaddingPx by lazy { 3.toPx() }
    private val smallTextSizePx by lazy { 10.toPx().toFloat() }

    fun forceLightModeColor(productCard: ProductCardGridView?) {
        productCard?.let {
            val cardView = productCard.findViewById<CardUnify2>(productcardR.id.cardViewProductCard)
            cardView.setCardUnifyBackgroundColor(ContextCompat.getColor(cardView.context, shopR.color.dms_static_white))

            val productName = productCard.findViewById<Typography>(productcardR.id.textViewProductName)
            productName.setTextColor(ContextCompat.getColor(productName.context, shopR.color.dms_static_Unify_Unify_NN950_96_light))

            val productPrice = productCard.findViewById<Typography>(productcardR.id.textViewPrice)
            productPrice.setTextColor(ContextCompat.getColor(productPrice.context, shopR.color.dms_static_Unify_Unify_NN950_96_light))

            val labelDiscount = productCard.findViewById<Label>(productcardR.id.labelDiscount)
            labelDiscount.forceLightRed()

            val productSlashPrice = productCard.findViewById<Typography>(productcardR.id.textViewSlashedPrice)
            productSlashPrice.setTextColor(ContextCompat.getColor(productSlashPrice.context, shopR.color.dms_static_light_NN950_44))

            val soldCount = productCard.findViewById<Typography>(productcardR.id.textViewSales)
            soldCount.setTextColor(ContextCompat.getColor(soldCount.context, shopR.color.dms_static_light_NN950_68))

            val rating = productCard.findViewById<Typography>(productcardR.id.salesRatingFloat)
            rating.setTextColor(ContextCompat.getColor(rating.context, shopR.color.dms_static_light_NN950_68))

            val gimmick = productCard.findViewById<Typography>(productcardR.id.textViewGimmick)
            gimmick.setTextColor(ContextCompat.getColor(gimmick.context, shopR.color.dms_static_light_YN400))

            //Divider bottom sold count
            val salesRatingFloatLine = productCard.findViewById<View>(productcardR.id.salesRatingFloatLine)
            salesRatingFloatLine.setBackgroundColor(ContextCompat.getColor(salesRatingFloatLine.context, shopR.color.dms_static_light_NN950_32))

            //Bottom sold count
            val textViewIntegrity = productCard.findViewById<Typography>(productcardR.id.textViewIntegrity)
            textViewIntegrity.setTextColor(ContextCompat.getColor(textViewIntegrity.context, shopR.color.dms_static_light_NN950_68))

            val textViewFulfillment = productCard.findViewById<Typography>(productcardR.id.textViewFulfillment)
            textViewFulfillment.setTextColor(ContextCompat.getColor(textViewFulfillment.context, shopR.color.dms_static_light_NN950_68))
        }
    }

    fun forceLightModeColor(productCard: ProductCardListView?) {
        productCard?.let {
            val cardView = productCard.findViewById<CardUnify2>(productcardR.id.cardViewProductCard)
            cardView.setCardUnifyBackgroundColor(ContextCompat.getColor(cardView.context, shopR.color.dms_static_white))

            val productName = productCard.findViewById<Typography>(productcardR.id.textViewProductName)
            productName.setTextColor(ContextCompat.getColor(productName.context, shopR.color.dms_static_Unify_Unify_NN950_96_light))

            val productPrice = productCard.findViewById<Typography>(productcardR.id.textViewPrice)
            productPrice.setTextColor(ContextCompat.getColor(productPrice.context, shopR.color.dms_static_Unify_Unify_NN950_96_light))

            val labelDiscount = productCard.findViewById<Label>(productcardR.id.labelDiscount)
            labelDiscount.forceLightRed()

            val productSlashPrice = productCard.findViewById<Typography>(productcardR.id.textViewSlashedPrice)
            productSlashPrice.setTextColor(ContextCompat.getColor(productSlashPrice.context, shopR.color.dms_static_light_NN950_44))

            val soldCount = productCard.findViewById<Typography>(productcardR.id.textViewSales)
            soldCount.setTextColor(ContextCompat.getColor(soldCount.context, shopR.color.dms_static_light_NN950_68))

            val rating = productCard.findViewById<Typography>(productcardR.id.salesRatingFloat)
            rating.setTextColor(ContextCompat.getColor(rating.context, shopR.color.dms_static_light_NN950_68))

            val gimmick = productCard.findViewById<Typography>(productcardR.id.textViewGimmick)
            gimmick.setTextColor(ContextCompat.getColor(gimmick.context, shopR.color.dms_static_light_YN400))

            //Divider bottom sold count
            val salesRatingFloatLine = productCard.findViewById<Typography>(productcardR.id.salesRatingFloatLine)
            salesRatingFloatLine.setBackgroundColor(ContextCompat.getColor(salesRatingFloatLine.context, shopR.color.dms_static_light_NN950_32))

            //Bottom sold count
            val textViewIntegrity = productCard.findViewById<Typography>(productcardR.id.textViewIntegrity)
            textViewIntegrity.setTextColor(ContextCompat.getColor(textViewIntegrity.context, shopR.color.dms_static_light_NN950_68))

            val textViewFulfillment = productCard.findViewById<Typography>(productcardR.id.textViewFulfillment)
            textViewFulfillment.setTextColor(ContextCompat.getColor(textViewFulfillment.context, shopR.color.dms_static_light_NN950_68))
        }
    }

    private fun Label.forceLightRed() {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSizePx)
        typeface = Typography.getFontType(context, true, Typography.SMALL)
        setTextColor(ContextCompat.getColor(context, shopR.color.dms_static_Unify_RN500_light))

        setPadding(horizontalPaddingPx, verticalPaddingPx, horizontalPaddingPx, verticalPaddingPx)

        val drawable = ContextCompat.getDrawable(context, unifycomponentsR.drawable.label_bg)
        drawable?.setColorFilter(context.resources.getColor(shopR.color.dms_static_Unify_RN100_light), PorterDuff.Mode.SRC_ATOP)

        setBackgroundDrawable(drawable)
    }
}
