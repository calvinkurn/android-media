package com.tokopedia.expresscheckout.view.variant

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.expresscheckout.R

/**
 * Created by Irfan Khoirul on 13/12/18.
 */

class CheckoutVariantItemDecorator : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        val dimen: Int? = view?.context?.resources?.getDimension(R.dimen.dp_8)?.toInt()
        val position = parent?.getChildAdapterPosition(view)
        when (position) {
            (parent?.adapter?.itemCount)?.minus(1) -> {
                outRect?.bottom = dimen?.times(2)
                outRect?.top = dimen
            }
            0 -> {
                outRect?.bottom = dimen
                outRect?.top = dimen?.times(2)
            }
            else -> {
                outRect?.bottom = dimen
                outRect?.top = dimen
            }
        }
    }

}