package com.tokopedia.tokopedianow.home.presentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.ViewUtil

class QuestCardItemDecoration : RecyclerView.ItemDecoration() {
    companion object {
        private const val START_POSITION = 0
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (position == START_POSITION) {
            outRect.left = ViewUtil.getDpFromDimen(
                context = view.context,
                id = R.dimen.tokopedianow_quest_card_horizontal_additional_padding
            ).toIntSafely()
        }

        if (position == itemCount.dec()) {
            outRect.right = ViewUtil.getDpFromDimen(
                context = view.context,
                id = R.dimen.tokopedianow_quest_card_horizontal_additional_padding
            ).toIntSafely()
        }
    }
}
