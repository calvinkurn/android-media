package com.tokopedia.shop.pageheader.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.shop.home.WidgetName.DISPLAY_DOUBLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_SINGLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_TRIPLE_COLUMN
import com.tokopedia.shop.home.WidgetName.SLIDER_SQUARE_BANNER
import com.tokopedia.unifycomponents.toPx


/**
 * Created by rizqiaryansa on 2020-02-24.
 */

class MarginItemDecorationShopPage: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = 4.toPx()
    }
}
