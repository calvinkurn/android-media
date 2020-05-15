package com.tokopedia.vouchercreation.common.utils.decorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.R

class VoucherDisplayItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val offset = context.resources.getDimension(R.dimen.mvc_create_voucher_display_recycler_view_decoration).toInt()
        when(parent.getChildAdapterPosition(view)) {
            0 -> outRect.left = offset
            state.itemCount - 1 -> outRect.right = offset
        }
    }

}