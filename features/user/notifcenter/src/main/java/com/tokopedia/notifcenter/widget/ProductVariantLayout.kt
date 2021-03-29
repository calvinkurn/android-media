package com.tokopedia.notifcenter.widget

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.Variant

class ProductVariantLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val productVariantContainer: LinearLayout
    private val productColorVariant: LinearLayout
    private val productColorVariantHex: ImageView
    private val productColorVariantValue: TextView
    private val productSizeVariant: LinearLayout
    private val productSizeVariantValue: TextView

    init {
        View.inflate(getContext(), R.layout.widget_product_variants, this)
        productVariantContainer = findViewById(R.id.ll_variant)
        productColorVariant = findViewById(R.id.ll_variant_color)
        productColorVariantHex = findViewById(R.id.iv_variant_color)
        productColorVariantValue = findViewById(R.id.tv_variant_color)
        productSizeVariant = findViewById(R.id.ll_variant_size)
        productSizeVariantValue = findViewById(R.id.tv_variant_size)
    }

    fun setupVariant(variants: List<Variant>) {
        val (colorVariant, sizeVariant) = getVariants(variants)
        if (variants.isEmpty()) {
            hide()
            return
        }

        productColorVariant.shouldShowWithAction(colorVariant != null) {
            val backgroundDrawable = getBackgroundDrawable(colorVariant?.hex)
            productColorVariantHex.background = backgroundDrawable
            productColorVariantValue.text = colorVariant?.value
        }

        productSizeVariant.shouldShowWithAction(sizeVariant != null) {
            productSizeVariantValue.text = sizeVariant?.value
        }
    }

    private fun getBackgroundDrawable(hexColor: String?): Drawable? {
        val backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.notifcenter_circle_color_variant_indicator)
        val color = Color.parseColor(hexColor)

        if (isWhiteColor(color)) {
            applyStrokeTo(backgroundDrawable)
            return backgroundDrawable
        }

        backgroundDrawable?.colorFilter = PorterDuffColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.SRC_ATOP)
        return backgroundDrawable
    }

    private fun isWhiteColor(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) > 0.90
    }

    private fun applyStrokeTo(backgroundDrawable: Drawable?) {
        if (backgroundDrawable is GradientDrawable) {
            val strokeWidth = 1f.toPx()
            backgroundDrawable.setStroke(strokeWidth.toInt(), ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N100))
        }
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