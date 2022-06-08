package com.tokopedia.shop.flashsale.common.extension

import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.domain.entity.Gradient


fun ImageView.setBackgroundFromGradient(gradient: Gradient) {

    val colors = intArrayOf(gradient.first.toColor(), gradient.second.toColor())
    val drawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
    drawable.cornerRadius =
        this.context?.resources?.getDimension(R.dimen.sfs_corner_radius).orZero()
    this.background = drawable

}