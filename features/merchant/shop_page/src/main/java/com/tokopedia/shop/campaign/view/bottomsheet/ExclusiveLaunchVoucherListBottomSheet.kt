package com.tokopedia.shop.campaign.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.campaign.di.component.DaggerShopCampaignComponent
import com.tokopedia.shop.campaign.di.module.ShopCampaignModule
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import com.tokopedia.shop.campaign.domain.entity.isMerchantVoucher
import com.tokopedia.shop.campaign.view.adapter.ExclusiveLaunchVoucherAdapter
import com.tokopedia.shop.campaign.view.viewmodel.ExclusiveLaunchVoucherListViewModel
import com.tokopedia.shop.common.extension.applyPaddingToLastItem
import com.tokopedia.shop.common.extension.showToaster
import com.tokopedia.shop.common.extension.showToasterError
import com.tokopedia.shop.databinding.BottomsheetExclusiveLaunchVoucherBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ExclusiveLaunchVoucherListBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_USE_DARK_BACKGROUND = "use_dark_background"
        private const val BUNDLE_KEY_VOUCHER_SLUGS = "category_slugs"

        @JvmStatic
        fun newInstance(
            useDarkBackground: Boolean,
            slugs: List<String>
        ): ExclusiveLaunchVoucherListBottomSheet {
            return ExclusiveLaunchVoucherListBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(BUNDLE_KEY_USE_DARK_BACKGROUND, useDarkBackground)
                    putStringArrayList(BUNDLE_KEY_VOUCHER_SLUGS, ArrayList(slugs))
                }
            }
        }
    }

    private var binding by autoClearedNullable<BottomsheetExclusiveLaunchVoucherBinding>()
    private val useDarkBackground by lazy { arguments?.getBoolean(BUNDLE_KEY_USE_DARK_BACKGROUND).orFalse() }
    private val voucherSlugs by lazy { arguments?.getStringArrayList(BUNDLE_KEY_VOUCHER_SLUGS)?.toList().orEmpty() }
    private val exclusiveLaunchAdapter = ExclusiveLaunchVoucherAdapter()
    private var onVoucherClaimSuccess: (ExclusiveLaunchVoucher) -> Unit = {}
    private var onVoucherUseSuccess: (ExclusiveLaunchVoucher) -> Unit = {}

    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

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
        viewModel.getExclusiveLaunchVouchers(voucherSlugs)
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
                    showToasterError(binding?.root ?: return@observe, result.throwable)
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
            setUseDarkBackground(useDarkBackground)
            setOnVoucherClick { selectedVoucherPosition ->
                val selectedVoucher = exclusiveLaunchAdapter.getItemAtOrNull(selectedVoucherPosition) ?: return@setOnVoucherClick
                showVoucherDetailBottomSheet(selectedVoucher)
            }
            setOnVoucherClaimClick { selectedVoucherPosition ->
                val selectedVoucher = exclusiveLaunchAdapter.getItemAtOrNull(selectedVoucherPosition) ?: return@setOnVoucherClaimClick
                showVoucherDetailBottomSheet(selectedVoucher)
            }
            setOnVoucherUseClick { selectedVoucherPosition ->
                val selectedVoucher = exclusiveLaunchAdapter.getItemAtOrNull(selectedVoucherPosition) ?: return@setOnVoucherUseClick
                showVoucherDetailBottomSheet(selectedVoucher)
            }
        }
    }

    private fun showVoucherDetailBottomSheet(selectedVoucher: ExclusiveLaunchVoucher) {
        if (!isAdded) return

        val promoVoucherCode = if (selectedVoucher.source is ExclusiveLaunchVoucher.VoucherSource.Promo) {
            selectedVoucher.source.voucherCode
        } else {
            ""
        }

        val isMerchantVoucher = selectedVoucher.isMerchantVoucher()
        val bottomSheet = VoucherDetailBottomSheet.newInstance(
            slug = selectedVoucher.slug,
            promoVoucherCode = promoVoucherCode,
            isMerchantVoucher = isMerchantVoucher
        ).apply {
            setOnVoucherRedeemSuccess { redeemResult ->
                handleRedeemVoucherSuccess(selectedVoucher, redeemResult)
            }
            setOnVoucherUseSuccess { handleUseVoucherSuccess(selectedVoucher) }
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun handleRedeemVoucherSuccess(
        selectedVoucher: ExclusiveLaunchVoucher,
        redeemResult: RedeemPromoVoucherResult
    ) {
        if (redeemResult.redeemMessage.isNotEmpty()){
            showToaster(view ?: return, redeemResult.redeemMessage)
            refreshVoucherStatus(selectedVoucher)
        }
    }

    private fun handleUseVoucherSuccess(selectedVoucher: ExclusiveLaunchVoucher) {
        onVoucherUseSuccess(selectedVoucher)
        dismiss()
    }

    private fun refreshVoucherStatus(selectedVoucher: ExclusiveLaunchVoucher) {
        val allVouchers = exclusiveLaunchAdapter.snapshot()
        val updatedVouchers = viewModel.updateVoucherAsClaimed(allVouchers, selectedVoucher)
        exclusiveLaunchAdapter.submit(updatedVouchers)
    }

    fun setOnVoucherUseSuccess(onVoucherUseSuccess: (ExclusiveLaunchVoucher) -> Unit) {
        this.onVoucherUseSuccess = onVoucherUseSuccess
    }

    fun setOnVoucherClaimSuccess(onVoucherClaimSuccess: (ExclusiveLaunchVoucher) -> Unit) {
        this.onVoucherClaimSuccess = onVoucherClaimSuccess
    }
}
