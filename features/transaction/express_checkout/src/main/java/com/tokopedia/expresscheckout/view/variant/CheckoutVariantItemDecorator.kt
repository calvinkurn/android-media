package com.tokopedia.expresscheckout.view.variant

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.expresscheckout.R

/**
 * Created by Irfan Khoirul on 13/12/18.
 */

class CheckoutVariantItemDecorator : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val dimen: Int? = view?.context?.resources?.getDimension(R.dimen.dp_8)?.toInt()
        val position = parent?.getChildAdapterPosition(view)
        when (position) {
            (parent.adapter?.itemCount)?.minus(1) -> {
                dimen?.times(2)?.let { outRect.bottom  = it}
                dimen?.let { outRect.top = it }
            }
            0 -> {
                dimen?.let { outRect.bottom = it }
                dimen?.times(2)?.let { outRect.top = it }
            }
            else -> {
                dimen?.let { outRect.bottom = it
                    outRect.top = it
                }
            }
        }
    }

}