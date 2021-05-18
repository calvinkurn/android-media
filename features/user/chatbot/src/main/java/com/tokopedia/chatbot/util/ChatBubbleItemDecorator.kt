package com.tokopedia.chatbot.util

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.unifyprinciples.Typography

class ChatBubbleItemDecorator(private val dateIndicator: (String) -> Unit) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val topChild = parent.getChildAt(parent.childCount - 1)
        val date = parent.findContainingViewHolder(topChild)?.itemView?.findViewById<Typography>(R.id.date)

        if (!date?.text.isNullOrEmpty()) {
            dateIndicator.invoke(date?.text.toString())
        }

    }

}
