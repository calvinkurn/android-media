package com.tokopedia.shop.flashsale.presentation.creation.rule.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RelatedCampaignItemDecoration(
    private val rightOffset: Int,
    private val topOffset: Int,
): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = rightOffset
        outRect.top = topOffset
    }

}