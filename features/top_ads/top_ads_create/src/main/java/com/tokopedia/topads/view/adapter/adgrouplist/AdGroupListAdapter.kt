package com.tokopedia.topads.view.adapter.adgrouplist

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.topads.view.adapter.adgrouplist.typefactory.AdGroupTypeFactory

class AdGroupListAdapter(typeFactory:AdGroupTypeFactory) : BaseListAdapter<Any,AdGroupTypeFactory>(typeFactory) {

    fun submitList(list:List<Visitable<Any>>){
        val diffUtilCallback = AdGroupListDiffer(visitables,list)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(list)
        result.dispatchUpdatesTo(this)
    }

}
