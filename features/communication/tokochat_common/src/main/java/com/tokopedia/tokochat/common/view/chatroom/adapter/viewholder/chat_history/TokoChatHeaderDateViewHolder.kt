package com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.chat_history

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.tokochat_common.databinding.TokochatItemHeaderDateBinding
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatHeaderDateUiModel
import com.tokopedia.tokochat_common.R
import com.tokopedia.utils.view.binding.viewBinding

class TokoChatHeaderDateViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val binding: TokochatItemHeaderDateBinding? by viewBinding()

    fun bind(element: TokoChatHeaderDateUiModel) {
        bindDate(element)
    }

    private fun bindDate(element: TokoChatHeaderDateUiModel) {
        binding?.tokochatTvHeaderDate?.text = element.relativeDate
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.tokochat_item_header_date
    }
}
