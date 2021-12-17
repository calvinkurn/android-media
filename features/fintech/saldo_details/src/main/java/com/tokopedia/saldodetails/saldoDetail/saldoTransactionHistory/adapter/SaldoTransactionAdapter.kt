package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.adapter

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.saldodetails.commom.listener.DataEndLessScrollListener
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.DepositHistoryList
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.TransactionErrorModel

class SaldoTransactionAdapter(adapterTypeFactory: SaldoDetailTransactionFactory) :
    BaseListAdapter<DepositHistoryList, SaldoDetailTransactionFactory>(adapterTypeFactory),
    DataEndLessScrollListener.OnDataEndlessScrollListener {


    private fun showEmptyState() {
        addElement(EmptyModel())
        notifyItemChanged(0)
    }

    fun showLoadingInAdapter() {
        removeErrorNetwork()
        showLoading()
    }

    fun showInitialLoadingFailed(throwable: Throwable){
        setErrorNetworkModel(TransactionErrorModel(throwable))
        showErrorNetwork()
    }

    fun addAllElements(element: List<Visitable<*>>) {
        hideLoading()
        val diffCallback = RatingDiffCallback(visitables, element)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(element)
        if (element.isEmpty()) {
            showEmptyState()
        }
        diffResult.dispatchUpdatesTo(this)
    }

    class RatingDiffCallback(
        private val oldList: List<Visitable<*>>,
        private val newList: List<Visitable<*>>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition] === newList[newPosition]
        }

        @Nullable
        override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
            return super.getChangePayload(oldPosition, newPosition)
        }
    }

    override fun endlessDataSize(): Int {
        return dataSize
    }
}