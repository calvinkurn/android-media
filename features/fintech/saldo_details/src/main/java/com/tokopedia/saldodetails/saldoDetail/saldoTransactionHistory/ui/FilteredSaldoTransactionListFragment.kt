package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.utils.*
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.saldo_fragment_transaction_list.*

class FilteredSaldoTransactionListFragment : BaseSaldoTransactionListFragment() {

    private val filterTitleList by lazy { TransactionTypeMapper.getFilterList() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.saldo_fragment_transaction_list,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        generateSortFilter()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun generateSortFilter() {
        val filterData = arrayListOf<SortFilterItem>()
        setFilterItem(filterTitleList, filterData)
        transactionFilter.addItem(filterData)
        saldoDetailsAnalytics.sendTransactionHistoryEvents(filterTitleList[0])
    }

    private fun setFilterItem(input: ArrayList<String>, filterList: ArrayList<SortFilterItem>) {
        input.forEachIndexed { index, title ->
            val chipSelection = if (index == 0) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
            val item  = SortFilterItem(title, chipSelection, ChipsUnify.SIZE_SMALL) {
                selectTransactionType(index, title)
            }
            filterList.add(item)
        }
    }

    fun selectTransactionType(index: Int, transactionTitle: String) {
        if (transactionHistoryViewModel?.selectedFilter != index) {
            val newType = TransactionTypeMapper.getTransactionListType(transactionTitle) ?: AllTransaction
            val oldType = TransactionTypeMapper.getTransactionListType(filterTitleList[transactionHistoryViewModel?.selectedFilter ?: 0])
            oldType?.let {
                transactionHistoryViewModel?.getLiveDataByTransactionType(it)?.removeObservers(viewLifecycleOwner)
            }
            transactionType = newType
            transactionHistoryViewModel?.selectedFilter = index
            saldoDetailsAnalytics.sendTransactionHistoryEvents(transactionTitle)
            initObservers()
        }
    }

    companion object {
        private const val PARAM_TRANSACTION_TYPE = "PARAM_TRANSACTION_TYPE"

        fun getInstance(transactionTitleStr: String): FilteredSaldoTransactionListFragment {
            return FilteredSaldoTransactionListFragment().apply {
                val bundle = Bundle()
                bundle.putString(PARAM_TRANSACTION_TYPE, transactionTitleStr)
                arguments = bundle
            }
        }

    }

}