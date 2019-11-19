package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.content.Context
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import com.tokopedia.home.R


class BannerOrganicDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = view.context.resources.getDimensionPixelSize(R.dimen.dp_12)
        }

        if (parent.getChildAdapterPosition(view) == state.itemCount-1) {
            outRect.right = view.context.resources.getDimensionPixelSize(R.dimen.dp_12)
        }
    }
}
