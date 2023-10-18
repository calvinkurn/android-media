package com.tokopedia.minicart.chatlist

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.R
import com.tokopedia.minicart.chatlist.viewholder.MiniCartChatSeparatorViewHolder
import com.tokopedia.minicart.chatlist.viewholder.MiniCartChatUnavailableReasonViewHolder
import javax.inject.Inject

class MiniCartChatListDecoration @Inject constructor() : RecyclerView.ItemDecoration() {

    private var noMargin: Int = 0
    private var context: Context? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (context == null) {
            context = parent.context
            noMargin = context?.resources?.getDimension(R.dimen.dp_0)?.toInt().orZero()
        }
        when (parent.getChildViewHolder(view)) {
            is MiniCartChatUnavailableReasonViewHolder -> setupOutRect(outRect, noMargin, noMargin, context?.resources?.getDimension(R.dimen.dp_16)?.toInt().orZero())
            is MiniCartChatSeparatorViewHolder -> setupOutRect(outRect, noMargin, noMargin, context?.resources?.getDimension(R.dimen.dp_16)?.toInt().orZero())
            else -> setupOutRect(outRect, noMargin, noMargin)
        }
    }

    private fun setupOutRect(outRect: Rect, left: Int, right: Int, top: Int? = null) {
        outRect.left = left
        outRect.right = right
        top?.let {
            outRect.top = it
        }
    }
}
