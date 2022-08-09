package com.tokopedia.content.common.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on April 13, 2022
 */
class FeedAccountTypeItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset12 = context.resources.getDimensionPixelOffset(unifyR.dimen.unify_space_12)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = offset12
        outRect.top = offset12
    }
}