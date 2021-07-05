package com.tokopedia.product.manage.feature.cashback.presentation.adapter.diffutil

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewholder.SetCashbackViewHolder.Companion.KEY_IS_SELECTED_CASHBACK
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewmodel.SetCashbackUiModel

class SetCashbackDiffUtil(private val oldList: List<SetCashbackUiModel>, private val newList: List<SetCashbackUiModel>)
    : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].cashback == newList[newItemPosition].cashback)
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].isSelected == newList[newItemPosition].isSelected)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val newItem = newList[newItemPosition]
        val oldItem = oldList[oldItemPosition]

        val diff = Bundle()

        if(oldItem.isSelected != newItem.isSelected) {
            diff.putBoolean(KEY_IS_SELECTED_CASHBACK, newItem.isSelected)
        }

        return if (diff.size() == 0) {
            null
        } else {
            diff
        }
    }
}