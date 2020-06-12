package com.tokopedia.topchat.chatsearch.view.adapter.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.R
import com.tokopedia.unifycomponents.toPx

class SearchResultsSeparatorItemDecoration(context: Context?) : RecyclerView.ItemDecoration() {

    private var divider: Drawable? = null

    init {
        context?.let {
            divider = ContextCompat.getDrawable(context, R.drawable.bg_chat_search_divider)
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = 0
        val right = parent.width
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            if (i == 5) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin + 8.toPx()
                val bottom = top + 8.toPx()
                divider?.setBounds(left, top, right, bottom)
                divider?.draw(c)
            }
        }
    }
}