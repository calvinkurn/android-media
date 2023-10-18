package com.tokopedia.minicart.chatlist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatProductUiModel
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatUnavailableReasonUiModel

class MiniCartChatListDiffUtilCallback(
    private val oldList: List<Any>,
    private val newList: List<Any>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return when {
            oldItem is MiniCartChatProductUiModel && newItem is MiniCartChatProductUiModel -> oldItem == newItem
            oldItem is MiniCartChatUnavailableReasonUiModel && newItem is MiniCartChatUnavailableReasonUiModel -> oldItem == newItem
            else -> false
        }
    }
}
