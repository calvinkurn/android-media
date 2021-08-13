package com.tokopedia.product.manage.feature.filter.presentation.adapter.diffutil

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataUiModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChipsAdapter.Companion.KEY_SELECT_BUNDLE

class FilterDataDiffUtil(private val oldList: List<FilterDataUiModel>, private val newList: List<FilterDataUiModel>)
    : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].id == newList[newItemPosition].id)
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].select == newList[newItemPosition].select)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        val bundle = Bundle()

        if (oldItem.select != newItem.select) {
            bundle.putBoolean(KEY_SELECT_BUNDLE, newItem.select)
        }

        return if(bundle.size() == 0) {
            null
        } else {
            bundle
        }
    }
}