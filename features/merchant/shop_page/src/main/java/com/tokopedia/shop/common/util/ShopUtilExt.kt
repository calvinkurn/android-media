package com.tokopedia.shop.common.util

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.orZero
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
        } catch (_: Exception) {
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

    fun String.clearHtmlTag(): String{
        val regex = """<[^>]+>""".toRegex()
        return regex.replace(this, "")
    }

    /**
     * This method can be used to determine whether view is visible on a certain screen area or not
     * @param screenWidth set constraint for the width of screen, getScreenWidth() will be used if not set
     * @param screenHeight set constraint for the height of screen, getScreenHeight() will be used if not set
     */
    fun View.isViewRectVisibleOnScreenArea(
        screenWidth: Int = getScreenWidth(),
        screenHeight: Int = getScreenHeight()
    ): Boolean {
        if (!isShown) {
            return false
        }
        val intArraySize = 2
        val viewLocationOnScreen = IntArray(intArraySize)
        getLocationOnScreen(viewLocationOnScreen)
        val viewLeft = viewLocationOnScreen.getOrNull(Int.ZERO).orZero()
        val viewTop = viewLocationOnScreen.getOrNull(Int.ONE).orZero()
        val viewRight = viewLeft + width
        val viewBottom = viewTop + height
        val viewRect = Rect(viewLeft, viewTop, viewRight, viewBottom)
        val screenRect = Rect(Int.ZERO, Int.ZERO, screenWidth, screenHeight)
        return screenRect.top <= viewRect.top && screenRect.bottom >= viewRect.bottom &&
            screenRect.left <= viewRect.left && screenRect.right >= viewRect.right
    }

    fun asd(){}
}
