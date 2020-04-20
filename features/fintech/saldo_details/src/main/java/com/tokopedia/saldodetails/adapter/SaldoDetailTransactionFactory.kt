package com.tokopedia.saldodetails.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.saldodetails.response.model.DepositHistoryList
import com.tokopedia.saldodetails.viewholder.SaldoListEmptyViewHolder
import com.tokopedia.saldodetails.viewholder.SaldoTransactionViewHolder

class SaldoDetailTransactionFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {

        val viewHolder: AbstractViewHolder<*>
        if (type == SaldoTransactionViewHolder.LAYOUT) {
            return SaldoTransactionViewHolder(parent)
        } else if (type == SaldoListEmptyViewHolder.LAYOUT) {
            return SaldoListEmptyViewHolder(parent)
        } else {
            viewHolder = super.createViewHolder(parent, type)
        }
        return viewHolder
    }

    fun type(vm: DepositHistoryList): Int {
        return SaldoTransactionViewHolder.LAYOUT
    }

    override fun type(vm: EmptyModel): Int {
        return SaldoListEmptyViewHolder.LAYOUT
    }
}
