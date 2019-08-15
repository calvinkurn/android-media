package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by fwidjaja on 2019-05-29.
 */
class ChipsItemDecoration(private val staticDimen: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = staticDimen
        outRect.top = staticDimen / 2
        outRect.bottom = staticDimen / 2
    }
}