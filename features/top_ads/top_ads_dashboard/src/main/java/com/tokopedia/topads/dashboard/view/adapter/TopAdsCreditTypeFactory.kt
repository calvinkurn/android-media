package com.tokopedia.topads.dashboard.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.view.adapter.viewholder.DataCreditViewHolder

class TopAdsCreditTypeFactory(private val listener: DataCreditViewHolder.OnSelectedListener): BaseAdapterTypeFactory(){
    fun type(dataCredit: DataCredit) = DataCreditViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            DataCreditViewHolder.LAYOUT -> DataCreditViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}