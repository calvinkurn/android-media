package com.tokopedia.shop.pageheader.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.R

class ShopPageActionButtonWidgetMarginItemDivider: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildLayoutPosition(view)
        if(position > 0 && position < parent.adapter?.itemCount.orZero()){
            val marginLeft = view.getDimens(R.dimen.header_button_margin_left)
            outRect.left = marginLeft
        }
    }
}
