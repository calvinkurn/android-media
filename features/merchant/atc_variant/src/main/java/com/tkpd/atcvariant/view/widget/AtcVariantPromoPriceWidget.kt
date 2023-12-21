package com.tkpd.atcvariant.view.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.databinding.AtcVariantPromoPriceBinding
import com.tkpd.atcvariant.util.setBorderWithColor
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.data.model.promoprice.PromoPriceUiModel
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class AtcVariantPromoPriceWidget : ConstraintLayout {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        init(context)
    }

    private var _binding: AtcVariantPromoPriceBinding? = null
    private fun init(context: Context) {
        _binding = AtcVariantPromoPriceBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.atc_variant_promo_price, this)
        )
    }

    fun renderView(
        promoPriceData: PromoPriceUiModel, originalPriceFmt: String
    ) {
        _binding?.atcVariantPromoContainerTextIcon?.showIfWithBlock(
            promoPriceData.promoPriceFmt.isNotEmpty()
        ) {
            _binding?.atcVariantPromoContainerTextIcon?.setBorderWithColor(
                borderWidth = 2,
                borderRadius = 8,
                borderColor = promoPriceData.separatorColor,
                backgroundColor = promoPriceData.cardBackgroundColor
            )

            _binding?.atcVariantPromoIconLeft?.loadImage(promoPriceData.mainIconUrl)
        }

        _binding?.atcVariantPromoTxtRight?.run {
            text = promoPriceData.promoPriceFmt
            setTextColor(promoPriceData.mainTextColor.safelyGetColor(context))
        }

        _binding?.atcVariantPromoPriceOriginalPrice?.text = originalPriceFmt
        _binding?.atcVariantPromoPriceSlashPrice?.run {
            text = promoPriceData.slashPriceFmt
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun String.safelyGetColor(context: Context): Int {
        return if (this.isEmpty()) {
            Color.parseColor(this)
        } else {
            MethodChecker.getColor(context, unifyprinciplesR.color.Unify_Static_Black)
        }
    }
}