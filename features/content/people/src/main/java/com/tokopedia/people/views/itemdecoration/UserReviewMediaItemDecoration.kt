package com.tokopedia.people.views.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.people.R

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
class UserReviewMediaItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset8 = context.resources.getDimensionPixelOffset(R.dimen.user_profile_dp_8)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)

        if (position != parent.adapter?.itemCount.orZero() - 1) {
            outRect.right = offset8
        }
    }
}
