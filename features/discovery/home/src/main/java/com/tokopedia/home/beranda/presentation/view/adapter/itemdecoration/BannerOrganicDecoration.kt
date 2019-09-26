package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.content.Context
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.home.R


class BannerOrganicDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        if (parent.layoutManager?.getPosition(view) == 0) {
            outRect.left = view.context.resources.getDimensionPixelSize(R.dimen.dp_16)
        }

        if (parent.layoutManager?.getPosition(view) == parent.layoutManager?.childCount) {
            outRect.right = view.context.resources.getDimensionPixelSize(R.dimen.dp_16)
        }
    }
}
