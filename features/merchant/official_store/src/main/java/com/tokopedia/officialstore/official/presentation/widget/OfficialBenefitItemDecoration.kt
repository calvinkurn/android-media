package com.tokopedia.officialstore.official.presentation.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt

/**
 * Created by dhaba
 */
class OfficialBenefitItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        when {
            parent.getChildAdapterPosition(view) == 0 -> {
                outRect.left = 16f.toDpInt()
                outRect.right = 4f.toDpInt()
            }
            parent.getChildAdapterPosition(view) == state.itemCount-1 -> {
                outRect.right = 16f.toDpInt()
                outRect.left = 4f.toDpInt()
            }
            else -> {
                outRect.left = 4f.toDpInt()
                outRect.right = 4f.toDpInt()
            }
        }
    }
}
