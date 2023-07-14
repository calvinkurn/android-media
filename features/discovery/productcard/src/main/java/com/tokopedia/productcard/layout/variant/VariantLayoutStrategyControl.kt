package com.tokopedia.productcard.layout.variant

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.EXTRA_CHAR_SPACE
import com.tokopedia.productcard.utils.LABEL_VARIANT_CHAR_LIMIT
import com.tokopedia.productcard.utils.LABEL_VARIANT_TAG
import com.tokopedia.productcard.utils.MAX_LABEL_VARIANT_COUNT
import com.tokopedia.productcard.utils.MIN_LABEL_VARIANT_COUNT
import com.tokopedia.productcard.utils.createColorSampleDrawable
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.productcard.utils.toUnifyLabelType
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

internal class VariantLayoutStrategyControl : VariantLayoutStrategy {

    override fun renderVariant(
        willShowVariant: Boolean,
        view: View,
        productCardModel: ProductCardModel,
    ) {
        val container = view.findViewById<LinearLayout?>(R.id.labelVariantContainer)
        val colorSampleSize = 14.toPx()

        container?.shouldShowWithAction(willShowVariant) { labelVariantContainer ->
            labelVariantContainer.removeAllViews()

            val marginStart = 4.toPx()

            productCardModel.getRenderedLabelGroupVariantList()
                .forEachIndexed { index, labelVariant ->
                    val hasMarginStart = index > 0

                    when {
                        labelVariant.isColor() -> {
                            labelVariantContainer.addLabelVariantColor(
                                labelVariant,
                                hasMarginStart,
                                colorSampleSize,
                                marginStart,
                            )
                        }
                        labelVariant.isSize() -> {
                            labelVariantContainer.addLabelVariantSize(
                                labelVariant,
                                hasMarginStart,
                                marginStart,
                            )
                        }
                        labelVariant.isCustom() -> {
                            labelVariantContainer.addLabelVariantCustom(labelVariant, marginStart)
                        }
                    }
                }
        }

        view.findViewById<LinearLayout?>(R.id.labelColorVariantReposition).hide()
        view.findViewById<Typography?>(R.id.labelSizeVariantReposition).hide()
    }

    override val sizeCharLimit: Int
        get() = LABEL_VARIANT_CHAR_LIMIT

    override val extraCharSpace: Int
        get() = EXTRA_CHAR_SPACE

    override val colorLimit: Int
        get() = MAX_LABEL_VARIANT_COUNT

    override fun getLabelVariantSizeCount(
        productCardModel: ProductCardModel,
        colorVariantTaken: Int,
    ): Int {
        val hasLabelVariantColor = colorVariantTaken > 0

        return if (hasLabelVariantColor) 0 else MAX_LABEL_VARIANT_COUNT
    }

    override fun getLabelVariantColorCount(
        colorVariant: List<ProductCardModel.LabelGroupVariant>,
    ): Int {
        return if (colorVariant.size >= MIN_LABEL_VARIANT_COUNT)
            colorLimit
        else 0
    }

    private fun LinearLayout.addLabelVariantColor(
        labelVariant: ProductCardModel.LabelGroupVariant,
        hasMarginStart: Boolean,
        colorSampleSize: Int,
        marginStart: Int
    ) {
        val gradientDrawable = createColorSampleDrawable(context, labelVariant.hexColor)

        val layoutParams = LinearLayout.LayoutParams(colorSampleSize, colorSampleSize)
        layoutParams.marginStart = if (hasMarginStart) marginStart else 0

        val colorSampleImageView = ImageView(context)
        colorSampleImageView.setImageDrawable(gradientDrawable)
        colorSampleImageView.layoutParams = layoutParams
        colorSampleImageView.tag = LABEL_VARIANT_TAG

        addView(colorSampleImageView)
    }

    private fun LinearLayout.addLabelVariantSize(
        labelVariant: ProductCardModel.LabelGroupVariant,
        hasMarginStart: Boolean,
        marginStart: Int
    ) {
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.marginStart = if (hasMarginStart) marginStart else 0

        val unifyLabel = Label(context)
        unifyLabel.setLabelType(labelVariant.type.toUnifyLabelType())
        unifyLabel.text = labelVariant.title
        unifyLabel.layoutParams = layoutParams
        unifyLabel.tag = LABEL_VARIANT_TAG

        addView(unifyLabel)
    }

    private fun LinearLayout.addLabelVariantCustom(labelVariant: ProductCardModel.LabelGroupVariant, marginStart: Int) {
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.topMargin = 1.toPx() // Small hack to make custom label center
        layoutParams.marginStart = marginStart

        val typography = Typography(context)
        typography.weightType = Typography.BOLD
        typography.setType(Typography.SMALL)
        typography.text = "+${labelVariant.title}"
        typography.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
        typography.layoutParams = layoutParams
        typography.tag = LABEL_VARIANT_TAG

        addView(typography)
    }
}
