package com.tokopedia.chatbot.util

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.unifyprinciples.Typography

class ChatReplyOnBoardingBubbleItemDecorator(private val view: (View) -> Unit) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val bottomChild = parent.getChildAt(0)
        if (bottomChild!=null){
            view.invoke(bottomChild)

        }

    }

}
