package com.tokopedia.play.ui.productfeatured.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.R
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by jegul on 24/02/21
 */
class ProductFeaturedItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset16 = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl4)
    private val endOffset = offset16 + context.resources.getDimensionPixelSize(
        R.dimen.play_product_featured_width
    )
    private val inBetweenOffset = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl3)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount.orZero()

        outRect.left = if (position <= 0) offset16
        else inBetweenOffset

        if (position == itemCount - 1) {
            outRect.right = endOffset
        }
    }
}