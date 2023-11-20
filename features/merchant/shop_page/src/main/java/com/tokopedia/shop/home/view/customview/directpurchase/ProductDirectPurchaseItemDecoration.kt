package com.tokopedia.shop.home.view.customview.directpurchase

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class ProductDirectPurchaseItemDecoration(
    context: Context
) : RecyclerView.ItemDecoration() {

    private val offset6 = 6.toPx()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)

        when (itemPosition) {
            0 -> {
                outRect.right = offset6
            }

            state.itemCount - 1 -> {
                outRect.left = offset6
            }
            else -> {
                outRect.left = offset6
                outRect.right = offset6
            }
        }
    }

}