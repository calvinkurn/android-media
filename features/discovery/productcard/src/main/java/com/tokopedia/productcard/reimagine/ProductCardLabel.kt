package com.tokopedia.productcard.reimagine

import android.content.Context
import android.graphics.Color
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.safeParseColor
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class ProductCardLabel(
    private val background: Drawable?,
    private val typography: Typography?,
) {

    private val context: Context?
        get() = typography?.context

    fun render(labelGroup: ProductCardModel.LabelGroup) {
        renderBackground(labelGroup)
        renderTitle(labelGroup)
    }

    private fun renderBackground(labelGroup: ProductCardModel.LabelGroup) {
        val background = background as? GradientDrawable ?: return

        background.run {
            mutate()

            renderColor(labelGroup)
            renderOutline(labelGroup)
            renderOpacity(labelGroup)
        }
    }

    private fun GradientDrawable.renderColor(labelGroup: ProductCardModel.LabelGroup) {
        val colorList = labelGroup.backgroundColor()?.split(TYPE_DELIMITER) ?: listOf()

        when {
            colorList.size == SOLID_COLOR_LIST_SIZE ->
                setColor(safeParseColor(colorList.first(), Color.BLACK))

            colorList.size > SOLID_COLOR_LIST_SIZE ->
                colors = colorList.map { safeParseColor(it, Color.BLACK) }.toIntArray()

            colorList.isEmpty() ->
                setColor(Color.BLACK)
        }
    }

    private fun GradientDrawable.renderOutline(labelGroup: ProductCardModel.LabelGroup) {
        val outlineColor = labelGroup.outlineColor()

        val (width, color) =
            if (outlineColor != null)
                STROKE_WIDTH_DP.toPx() to safeParseColor(outlineColor, TRANSPARENT)
            else
                Int.ZERO to TRANSPARENT

        setStroke(width, color)
    }

    private fun GradientDrawable.renderOpacity(labelGroup: ProductCardModel.LabelGroup) {
        alpha = (backgroundOpacity(labelGroup) * OPACITY_INT).toInt()
    }

    private fun backgroundOpacity(labelGroup: ProductCardModel.LabelGroup) =
        labelGroup.backgroundOpacity() ?: DEFAULT_OPACITY

    private fun renderTitle(labelGroup: ProductCardModel.LabelGroup) {
        val context = context ?: return

        TextAndContentDescriptionUtil.setTextAndContentDescription(
            typography,
            labelGroup.title,
            context.getString(R.string.product_card_content_desc_label_text),
        )

        val defaultColor =
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_Static_White)

        val labelGroupTextColor = labelGroup.textColor()

        val textColor =
            if (labelGroupTextColor == null) defaultColor
            else safeParseColor(labelGroupTextColor, defaultColor)

        typography?.setTextColor(textColor)
    }

    companion object {
        private const val TYPE_DELIMITER = ","
        private const val SOLID_COLOR_LIST_SIZE = 1
        private const val STROKE_WIDTH_DP = 1
        private const val DEFAULT_OPACITY = 1.0f
        private const val OPACITY_INT = 255
    }
}
