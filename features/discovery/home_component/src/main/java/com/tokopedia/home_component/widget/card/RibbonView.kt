package com.tokopedia.home_component.widget.card

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import com.tokopedia.home_component.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.productcard.R as productcardR
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class RibbonView: LinearLayout {

    private val text by lazyView<Typography?>(R.id.productCardRibbonText)
    private val slip by lazyView<ImageView?>(R.id.productCardRibbonSlip)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        orientation = VERTICAL

        View.inflate(context, R.layout.widget_small_product_card_ribbon, this)
    }

    fun render(ribbon: SmallProductModel.Ribbon?) {
        if (ribbon != null) showRibbon(ribbon)
        else hide()
    }

    private fun showRibbon(ribbon: SmallProductModel.Ribbon) {
        show()

        val ribbonColorSet = ribbonColorSet(ribbon)

        text?.run {
            text = ribbon.text
            (background as? GradientDrawable)?.renderColor(ribbonColorSet.first)
        }

        slip?.run {
            (background as? GradientDrawable)?.renderColor(ribbonColorSet.second)
        }
    }

    private fun ribbonColorSet(ribbon: SmallProductModel.Ribbon): Pair<IntArray, IntArray> {
        return when (ribbon.type) {
            SmallProductModel.Ribbon.Type.Gold -> goldRibbonBackground() to goldRibbonSlip()
            SmallProductModel.Ribbon.Type.Red -> redRibbonBackground() to redRibbonSlip()
        }
    }

    private fun GradientDrawable.renderColor(colorList: IntArray) {
        mutate()

        if (colorList.size > 1)
            colors = colorList
        else if (colorList.size == 1)
            setColor(colorList.first())
    }

    private fun goldRibbonBackground() =
        intArrayOf(
            getColor(productcardR.color.dms_gold_ribbon_gradient_dark),
            getColor(productcardR.color.dms_gold_ribbon_gradient_light),
        )

    private fun goldRibbonSlip() = intArrayOf(getColor(productcardR.color.dms_gold_ribbon_anchor))

    private fun redRibbonBackground() = intArrayOf(getColor(unifyprinciplesR.color.Unify_RN500))

    private fun redRibbonSlip() = intArrayOf(getColor(productcardR.color.dms_ribbon_anchor_static_dark))

    private fun getColor(@ColorRes colorId: Int): Int = ContextCompat.getColor(context, colorId)

    data class Margin(val start: Int = 0, val top: Int = 0)

    private fun <T: View?> View.lazyView(@IdRes id: Int): Lazy<T?> =
        lazyThreadSafetyNone { findViewById(id) }
}
