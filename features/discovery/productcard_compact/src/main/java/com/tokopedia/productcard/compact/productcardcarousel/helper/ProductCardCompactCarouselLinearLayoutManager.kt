package com.tokopedia.productcard.compact.productcardcarousel.helper

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductCardCompactCarouselLinearLayoutManager(context: Context): LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
    override fun requestChildRectangleOnScreen(
        parent: RecyclerView,
        child: View,
        rect: Rect,
        immediate: Boolean,
        focusedChildVisible: Boolean
    ): Boolean = false
}
