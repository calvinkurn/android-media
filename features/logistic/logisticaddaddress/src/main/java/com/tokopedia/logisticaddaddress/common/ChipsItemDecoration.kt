package com.tokopedia.logisticaddaddress.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

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
