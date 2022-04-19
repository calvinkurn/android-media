package com.tokopedia.selleronboarding.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.annotation.DrawableRes

/**
 * Created By @ilhamsuaib on 25/08/21
 */

internal const val PADDING_0 = 0
internal const val PADDING_HALF_DIVIDER = 2

internal fun ImageView.adjustImageGravity(
    @DrawableRes resDrawable: Int,
    gravity: OnboardingConst.Gravity
) {
    val bitmap = BitmapFactory.decodeResource(this.context.resources, resDrawable)
    adjustImageGravity(bitmap, gravity)
}

internal fun ImageView.adjustImageGravity(
    bitmap: Bitmap,
    gravity: OnboardingConst.Gravity
) {
    val imageViewHeight = this.height
    val bitmapHeight = bitmap.height
    if (imageViewHeight > bitmapHeight) {
        val space = imageViewHeight - bitmapHeight
        when (gravity) {
            OnboardingConst.Gravity.START_BOTTOM -> {
                this.setPadding(PADDING_0, space, this.paddingEnd, PADDING_0)
            }
            OnboardingConst.Gravity.END_TOP -> {
                this.setPadding(this.paddingStart, PADDING_0, PADDING_0, space)
            }
            OnboardingConst.Gravity.END_CENTER_VERTICAL -> {
                this.setPadding(
                    this.paddingStart,
                    space.div(PADDING_HALF_DIVIDER),
                    PADDING_0,
                    space.div(PADDING_HALF_DIVIDER)
                )
            }
            OnboardingConst.Gravity.START_CENTER_VERTICAL -> {
                this.setPadding(
                    PADDING_0,
                    space.div(PADDING_HALF_DIVIDER),
                    this.paddingEnd,
                    space.div(PADDING_HALF_DIVIDER)
                )
            }
        }
    }
}