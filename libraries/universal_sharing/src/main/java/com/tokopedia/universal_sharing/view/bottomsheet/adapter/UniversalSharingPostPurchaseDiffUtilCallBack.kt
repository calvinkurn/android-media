package com.tokopedia.universal_sharing.view.bottomsheet.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.universal_sharing.view.bottomsheet.typefactory.UniversalSharingTypeFactory

class UniversalSharingPostPurchaseDiffUtilCallBack(
    private val oldList: List<Visitable<in UniversalSharingTypeFactory>>,
    private val newList: List<Visitable<in UniversalSharingTypeFactory>>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
