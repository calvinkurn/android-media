package com.tokopedia.tokofood.purchase.promopage.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class TokoFoodPromoAdapter(adapterTypeFactory: TokoFoodPromoAdapterTypeFactory) :
        BaseListAdapter<Visitable<*>, TokoFoodPromoAdapterTypeFactory>(adapterTypeFactory) {

    fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(TokoFoodPromoDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

}