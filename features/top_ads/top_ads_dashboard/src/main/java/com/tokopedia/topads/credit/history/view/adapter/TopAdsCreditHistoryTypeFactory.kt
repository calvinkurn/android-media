package com.tokopedia.topads.credit.history.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.credit.history.data.model.CreditHistory
import com.tokopedia.topads.credit.history.view.adapter.viewholder.CreditHistoryViewHolder

class TopAdsCreditHistoryTypeFactory: BaseAdapterTypeFactory() {
    fun type(creditHistory: CreditHistory): Int  = CreditHistoryViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            CreditHistoryViewHolder.LAYOUT -> CreditHistoryViewHolder(parent!!)
            else -> super.createViewHolder(parent, type)
        }
    }
}