package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.databinding.PriceBoxBinding
import com.tokopedia.home_component.util.convertDpToPixel
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class PriceBoxView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private var binding = PriceBoxBinding.inflate(LayoutInflater.from(context), this)

    init {
        val shapePathModel = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, convertDpToPixel(12f, context).toFloat())
            .build()

        val materialShapeDrawable = MaterialShapeDrawable(shapePathModel)
        val defaultColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN0)
        materialShapeDrawable.setTint(defaultColor)

        background = materialShapeDrawable

        binding.phDiscountedProductPrice.run {
            paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        // TODO: Should not use hardcoded background url
        ImageUtils.loadImage(
            binding.cardBackground,
            "https://images.tokopedia.net/img/discopage/merchant-voucher-grid-infinite/supergraphic-cashback.png"
        )
    }
}
