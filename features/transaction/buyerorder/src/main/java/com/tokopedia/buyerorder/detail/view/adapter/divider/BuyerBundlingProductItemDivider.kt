package com.tokopedia.buyerorder.detail.view.adapter.divider

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker

class BuyerBundlingProductItemDivider(context: Context): BuyerBundlingDivider() {

    override val divider: Drawable? = MethodChecker.getDrawable(context, com.tokopedia.abstraction.R.drawable.bg_line_separator_thin)
    override val padding = context.resources.getDimension(com.tokopedia.unifycomponents.R.dimen.layout_lvl2).toInt()

    private val defaultMarginTop = context.resources.getDimension(com.tokopedia.unifycomponents.R.dimen.spacing_lvl4).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = defaultMarginTop
    }

}