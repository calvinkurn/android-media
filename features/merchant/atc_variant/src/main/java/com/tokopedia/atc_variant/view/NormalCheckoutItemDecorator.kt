package com.tokopedia.atc_variant.view

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.atc_variant.R

/**
 * Created by Irfan Khoirul on 13/12/18.
 */

class NormalCheckoutItemDecorator : RecyclerView.ItemDecoration() {

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