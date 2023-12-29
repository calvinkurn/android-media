package com.tokopedia.productcard.layout.variant

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.COLOR_LIMIT_REPOSITION
import com.tokopedia.productcard.utils.EXTRA_CHAR_SPACE_REPOSITION
import com.tokopedia.productcard.utils.LABEL_VARIANT_CHAR_LIMIT_REPOSITION
import com.tokopedia.productcard.utils.LABEL_VARIANT_TAG
import com.tokopedia.productcard.utils.MAX_LABEL_VARIANT_COUNT
import com.tokopedia.productcard.utils.MIN_LABEL_VARIANT_COUNT
import com.tokopedia.productcard.utils.createColorSampleDrawable
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

internal class VariantLayoutStrategyReposition: VariantLayoutStrategy {
    override fun renderVariant(
        willShowVariant: Boolean,
        view: View,
        productCardModel: ProductCardModel,
    ) {
        val colorContainer = view.findViewById<LinearLayout?>(R.id.labelColorVariantReposition)
        val colorSampleSize = 12.toPx()
        val sizeTextView = view.findViewById<Typography?>(R.id.labelSizeVariantReposition)

        val renderedLabelGroupVariantList = productCardModel.getRenderedLabelGroupVariantList()
        val renderedLabelVariantSizeList = renderedLabelGroupVariantList.filter { it.isSize() }
        val renderedLabelVariantColorList = renderedLabelGroupVariantList.filter { it.isColor() }
        val labelVariantSizeList = productCardModel.labelGroupVariantList.filter { it.isSize() }
        val labelVariantColorList = productCardModel.labelGroupVariantList.filter { it.isColor() }

        val hiddenSizeCount = labelVariantSizeList.size - renderedLabelVariantSizeList.size
        val hiddenColorCount = labelVariantColorList.size - renderedLabelVariantColorList.size

        sizeTextView.shouldShowWithAction(
            willShowVariant
                && !productCardModel.isShowDiscountOrSlashPrice()
                && renderedLabelVariantSizeList.isNotEmpty()
        ) {
            it.renderLabelVariantSize(
                renderedLabelVariantSizeList,
                hiddenSizeCount,
            )
        }

        colorContainer.shouldShowWithAction(
            willShowVariant
                && !productCardModel.isShowDiscountOrSlashPrice()
                && renderedLabelVariantColorList.isNotEmpty()
        ) {
            it.renderVariantColor(
                renderedLabelVariantColorList,
                hiddenColorCount,
                colorSampleSize,
            )
        }

        view.findViewById<LinearLayout?>(R.id.labelVariantContainer).hide()
    }

    override val sizeCharLimit: Int
        get() = LABEL_VARIANT_CHAR_LIMIT_REPOSITION

    override val extraCharSpace: Int
        get() = EXTRA_CHAR_SPACE_REPOSITION

    override val colorLimit: Int
        get() = COLOR_LIMIT_REPOSITION

    override fun getLabelVariantSizeCount(
        productCardModel: ProductCardModel,
        colorVariantTaken: Int,
    ): Int {
        return if (productCardModel.getLabelPrice() == null
            || !productCardModel.hasLabelVariantColor()
        ) {
            MAX_LABEL_VARIANT_COUNT
        } else {
            0
        }
    }

    override fun getLabelVariantColorCount(
        colorVariant: List<ProductCardModel.LabelGroupVariant>,
    ): Int {
        return if (colorVariant.size >= MIN_LABEL_VARIANT_COUNT)
            colorLimit
        else 0
    }

    private fun Typography.renderLabelVariantSize(
        listLabelVariant: List<ProductCardModel.LabelGroupVariant>,
        hiddenSizeCount: Int,
    ) {
        var sizeText = listLabelVariant.joinToString(", ") { it.title }

        if (hiddenSizeCount > 0)
            sizeText += ", +$hiddenSizeCount"

        text = sizeText
    }

    private fun LinearLayout.renderVariantColor(
        listLabelVariant: List<ProductCardModel.LabelGroupVariant>,
        hiddenColorCount: Int,
        colorSampleSize: Int,
    ) {
        this.removeAllViews()

        listLabelVariant.forEachIndexed { index, labelGroupVariant ->
            val gradientDrawable = createColorSampleDrawable(context, labelGroupVariant.hexColor)
            val colorOffset = context.resources.getDimensionPixelSize(
                R.dimen.product_card_label_variant_reposition_color_offset
            )

            val layoutParams = LinearLayout.LayoutParams(colorSampleSize, colorSampleSize)
            layoutParams.marginStart = if (index > 0) colorOffset else 0

            val colorSampleImageView = ImageView(context)
            colorSampleImageView.setImageDrawable(gradientDrawable)
            colorSampleImageView.layoutParams = layoutParams
            colorSampleImageView.tag = LABEL_VARIANT_TAG

            addView(colorSampleImageView)
        }

        if (hiddenColorCount > 0) {
            val additionalTextView = Typography(context)
            additionalTextView.setType(Typography.SMALL)
            additionalTextView.text = " +$hiddenColorCount"
            additionalTextView.tag = LABEL_VARIANT_TAG

            addView(additionalTextView)
        }
    }
}
