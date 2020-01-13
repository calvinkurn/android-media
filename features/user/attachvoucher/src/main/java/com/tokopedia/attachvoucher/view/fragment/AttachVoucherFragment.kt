package com.tokopedia.attachvoucher.view.fragment

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
import com.tokopedia.attachvoucher.R
import com.tokopedia.attachvoucher.analytic.AttachVoucherAnalytic
import com.tokopedia.attachvoucher.data.Voucher
import com.tokopedia.attachvoucher.data.VoucherType
import com.tokopedia.attachvoucher.di.AttachVoucherComponent
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherAdapter
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherTypeFactory
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherTypeFactoryImpl
import com.tokopedia.attachvoucher.view.viewmodel.AttachVoucherViewModel
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.fragment_attachvoucher_attach_voucher.*
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
        return inflater.inflate(R.layout.fragment_attachvoucher_attach_voucher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.initializeArguments(arguments)
        setupRecyclerView()
        setupObserver()
        setupFilter()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupRecyclerView() {

    }

    private fun setupObserver() {
        observeFilter()
        observeVoucherResponse()
        observeVoucherState()
    }

    private fun observeFilter() {
        viewModel.filter.observe(viewLifecycleOwner, Observer { type ->
            when (type) {
                VoucherType.CASH_BACK -> setActiveFilter(filterCashBack, filterFreeOngkir)
                VoucherType.FREE_ONGKIR -> setActiveFilter(filterFreeOngkir, filterCashBack)
                AttachVoucherViewModel.NO_FILTER -> clearFilter()
            }
        })
    }

    private fun observeVoucherResponse() {
        viewModel.filteredVouchers.observe(viewLifecycleOwner, Observer { vouchers ->
            adapter.clearSelected()
            clearAllData()
            renderList(vouchers)
        })
    }

    private fun observeVoucherState() {
        adapter.selectedInvoice.observe(viewLifecycleOwner, Observer { voucher ->
            if (voucher != null) {
                enableAttachButton(voucher)
            } else {
                disableAttachButton()
            }
        })
    }

    private fun enableAttachButton(invoice: Voucher) {
        btnAttach?.isEnabled = true
        btnAttach.setOnClickListener {
            // TODO: impl click
        }
    }

    private fun disableAttachButton() {
        btnAttach?.isEnabled = false
    }

    private fun setupFilter() {
        filterCashBack?.setOnClickListener {
            viewModel.toggleFilter(VoucherType.CASH_BACK)
        }
        filterFreeOngkir?.setOnClickListener {
            viewModel.toggleFilter(VoucherType.FREE_ONGKIR)
        }
    }

    private fun setActiveFilter(selected: ChipsUnify?, deselected: ChipsUnify?) {
        selected?.chipType = ChipsUnify.TYPE_SELECTED
        deselected?.chipType = ChipsUnify.TYPE_ALTERNATE
    }

    private fun clearFilter() {
        filterCashBack?.chipType = ChipsUnify.TYPE_ALTERNATE
        filterFreeOngkir?.chipType = ChipsUnify.TYPE_ALTERNATE
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
        if (page != 1) return
        viewModel.loadVouchers()
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