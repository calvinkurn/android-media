package com.tokopedia.affiliate.feature.dashboard.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.feature.dashboard.di.DaggerDashboardComponent
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.CommissionDetailTypeFactoryImpl
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetailViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionTransactionViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.EmptyCommissionViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.coroutinedata.CommissionData
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_commission_detail.*
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-08-12
 */
class CommissionDetailFragment: BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>() {

    companion object {
        val PARAM_AFF_ID = "AFF_ID"
        fun newInstance(bundle: Bundle): CommissionDetailFragment {
            val fragment = CommissionDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var isLoading = false
    private var isForceRefresh = false
    private var affId = ""
    private var cursor = ""
    private var hasNext = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var commissionDetailViewModel: CommissionDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            commissionDetailViewModel = viewModelProvider.get(CommissionDetailViewModel::class.java)
        }
        arguments?.let {
            affId = it.getString(PARAM_AFF_ID, "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_commission_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        commissionDetailViewModel.commissionDetailRsp.observe(this, Observer {
            isLoading = false
            when (it) {
                is Success -> onSuccessGetFirstData(it.data)
                is Fail -> onErrorGetFirstData(it.throwable)
            }
        })

        commissionDetailViewModel.transactionDetailLoadMoreRsp.observe(this, Observer {
            isLoading = false
            when (it) {
                is Success -> onSuccessGetLoadMoreData(it.data)
                is Fail -> onErrorLoadMoreData(it.throwable)
            }
        })

    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return CommissionDetailTypeFactoryImpl()
    }

    override fun onItemClicked(t: Visitable<*>?) {

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        val affiliateComponent = DaggerAffiliateComponent.builder()
                .baseAppComponent((activity!!.applicationContext as BaseMainApplication).baseAppComponent).build() as DaggerAffiliateComponent

        DaggerDashboardComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this)
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return swipe_refresh_layout
    }

    override fun onSwipeRefresh() {
        hideSnackBarRetry()
        swipeToRefresh.isRefreshing = true
        commissionDetailViewModel.getFirstData(affId, true)
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return rv_commission_detail
    }

    override fun loadData(page: Int) {
        isLoading = true
        if (isLoadingInitialData) {
            commissionDetailViewModel.getFirstData(affId, isForceRefresh)
        } else {
            if (hasNext && cursor.isNotEmpty()) {
                commissionDetailViewModel.loadMoreTxDetail(affId, cursor)
            }
        }
    }

    fun onSuccessGetFirstData(commissionData: CommissionData) {
        adapter.clearAllElements()
        val dataList: MutableList<Visitable<*>> = ArrayList()
        dataList.add(commissionData.commissionDetailHeaderViewModel)
        if (commissionData.commissionTransactionViewModel.historyList.isNotEmpty()) {
            dataList.addAll(commissionData.commissionTransactionViewModel.historyList)
        } else {
            dataList.add(EmptyCommissionViewModel())
        }
        cursor = commissionData.commissionTransactionViewModel.nextCursor
        hasNext = commissionData.commissionTransactionViewModel.hasNext
        renderList(dataList, hasNext)
    }

    fun onErrorGetFirstData(throwable: Throwable) {
        view?.let {
            Toaster.showErrorWithAction(it, throwable.localizedMessage, Snackbar.LENGTH_LONG, "Coba Lagi", View.OnClickListener {
                loadInitialData()
            })
        }
    }

    fun onSuccessGetLoadMoreData(commissionTransactionViewModel: CommissionTransactionViewModel) {
        val dataList: MutableList<Visitable<*>> = ArrayList()
        dataList.addAll(commissionTransactionViewModel.historyList)
        cursor = commissionTransactionViewModel.nextCursor
        hasNext = commissionTransactionViewModel.hasNext
        renderList(dataList, hasNext)
    }

    fun onErrorLoadMoreData(throwable: Throwable) {
        view?.let {
            Toaster.showErrorWithAction(it, throwable.localizedMessage, Snackbar.LENGTH_LONG, "Coba Lagi", View.OnClickListener {
                loadData(0)
            })
        }
    }
}