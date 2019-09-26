package com.tokopedia.home.util

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat

/**
 * Created by Lukas on 26/09/19
 */

object CustomGradientDrawable{
    fun generate(context: Context, color: Int): GradientDrawable{
        return GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(ContextCompat.getColor(context, android.R.color.white), color)).apply {
            cornerRadius = 0f
        }
    }
}