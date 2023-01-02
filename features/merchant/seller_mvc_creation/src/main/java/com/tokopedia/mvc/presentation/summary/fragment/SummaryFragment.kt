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
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.getPercentFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentSummaryBinding
import com.tokopedia.mvc.databinding.SmvcFragmentSummaryPreviewBinding
import com.tokopedia.mvc.databinding.SmvcFragmentSummarySubmissionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailProductSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherInfoSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherSettingSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherTypeSectionBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.presentation.summary.helper.SummaryPageRedirectionHelper
import com.tokopedia.mvc.presentation.summary.viewmodel.SummaryViewModel
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.unifycomponents.toPx
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
        private const val VOUCHER_IMAGE_URL = "https://images.tokopedia.net/img/android/campaign/mvc/mvc_voucher.png"
        private const val UPLOAD_ERROR_IMAGE_URL = "https://images.tokopedia.net/img/android/campaign/merchant-voucher-creation/error_upload_coupon.png"

        @JvmStatic
        fun newInstance(
            pageMode: PageMode,
            voucherId: Long,
            voucherConfiguration: VoucherConfiguration?
        ): SummaryFragment {
            return SummaryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                    putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
                    putLong(BundleConstant.BUNDLE_VOUCHER_ID, voucherId)
                }
            }
        }
    }

    private var binding by autoClearedNullable<SmvcFragmentSummaryBinding>()
    private val pageMode by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val configuration by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration }
    private val voucherId by lazy { arguments?.getLong(BundleConstant.BUNDLE_VOUCHER_ID) }
    private val redirectionHelper = SummaryPageRedirectionHelper(this)

    @Inject
    lateinit var viewModel: SummaryViewModel

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
        println("ok")
    }

    override fun onViewProductResult() {
        println("ok view")
    }

    private fun setupPageMode() {
        if (pageMode == PageMode.EDIT) {
            viewModel.setupEditMode(voucherId ?: return)
        } else {
            viewModel.setConfiguration(configuration ?: return)
        }
    }

    private fun setupObservables() {
        viewModel.configuration.observe(viewLifecycleOwner) {
            binding?.apply {
                layoutType.updateLayoutType(it)
                layoutSetting.updatePageData(it)
                layoutProducts.updateProductData(it)
                layoutInfo.updatePageInfo(it)
            }
        }
        viewModel.maxExpense.observe(viewLifecycleOwner) {
            binding?.layoutSubmission?.labelSpendingEstimation
                ?.spendingEstimationText = it.getCurrencyFormatted()
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
        val drawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(greenDark, greenLight)
        )
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
    }

    private fun SmvcVoucherDetailVoucherInfoSectionBinding.setupLayoutInfo() {
        tpgEditAction.setOnClickListener {
            val configuration = viewModel.configuration.value
            onInformationCouponBtnChangeClicked(configuration ?: return@setOnClickListener)
        }
        tpgEditAction.visible()
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
                tpgVoucherNominal.text = benefitPercent.getCurrencyFormatted()
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
        information: VoucherConfiguration
    ) {
        val resources = root.context.resources
        with(information) {
            val targetItems = resources.getStringArray(R.array.target_items)
            tpgVoucherName.text = voucherName
            tpgVoucherCode.text = voucherCode
            tpgVoucherTarget.text = if (isVoucherPublic) {
                getString(R.string.smvc_voucher_public_label)
            } else {
                getString(R.string.smvc_voucher_private_label)
            }
            llVoucherCode.isVisible = isVoucherPublic
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

    private fun showErrorUploadDialog() {
        DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
            setImageUrl(UPLOAD_ERROR_IMAGE_URL)
            setTitle(context.getString(R.string.smvc_summary_page_error_dialog_title))
            setDescription(context.getString(R.string.smvc_summary_page_error_dialog_desc))
            setPrimaryCTAText(context.getString(R.string.smvc_summary_page_error_dialog_positive_action))
            setSecondaryCTAText(context.getString(R.string.smvc_summary_page_error_dialog_negative_action))
            setPrimaryCTAClickListener {
                dismiss()
            }
            setSecondaryCTAClickListener {
                dismiss()
                activity?.finish()
            }
            show()
        }
    }

    private fun onTypeCouponBtnChangeClicked(configuration: VoucherConfiguration) {
        // TODO: redirect to step 1
    }

    private fun onInformationCouponBtnChangeClicked(configuration: VoucherConfiguration) {
        // TODO: redirect to step 2
    }

    private fun onConfigurationCouponBtnChangeClicked(configuration: VoucherConfiguration) {
        // TODO: redirect to step 3
    }

    private fun onChangeProductBtnChangeClicked(configuration: VoucherConfiguration) {
        //TODO: Replace with real warehouseId
        redirectionHelper.redirectToAddProductPage(
            activity = activity ?: return,
            configuration = configuration,
            selectedWarehouseId = 0
        )
    }

    private fun onProductListBtnChangeClicked(configuration: VoucherConfiguration) {
        //TODO: Replace with real warehouseId
        redirectionHelper.redirectToViewProductPage(
            activity = activity ?: return,
            configuration = configuration,
            selectedProducts = listOf(),
            selectedWarehouseId = 0
        )
    }
}
