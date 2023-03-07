package com.tokopedia.people.views.itemdecoration
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PostItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position % 2 == 0) {
            outRect.right = space
        }
        outRect.bottom = space
    }
}
