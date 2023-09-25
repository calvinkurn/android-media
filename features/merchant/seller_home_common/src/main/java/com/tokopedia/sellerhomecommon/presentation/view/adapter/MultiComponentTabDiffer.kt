package com.tokopedia.sellerhomecommon.presentation.view.adapter

import androidx.recyclerview.widget.DiffUtil.Callback
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab


class MultiComponentTabDiffer(
    private val oldTab: List<MultiComponentTab>,
    private val newTab: List<MultiComponentTab>
) :
    Callback() {

    override fun getOldListSize(): Int {
        return oldTab.size
    }

    override fun getNewListSize(): Int {
        return newTab.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTab[oldItemPosition].id === newTab[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTab[oldItemPosition].components.hashCode() ==
                newTab[newItemPosition].components.hashCode() &&
                oldTab[oldItemPosition].isLoaded == newTab[newItemPosition].isLoaded &&
                oldTab[oldItemPosition].isError == newTab[newItemPosition].isError
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return null
    }
}