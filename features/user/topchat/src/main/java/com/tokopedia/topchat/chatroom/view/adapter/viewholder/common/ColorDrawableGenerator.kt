package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.unifycomponents.toPx

object ColorDrawableGenerator {

    fun generate(context: Context, hexColor: String): Drawable? {
        val intColor = Color.parseColor(hexColor)
        val backgroundDrawable = ContextCompat.getDrawable(
                context,
                com.tokopedia.chat_common.R.drawable.topchat_circle_color_variant_indicator
        )?.mutate()
        if (backgroundDrawable is GradientDrawable) {
            backgroundDrawable.setColor(intColor)
        }
        if (isWhiteColor(intColor)) {
            applyStrokeTo(context, backgroundDrawable)
        }
        return backgroundDrawable
    }

    private fun isWhiteColor(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) > 0.90
    }

    private fun applyStrokeTo(context: Context, backgroundDrawable: Drawable?) {
        if (backgroundDrawable is GradientDrawable) {
            val strokeWidth = 1.toPx()
            backgroundDrawable.setStroke(
                    strokeWidth,
                    MethodChecker.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N100
                    )
            )
        }
    }

}