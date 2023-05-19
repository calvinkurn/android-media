package com.tokopedia.shop.campaign.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.shop.R
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.campaign.di.component.DaggerShopCampaignComponent
import com.tokopedia.shop.campaign.di.module.ShopCampaignModule
import com.tokopedia.shop.campaign.domain.entity.PromoVoucherDetail
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import com.tokopedia.shop.campaign.view.viewmodel.PromoVoucherDetailViewModel
import com.tokopedia.shop.common.extension.showToasterError
import com.tokopedia.shop.databinding.BottomsheetPromoVoucherDetailBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class PromoVoucherDetailBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_CATEGORY_SLUG = "category_slug"
        @JvmStatic
        fun newInstance(categorySlug : String): PromoVoucherDetailBottomSheet {
            return PromoVoucherDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_CATEGORY_SLUG, categorySlug)
                }
            }
        }
    }

    private var binding by autoClearedNullable<BottomsheetPromoVoucherDetailBinding>()

    private var onVoucherRedeemSuccess: (RedeemPromoVoucherResult) -> Unit = {}
    private var onVoucherRedeemFailed: (Throwable) -> Unit = {}

    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
        showKnob = true
        isFullpage = true
        showCloseIcon = false
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[PromoVoucherDetailViewModel::class.java] }
    private val categorySlug by lazy { arguments?.getString(BUNDLE_KEY_CATEGORY_SLUG).orEmpty() }

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
                .inject(this@PromoVoucherDetailBottomSheet)
        }
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BottomsheetPromoVoucherDetailBinding.inflate(inflater, container, false)
        setChild(binding?.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeVoucherDetail()
        observeRedeemVoucher()
        viewModel.getVoucherDetail(categorySlug)
    }

    private fun observeVoucherDetail() {
        viewModel.voucherDetail.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    displayVoucherDetail(result.data)
                    binding?.loader?.gone()
                    binding?.card?.visible()
                }
                is Fail -> {
                    binding?.loader?.gone()
                    binding?.card?.gone()
                    showToasterError(result.throwable)
                }
            }
        }
    }

    private fun observeRedeemVoucher() {
        viewModel.redeemResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    handleRedeemVoucherResult(result.data)
                    binding?.btnUseVoucher?.stopLoading()
                    onVoucherRedeemSuccess(result.data)
                }
                is Fail -> {
                    binding?.btnUseVoucher?.stopLoading()
                    showToasterError(result.throwable)
                    onVoucherRedeemFailed(result.throwable)
                }
            }
        }
    }

    private fun displayVoucherDetail(voucherDetail: PromoVoucherDetail) {
        binding?.run {
            tpgVoucherName.text = voucherDetail.title
            tpgMinPurchase.text = voucherDetail.minimumUsage
            tpgTnc.text = MethodChecker.fromHtml(voucherDetail.tnc)
            imgVoucher.loadImage(voucherDetail.imageUrlMobile)
            tpgMinPurchase.text = voucherDetail.minimumUsage
            tpgPromoPeriod.text = voucherDetail.activePeriodDate
            tpgHowToUse.text = MethodChecker.fromHtml(voucherDetail.howToUse)
            btnClaimVoucher.text = voucherDetail.buttonLabel
            tpgVoucherPrice.text = voucherDetail.voucherPrice
        }

        if (voucherDetail.isClaimed) {
            showUseVoucherButton()
        } else {
            showClaimVoucherButton()
        }
    }

    private fun handleRedeemVoucherResult(result: RedeemPromoVoucherResult) {
        showUseVoucherButton()
    }

    private fun setupView() {
        binding?.run {
            btnUseVoucher.setOnClickListener {
                binding?.btnUseVoucher?.startLoading()
                viewModel.redeemVoucher()
                dismiss()
            }
            imgVoucher.cornerRadius = Int.ZERO
        }
    }

    private fun UnifyButton.startLoading() {
        this.isLoading = true
        this.loadingText = context?.getString(R.string.shop_page_please_wait).orEmpty()
        this.isClickable = false
    }

    private fun UnifyButton.stopLoading() {
        this.isLoading = false
        this.loadingText = ""
        this.isClickable = true
    }

    private fun showUseVoucherButton() {
        binding?.card?.visible()

        binding?.btnUseVoucher?.visible()

        binding?.btnClaimVoucher?.gone()
        binding?.tpgVoucherPrice?.gone()
    }

    private fun showClaimVoucherButton() {
        binding?.card?.visible()

        binding?.btnUseVoucher?.gone()

        binding?.btnClaimVoucher?.visible()
        binding?.tpgVoucherPrice?.visible()
    }

    fun setOnVoucherRedeemSuccess(onVoucherRedeemSuccess : (RedeemPromoVoucherResult) -> Unit) {
        this.onVoucherRedeemSuccess = onVoucherRedeemSuccess
    }

    fun setOnVoucherRedeemFailed(onVoucherRedeemFailed : (Throwable) -> Unit) {
        this.onVoucherRedeemFailed = onVoucherRedeemFailed
    }
}
