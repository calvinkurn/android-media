package com.tokopedia.tokofood.purchase.purchasepage.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class TokoFoodPurchaseAdapter(adapterTypeFactory: TokoFoodPurchaseAdapterTypeFactory) :
        BaseListAdapter<Visitable<*>, TokoFoodPurchaseAdapterTypeFactory>(adapterTypeFactory) {

    fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(TokoFoodPurchaseDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

}