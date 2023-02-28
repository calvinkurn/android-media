package com.tokopedia.tokofood.feature.search.initialstate.presentation.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getDimens

class ChipsItemDecoration: RecyclerView.ItemDecoration() {

    private val paddingItemResource = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = view.getDimens(paddingItemResource)
        outRect.top = view.getDimens(paddingItemResource)
    }
}
