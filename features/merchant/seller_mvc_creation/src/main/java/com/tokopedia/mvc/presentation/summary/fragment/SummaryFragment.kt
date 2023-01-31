package com.tokopedia.mvc.presentation.summary.fragment

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst.SellerApp.TOPADS_HEADLINE_CREATE
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_MVC_LIST
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.getPercentFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.R
import com.tokopedia.mvc.common.util.SharedPreferencesUtil
import com.tokopedia.mvc.databinding.SmvcFragmentSummaryBinding
import com.tokopedia.mvc.databinding.SmvcFragmentSummaryPreviewBinding
import com.tokopedia.mvc.databinding.SmvcFragmentSummarySubmissionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailProductSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherInfoSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherSettingSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherTypeSectionBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.presentation.bottomsheet.ExpenseEstimationBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.SuccessUploadBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.displayvoucher.DisplayVoucherBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.voucherperiod.VoucherPeriodBottomSheet
import com.tokopedia.mvc.presentation.summary.helper.SummaryPageRedirectionHelper
import com.tokopedia.mvc.presentation.summary.viewmodel.SummaryViewModel
import com.tokopedia.mvc.util.SharingUtil
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.mvc.util.tracker.SummaryPageTracker
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.universal_sharing.view.bottomsheet.ClipboardHandler
import com.tokopedia.utils.date.DateUtil.DEFAULT_LOCALE
import com.tokopedia.utils.date.DateUtil.DEFAULT_VIEW_TIME_FORMAT
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.text.SimpleDateFormat
import javax.inject.Inject

class SummaryFragment :
    BaseDaggerFragment(),
    SummaryPageRedirectionHelper.SummaryPageResultListener {

    companion object {
        private const val CORNER_RADIUS_HEADER = 16
        private const val TNC_LINK = "https://www.tokopedia.com/help/seller/article/syarat-ketentuan-kupon-toko-saya"
        private const val UPLOAD_ERROR_IMAGE_URL = "https://images.tokopedia.net/img/android/campaign/merchant-voucher-creation/error_upload_coupon.png"

        @JvmStatic
        fun newInstance(
            pageMode: PageMode,
            voucherId: Long,
            voucherConfiguration: VoucherConfiguration?,
            selectedProducts: List<SelectedProduct>,
            enableDuplicateVoucher: Boolean
        ): SummaryFragment {
            return SummaryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                    putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
                    putParcelableArrayList(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS, ArrayList(selectedProducts))
                    putLong(BundleConstant.BUNDLE_VOUCHER_ID, voucherId)
                    putBoolean(BundleConstant.BUNDLE_KEY_ENABLE_DUPLICATE_VOUCHER, enableDuplicateVoucher)
                }
            }
        }
    }

    private var binding by autoClearedNullable<SmvcFragmentSummaryBinding>()
    private val pageMode by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val configuration by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration }
    private val selectedProducts by lazy { arguments?.getParcelableArrayList<SelectedProduct>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS).orEmpty() }
    private val voucherId by lazy { arguments?.getLong(BundleConstant.BUNDLE_VOUCHER_ID) }
    private val enableDuplicateVoucher by lazy { arguments?.getBoolean(BundleConstant.BUNDLE_KEY_ENABLE_DUPLICATE_VOUCHER).orFalse() }
    private val loadingDialog by lazy {
        context?.let {
            LoaderDialog(it)
        }
    }
    private val redirectionHelper = SummaryPageRedirectionHelper(this)

    @Inject
    lateinit var viewModel: SummaryViewModel
    @Inject
    lateinit var tracker: SummaryPageTracker

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentSummaryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        binding?.setupView()
        setupObservables()
        setupPageMode()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        redirectionHelper.onActivityResult(requestCode, resultCode, data)
    }

    override fun onAddProductResult() {
        activity?.finish()
    }

    override fun onViewProductResult() {
        activity?.finish()
    }

    override fun onVoucherTypePageResult() {
        activity?.finish()
    }

    private fun setupPageMode() {
        if (pageMode == PageMode.EDIT) {
            viewModel.setupEditMode(voucherId ?: return)
        } else {
            viewModel.setConfiguration(configuration ?: return)
            viewModel.updateProductList(selectedProducts)
        }
        if (enableDuplicateVoucher) {
            viewModel.setAsDuplicateCoupon()
        }
    }

    private fun setupObservables() {
        observeInitialData()
        observeUploadCouponState()
        observeMaxExpense()
        observeCouponImage()
    }

    private fun observeInitialData() {
        viewModel.configuration.observe(viewLifecycleOwner) {
            binding?.apply {
                layoutPreview.updateLayoutPreview(it)
                layoutType.updateLayoutType(it)
                layoutSetting.updatePageData(it)
                layoutProducts.updateProductData(it)
                layoutInfo.updatePageInfo(it)
            }
        }
        viewModel.enableCouponTypeChange.observe(viewLifecycleOwner) {
            binding?.layoutType?.apply {
                tpgEditAction.isEnabled = it
                tickerTypeEditingDisabled.isVisible = !it
            }
        }
        viewModel.submitButtonText.observe(viewLifecycleOwner) {
            binding?.layoutSubmission?.btnSubmit?.text = context?.getString(it).orEmpty()
        }
        viewModel.error.observe(viewLifecycleOwner) {
            view?.showToasterError(it)
        }
        viewModel.products.observe(viewLifecycleOwner) {
            binding?.layoutProducts?.tpgProductList?.text = getString(
                R.string.smvc_summary_page_product_format,
                it.size
            )
        }
    }

    private fun observeUploadCouponState() {
        viewModel.uploadCouponSuccess.observe(viewLifecycleOwner) {
            if (it.voucherId.isZero()) {
                showSuccessUploadBottomSheet(it)
            } else {
                context?.run {
                    val message = getString(R.string.smvc_summary_page_success_upload_message, it.voucherName)
                    SharedPreferencesUtil.setUploadResult(this, message)
                    RouteManager.route(this, SELLER_MVC_LIST)
                }
            }
        }
        viewModel.errorUpload.observe(viewLifecycleOwner) {
            showErrorUploadDialog(ErrorHandler.getErrorMessage(context, it))
        }
        viewModel.isInputValid.observe(viewLifecycleOwner) {
            binding?.layoutSubmission?.btnSubmit?.isEnabled = it
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                loadingDialog?.show()
            } else {
                loadingDialog?.dismiss()
            }
        }
    }

    private fun observeMaxExpense() {
        viewModel.maxExpense.observe(viewLifecycleOwner) {
            binding?.layoutSubmission?.labelSpendingEstimation
                ?.spendingEstimationText = it.getCurrencyFormatted()
        }
    }

    private fun observeCouponImage() {
        viewModel.couponImage.observe(viewLifecycleOwner) {
            binding?.layoutPreview?.ivPreview?.loadImage(it)
        }
    }

    private fun SmvcFragmentSummaryBinding.setupView() {
        header.setupHeader()
        layoutPreview.setupLayoutPreview()
        layoutType.setupLayoutType()
        layoutInfo.setupLayoutInfo()
        layoutSetting.setupLayoutSetting()
        layoutProducts.setupLayoutProducts()
        layoutSubmission.setupLayoutSubmission()
    }

    private fun HeaderUnify.setupHeader() {
        title = context.getString(R.string.smvc_summary_page_title)
        setNavigationOnClickListener {
            activity?.finish()
        }
    }

    private fun SmvcFragmentSummaryPreviewBinding.setupLayoutPreview() {
        val greenDark = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        val greenLight = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN200)
        val drawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(greenDark, greenLight))
        val corner = CORNER_RADIUS_HEADER.toPx().toFloat()
        drawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, corner, corner, corner, corner)
        viewBg.background = drawable
    }

    private fun SmvcVoucherDetailVoucherTypeSectionBinding.setupLayoutType() {
        tpgEditAction.setOnClickListener {
            val configuration = viewModel.configuration.value
            onTypeCouponBtnChangeClicked(configuration ?: return@setOnClickListener)
        }
        tpgEditAction.visible()
        tickerTypeEditingDisabled.setHtmlDescription(
            context?.getString(R.string.smvc_voucher_type_editing_disabled).orEmpty()
        )
    }

    private fun SmvcVoucherDetailVoucherInfoSectionBinding.setupLayoutInfo() {
        tpgEditAction.setOnClickListener {
            val configuration = viewModel.configuration.value
            onInformationCouponBtnChangeClicked(configuration ?: return@setOnClickListener)
        }
        tpgEditAction.visible()
        iconCopy.setOnClickListener {
            activity?.let { nonNullActivity ->
                ClipboardHandler().copyToClipboard(nonNullActivity, tpgVoucherCode.text.toString())
                Toaster.build(
                    requireView(),
                    context?.getString(R.string.smvc_voucherlist_copy_to_clipboard_message).toString(),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    context?.getString(R.string.smvc_ok).toString()
                ).show()
            }
        }
    }

    private fun SmvcVoucherDetailVoucherSettingSectionBinding.setupLayoutSetting() {
        tpgEditAction.setOnClickListener {
            val configuration = viewModel.configuration.value
            onConfigurationCouponBtnChangeClicked(configuration ?: return@setOnClickListener)
        }
        tpgEditAction.visible()
    }

    private fun SmvcVoucherDetailProductSectionBinding.setupLayoutProducts() {
        tpgEditAction.setOnClickListener {
            val configuration = viewModel.configuration.value
            onChangeProductBtnChangeClicked(configuration ?: return@setOnClickListener)
        }
        tpgEditAction.visible()
        tpgSeeProduct.setOnClickListener {
            val configuration = viewModel.configuration.value
            onProductListBtnChangeClicked(configuration ?: return@setOnClickListener)
        }
    }

    private fun SmvcFragmentSummarySubmissionBinding.setupLayoutSubmission() {
        tfTnc.text = MethodChecker.fromHtml(getString(R.string.smvc_summary_page_tnc_text))
        tfTnc.setOnClickListener {
            routeToUrl(TNC_LINK)
        }
        btnSubmit.setOnClickListener {
            viewModel.saveCoupon()
        }
        labelSpendingEstimation.iconInfo?.setOnClickListener {
            ExpenseEstimationBottomSheet.newInstance().show(childFragmentManager)
        }
        cbTnc.setOnClickListener {
            viewModel.validateTnc(cbTnc.isChecked)
        }
    }

    private fun SmvcVoucherDetailVoucherSettingSectionBinding.updatePageData(
        configuration: VoucherConfiguration
    ) {
        val resources = root.context.resources
        val promoTypeWordings = resources.getStringArray(R.array.promo_type_items)
        val promoBuyerWordings = resources.getStringArray(R.array.target_buyer_items)
        with(configuration) {
            tpgPromoType.text = promoTypeWordings.getOrNull(promoType.id.dec()).orEmpty()
            if (benefitType == BenefitType.NOMINAL) {
                tpgVoucherNominal.text = benefitIdr.getCurrencyFormatted()
                llVoucherMaxPriceDeduction.gone()
                tpgVoucherMaxPriceDeduction.text = benefitIdr.getCurrencyFormatted()
                tpgDeductionType.text = getString(R.string.smvc_summary_page_deduction_nominal_text)
            } else {
                tpgVoucherNominal.text = benefitPercent.getPercentFormatted()
                llVoucherMaxPriceDeduction.show()
                tpgVoucherMaxPriceDeduction.text = benefitMax.getCurrencyFormatted()
                tpgDeductionType.text = getString(R.string.smvc_summary_page_deduction_percentage_text)
            }
            tpgVoucherMinimumBuy.text = minPurchase.getCurrencyFormatted()
            tpgVoucherQuota.text = quota.toString()
            tpgVoucherTargetBuyer.text = promoBuyerWordings.getOrNull(targetBuyer.id).orEmpty()
            tpgVoucherNominalLabel.text = "${tpgDeductionType.text} ${tpgPromoType.text}"
        }
    }

    private fun SmvcVoucherDetailVoucherInfoSectionBinding.updatePageInfo(
        configuration: VoucherConfiguration
    ) {
        with(configuration) {
            tpgVoucherName.text = voucherName
            tpgVoucherCode.text = voucherCode
            tpgVoucherTarget.text = if (isVoucherPublic) {
                getString(R.string.smvc_voucher_public_label)
            } else {
                getString(R.string.smvc_voucher_private_label)
            }
            llVoucherCode.isVisible = !isVoucherPublic
            llVoucherMultiperiod.isVisible = isPeriod
            tpgVoucherMultiperiod.text = getString(R.string.smvc_summary_page_multiperiod_format, totalPeriod)
            tpgVoucherMultiperiodAction.setOnClickListener {
                onMultiPeriodClicked(this)
            }
            try {
                val formatter = SimpleDateFormat(DEFAULT_VIEW_TIME_FORMAT, DEFAULT_LOCALE)
                tpgVoucherStartPeriod.text = formatter.format(startPeriod)
                tpgVoucherEndPeriod.text = formatter.format(endPeriod)
            } catch (e: Exception) {
                tpgVoucherStartPeriod.gone()
                tpgVoucherEndPeriod.gone()
            }
        }
    }

    private fun SmvcVoucherDetailProductSectionBinding.updateProductData(configuration: VoucherConfiguration) {
        root.isVisible = configuration.isVoucherProduct
        tpgProductList.text = getString(
            R.string.smvc_summary_page_product_format,
            configuration.productIds.size
        )
    }

    private fun SmvcVoucherDetailVoucherTypeSectionBinding.updateLayoutType(configuration: VoucherConfiguration) {
        tpgVoucherType.text = if (configuration.isVoucherProduct) {
            getString(R.string.smvc_summary_page_product_coupon_text)
        } else {
            getString(R.string.smvc_summary_page_shop_coupon_text)
        }
    }

    private fun SmvcFragmentSummaryPreviewBinding.updateLayoutPreview(configuration: VoucherConfiguration) {
        cardPreview.setOnClickListener {
            DisplayVoucherBottomSheet
                .newInstance(configuration)
                .show(childFragmentManager, "")
        }
        viewModel.previewImage(
            isCreateMode = false,
            voucherConfiguration = configuration,
            parentProductIds = configuration.productIds,
            imageRatio = ImageRatio.SQUARE
        )
    }

    private fun showErrorUploadDialog(message: String) {
        DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
            setImageUrl(UPLOAD_ERROR_IMAGE_URL)
            setTitle(context.getString(R.string.smvc_summary_page_error_dialog_title))
            setDescription(context.getString(R.string.smvc_summary_page_error_dialog_desc, message))
            setPrimaryCTAText(context.getString(R.string.smvc_summary_page_error_dialog_positive_action))
            setSecondaryCTAText(context.getString(R.string.smvc_summary_page_error_dialog_negative_action))
            setPrimaryCTAClickListener {
                dismiss()
                viewModel.saveCoupon()
            }
            setSecondaryCTAClickListener {
                dismiss()
                activity?.finish()
            }
            show()
        }
    }

    private fun showSuccessUploadBottomSheet(configuration: VoucherConfiguration) {
        val bottomSheet = SuccessUploadBottomSheet
            .createInstance(configuration)
            .setOnAdsClickListener(::onSuccessBottomsheetAdsClick)
            .setOnBroadCastClickListener(::onSuccessBottomsheetBroadCastClick)
        bottomSheet.setOnDismissListener {
            RouteManager.route(context, SELLER_MVC_LIST)
            tracker.sendClickCloseEvent(configuration.voucherId.toString())
        }
        bottomSheet.show(childFragmentManager)
    }

    private fun onMultiPeriodClicked(configuration: VoucherConfiguration) {
        val voucherPeriodBottomSheet = VoucherPeriodBottomSheet.newInstance(
            title = context?.resources?.getString(R.string.voucher_bs_period_title_1).toBlankOrString(),
            dateList = viewModel.getOtherPeriod(configuration)
        )
        voucherPeriodBottomSheet.show(childFragmentManager, "")
    }

    private fun onTypeCouponBtnChangeClicked(configuration: VoucherConfiguration) {
        redirectionHelper.redirectToVoucherTypePage(this, configuration)
    }

    private fun onInformationCouponBtnChangeClicked(configuration: VoucherConfiguration) {
        val isAdding = viewModel.checkIsAdding(configuration)
        redirectionHelper.redirectToCouponInfoPage(this, configuration, isAdding)
    }

    private fun onConfigurationCouponBtnChangeClicked(configuration: VoucherConfiguration) {
        val isAdding = viewModel.checkIsAdding(configuration)
        redirectionHelper.redirectToCouponConfigurationPage(this, configuration, isAdding)
    }

    private fun onChangeProductBtnChangeClicked(configuration: VoucherConfiguration) {
        redirectionHelper.redirectToAddProductPage(
            this,
            configuration = configuration,
            selectedProducts = viewModel.products.value.orEmpty(),
            selectedWarehouseId = configuration.warehouseId
        )
    }

    private fun onProductListBtnChangeClicked(configuration: VoucherConfiguration) {
        redirectionHelper.redirectToViewProductPage(
            this,
            configuration = configuration,
            selectedProducts = viewModel.products.value.orEmpty(),
            selectedWarehouseId = configuration.warehouseId
        )
    }

    private fun onSuccessBottomsheetBroadCastClick(voucherConfiguration: VoucherConfiguration) {
        context?.let { SharingUtil.shareToBroadCastChat(it, voucherConfiguration.voucherId) }
        tracker.sendClickBroadcastPopUpEvent()
    }

    private fun onSuccessBottomsheetAdsClick(voucherConfiguration: VoucherConfiguration) {
        RouteManager.route(context, TOPADS_HEADLINE_CREATE)
        tracker.sendClickTopadsPopUpEvent()
    }
}
