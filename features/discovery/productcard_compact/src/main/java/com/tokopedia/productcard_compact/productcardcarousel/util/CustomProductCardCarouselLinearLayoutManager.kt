package com.tokopedia.productcard_compact.productcardcarousel.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomProductCardCarouselLinearLayoutManager(context: Context): LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
    override fun requestChildRectangleOnScreen(
        parent: RecyclerView,
        child: View,
        rect: Rect,
        immediate: Boolean,
        focusedChildVisible: Boolean
    ): Boolean = false
}
