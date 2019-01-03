package com.tokopedia.topads.dashboard.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.data.model.credit_history.CreditHistory
import com.tokopedia.topads.dashboard.view.adapter.viewholder.CreditHistoryViewHolder

class TopAdsCreditHistoryTypeFactory: BaseAdapterTypeFactory() {
    fun type(creditHistory: CreditHistory): Int  = CreditHistoryViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            CreditHistoryViewHolder.LAYOUT -> CreditHistoryViewHolder(parent!!)
            else -> super.createViewHolder(parent, type)
        }
    }
}