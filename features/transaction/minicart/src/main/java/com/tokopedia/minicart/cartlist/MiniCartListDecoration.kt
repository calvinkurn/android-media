package com.tokopedia.minicart.cartlist

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.viewholder.*
import javax.inject.Inject

class MiniCartListDecoration @Inject constructor() : RecyclerView.ItemDecoration() {

    private var noMargin: Int = 0
    private var defaultMargin: Int = 0
    private var mediumMargin: Int = 0
    private var smallMargin: Int = 0
    private var context: Context? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (context == null) {
            context = parent.context
            noMargin = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            defaultMargin = context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
            mediumMargin = context?.resources?.getDimension(R.dimen.dp_12)?.toInt() ?: 0
            smallMargin = context?.resources?.getDimension(R.dimen.dp_10)?.toInt() ?: 0
        }

        when (parent.getChildViewHolder(view)) {
            is MiniCartProductViewHolder -> setupOutRect(outRect, defaultMargin, defaultMargin, noMargin, mediumMargin)
            is MiniCartUnavailableHeaderViewHolder -> setupOutRect(outRect, defaultMargin, defaultMargin, defaultMargin, mediumMargin)
            is MiniCartUnavailableReasonViewHolder -> setupOutRect(outRect, defaultMargin, defaultMargin, noMargin, smallMargin)
            is MiniCartSeparatorViewHolder -> setupOutRect(outRect, noMargin, noMargin, noMargin, noMargin)
            is MiniCartAccordionViewHolder -> setupOutRect(outRect, noMargin, noMargin, noMargin, noMargin)
            else -> setupOutRect(outRect, defaultMargin, defaultMargin, noMargin, defaultMargin)
        }
    }

    private fun setupOutRect(outRect: Rect, left: Int, right: Int, top: Int, bottom: Int) {
        outRect.top = top
        outRect.bottom = bottom
        outRect.left = left
        outRect.right = right
    }
}
