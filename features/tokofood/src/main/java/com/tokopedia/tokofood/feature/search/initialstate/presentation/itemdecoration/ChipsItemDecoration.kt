package com.tokopedia.tokofood.feature.search.initialstate.presentation.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.unifycomponents.toPx

class ChipsItemDecoration: RecyclerView.ItemDecoration() {

    private val PADDING_ITEM = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = view.getDimens(PADDING_ITEM)
        outRect.top = view.getDimens(PADDING_ITEM)
    }
}