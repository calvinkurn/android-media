package com.tokopedia.content.product.picker.seller.view.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
class ProductListItemDecoration(
    context: Context,
) : RecyclerView.ItemDecoration() {

    private val offset4 = context.resources.getDimensionPixelOffset(unifyprinciplesR.dimen.spacing_lvl2)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = offset4
        outRect.bottom = offset4
    }
}
