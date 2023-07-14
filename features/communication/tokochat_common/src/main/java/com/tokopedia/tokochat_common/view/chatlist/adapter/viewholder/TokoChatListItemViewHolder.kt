package com.tokopedia.tokochat_common.view.chatlist.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat_common.databinding.TokochatListItemChatListBinding
import com.tokopedia.tokochat_common.view.chatlist.listener.TokoChatListItemListener
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.util.TokoChatUrlUtil.IC_TOKOFOOD_SOURCE
import com.tokopedia.utils.view.binding.viewBinding

class TokoChatListItemViewHolder(
    view: View,
    listener: TokoChatListItemListener
) : BaseViewHolder(view) {

    private val binding: TokochatListItemChatListBinding? by viewBinding()

    fun bind(element: String) {
        binding?.tokochatListIvDriver?.loadImage("https://images.tokopedia.net/img/cache/300/tPxBYm/2022/9/21/ac3cb4d9-00e5-43fe-a745-eb007a887c03.jpg")
        binding?.tokochatListIvLogo?.loadImage(IC_TOKOFOOD_SOURCE)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.tokochat_list_item_chat_list
    }
}
