package com.tokopedia.saldodetails.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.saldodetails.domain.model.SalesTransactionDetail
import com.tokopedia.saldodetails.response.model.DepositHistoryList
import com.tokopedia.saldodetails.viewholder.*

class SaldoDetailTransactionFactory(private val onItemClick : (Visitable<*>) -> Unit,
                                    private val retryLoading : () -> Unit,
                                    private val isSaleTabAdapter : Boolean = false) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        when (type) {
            SaldoTransactionViewHolder.LAYOUT -> {
                return SaldoTransactionViewHolder(parent, onItemClick)
            }
            SaldoListEmptyViewHolder.LAYOUT -> {
                return SaldoListEmptyViewHolder(parent, isSaleTabAdapter)
            }
            SalesSaldoTransactionViewHolder.LAYOUT -> {
                return SalesSaldoTransactionViewHolder(parent, onItemClick)
            }
            TransactionLoadingFailedViewHolder.LAYOUT -> {
                return TransactionLoadingFailedViewHolder(parent, retryLoading)
            }
            TransactionLoadingViewHolder.LAYOUT -> {
                return TransactionLoadingViewHolder(parent)
            }
            TransactionLoadMoreViewHolder.LAYOUT -> {
                return TransactionLoadMoreViewHolder(parent)
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

    override fun type(vm: ErrorNetworkModel): Int {
        return TransactionLoadingFailedViewHolder.LAYOUT
    }

    override fun type(vm: LoadingModel): Int {
        return TransactionLoadingViewHolder.LAYOUT
    }

    override fun type(vm: LoadingMoreModel): Int {
        return TransactionLoadMoreViewHolder.LAYOUT
    }

    fun type(salesTransactionDetail: SalesTransactionDetail): Int {
        return SalesSaldoTransactionViewHolder.LAYOUT
    }
}
