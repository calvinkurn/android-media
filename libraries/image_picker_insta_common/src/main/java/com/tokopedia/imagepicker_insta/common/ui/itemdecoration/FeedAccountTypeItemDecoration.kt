package com.tokopedia.imagepicker_insta.common.ui.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.common.R

/**
 * Created By : Jonathan Darwin on April 13, 2022
 */
class FeedAccountTypeItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset12 = context.resources.getDimensionPixelOffset(R.dimen.dp_12)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = offset12
        outRect.top = offset12
    }
}