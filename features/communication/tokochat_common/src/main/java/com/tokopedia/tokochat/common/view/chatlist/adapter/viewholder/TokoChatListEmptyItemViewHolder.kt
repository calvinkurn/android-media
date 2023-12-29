package com.tokopedia.tokochat.common.view.chatlist.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_TOKOCHAT_LIST_EMPTY
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatListItemEmptyChatBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoChatListEmptyItemViewHolder(view: View) : BaseViewHolder(view) {

    private val binding: TokochatListItemEmptyChatBinding? by viewBinding()

    fun bind() {
        binding?.tokochatListIvEmptyChat?.loadImage(IMG_TOKOCHAT_LIST_EMPTY)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.tokochat_list_item_empty_chat
    }
}
