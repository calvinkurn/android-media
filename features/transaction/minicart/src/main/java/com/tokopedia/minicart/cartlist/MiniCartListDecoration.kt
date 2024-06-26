package com.tokopedia.minicart.cartlist

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.viewholder.MiniCartAccordionViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartProductViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartSeparatorViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartTickerErrorViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartTickerWarningViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartUnavailableHeaderViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartUnavailableReasonViewHolder
import javax.inject.Inject

class MiniCartListDecoration @Inject constructor() : RecyclerView.ItemDecoration() {

    private var noMargin: Int = 0
    private var defaultMargin: Int = 0
    private var mediumMargin: Int = 0
    private var smallMargin: Int = 0
    private var tinyMargin: Int = 0
    private var lastItemMargin: Int = 0
    private var startItemMargin: Int = 0
    private var context: Context? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (context == null) {
            context = parent.context
            noMargin = context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            defaultMargin = context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
            mediumMargin = context?.resources?.getDimension(R.dimen.dp_12)?.toInt() ?: 0
            smallMargin = context?.resources?.getDimension(R.dimen.dp_10)?.toInt() ?: 0
            tinyMargin = context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
            startItemMargin = context?.resources?.getDimension(R.dimen.dp_20)?.toInt() ?: 0
            lastItemMargin = context?.resources?.getDimension(R.dimen.dp_64)?.toInt() ?: 0
        }

        when (parent.getChildViewHolder(view)) {
            is MiniCartProductViewHolder -> setupOutRect(outRect, startItemMargin, defaultMargin, noMargin, noMargin)
            is MiniCartUnavailableHeaderViewHolder -> setupOutRect(outRect, defaultMargin, defaultMargin, defaultMargin, mediumMargin)
            is MiniCartUnavailableReasonViewHolder -> setupOutRect(outRect, defaultMargin, defaultMargin, noMargin, defaultMargin)
            is MiniCartSeparatorViewHolder -> setupOutRect(outRect, noMargin, noMargin, noMargin, noMargin)
            is MiniCartAccordionViewHolder -> setupOutRect(outRect, noMargin, noMargin, noMargin, noMargin)
            is MiniCartTickerErrorViewHolder -> setupOutRect(outRect, defaultMargin, defaultMargin, noMargin, tinyMargin)
            is MiniCartTickerWarningViewHolder -> setupOutRect(outRect, defaultMargin, defaultMargin, noMargin, tinyMargin)
            else -> setupOutRect(outRect, defaultMargin, defaultMargin, noMargin, defaultMargin)
        }
    }

    private fun setupOutRect(outRect: Rect, left: Int, right: Int, top: Int, bottom: Int) {
        outRect.left = left
        outRect.right = right
        outRect.top = top
        outRect.bottom = bottom
    }
}
