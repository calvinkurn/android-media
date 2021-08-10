package com.tokopedia.minicart.chatlist

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.R
import javax.inject.Inject

class MiniCartChatListDecoration @Inject constructor() : RecyclerView.ItemDecoration() {

    private var noMargin: Int = 0
    private var context: Context? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (context == null) {
            context = parent.context
            noMargin = context?.resources?.getDimension(R.dimen.dp_0)?.toInt().orZero()
        }
        setupOutRect(outRect, noMargin, noMargin)
    }

    private fun setupOutRect(outRect: Rect, left: Int, right: Int) {
        outRect.left = left
        outRect.right = right
    }
}