package com.tokopedia.shop.flashsale.common.extension

import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.domain.entity.Gradient

fun ImageView.setBackgroundFromGradient(gradient: Gradient) {
    val colors = intArrayOf(gradient.first.toColor(), gradient.second.toColor())
    val drawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
    drawable.cornerRadius = this.context?.resources?.getDimension(R.dimen.sfs_corner_radius).orZero()

    val borderWidth =  this.context?.resources?.getDimension(R.dimen.sfs_hex_color_border_stroke_width)?.toIntSafely().orZero()
    val borderColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN200)
    drawable.setStroke(borderWidth, borderColor)

    this.background = drawable
}