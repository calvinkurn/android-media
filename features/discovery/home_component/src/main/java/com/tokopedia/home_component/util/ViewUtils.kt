package com.tokopedia.home_component.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.*
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.gone
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
        when(channelModel?.channelConfig?.dividerType) {
            ChannelConfig.DIVIDER_NO_DIVIDER -> {
                dividerTop?.gone()
                dividerBottom?.gone()
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

fun convertDpToPixel(dp: Float, context: Context): Int {
    val r = context.resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics).toInt()
}

fun RecyclerView.removeAllItemDecoration() {
    if (this.itemDecorationCount > 0)
    for (i in 0 until this.itemDecorationCount) {
        this.removeItemDecorationAt(i)
    }
}