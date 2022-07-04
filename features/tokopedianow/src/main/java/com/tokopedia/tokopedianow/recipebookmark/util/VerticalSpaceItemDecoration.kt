package com.tokopedia.tokopedianow.recipebookmark.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.R

class VerticalSpaceItemDecoration(private val context: Context): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = context.resources.getDimension(R.dimen.tokopedianow_space_item_recipe_bookmark).toIntSafely()
    }
}