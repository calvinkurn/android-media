package com.tokopedia.shop.campaign.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.di.component.DaggerShopCampaignComponent
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.view.adapter.ExclusiveLaunchVoucherAdapter
import com.tokopedia.shop.campaign.view.viewmodel.ExclusiveLaunchVoucherListViewModel
import com.tokopedia.shop.databinding.BottomsheetExclusiveLaunchVoucherBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ExclusiveLaunchVoucherListBottomSheet: BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun newInstance(): ExclusiveLaunchVoucherListBottomSheet {
            return ExclusiveLaunchVoucherListBottomSheet()
        }
    }

    private var binding by autoClearedNullable<BottomsheetExclusiveLaunchVoucherBinding>()

    private val exclusiveLaunchAdapter = ExclusiveLaunchVoucherAdapter()
    private var onVoucherClaimSuccess : (ExclusiveLaunchVoucher) -> Unit = {}

    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[ExclusiveLaunchVoucherListViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        setCloseClickListener { dismiss() }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupDependencyInjection() {
        DaggerShopCampaignComponent.builder()
            .build()
            .inject(this)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BottomsheetExclusiveLaunchVoucherBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(getString(R.string.shop_page_exclusive_launch_voucher))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeVouchers()
    }

    private fun observeVouchers() {
        viewModel.vouchers.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    displayVouchers(result.data)
                    binding?.loader?.gone()
                }
                is Fail -> {
                    binding?.loader?.gone()
                }
            }
        }
    }

    private fun displayVouchers(data: List<ExclusiveLaunchVoucher>) {

    }

    private fun setupView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = exclusiveLaunchAdapter
        }

        exclusiveLaunchAdapter.setOnVoucherClick { selectedVoucher ->
            onVoucherClaimSuccess(selectedVoucher)
            dismiss()
        }
        exclusiveLaunchAdapter.setOnVoucherClaimClick { selectedVoucher ->
            showVoucherDetailBottomSheet(selectedVoucher)
        }
    }
    

    private fun showVoucherDetailBottomSheet(selectedVoucher: ExclusiveLaunchVoucher) {

    }

    fun setOnVoucherClaimSuccess(onVoucherClaimSuccess : (ExclusiveLaunchVoucher) -> Unit) {
        this.onVoucherClaimSuccess = onVoucherClaimSuccess
    }

}
