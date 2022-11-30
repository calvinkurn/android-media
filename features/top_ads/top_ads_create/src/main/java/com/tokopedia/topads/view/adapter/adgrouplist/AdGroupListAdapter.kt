package com.tokopedia.topads.view.adapter.adgrouplist

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.topads.view.adapter.adgrouplist.typefactory.AdGroupTypeFactory

class AdGroupListAdapter(private val typeFactory:AdGroupTypeFactory) : BaseListAdapter<Any,AdGroupTypeFactory>(typeFactory) {

    fun submitList(list:List<Visitable<Any>>){
        setVisitables(list)
    }

}
