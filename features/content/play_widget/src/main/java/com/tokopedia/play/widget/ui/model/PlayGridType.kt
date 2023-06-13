package com.tokopedia.play.widget.ui.model

import android.content.Context
import android.view.ViewGroup
import com.tokopedia.play.widget.R

/**
 * @author by astidhiyaa on 14/12/22
 */
enum class PlayGridType {
    Unknown,
    Medium,
    Large,
}

fun PlayGridType.getWidthAndHeight(ctx: Context): Pair<Int, Int> {
    return when (this) {
        PlayGridType.Medium -> Pair(
            ctx.resources.getDimensionPixelOffset(R.dimen.play_widget_card_medium_width),
            ctx.resources.getDimensionPixelOffset(R.dimen.play_widget_card_medium_height)
        )
        else -> Pair(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ctx.resources.getDimensionPixelOffset(R.dimen.play_widget_card_large_height)
        )
    }
}
