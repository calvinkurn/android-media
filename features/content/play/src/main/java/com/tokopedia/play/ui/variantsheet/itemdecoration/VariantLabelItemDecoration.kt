package com.tokopedia.play.ui.variantsheet.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by kenny.hadisaputra on 16/03/22
 */
class VariantLabelItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset2 = context.resources.getDimensionPixelOffset(
        unifyR.dimen.spacing_lvl1
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = offset2
    }
}