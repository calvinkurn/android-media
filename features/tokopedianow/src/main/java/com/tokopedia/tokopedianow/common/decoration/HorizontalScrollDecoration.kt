package com.tokopedia.tokopedianow.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.listener.HorizontalItemTouchListener

class HorizontalScrollDecoration : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        view.findViewById<RecyclerView?>(R.id.recycler_view)
            ?.addOnItemTouchListener(HorizontalItemTouchListener())
    }
}
