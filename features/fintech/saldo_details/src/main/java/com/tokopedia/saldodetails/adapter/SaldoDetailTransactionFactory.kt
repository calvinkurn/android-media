package com.tokopedia.saldodetails.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.saldodetails.domain.model.SalesTransactionDetail
import com.tokopedia.saldodetails.response.model.DepositHistoryList
import com.tokopedia.saldodetails.viewholder.SaldoListEmptyViewHolder
import com.tokopedia.saldodetails.viewholder.SaldoTransactionViewHolder
import com.tokopedia.saldodetails.viewholder.SalesSaldoTransactionViewHolder

class SaldoDetailTransactionFactory(private val onItemClick : (Visitable<*>) -> Unit) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        when (type) {
            SaldoTransactionViewHolder.LAYOUT -> {
                return SaldoTransactionViewHolder(parent, onItemClick)
            }
            SaldoListEmptyViewHolder.LAYOUT -> {
                return SaldoListEmptyViewHolder(parent)
            }
            SalesSaldoTransactionViewHolder.LAYOUT -> {
                return SalesSaldoTransactionViewHolder(parent, onItemClick)
            }
            else -> {
                viewHolder = super.createViewHolder(parent, type)
            }
        }
        return viewHolder
    }

    fun type(vm: DepositHistoryList): Int {
        return SaldoTransactionViewHolder.LAYOUT
    }

    override fun type(vm: EmptyModel): Int {
        return SaldoListEmptyViewHolder.LAYOUT
    }

    fun type(salesTransactionDetail: SalesTransactionDetail): Int {
        return SalesSaldoTransactionViewHolder.LAYOUT
    }
}
