package com.tokopedia.shop.common.util

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

object ShopUtilExt {

    fun ProductCardModel.isButtonAtcShown(): Boolean {
        return this.hasAddToCartButton
    }

    fun Typography.setAdaptiveLabelDiscountColor(isAdaptive: Boolean) {
        try {
            if (this.id == R.id.labelDiscount) {
                val adaptiveTextColor = MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_RN500
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
        // callback to hide floating button when snackbar is shown
        this.addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                (bottomViewContainer?.parent as? ViewGroup)?.findViewById<FloatingButtonUnify>(R.id.button_scroll_to_top)?.hide()
                this@setAnchorViewToShopHeaderBottomViewContainer.removeCallback(this)
            }
        })
        bottomViewContainer?.let {
            anchorView = it
        }
        return this
    }
}
