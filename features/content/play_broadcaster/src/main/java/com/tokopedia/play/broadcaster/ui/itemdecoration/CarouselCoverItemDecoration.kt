package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.R

/**
 * Created by jegul on 24/06/20
 */
class CarouselCoverItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset12 = context.resources.getDimensionPixelOffset(R.dimen.play_dp_12)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = offset12
    }
}