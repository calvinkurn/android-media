package com.tokopedia.orderhistory.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.orderhistory.R
import com.tokopedia.orderhistory.analytic.OrderHistoryAnalytic
import com.tokopedia.orderhistory.di.OrderHistoryComponent
import com.tokopedia.orderhistory.view.adapter.OrderHistoryAdapter
import com.tokopedia.orderhistory.view.adapter.OrderHistoryTypeFactory
import com.tokopedia.orderhistory.view.adapter.OrderHistoryTypeFactoryImpl
import com.tokopedia.orderhistory.view.viewmodel.OrderHistoryViewModel
import javax.inject.Inject

class OrderHistoryFragment : BaseListFragment<Visitable<*>, OrderHistoryTypeFactory>() {

    private val screenName = "order_history"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var analytic: OrderHistoryAnalytic

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(OrderHistoryViewModel::class.java) }
    private lateinit var adapter: OrderHistoryAdapter
    private var shopId: String? = null

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orderhistory_order_history, container, false).also {
            initializeArguments()
            setupObserver()
        }
    }

    private fun initializeArguments() {
        shopId = arguments?.getString(ApplinkConst.OrderHistory.PARAM_SHOP_ID)
    }

    private fun setupObserver() {

    }

    private fun isFirstPage(): Boolean {
        return currentPage == 1
    }

    override fun getScreenName(): String = screenName

    override fun initInjector() {
        getComponent(OrderHistoryComponent::class.java).inject(this)
    }

    override fun getAdapterTypeFactory(): OrderHistoryTypeFactory {
        return OrderHistoryTypeFactoryImpl()
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {

    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, OrderHistoryTypeFactory> {
        return OrderHistoryAdapter(adapterTypeFactory).also { adapter = it }
    }

    companion object {
        fun createInstance(extra: Bundle?): OrderHistoryFragment {
            return OrderHistoryFragment().apply {
                arguments = extra
            }
        }
    }
}