package com.tokopedia.home_component.widget.card

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.tokopedia.home_component.R
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class NetPriceLabel(
    private val background: Drawable?,
    private val typography: Typography?,
) {

    private val context: Context?
        get() = typography?.context

    fun render(labelGroup: SmallProductModel.LabelGroup) {
        renderBackground(labelGroup)
        renderTitle(labelGroup)
    }

    private fun renderBackground(labelGroup: SmallProductModel.LabelGroup) {
        val background = background as? GradientDrawable ?: return

        background.run {
            mutate()

            renderColor(labelGroup)
            renderOutline(labelGroup)
            renderOpacity(labelGroup)
        }
    }

    private fun GradientDrawable.renderColor(labelGroup: SmallProductModel.LabelGroup) {
        val colorList = labelGroup.backgroundColor()?.split(TYPE_DELIMITER) ?: listOf()

        val colorIntArray = when {
            colorList.size == SOLID_COLOR_LIST_SIZE -> {
                val color = safeParseColor(colorList.first(), Color.BLACK)
                intArrayOf(color, color)
            }

            colorList.size > SOLID_COLOR_LIST_SIZE ->
                colorList.map { safeParseColor(it, Color.BLACK) }.toIntArray()

            else ->
                intArrayOf(Color.BLACK, Color.BLACK)
        }

        colors = colorIntArray
    }

    private fun GradientDrawable.renderOutline(labelGroup: SmallProductModel.LabelGroup) {
        val outlineColor = labelGroup.outlineColor()

        val (width, color) =
            if (outlineColor != null)
                STROKE_WIDTH_DP.toPx() to safeParseColor(outlineColor, Color.TRANSPARENT)
            else
                Int.ZERO to Color.TRANSPARENT

        setStroke(width, color)
    }

    private fun GradientDrawable.renderOpacity(labelGroup: SmallProductModel.LabelGroup) {
        alpha = (backgroundOpacity(labelGroup) * OPACITY_INT).toInt()
    }

    private fun backgroundOpacity(labelGroup: SmallProductModel.LabelGroup) =
        labelGroup.backgroundOpacity() ?: DEFAULT_OPACITY

    private fun renderTitle(labelGroup: SmallProductModel.LabelGroup) {
        val context = context ?: return

        TextAndContentDescriptionUtil.setTextAndContentDescription(
            typography,
            labelGroup.title,
            context.getString(R.string.content_desc_small_product_card_net_price)
        )

        setTextColor(context, labelGroup)
    }

    private fun setTextColor(
        context: Context,
        labelGroup: SmallProductModel.LabelGroup
    ) {
        val defaultColor =
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_Static_White)

        val labelGroupTextColor = labelGroup.textColor()

        val textColor =
            if (labelGroupTextColor == null) defaultColor
            else safeParseColor(labelGroupTextColor, defaultColor)

        typography?.setTextColor(textColor)
    }

    private fun safeParseColor(color: String, defaultColor: Int): Int {
        return try {
            Color.parseColor(color)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            defaultColor
        }
    }

    companion object {
        private const val TYPE_DELIMITER = ","
        private const val SOLID_COLOR_LIST_SIZE = 1
        private const val STROKE_WIDTH_DP = 1
        private const val DEFAULT_OPACITY = 0f
        private const val OPACITY_INT = 255
    }
}
