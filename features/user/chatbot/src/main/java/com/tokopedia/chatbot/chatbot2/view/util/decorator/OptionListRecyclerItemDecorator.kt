package com.tokopedia.chatbot.chatbot2.view.util.decorator

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R

class OptionListRecyclerItemDecorator : RecyclerView.ItemDecoration {

    private var mDivider: Drawable? = null

    constructor(context: Context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.option_item_separator)
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mDivider != null) {
            val dividerLeft = parent.paddingLeft
            val dividerRight = parent.width - parent.paddingRight
            val childCount = parent.childCount
            for (i in 0..childCount - 1) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val dividerTop: Int = child.bottom + params.bottomMargin
                val dividerBottom = dividerTop + mDivider!!.intrinsicHeight
                mDivider!!.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                mDivider!!.draw(canvas)
            }
        }
    }
}
