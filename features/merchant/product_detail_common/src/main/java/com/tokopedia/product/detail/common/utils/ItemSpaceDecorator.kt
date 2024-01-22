package com.tokopedia.product.detail.common.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created by yovi.putra on 09/11/23"
 * Project name: android-tokopedia-core
 **/

/**
 *  [ItemSpaceDecorator] is a class to added space between items in the recycler view
 *  @param space is integer type in PX
 */
class ItemSpaceDecorator(val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val marginEnd = if (parent.getChildAdapterPosition(view) < state.itemCount - Int.ONE) {
            space
        } else {
            Int.ZERO
        }
        outRect.set(Int.ZERO, Int.ZERO, marginEnd, Int.ZERO)
    }
}
