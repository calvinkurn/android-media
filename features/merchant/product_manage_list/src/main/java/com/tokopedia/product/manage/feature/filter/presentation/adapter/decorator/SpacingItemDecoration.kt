package com.tokopedia.product.manage.feature.filter.presentation.adapter.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration(private val dimen: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = this.dimen
        outRect.top = this.dimen / 2
        outRect.bottom = this.dimen / 2
    }
}
