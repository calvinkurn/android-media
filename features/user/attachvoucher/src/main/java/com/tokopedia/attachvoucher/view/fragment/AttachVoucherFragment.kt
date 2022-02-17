package com.tokopedia.attachvoucher.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.attachcommon.data.VoucherPreview
import com.tokopedia.attachvoucher.R
import com.tokopedia.attachvoucher.analytic.AttachVoucherAnalytic
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.databinding.FragmentAttachvoucherAttachVoucherBinding
import com.tokopedia.attachvoucher.di.AttachVoucherComponent
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase.MVFilter.VoucherType
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherAdapter
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherTypeFactory
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherTypeFactoryImpl
import com.tokopedia.attachvoucher.view.viewmodel.AttachVoucherViewModel
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class AttachVoucherFragment : BaseListFragment<Visitable<*>, AttachVoucherTypeFactory>() {

    private val screenName = "attach_voucher"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding: FragmentAttachvoucherAttachVoucherBinding? by viewBinding()

    @Inject
    lateinit var analytic: AttachVoucherAnalytic

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(AttachVoucherViewModel::class.java) }

    private lateinit var adapter: AttachVoucherAdapter

    override fun initInjector() = getComponent(AttachVoucherComponent::class.java).inject(this)
    override fun getAdapterTypeFactory() = AttachVoucherTypeFactoryImpl()
    override fun createAdapterInstance() = AttachVoucherAdapter(adapterTypeFactory).also { adapter = it }
    override fun getScreenName() = screenName
    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_attachvoucher_attach_voucher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupObserver()
        setupFilter()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun loadData(page: Int) {
        viewModel.loadVouchers(page)
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.setHasFixedSize(true)
    }

    private fun setupObserver() {
        observeFilter()
        observeVoucherResponse()
        observeVoucherState()
        observeError()
    }

    private fun observeFilter() {
        viewModel.filter.observe(viewLifecycleOwner, Observer { type ->
            adapter.clearSelected()
            clearAllData()
            loadInitialData()
            analytic.trackOnChangeFilter(type)
            when (type) {
                VoucherType.paramCashback -> setActiveFilter(binding?.filterCashBack, binding?.filterFreeOngkir)
                VoucherType.paramFreeOngkir -> setActiveFilter(binding?.filterFreeOngkir, binding?.filterCashBack)
                AttachVoucherViewModel.NO_FILTER -> clearFilter()
            }
        })
    }

    private fun observeVoucherResponse() {
        viewModel.voucher.observe(viewLifecycleOwner, Observer { vouchers ->
            changeState(vouchers)
            renderList(vouchers, viewModel.hasNext)
        })
    }

    private fun changeState(vouchers: List<VoucherUiModel>) {
        if (isFirstPage() && vouchers.isEmpty()) {
            if (viewModel.hasNoFilter()) {
                changeActionState(View.GONE)
            } else {
                binding?.flAttach?.hide()
            }
        } else {
            changeActionState(View.VISIBLE)
        }
    }

    private fun isFirstPage(): Boolean {
        return viewModel.currentPage == 1
    }

    private fun changeActionState(visibility: Int) {
        binding?.let {
            it.flAttach.visibility = visibility
            it.filterContainer.visibility = visibility
        }
    }

    private fun observeVoucherState() {
        adapter.selectedVoucher.observe(viewLifecycleOwner, Observer { voucher ->
            if (voucher != null) {
                enableAttachButton(voucher)
            } else {
                disableAttachButton()
            }
        })
    }

    private fun observeError() {
        viewModel.error.observe(viewLifecycleOwner, Observer { throwable ->
            if (isFirstPage()) {
                binding?.flAttach?.hide()
            } else {
                binding?.flAttach?.show()
            }
            showGetListError(throwable)
            disableAttachButton()
        })
    }

    private fun enableAttachButton(voucher: VoucherUiModel) {
        binding?.btnAttach?.isEnabled = true
        binding?.btnAttach?.setOnClickListener {
            analytic.trackOnAttachVoucher(voucher)
            activity?.setResult(Activity.RESULT_OK, getVoucherPreviewIntent(voucher))
            activity?.finish()
        }
    }

    private fun getVoucherPreviewIntent(voucher: VoucherUiModel): Intent {
        val voucherPreview = VoucherPreview(
                source = "inbox",
                voucherId = voucher.voucherId,
                tnc = voucher.tnc ?: "",
                voucherCode = voucher.voucherCode ?: "",
                voucherName = voucher.voucherName ?: "",
                minimumSpend = voucher.minimumSpend,
                validThru = voucher.validThru.toLong(),
                desktopUrl = voucher.merchantVoucherBanner?.desktopUrl ?: "",
                mobileUrl = voucher.merchantVoucherBanner?.mobileUrl ?: "",
                amount = voucher.availableAmount.toIntOrZero(),
                amountType = voucher.amountType ?: -1,
                identifier = voucher.identifier,
                voucherType = voucher.type ?: -1,
                isPublic = voucher.isPublic,
                isLockToProduct = voucher.isLockToProduct,
                applink = voucher.applink
        )
        val stringVoucherPreview = CommonUtil.toJson(voucherPreview)
        return Intent().apply {
            putExtra(ApplinkConst.AttachVoucher.PARAM_VOUCHER_PREVIEW, stringVoucherPreview)
        }
    }

    private fun disableAttachButton() {
        binding?.btnAttach?.isEnabled = false
    }

    private fun setupFilter() {
        binding?.let {
            it.filterCashBack.setOnClickListener {
                viewModel.toggleFilter(VoucherType.paramCashback)
            }
            it.filterFreeOngkir.setOnClickListener {
                viewModel.toggleFilter(VoucherType.paramFreeOngkir)
            }
        }
    }

    private fun setActiveFilter(selected: ChipsUnify?, deselected: ChipsUnify?) {
        selected?.chipType = ChipsUnify.TYPE_SELECTED
        deselected?.chipType = ChipsUnify.TYPE_ALTERNATE
    }

    private fun clearFilter() {
        binding?.let {
            it.filterCashBack.chipType = ChipsUnify.TYPE_ALTERNATE
            it.filterFreeOngkir.chipType = ChipsUnify.TYPE_ALTERNATE
        }
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    companion object {
        fun createInstance(): AttachVoucherFragment {
            return AttachVoucherFragment()
        }
    }
}