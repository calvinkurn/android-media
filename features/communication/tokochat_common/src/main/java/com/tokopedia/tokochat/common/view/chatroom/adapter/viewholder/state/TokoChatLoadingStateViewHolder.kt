package com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.state

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.tokochat_common.R

class TokoChatLoadingStateViewHolder(itemView: View): BaseViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.tokochat_item_shimmer
    }
}
