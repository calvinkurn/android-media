package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsAnalytics
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponent
import com.tokopedia.saldodetails.commom.listener.DataEndLessScrollListener
import com.tokopedia.saldodetails.commom.utils.NavToolbarExt
import com.tokopedia.saldodetails.commom.utils.SalesTransaction
import com.tokopedia.saldodetails.commom.utils.TransactionType
import com.tokopedia.saldodetails.commom.utils.TransactionTypeMapper
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.adapter.SaldoDetailTransactionFactory
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.adapter.SaldoTransactionAdapter
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.DepositHistoryList
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.SalesTransactionDetail
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.viewmodel.*
import com.tokopedia.saldodetails.transactionDetailPages.penjualan.SaldoSalesDetailActivity
import com.tokopedia.saldodetails.transactionDetailPages.withdrawal.SaldoWithdrawalDetailActivity
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.saldo_fragment_transaction_list.*
import javax.inject.Inject

open class BaseSaldoTransactionListFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var saldoDetailsAnalytics: SaldoDetailsAnalytics

    protected val transactionHistoryViewModel: TransactionHistoryViewModel? by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        parentFragment?.let {
            val viewModelProvider = ViewModelProvider(it, viewModelFactory.get())
            viewModelProvider.get(TransactionHistoryViewModel::class.java)
        } ?: run {
            null
        }
    }

    override fun getScreenName(): String? = null

    protected lateinit var transactionType: TransactionType

    protected val adapter: SaldoTransactionAdapter by lazy {
        SaldoTransactionAdapter(getAdapterTypeFactory())
    }

    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transactionTitleStr = arguments?.getString(PARAM_TRANSACTION_TYPE, null)
        val transactionType = TransactionTypeMapper.getTransactionListType(transactionTitleStr)
        if (transactionType != null)
            this.transactionType = transactionType
    }

    override fun initInjector() {
        getComponent(SaldoDetailsComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.base_saldo_fragment_transaction_list,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (::transactionType.isInitialized) {
            initRecyclerView()
            initObservers()
        }
    }

    private fun initRecyclerView() {
        transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        transactionRecyclerView.adapter = adapter
        endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener()
        transactionRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener!!)
        endlessRecyclerViewScrollListener?.resetState()

        transactionRecyclerView.post {
            context?.let {
                transactionRecyclerView.layoutParams.height =
                    getScreenHeight() - NavToolbarExt.getToolbarHeight(it) - NavToolbarExt.getStatusbarHeight(it) - RECYCLERVIEW_HEIGHT_OFFSET.toPx()
                transactionRecyclerView.requestLayout()
            }
        }
    }

    private fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndLessScrollListener(transactionRecyclerView?.layoutManager, adapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                transactionHistoryViewModel?.loadMoreTransaction(transactionType)
            }
        }
    }

    private fun initObservers() {
        transactionHistoryViewModel?.getLiveDataByTransactionType(transactionType)?.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is InitialLoadingError -> {
                    saldoDetailsAnalytics.sendApiFailureEvents(
                        transactionHistoryViewModel?.getEventLabelForList(transactionType) ?: ""
                    )
                    adapter.showInitialLoadingFailed(it.throwable)
                }
                is LoadMoreError -> {
                    saldoDetailsAnalytics.sendApiFailureEvents(
                        transactionHistoryViewModel?.getEventLabelForList(transactionType) ?: ""
                    )
                    showLoadMoreRetryToast()
                }
                LoadingMoreState -> adapter.showLoadingInAdapter()
                InitialLoadingState -> {
                    endlessRecyclerViewScrollListener?.resetState()
                    adapter.clearAllElements()
                    adapter.showLoadingInAdapter()
                }
                is SaldoHistoryResponse -> onDataLoaded(it.historyList, it.hasMore)
            }
        }
    }

    private fun showLoadMoreRetryToast() {
        view?.let {
            Toaster.build(
                it, getString(R.string.saldo_failed_to_load_more_transaction),
                Toaster.LENGTH_INDEFINITE, Toaster.TYPE_ERROR, getString(R.string.saldo_retry)
            ) {
                transactionHistoryViewModel?.loadMoreTransaction(transactionType)
            }.show()
        }
    }

    protected open fun onDataLoaded(historyList: List<Visitable<*>>, hasMore: Boolean) {
        adapter.addAllElements(historyList)
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(hasMore)
    }

    private fun getAdapterTypeFactory(): SaldoDetailTransactionFactory =
        SaldoDetailTransactionFactory(
            ::openDetailPage,
            ::retryInitialLoading,
            (transactionType == SalesTransaction)
        )

    private fun openDetailPage(visitable: Visitable<*>) {
        saldoDetailsAnalytics.sendTransactionHistoryEvents(
            transactionHistoryViewModel?.getEventLabelForDetail(transactionType.title) ?: "")
        if (visitable is DepositHistoryList) {
            context?.let {
                startActivity(
                    SaldoWithdrawalDetailActivity.newInstance(
                        it,
                        visitable.withdrawalId
                    )
                )
            }
        } else if (visitable is SalesTransactionDetail) {
            context?.let {
                startActivity(
                    SaldoSalesDetailActivity.newInstance(
                        it,
                        visitable.summaryID
                    )
                )
            }
        }
    }

    private fun retryInitialLoading() {
        transactionHistoryViewModel?.retryAllTabLoading()
    }

    companion object {
        const val PARAM_TRANSACTION_TYPE = "PARAM_TRANSACTION_TYPE"

        private const val RECYCLERVIEW_HEIGHT_OFFSET = 160

        fun getInstance(transactionTitleStr: String): BaseSaldoTransactionListFragment {
            return BaseSaldoTransactionListFragment().apply {
                val bundle = Bundle()
                bundle.putString(PARAM_TRANSACTION_TYPE, transactionTitleStr)
                arguments = bundle
            }
        }

    }
}
