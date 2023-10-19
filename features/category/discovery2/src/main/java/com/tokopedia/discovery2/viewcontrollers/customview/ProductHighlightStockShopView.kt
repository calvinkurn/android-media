package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.discovery2.Constant.ProductHighlight.Type
import com.tokopedia.discovery2.databinding.ProductHighlightStockShopBinding
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifyprinciples.Typography

class ProductHighlightStockShopView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: ProductHighlightStockShopBinding? = null

    var fontType: Type = Type.SINGLE
        set(value) {
            field = value

            configureShopName(field)
        }

    init {
        binding = ProductHighlightStockShopBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun renderShopInfo(shopBadge: String?, name: String?) {

        binding?.run {
            shopLogo.isVisible = !shopBadge.isNullOrEmpty()
            shopName.isVisible = !name.isNullOrEmpty()

            shopBadge?.let { shopLogo.setImageUrl(shopBadge) }
            shopName.text = name
        }
    }

    fun renderStockBar(value: Double?, label: String?) {
        binding?.progressBarStock?.isVisible = value != null

        binding?.progressBarStock?.apply {
            setValue(value)
            setLabel(label)
        }
    }

    private fun configureShopName(fontType: Type) {
        binding?.shopName?.apply {
            when (fontType) {
                Type.SINGLE, Type.DOUBLE -> setType(Typography.SMALL)
                Type.TRIPLE -> {
                    textSize = 8f
                    setWeight(Typography.BOLD)
                }
            }
        }
    }
}
