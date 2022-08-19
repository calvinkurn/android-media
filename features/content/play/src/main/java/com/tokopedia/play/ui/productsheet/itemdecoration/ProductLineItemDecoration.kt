package com.tokopedia.play.ui.productsheet.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
import com.tokopedia.unifyprinciples.R as unifyR
/**
 * Created by jegul on 03/03/20
 */
class ProductLineItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val defaultOffset = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl4)
    private val topBottomOffset = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl2)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount.orZero()

        val adapter = parent.adapter as ProductSheetAdapter
        if (position > 0 &&
            adapter.getItem(position) is ProductSheetAdapter.Item.ProductWithSection &&
                adapter.getItem(position - 1) is ProductSheetAdapter.Item.Product) {
            outRect.top = defaultOffset
        } else if (position == 0) {
            outRect.top = topBottomOffset
        }

        if(position == itemCount - 1) outRect.bottom = topBottomOffset
    }
}