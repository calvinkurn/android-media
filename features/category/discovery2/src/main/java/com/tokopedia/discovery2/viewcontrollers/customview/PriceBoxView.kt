package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.graphics.ColorUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.Constant.ProductHighlight.Type
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.databinding.PriceBoxBinding
import com.tokopedia.home_component.util.convertDpToPixel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.BOLD
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class PriceBoxView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private var binding = PriceBoxBinding.inflate(LayoutInflater.from(context), this)

    private var shapeDrawable: MaterialShapeDrawable? = null

    var fontType: Type = Type.SINGLE
        set(value) {
            field = value

            configureMainPrice()
            configureSlashPrice()
            configureRibbon()
            configureMargin()

            binding.phFreeText.textSize = 8f
        }

    private fun configureMargin() {
        val vertical = when (fontType) {
            Type.SINGLE -> R.dimen.dp_6
            Type.DOUBLE -> R.dimen.dp_4
            Type.TRIPLE -> R.dimen.dp_2
        }

        val horizontal = R.dimen.dp_8

        binding.phProductPrice.setMargin(context.resources.getDimensionPixelSize(horizontal), context.resources.getDimensionPixelSize(vertical), context.resources.getDimensionPixelSize(horizontal), context.resources.getDimensionPixelSize(vertical))
    }

    private fun configureRibbon() {
        val fontType = when (fontType) {
            Type.SINGLE -> Typography.DISPLAY_2
            Type.DOUBLE -> Typography.DISPLAY_3
            Type.TRIPLE -> Typography.SMALL
        }

        binding.discountRibbon.setType(fontType)
    }

    private fun configureMainPrice() {
        val type = when (fontType) {
            Type.SINGLE -> Typography.DISPLAY_2
            Type.DOUBLE -> Typography.DISPLAY_3
            Type.TRIPLE -> Typography.SMALL
        }

        binding.phProductPrice.setType(type)

        if (fontType == Type.TRIPLE) binding.phProductPrice.setWeight(BOLD)
    }

    private fun configureSlashPrice() {
        binding.phDiscountedProductPrice.apply {
            when (this@PriceBoxView.fontType) {
                Type.SINGLE -> setType(Typography.SMALL)
                Type.DOUBLE, Type.TRIPLE -> textSize = 8f
            }
        }
    }

    init {
        val shapePathModel = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, convertDpToPixel(CORNER_SIZE, context).toFloat())
            .build()

        shapeDrawable = MaterialShapeDrawable(shapePathModel)
        val defaultColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN0)
        shapeDrawable?.setTint(defaultColor)

        background = shapeDrawable

        binding.cardBackground.shapeAppearanceModel = shapePathModel
    }

    fun renderProductPrice(price: String?) {
        binding.phProductPrice.isVisible = !price.isNullOrEmpty()

        binding.phProductPrice.text = price
    }

    fun renderDiscountedPrice(slashPrice: String?) {
        binding.phDiscountedProductPrice.isVisible = !slashPrice.isNullOrEmpty()

        binding.phDiscountedProductPrice.text = slashPrice

        binding.phDiscountedProductPrice.run {
            paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    fun renderBenefit(benefit: String?) {
        binding.freeTextContainer.isVisible = !benefit.isNullOrEmpty()

        if (binding.freeTextContainer.isVisible) {
            binding.priceContainer.setMargin(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
        } else {
            val bottom = when (fontType) {
                Type.SINGLE -> R.dimen.dp_6
                Type.DOUBLE -> R.dimen.dp_4
                Type.TRIPLE -> R.dimen.dp_2
            }
            binding.priceContainer.setPadding(Int.ZERO, Int.ZERO, Int.ZERO, context.resources.getDimensionPixelSize(bottom))
        }

        binding.phFreeText.text = benefit
    }

    fun renderDiscountPercentage(
        percentage: String?,
        fontColor: String?,
        backgroundColor: String?
    ) {
        binding.discountRibbon.isVisible = !percentage.isNullOrEmpty()

        binding.discountRibbon.apply {
            setText(percentage)
            setFontColor(fontColor)
            setBackgroundColor(backgroundColor)
        }
    }

    fun setFontColor(color: String?) {
        if (color.isNullOrEmpty()) return

        with(binding) {
            phProductPrice.setTextColor(Color.parseColor(color))
            phDiscountedProductPrice.setTextColor(Color.parseColor(color))
            phDiscountedProductPrice.alpha = SLASH_PRICE_ALPHA
            phFreeText.setTextColor(Color.parseColor(color))
        }
    }

    fun setBackgroundColor(color: String?, url: String?) {
        if (color.isNullOrEmpty() || url.isNullOrEmpty()) return

        binding.priceContainer.addOneTimeGlobalLayoutListener {
            binding.cardBackground.apply {
                maxHeight = binding.priceContainer.height
                minimumHeight = binding.priceContainer.height

                layoutParams.width = binding.priceContainer.width / 2
                requestLayout()
            }
        }

        val parseColor = Color.parseColor(color)

        binding.priceContainer.setBackgroundColor(ColorUtils.setAlphaComponent(parseColor, PRICE_BG_ALPHA))

        binding.cardBackground.loadImage(url)

        binding.freeTextContainer.setBackgroundColor(ColorUtils.setAlphaComponent(parseColor, FREE_TEXT_BG_ALPHA))
    }

    fun changeToInactive() {
        val textColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN400)

        with(binding) {
            priceContainer.setBackgroundColor(
                MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_NN100
                )
            )
            phProductPrice.setTextColor(textColor)
            phDiscountedProductPrice.setTextColor(textColor)
            phDiscountedProductPrice.alpha = SLASH_PRICE_ALPHA
            phFreeText.setTextColor(textColor)

            val inactiveBackgroundColor = MethodChecker.getColor(
                context,
                unifyprinciplesR.color.Unify_NN200
            )

            discountRibbon.changeToDisable(textColor, inactiveBackgroundColor)

            binding.freeTextContainer.setBackgroundColor(
                ColorUtils.setAlphaComponent(
                    inactiveBackgroundColor,
                    FREE_TEXT_IN_ACTIVE_BG_ALPHA
                )
            )
        }
    }

    companion object {
        private const val CORNER_SIZE = 12f
        private const val SLASH_PRICE_ALPHA = 0.6f
        private const val FREE_TEXT_BG_ALPHA = 45
        private const val FREE_TEXT_IN_ACTIVE_BG_ALPHA = 180
        private const val PRICE_BG_ALPHA = 20
    }
}
