package com.tokopedia.attachvoucher.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.attachvoucher.R
import com.tokopedia.attachvoucher.analytic.AttachVoucherAnalytic
import com.tokopedia.attachvoucher.di.AttachVoucherComponent
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherAdapter
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherTypeFactory
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherTypeFactoryImpl
import com.tokopedia.attachvoucher.view.viewmodel.AttachVoucherViewModel
import javax.inject.Inject

class AttachVoucherFragment : BaseListFragment<Visitable<*>, AttachVoucherTypeFactory>() {

    private val screenName = "attach_voucher"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytic: AttachVoucherAnalytic

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(AttachVoucherViewModel::class.java) }

    private lateinit var adapter: AttachVoucherAdapter
    private var listener: Listener? = null

    interface Listener {
        fun onClickAttachVoucher(intent: Intent)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_attachvoucher_attach_voucher, container, false).also {
            viewModel.initializeArguments(arguments)
            setupRecyclerView(it)
            setupObserver()
        }
    }

    private fun setupRecyclerView(view: View) {

    }

    private fun setupObserver() {

    }

    private fun isFirstPage(): Boolean {
        return currentPage == 1
    }

    override fun getScreenName(): String = screenName

    override fun initInjector() {
        getComponent(AttachVoucherComponent::class.java).inject(this)
    }

    override fun getAdapterTypeFactory(): AttachVoucherTypeFactory {
        return AttachVoucherTypeFactoryImpl()
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {

    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, AttachVoucherTypeFactory> {
        adapter = AttachVoucherAdapter(adapterTypeFactory)
        return adapter
    }

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    companion object {
        fun createInstance(extra: Bundle?): AttachVoucherFragment {
            return AttachVoucherFragment().apply {
                arguments = extra
            }
        }
    }
}