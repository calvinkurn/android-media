package com.tokopedia.analyticsdebugger.serverlogger.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isMoreThanZero

class ServerLoggerItemDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position.isMoreThanZero()) {
            parent.adapter.also {
                outRect.right = view.resources.getDimensionPixelSize(com.tokopedia.unifycomponents.R.dimen.spacing_lvl3)
            }
        }
    }
}