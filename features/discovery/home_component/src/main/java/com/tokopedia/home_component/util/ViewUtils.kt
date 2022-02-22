package com.tokopedia.home_component.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.*
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

/**
 * Created by Lukas on 2019-08-20
 */
object ChannelWidgetUtil {
    fun validateHomeComponentDivider(
        channelModel: ChannelModel?,
        dividerTop: DividerUnify?,
        dividerBottom: DividerUnify?
    ) {
//        when(channelModel?.channelConfig?.dividerType) {
        when(1) {
            ChannelConfig.DIVIDER_NO_DIVIDER -> {
                dividerTop?.invisible()
                dividerBottom?.gone()
            }
            ChannelConfig.DIVIDER_TOP -> {
                dividerTop?.visible()
                dividerBottom?.gone()
            }
            ChannelConfig.DIVIDER_BOTTOM -> {
                dividerTop?.invisible()
                dividerBottom?.visible()
            }
            ChannelConfig.DIVIDER_TOP_AND_BOTTOM -> {
                dividerTop?.visible()
                dividerBottom?.visible()
            }
        }
    }
}

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

//function check is gradient all white, if empty default color is white
fun getGradientBackgroundViewAllWhite(colorArray: ArrayList<String>, context: Context) : Boolean {
    val colorWhite = getHexColorFromIdColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
    if (colorArray.isNotEmpty()) {
        if (colorArray.size > 1) {
            val colorArrayNotWhite = colorArray.filter { it != colorWhite }
            if (colorArrayNotWhite.isNotEmpty())
                return false
            return true
        } else {
            return colorArray[0].equals(colorWhite, true)
        }
    } else
        return true
}

fun convertDpToPixel(dp: Float, context: Context): Int {
    val r = context.resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics).toInt()
}

fun Float.toSp(): Float = Resources.getSystem().displayMetrics.scaledDensity * this

fun Float.toDpInt(): Int = this.toPx().toInt()

fun Float.toDpFloat(): Float = this.toPx()

fun RecyclerView.removeAllItemDecoration() {
    if (this.itemDecorationCount > 0)
    for (i in 0 until this.itemDecorationCount) {
        this.removeItemDecorationAt(i)
    }
}