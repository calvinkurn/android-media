package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.utils.*
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.saldo_fragment_transaction_list.*
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.TickerDownloadFeeTransactionModel as TickerDownloadFeeTransactionModel1

class SalesSaldoTransactionListFragment : BaseSaldoTransactionListFragment() {

    companion object {
        private const val PARAM_TRANSACTION_TYPE = "PARAM_TRANSACTION_TYPE"

        fun getInstance(transactionTitleStr: String): SalesSaldoTransactionListFragment {
            return SalesSaldoTransactionListFragment().apply {
                val bundle = Bundle()
                bundle.putString(PARAM_TRANSACTION_TYPE, transactionTitleStr)
                arguments = bundle
            }
        }
    }

    override fun onDataLoaded(historyList: List<Visitable<*>>, hasMore: Boolean) {
        val list: List<Visitable<*>> = if(GlobalConfig.isSellerApp()) {
            if(historyList.isEmpty()) {
                listOf(TickerDownloadFeeTransactionModel1()) + EmptyModel()
            } else {
                listOf(TickerDownloadFeeTransactionModel1()) + historyList
            }
        } else {
            historyList
        }
        super.onDataLoaded(list, hasMore)
    }
}