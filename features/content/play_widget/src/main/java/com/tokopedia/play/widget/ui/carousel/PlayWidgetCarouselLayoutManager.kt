package com.tokopedia.play.widget.ui.carousel

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetCarouselLayoutManager(
    context: Context,
) : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
        if (lp != null) {
            lp.width = (0.65 * width).toInt()
            lp.height = (1 / 0.5625 * lp.width).toInt()
        }
        return super.checkLayoutParams(lp)
    }
}
