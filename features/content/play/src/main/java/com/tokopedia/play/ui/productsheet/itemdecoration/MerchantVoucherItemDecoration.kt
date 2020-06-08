package com.tokopedia.play.ui.productsheet.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.R

/**
 * Created by jegul on 03/03/20
 */
class MerchantVoucherItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerWidth = context.resources.getDimensionPixelOffset(R.dimen.play_voucher_divider_width)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (position != 0) {
            outRect.left = dividerWidth
        } else super.getItemOffsets(outRect, view, parent, state)
    }
}