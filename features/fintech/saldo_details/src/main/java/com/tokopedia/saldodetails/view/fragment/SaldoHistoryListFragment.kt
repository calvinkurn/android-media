package com.tokopedia.saldodetails.view.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.saldodetails.adapter.SaldoDepositAdapter
import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory
import com.tokopedia.saldodetails.adapter.listener.DataEndLessScrollListener
import com.tokopedia.saldodetails.presenter.SaldoHistoryPresenter
import com.tokopedia.saldodetails.response.model.DepositHistoryList
import com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment.Companion.FOR_ALL
import com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment.Companion.FOR_BUYER
import com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment.Companion.FOR_SELLER
import com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment.Companion.TRANSACTION_TYPE


class SaldoHistoryListFragment : BaseListFragment<DepositHistoryList, SaldoDetailTransactionFactory>() {
    private var recyclerView: RecyclerView? = null
    private var adapter: SaldoDepositAdapter? = null
    private var presenter: SaldoHistoryPresenter? = null
    private var transactionType: String? = null

    private fun setPresenter(saldoHistoryPresenter: SaldoHistoryPresenter) {
        this.presenter = saldoHistoryPresenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.saldodetails.R.layout.fragment_saldo_history_list, container, false)
        initViews(view)
        initialVar()
        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_history_recycler_view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getRecyclerViewResourceId(): Int {
        return com.tokopedia.saldodetails.R.id.saldo_history_recycler_view
    }


    override fun getAdapter(): SaldoDepositAdapter? {
        return adapter
    }

    override fun createAdapterInstance(): BaseListAdapter<DepositHistoryList, SaldoDetailTransactionFactory> {
        adapter = SaldoDepositAdapter(adapterTypeFactory)
        return adapter as SaldoDepositAdapter
    }

    private fun initialVar() {
        transactionType = arguments!!.getString(TRANSACTION_TYPE)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = adapter
        recyclerView!!.setHasFixedSize(false)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {

        return object : DataEndLessScrollListener(recyclerView!!.layoutManager!!, adapter!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                when (transactionType) {
                    FOR_ALL -> presenter!!.loadMoreAllTransaction(page, ALL_SALDO)
                    FOR_BUYER -> presenter!!.loadMoreBuyerTransaction(page, BUYER_SALDO)
                    FOR_SELLER -> presenter!!.loadMoreSellerTransaction(page, SELLER_SALDO)
                }
            }
        }
    }

    override fun loadData(page: Int) {

    }

    override fun getAdapterTypeFactory(): SaldoDetailTransactionFactory {
        return SaldoDetailTransactionFactory()
    }

    override fun onItemClicked(depositHistoryList: DepositHistoryList) {

    }

    override fun initInjector() {

    }

    override fun getRecyclerView(view: View): RecyclerView {
        this.recyclerView = super.getRecyclerView(view)
        return super.getRecyclerView(view)
    }

    override fun getScreenName(): String? {
        return null
    }

    companion object {

        private val SELLER_SALDO = 1
        private val BUYER_SALDO = 0
        private val ALL_SALDO = 2

        fun createInstance(type: String, saldoHistoryPresenter: SaldoHistoryPresenter): SaldoHistoryListFragment {
            val saldoHistoryListFragment = SaldoHistoryListFragment()
            val bundle = Bundle()
            bundle.putString(TRANSACTION_TYPE, type)
            saldoHistoryListFragment.arguments = bundle
            saldoHistoryListFragment.setPresenter(saldoHistoryPresenter)
            return saldoHistoryListFragment
        }
    }

}
