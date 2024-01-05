package com.tokopedia.tokopedianow.common.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import com.tokopedia.tokopedianow.R
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen

class ProductCardCarouselDecoration(
    private val context: Context
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val marginEndCorner = getDpFromDimen(
            context = context,
            com.tokopedia.productcard.compact.R.dimen.product_card_compact_product_card_margin_end_corner
        ).toInt()

        val marginEnd = getDpFromDimen(
            context = context,
            com.tokopedia.productcard.compact.R.dimen.product_card_compact_product_card_margin_end
        ).toInt()

        val currentPosition = parent.getChildAdapterPosition(view)
        val lastPosition = state.itemCount - 1

        if (currentPosition == lastPosition) {
            outRect.right = marginEndCorner
        } else {
            outRect.right = marginEnd
        }
    }
}
