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
import com.tokopedia.discovery2.databinding.PriceBoxBinding
import com.tokopedia.home_component.util.convertDpToPixel
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.BOLD
import com.tokopedia.utils.image.ImageUtils
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

            binding.phFreeText.textSize = 8f
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
            .setAllCorners(CornerFamily.ROUNDED, convertDpToPixel(12f, context).toFloat())
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

        binding.phFreeText.text = benefit
    }

    fun renderDiscountPercentage(
        percentage: String?,
        fontColor: String?,
        backgroundColor: String?
    ) {
        binding.discountRibbon.isVisible = !percentage.isNullOrEmpty()

        binding.discountRibbon.apply {
            setText("$percentage%")
            setFontColor(fontColor)
            setBackgroundColor(backgroundColor)
        }
    }

    fun setFontColor(color: String?) {
        if (color.isNullOrEmpty()) return

        with(binding) {
            phProductPrice.setTextColor(Color.parseColor(color))
            phDiscountedProductPrice.setTextColor(Color.parseColor(color))
            phDiscountedProductPrice.alpha = 0.6f
            phFreeText.setTextColor(Color.parseColor(color))
        }
    }

    fun setBackgroundColor(color: String?, url: String?) {
        if (color.isNullOrEmpty() || url.isNullOrEmpty()) return

        val parseColor = Color.parseColor(color)

        binding.root.setBackgroundColor(ColorUtils.setAlphaComponent(parseColor, 20))

        ImageUtils.loadImage(binding.cardBackground, url)
    }

    fun changeToInactive() {
        val textColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN400)

        with(binding) {
            root.setBackgroundColor(
                MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_NN100
                )
            )
            phProductPrice.setTextColor(textColor)
            phDiscountedProductPrice.setTextColor(textColor)
            phDiscountedProductPrice.alpha = 0.6f
            phFreeText.setTextColor(textColor)

            discountRibbon.changeToDisable(
                textColor,
                MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN200)
            )
        }
    }

    enum class Type {
        SINGLE,
        DOUBLE,
        TRIPLE
    }
}
