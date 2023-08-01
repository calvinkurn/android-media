package com.tokopedia.tokochat.common.view.chatlist.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.tokochat_common.R

class TokoChatListLoaderViewHolder(itemView: View): BaseViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.tokochat_list_item_loader
    }
}
