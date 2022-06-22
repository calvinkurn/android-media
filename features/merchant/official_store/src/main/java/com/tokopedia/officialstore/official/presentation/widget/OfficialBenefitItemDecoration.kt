package com.tokopedia.officialstore.official.presentation.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt

/**
 * Created by dhaba
 */
class OfficialBenefitItemDecoration : RecyclerView.ItemDecoration() {
    companion object {
        private var MARGIN_OUTSIDE_CARD = 16f.toDpInt()
        private var MARGIN_INSIDE_CARD = 4f.toDpInt()
        private const val FIRST_POSITION_CARD = 0
    }

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        when (parent.getChildAdapterPosition(view)) {
            FIRST_POSITION_CARD -> {
                outRect.left = MARGIN_OUTSIDE_CARD
                outRect.right = MARGIN_INSIDE_CARD
            }
            state.itemCount - 1 -> {
                outRect.right = MARGIN_OUTSIDE_CARD
                outRect.left = MARGIN_INSIDE_CARD
            }
            else -> {
                outRect.left = MARGIN_INSIDE_CARD
                outRect.right = MARGIN_INSIDE_CARD
            }
        }
    }
}
