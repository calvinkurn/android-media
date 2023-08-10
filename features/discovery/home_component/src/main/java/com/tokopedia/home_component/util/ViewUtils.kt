package com.tokopedia.home_component.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.PaintDrawable
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.toPx

/**
 * Created by Lukas on 2019-08-20
 */
object ChannelWidgetUtil {
    private const val DEFAULT_DIVIDER_HEIGHT = 1
    private const val BOTTOM_PADDING_WITHOUT_DIVIDER = 8

    fun validateHomeComponentDivider(
        channelModel: ChannelModel?,
        dividerTop: DividerUnify?,
        dividerBottom: DividerUnify?,
        useBottomPadding: Boolean = false
    ) {
        val dividerSize = channelModel?.channelConfig?.dividerSize?.toPx()
            ?: DEFAULT_DIVIDER_HEIGHT.toPx()
        dividerTop?.layoutParams?.height = dividerSize
        dividerBottom?.layoutParams?.height = dividerSize
        when (channelModel?.channelConfig?.dividerType) {
            ChannelConfig.DIVIDER_NO_DIVIDER -> {
                dividerTop?.gone()
                if (useBottomPadding) dividerBottom?.setAsPadding(BOTTOM_PADDING_WITHOUT_DIVIDER) else dividerBottom?.gone()
            }
            ChannelConfig.DIVIDER_TOP -> {
                dividerTop?.visible()
                dividerBottom?.gone()
            }
            ChannelConfig.DIVIDER_BOTTOM -> {
                dividerTop?.gone()
                dividerBottom?.visible()
            }
            ChannelConfig.DIVIDER_TOP_AND_BOTTOM -> {
                dividerTop?.visible()
                dividerBottom?.visible()
            }
        }
    }

    private fun DividerUnify.setAsPadding(height: Int) {
        this.layoutParams?.height = height.toPx()
        this.invisible()
    }
}

const val DEFAULT_ROUNDED_CORNER = 12f

fun View.setGradientBackground(colorArray: ArrayList<String>) {
    try {
        if (colorArray.size > 1) {
            val colors = IntArray(colorArray.size)
            for (i in 0 until colorArray.size) {
                colors[i] = Color.parseColor(colorArray[i])
            }
            val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
            gradient.cornerRadius = 0f
            this.background = gradient
        } else {
            this.setBackgroundColor(Color.parseColor(colorArray[0]))
        }
    } catch (e: Exception) {
    }
}

fun View.setGradientBackgroundRounded(colorArray: ArrayList<String>, cornerRadius: Float = DEFAULT_ROUNDED_CORNER) {
    try {
        val drawable: Drawable
        if (colorArray.size > 1) {
            val colors = IntArray(colorArray.size)
            for (i in 0 until colorArray.size) {
                colors[i] = Color.parseColor(colorArray[i])
            }
            drawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
            drawable.cornerRadius = cornerRadius.toDpFloat()
        } else {
            drawable = PaintDrawable(Color.parseColor(colorArray[0]))
            drawable.setCornerRadius(cornerRadius.toDpFloat())
        }
        this.background = drawable
    } catch (_: Exception) { }
}

// function check is gradient all white, if empty default color is white
fun getGradientBackgroundViewAllWhite(colorArray: ArrayList<String>, context: Context): Boolean {
    val colorWhite = getHexColorFromIdColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
    if (colorArray.isNotEmpty()) {
        if (colorArray.size > 1) {
            val colorArrayNotWhite = colorArray.filter { it.uppercase().take(7) != colorWhite }
            if (colorArrayNotWhite.isNotEmpty()) {
                return false
            }
            return true
        } else {
            return colorArray[0].equals(colorWhite, true)
        }
    } else {
        return true
    }
}

fun convertDpToPixel(dp: Float, context: Context): Int {
    val r = context.resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics).toInt()
}

fun Float.toDpInt(): Int = this.toPx().toInt()

fun Float.toDpFloat(): Float = this.toPx()

fun RecyclerView.removeAllItemDecoration() {
    if (this.itemDecorationCount > 0) {
        for (i in 0 until this.itemDecorationCount) {
            this.removeItemDecorationAt(i)
        }
    }
}
