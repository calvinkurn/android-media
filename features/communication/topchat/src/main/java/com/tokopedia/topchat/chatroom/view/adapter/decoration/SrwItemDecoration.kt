package com.tokopedia.topchat.chatroom.view.adapter.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topchat.R

class SrwItemDecoration(context: Context?) : RecyclerView.ItemDecoration() {

    private val divider: Drawable = MethodChecker.getDrawable(
            context, R.drawable.bg_topchat_line_separator_srw
    )

    var source: SrwItemSource = SrwItemSource.SRW_BUBBLE

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        val startIndex = if (source == SrwItemSource.SRW_BUBBLE) {
            FIRST_INDEX
        } else{
            SECOND_INDEX
        }
        for (i in startIndex until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top: Int = child.top - params.topMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    enum class SrwItemSource {
        TAB_LAYOUT,
        SRW_BUBBLE
    }

    companion object {
        private const val FIRST_INDEX = 0
        private const val SECOND_INDEX = 1
    }
}