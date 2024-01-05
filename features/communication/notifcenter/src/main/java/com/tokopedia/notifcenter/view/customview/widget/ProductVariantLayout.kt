package com.tokopedia.notifcenter.view.customview.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.Variant

class ProductVariantLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val productVariantContainer: LinearLayout
    private val productColorVariant: LinearLayout
    private val productColorVariantValue: com.tokopedia.unifyprinciples.Typography
    private val productSizeVariant: LinearLayout
    private val productSizeVariantValue: com.tokopedia.unifyprinciples.Typography

    init {
        View.inflate(getContext(), R.layout.widget_product_variants, this)
        productVariantContainer = findViewById(R.id.ll_variant)
        productColorVariant = findViewById(R.id.ll_variant_color)
        productColorVariantValue = findViewById(R.id.tv_variant_color)
        productSizeVariant = findViewById(R.id.ll_variant_size)
        productSizeVariantValue = findViewById(R.id.tv_variant_size)
    }

    fun setupVariant(variants: List<Variant>): Boolean {
        val (colorVariant, sizeVariant) = getVariants(variants)
        if (variants.isEmpty()) {
            hide()
            return false
        }

        productColorVariant.shouldShowWithAction(colorVariant != null) {
            productColorVariantValue.text = colorVariant?.value
        }

        productSizeVariant.shouldShowWithAction(sizeVariant != null) {
            productSizeVariantValue.text = sizeVariant?.value
        }
        return true
    }

    private fun getVariants(variants: List<Variant>): List<Variant?> {
        var colorVariant: Variant? = null
        var sizeVariant: Variant? = null
        for (variant in variants) {
            when {
                variant.hex.isNotEmpty() -> colorVariant = variant
                variant.hex.isEmpty() -> sizeVariant = variant
            }
        }
        return listOf(colorVariant, sizeVariant)
    }
}
