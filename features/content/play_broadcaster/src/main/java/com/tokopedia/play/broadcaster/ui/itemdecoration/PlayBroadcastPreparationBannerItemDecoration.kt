package com.tokopedia.play.broadcaster.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.R

class PlayBroadcastPreparationBannerItemDecoration(
    context: Context
) : RecyclerView.ItemDecoration() {

    private val spaceItem = context.resources.getDimensionPixelOffset(R.dimen.spacing_lvl3)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = spaceItem
        outRect.right = spaceItem
    }

}
