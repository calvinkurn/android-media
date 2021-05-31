package com.tokopedia.saldodetails.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.saldodetails.adapter.SaldoDepositAdapter
import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory
import com.tokopedia.saldodetails.adapter.listener.DataEndLessScrollListener
import com.tokopedia.saldodetails.contract.SaldoHistoryContract
import com.tokopedia.saldodetails.viewmodels.SaldoHistoryViewModel
import com.tokopedia.saldodetails.response.model.DepositActivityResponse
import com.tokopedia.saldodetails.response.model.DepositHistoryList
import com.tokopedia.saldodetails.utils.*
import com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment.Companion.FOR_ALL
import com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment.Companion.FOR_BUYER
import com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment.Companion.FOR_SELLER
import com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment.Companion.TRANSACTION_TYPE


class SaldoHistoryListFragment : BaseListFragment<DepositHistoryList, SaldoDetailTransactionFactory>() {

    companion object {
        fun createInstance(type: String, saldoHistoryViewModel: SaldoHistoryViewModel, saldoHistoryFragrmnt : SaldoHistoryContract.View ): SaldoHistoryListFragment {
            val saldoHistoryListFragment = SaldoHistoryListFragment()
            val bundle = Bundle()
            bundle.putString(TRANSACTION_TYPE, type)
            saldoHistoryListFragment.arguments = bundle
            saldoHistoryListFragment.viewModel = saldoHistoryViewModel
            saldoHistoryListFragment.saldoHistoryFragrmnt = saldoHistoryFragrmnt
            return saldoHistoryListFragment
        }
    }

    private var recyclerView: RecyclerView? = null
    private var adapter: SaldoDepositAdapter? = null
    lateinit var saldoHistoryFragrmnt: SaldoHistoryContract.View
    lateinit var viewModel: SaldoHistoryViewModel
    private var transactionType: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.saldodetails.R.layout.fragment_saldo_history_list, container, false)
        if (savedInstanceState == null) {
            initViews(view)
            initialVar()
            addObserver()
        }
        return view
    }

    private fun addObserver() {
        if(::viewModel.isInitialized) {
            when (transactionType) {
                FOR_ALL -> addAllTransactionObserver()
                FOR_BUYER -> buyerObserver()
                FOR_SELLER -> sellerObserver()
            }
        }
    }

    private fun sellerObserver() = viewModel.sellerResponseLiveData.observe(this, Observer {
        it?.let {
            setData(it)
        }
    })

    private fun setData(it: Resources<DepositActivityResponse>) {
        when (it) {
            is Loading -> {
                saldoHistoryFragrmnt.setActionsEnabled(false)
                getAdapter()?.showLoading()
            }
            is Success -> {
                getAdapter()?.clearAllElements()
                onSuccess(it.data)
            }
            is ErrorMessage<*,*> -> onError(if (it.data is Int) getString(it.data) else it.data.toString())
            is AddElements -> onSuccess(it.data)
        }
    }

    private fun onError(data : String?){
        saldoHistoryFragrmnt.setActionsEnabled(true)
        if (getAdapter() != null && getAdapter()?.itemCount == 0) {
            data?.let {saldoHistoryFragrmnt.showEmptyState(data) } ?: saldoHistoryFragrmnt.showEmptyState()
        } else {
            data?.let {  saldoHistoryFragrmnt.setRetry(data) }  ?: saldoHistoryFragrmnt.setRetry()
        }
    }

    private fun onSuccess(data : DepositActivityResponse){
        saldoHistoryFragrmnt.setActionsEnabled(true)
        getAdapter()?.hideLoading()
        getAdapter()?.addElement(data.depositHistoryList)
        updateScrollListenerState(data.isHaveNextPage)
        if (getAdapter()?.itemCount == 0) {
            getAdapter()?.addElement(getDefaultEmptyViewModel())
        }
    }

    private fun buyerObserver() = viewModel.buyerResponseLiveData.observe(this, Observer { it?.let { setData(it) } })

    private fun addAllTransactionObserver() = viewModel.allDepositResponseLiveData.observe(this, Observer { it?.let { setData(it) } })

    private fun initViews(view: View) {
        recyclerView = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_history_recycler_view)
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
        transactionType = arguments?.getString(TRANSACTION_TYPE)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
        recyclerView?.adapter = adapter
        recyclerView?.setHasFixedSize(false)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {

        return object : DataEndLessScrollListener(recyclerView?.layoutManager, adapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                when (transactionType) {
                    FOR_ALL -> viewModel.loadMoreAllTransaction(page)
                    FOR_BUYER -> viewModel.loadMoreBuyerTransaction(page)
                    FOR_SELLER -> viewModel.loadMoreSellerTransaction(page)
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

    override fun getRecyclerView(view: View): RecyclerView? {
        this.recyclerView = super.getRecyclerView(view)
        return super.getRecyclerView(view)
    }

    override fun getScreenName(): String? {
        return null
    }

     fun getDefaultEmptyViewModel(): Visitable<*>? {
        return EmptyModel()
    }
}
