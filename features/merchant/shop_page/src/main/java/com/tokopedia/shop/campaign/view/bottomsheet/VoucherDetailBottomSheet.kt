package com.tokopedia.shop.campaign.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.shop.R
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.campaign.di.component.DaggerShopCampaignComponent
import com.tokopedia.shop.campaign.di.module.ShopCampaignModule
import com.tokopedia.shop.campaign.domain.entity.VoucherDetail
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import com.tokopedia.shop.campaign.view.viewmodel.VoucherDetailViewModel
import com.tokopedia.shop.common.extension.showToasterError
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.shop.campaign.util.tracker.VoucherDetailBottomSheetTracker
import com.tokopedia.shop.databinding.BottomsheetVoucherDetailBinding
import com.tokopedia.user.session.UserSessionInterface

class VoucherDetailBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_SHOP_ID = "shop_id"
        private const val BUNDLE_KEY_VOUCHER_SLUG = "voucher_slug"
        private const val BUNDLE_KEY_PROMO_VOUCHER_CODE = "promo_voucher_code"
        private const val BUNDLE_KEY_CAMPAIGN_ID = "campaign_id"
        private const val BUNDLE_KEY_WIDGET_ID = "widget_id"

        @JvmStatic
        fun newInstance(
            shopId: String,
            voucherSlug: String,
            promoVoucherCode: String,
            campaignId: String,
            widgetId: String
        ): VoucherDetailBottomSheet {
            return VoucherDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_SHOP_ID, shopId)
                    putString(BUNDLE_KEY_VOUCHER_SLUG, voucherSlug)
                    putString(BUNDLE_KEY_PROMO_VOUCHER_CODE, promoVoucherCode)
                    putString(BUNDLE_KEY_CAMPAIGN_ID, campaignId)
                    putString(BUNDLE_KEY_WIDGET_ID, widgetId)
                }
            }
        }
    }

    private var binding by autoClearedNullable<BottomsheetVoucherDetailBinding>()

    private var onVoucherRedeemSuccess: (RedeemPromoVoucherResult) -> Unit = {}
    private var onVoucherUseSuccess: () -> Unit = {}
    
    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
        showKnob = true
        showCloseIcon = false
        isDragable = true
        isHideable = true
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var tracker: VoucherDetailBottomSheetTracker

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[VoucherDetailViewModel::class.java] }

    private val shopId by lazy { arguments?.getString(BUNDLE_KEY_SHOP_ID).orEmpty() }
    private val voucherSlug by lazy { arguments?.getString(BUNDLE_KEY_VOUCHER_SLUG).orEmpty() }
    private val promoVoucherCode by lazy { arguments?.getString(BUNDLE_KEY_PROMO_VOUCHER_CODE).orEmpty() }
    private val campaignId by lazy { arguments?.getString(BUNDLE_KEY_CAMPAIGN_ID).orEmpty() }
    private val widgetId by lazy { arguments?.getString(BUNDLE_KEY_WIDGET_ID).orEmpty() }

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
                .inject(this@VoucherDetailBottomSheet)
        }
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BottomsheetVoucherDetailBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        isFullpage = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeVoucherDetail()
        observePromoVoucherRedeemResult()
        observeUseVoucherPromoResult()
        viewModel.getVoucherDetail(voucherSlug)
    }



    private fun setupView() {
        binding?.run {
            btnUsePromoVoucher.setOnClickListener {
                tracker.sendUseVoucherEvent(campaignId = campaignId, shopId = shopId)
                binding?.btnUsePromoVoucher?.startLoading()
                viewModel.usePromoVoucher(shopId, promoVoucherCode)
            }

            btnClaimPromoVoucher.setOnClickListener {
                binding?.btnClaimPromoVoucher?.startLoading()
                viewModel.claimPromoVoucher()
                tracker.sendRedeemVoucherEvent(campaignId = campaignId, shopId = shopId)
            }
            imgVoucher.cornerRadius = Int.ZERO
        }
    }


    private fun observeVoucherDetail() {
        viewModel.voucherDetail.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    displayVoucherDetail(result.data)
                    binding?.loader?.gone()
                    binding?.card?.visible()
                    binding?.content?.visible()
                }
                is Fail -> {
                    binding?.content?.gone()
                    binding?.loader?.gone()
                    binding?.card?.gone()
                    showToasterError(binding?.root ?: return@observe, result.throwable)
                }
            }
        }
    }

    private fun observePromoVoucherRedeemResult() {
        viewModel.redeemResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    binding?.btnClaimPromoVoucher?.stopLoading()
                    onVoucherRedeemSuccess(result.data)
                    dismiss()
                }
                is Fail -> {
                    binding?.btnClaimPromoVoucher?.stopLoading()
                    showToasterError(binding?.btnClaimPromoVoucher ?: return@observe, result.throwable)
                }
            }
        }
    }

    private fun observeUseVoucherPromoResult() {
        viewModel.useVoucherResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    binding?.btnUsePromoVoucher?.stopLoading()
                    onVoucherUseSuccess()
                    dismiss()
                }
                is Fail -> {
                    binding?.btnUsePromoVoucher?.stopLoading()

                    showToasterError(binding?.btnUsePromoVoucher ?: return@observe, result.throwable)
                }
            }
        }
    }

    private fun displayVoucherDetail(voucherDetail: VoucherDetail) {
        tracker.sendVoucherDetailBottomSheetImpression(
            campaignId = campaignId,
            widgetId = widgetId,
            shopId = shopId
        )

        binding?.run {
            imgVoucher.loadImage(voucherDetail.imageUrlMobile)

            tpgVoucherName.text = voucherDetail.title
            tpgMinPurchase.text = voucherDetail.minimumUsage
            tpgPromoPeriod.text = voucherDetail.expired
            tpgVoucherPrice.text = voucherDetail.voucherPrice

            webViewTnc.loadPartialWebView(voucherDetail.tnc)

            btnClaimPromoVoucher.text = voucherDetail.buttonLabel
            btnClaimPromoVoucher.isEnabled = !voucherDetail.isDisabledButton

            iconClock.isEnabled = !voucherDetail.isDisabledButton
            iconMinPurchase.isEnabled = !voucherDetail.isDisabledButton

        }

        handlePromoVoucher(promoVoucherCode)
    }

    private fun handlePromoVoucher(promoVoucherCode: String) {
        if (promoVoucherCode.isEmpty()) {
            showClaimPromoVoucherButton()
        } else {
            showUsePromoVoucherButton()
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

    private fun showUsePromoVoucherButton() {
        binding?.card?.visible()

        binding?.btnUsePromoVoucher?.visible()
        binding?.btnClaimPromoVoucher?.gone()

        binding?.tpgVoucherPrice?.gone()
    }

    private fun showClaimPromoVoucherButton() {
        binding?.card?.visible()

        binding?.btnUsePromoVoucher?.gone()
        binding?.btnClaimPromoVoucher?.visible()

        binding?.tpgVoucherPrice?.visible()
    }

    fun setOnVoucherRedeemSuccess(onVoucherRedeemSuccess : (RedeemPromoVoucherResult) -> Unit) {
        this.onVoucherRedeemSuccess = onVoucherRedeemSuccess
    }

    fun setOnVoucherUseSuccess(onVoucherUseSuccess : () -> Unit) {
        this.onVoucherUseSuccess = onVoucherUseSuccess
    }

}
