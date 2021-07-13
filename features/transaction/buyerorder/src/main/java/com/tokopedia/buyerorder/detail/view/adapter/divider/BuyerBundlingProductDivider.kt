package com.tokopedia.buyerorder.detail.view.adapter.divider

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorder.R

class BuyerBundlingProductDivider(context: Context): BuyerBundlingDivider() {

    override val divider: Drawable? = MethodChecker.getDrawable(context, R.drawable.bg_buyer_cancel_thick_divider)
    override val padding: Int = 0

    private val defaultMarginTop = context.resources.getDimension(com.tokopedia.unifycomponents.R.dimen.spacing_lvl5).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        outRect.top =
                if (position == 0) {
                    0
                } else {
                    defaultMarginTop
                }
    }

}