package com.tokopedia.shop.campaign.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.campaign.di.component.DaggerShopCampaignComponent
import com.tokopedia.shop.campaign.di.module.ShopCampaignModule
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.view.adapter.ExclusiveLaunchVoucherAdapter
import com.tokopedia.shop.campaign.view.viewmodel.ExclusiveLaunchVoucherListViewModel
import com.tokopedia.shop.common.extension.applyPaddingToLastItem
import com.tokopedia.shop.common.extension.showToasterError
import com.tokopedia.shop.databinding.BottomsheetExclusiveLaunchVoucherBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ExclusiveLaunchVoucherListBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun newInstance(): ExclusiveLaunchVoucherListBottomSheet {
            return ExclusiveLaunchVoucherListBottomSheet()
        }
    }

    private var binding by autoClearedNullable<BottomsheetExclusiveLaunchVoucherBinding>()

    private val exclusiveLaunchAdapter = ExclusiveLaunchVoucherAdapter()
    private var onVoucherClaim: (ExclusiveLaunchVoucher) -> Unit = {}
    private var onVoucherUse: (ExclusiveLaunchVoucher) -> Unit = {}
    private var onVoucherClick: (ExclusiveLaunchVoucher) -> Unit = {}

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
        activity?.run {
            DaggerShopCampaignComponent
                .builder()
                .shopCampaignModule(ShopCampaignModule())
                .shopComponent(ShopComponentHelper().getComponent(application, this))
                .build()
                .inject(this@ExclusiveLaunchVoucherListBottomSheet)
        }
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
        viewModel.getExclusiveLaunchVouchers()
    }

    private fun observeVouchers() {
        viewModel.vouchers.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    displayVouchers(result.data)
                    binding?.loader?.gone()
                }
                is Fail -> {
                    binding?.loader?.gone()
                    showToasterError(result.throwable)
                }
            }
        }
    }

    private fun displayVouchers(vouchers: List<ExclusiveLaunchVoucher>) {
        exclusiveLaunchAdapter.submit(vouchers)
    }

    private fun setupView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = exclusiveLaunchAdapter
            applyPaddingToLastItem()
        }

        exclusiveLaunchAdapter.apply {
            setUseDarkBackground(false)
            setOnVoucherClick { selectedVoucherPosition ->
                val selectedVoucher =
                    exclusiveLaunchAdapter.getItemAtOrNull(selectedVoucherPosition)
                        ?: return@setOnVoucherClick
                onVoucherClick(selectedVoucher)

                dismiss()
            }
            setOnVoucherClaimClick { selectedVoucherPosition ->
                val selectedVoucher =
                    exclusiveLaunchAdapter.getItemAtOrNull(selectedVoucherPosition)
                        ?: return@setOnVoucherClaimClick
                showVoucherDetailBottomSheet(selectedVoucher)
                //onVoucherClaim(selectedVoucher)

               // dismiss()
            }
            setOnVoucherUseClick { selectedVoucherPosition ->
                val selectedVoucher =
                    exclusiveLaunchAdapter.getItemAtOrNull(selectedVoucherPosition)
                        ?: return@setOnVoucherUseClick
                onVoucherUse(selectedVoucher)

                dismiss()
            }
        }
    }

    private fun showVoucherDetailBottomSheet(selectedVoucher: ExclusiveLaunchVoucher) {
        if (!isAdded) return
        val bottomSheet = PromoVoucherDetailBottomSheet.newInstance(categorySlug = selectedVoucher.categorySlug)

        bottomSheet.setOnVoucherRedeemSuccess { redeemResult ->
            dismiss()
        }
        bottomSheet.setOnVoucherRedeemFailed { throwable ->
            dismiss()
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    fun setOnVoucherUse(onVoucherUse: (ExclusiveLaunchVoucher) -> Unit) {
        this.onVoucherUse = onVoucherUse
    }

    fun setOnVoucherClaim(onVoucherClaim: (ExclusiveLaunchVoucher) -> Unit) {
        this.onVoucherClaim = onVoucherClaim
    }


}
