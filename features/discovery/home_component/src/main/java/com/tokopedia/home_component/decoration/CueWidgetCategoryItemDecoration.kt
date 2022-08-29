package com.tokopedia.home_component.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt

/**
 * Created by dhaba
 */
class CueWidgetCategoryItemDecoration :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = 4f.toDpInt()
        outRect.right = 4f.toDpInt()
        outRect.top = 6f.toDpInt()
        outRect.bottom = 6f.toDpInt()
    }
}