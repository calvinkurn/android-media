package com.tokopedia.play_common.ui.chat.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play_common.R

/**
 * Created by jegul on 09/06/20
 */
class PlayChatItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.play_bro_chat_spacing)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = dividerHeight
    }
}