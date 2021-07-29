package com.tokopedia.saldodetails.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory
import com.tokopedia.saldodetails.adapter.SaldoTransactionAdapter
import com.tokopedia.saldodetails.adapter.listener.DataEndLessScrollListener
import com.tokopedia.saldodetails.di.SaldoDetailsComponent
import com.tokopedia.saldodetails.response.model.DepositHistoryList
import com.tokopedia.saldodetails.view.fragment.new.*
import com.tokopedia.saldodetails.view.viewmodel.TransactionHistoryViewModel
import com.tokopedia.saldodetails.view.viewmodel.state.*
import kotlinx.android.synthetic.main.saldo_fragment_transaction_list.*
import javax.inject.Inject

class SaldoTransactionListFragment : BaseDaggerFragment() {


    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val transactionHistoryViewModel: TransactionHistoryViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        parentFragment?.let {
            val viewModelProvider = ViewModelProvider(it, viewModelFactory.get())
            viewModelProvider.get(TransactionHistoryViewModel::class.java)
        } ?: run {
            null
        }
    }

    override fun getScreenName(): String? = null

    private lateinit var transactionType: TransactionType

    private val adapter : SaldoTransactionAdapter by lazy {
        SaldoTransactionAdapter(getAdapterTypeFactory())
    }

    private var endlessRecyclerViewScrollListener : EndlessRecyclerViewScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transactionTitleStr = arguments?.getString(PARAM_TRANSACTION_TYPE, null)
        val transactionType = TransactionTypeMapper.getTransactionListType(transactionTitleStr)
        if(transactionType != null)
            this.transactionType = transactionType
    }

    override fun initInjector() {
        getComponent(SaldoDetailsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.saldo_fragment_transaction_list,
                container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(::transactionType.isInitialized){
            initRecyclerView()
            initObservers()
        }
    }

    private fun initRecyclerView(){
        transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        transactionRecyclerView.adapter = adapter
        endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener()
        transactionRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener!!)
        endlessRecyclerViewScrollListener?.resetState()
    }

    private fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndLessScrollListener(transactionRecyclerView?.layoutManager, adapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                transactionHistoryViewModel?.loadMoreTransaction(page, transactionType)
            }
        }
    }

    private fun initObservers() {
        transactionHistoryViewModel?.getLiveDataByTransactionType(transactionType)?.observe(
                viewLifecycleOwner
        ) {
            when (it) {
                is InitialLoadingError -> {
                    //todo
                }
                is LoadMoreError -> {
                    //todo try to show error...
                }
                LoadingMoreState-> adapter.showLoadingInAdapter()
                InitialLoadingState -> {
                    endlessRecyclerViewScrollListener?.resetState()
                    adapter.clearAllElements()
                    adapter.showLoadingInAdapter()
                }
                is SaldoHistoryResponse -> onDataLoaded(it.historyList, it.hasMore)
            }
        }
    }

    private fun onDataLoaded(historyList: List<Visitable<*>>, hasMore: Boolean) {
        adapter.addAllElements(historyList)
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(hasMore)
    }

    private fun getAdapterTypeFactory(): SaldoDetailTransactionFactory  = SaldoDetailTransactionFactory(::openDetailPage)

    private fun openDetailPage(visitable: Visitable<*>){
        if(visitable is DepositHistoryList){
            //todo Abhijeet
        }
    }


    companion object {
        val PARAM_TRANSACTION_TYPE = "PARAM_TRANSACTION_TYPE"

        fun getInstance(transactionTitleStr: String): SaldoTransactionListFragment {
            return SaldoTransactionListFragment().apply {
                val bundle = Bundle()
                bundle.putString(PARAM_TRANSACTION_TYPE, transactionTitleStr)
                arguments = bundle
            }
        }

    }

}