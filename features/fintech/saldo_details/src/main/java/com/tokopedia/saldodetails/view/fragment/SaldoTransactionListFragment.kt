package com.tokopedia.saldodetails.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory
import com.tokopedia.saldodetails.adapter.SaldoTransactionAdapter
import com.tokopedia.saldodetails.di.SaldoDetailsComponent
import com.tokopedia.saldodetails.response.model.DepositHistoryList
import com.tokopedia.saldodetails.view.fragment.new.TransactionType
import com.tokopedia.saldodetails.view.fragment.new.TransactionTypeMapper
import com.tokopedia.saldodetails.view.viewmodel.TransactionHistoryViewModel
import com.tokopedia.saldodetails.view.viewmodel.state.*
import kotlinx.android.synthetic.main.saldo_fragment_transaction_list.*
import javax.inject.Inject

class SaldoTransactionListFragment : BaseDaggerFragment() {

    lateinit var adapter : SaldoTransactionAdapter


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
        adapter = SaldoTransactionAdapter(getAdapterTypeFactory())
        transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        transactionRecyclerView.adapter = adapter
    }

    private fun initObservers() {
        transactionHistoryViewModel?.getLiveDataByTransactionType(transactionType)?.observe(
                viewLifecycleOwner
        ) {
            when (it) {
                is InitialLoadingError -> {
                }
                is LoadMoreError -> {
                    //try to show error...
                }
                LoadingMoreState, InitialLoadingState -> {
                    adapter.showLoadingInAdapter()
                }
                is SaldoHistoryResponse -> onDataLoaded(it.historyList, it.hasMore)
            }
        }
    }

    private fun onDataLoaded(historyList: ArrayList<DepositHistoryList>, hasMore: Boolean) {
        adapter.addAllElements(historyList)
    }

    private fun getAdapterTypeFactory(): SaldoDetailTransactionFactory  = SaldoDetailTransactionFactory()

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