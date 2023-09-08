package com.tokopedia.shop.common.util

import android.graphics.drawable.Drawable
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.shop.R

object ShopUtilExt {

    fun ProductCardModel.isButtonAtcShown(): Boolean {
        return this.hasAddToCartButton
    }

    fun Typography.setAdaptiveLabelDiscountColor(isAdaptive: Boolean) {
        try {
            if (this.id == R.id.labelDiscount) {
                val adaptiveTextColor = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_RN500
                )
                val adaptiveBackground = MethodChecker.getDrawable(
                    context,
                    R.drawable.bg_shop_product_price_discount_adaptive
                )
                val staticTextColor =
                    MethodChecker.getColor(context, R.color.dms_static_Unify_RN500_light)
                val staticBackground = MethodChecker.getDrawable(
                    context,
                    R.drawable.bg_shop_product_price_discount_static
                )
                val textColor: Int
                val background: Drawable
                if (isAdaptive) {
                    textColor = adaptiveTextColor
                    background = adaptiveBackground
                } else {
                    textColor = staticTextColor
                    background = staticBackground
                }
                setTextColor(textColor)
                setBackground(background)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun Snackbar.setAnchorViewToShopHeaderBottomViewContainer(bottomViewContainer: View?): Snackbar {
        bottomViewContainer?.let {
            anchorView = it
        }
        return this
    }
}
