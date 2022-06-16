package com.tokopedia.play.ui.productsheet.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.R

/**
 * Created by jegul on 03/03/20
 */
class ProductLineItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val defaultOffset = context.resources.getDimensionPixelOffset(R.dimen.play_product_section_divider)
    private val topBottomOffset = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount.orZero()

        outRect.top =  if (position == 0) topBottomOffset else defaultOffset
        if(position == itemCount - 1) outRect.bottom = topBottomOffset
    }
}