package com.tokopedia.orderhistory.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
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
    private var listener: Listener? = null

    interface Listener {
        fun onClickOrderHistory(intent: Intent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orderhistory_order_history, container, false).also {
            viewModel.initializeArguments(arguments)
            setupObserver()
        }
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
        adapter = OrderHistoryAdapter(adapterTypeFactory)
        return adapter
    }

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    companion object {
        fun createInstance(extra: Bundle?): OrderHistoryFragment {
            return OrderHistoryFragment().apply {
                arguments = extra
            }
        }
    }
}