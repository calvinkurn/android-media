package com.tokopedia.mvc.presentation.summary.fragment

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
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
import com.tokopedia.mvc.domain.entity.VoucherInformation
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.presentation.summary.viewmodel.SummaryViewModel
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.date.DateUtil.DEFAULT_LOCALE
import com.tokopedia.utils.date.DateUtil.DEFAULT_VIEW_TIME_FORMAT
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.text.SimpleDateFormat
import javax.inject.Inject

class SummaryFragment: BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(
            pageMode: PageMode?,
            voucherId: String?,
            voucherConfiguration: VoucherConfiguration?,
        ): SummaryFragment {
            return SummaryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                    putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
                    putString(BundleConstant.BUNDLE_VOUCHER_ID, voucherId)
                }
            }
        }
    }

    private var binding by autoClearedNullable<SmvcFragmentSummaryBinding>()
    private val pageMode by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val configuration by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration }
    private val voucherId by lazy { arguments?.getString(BundleConstant.BUNDLE_VOUCHER_ID) }

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

    private fun setupPageMode() {
        if (pageMode == PageMode.EDIT) {
            viewModel.setupEditMode(voucherId ?: return)
        } else {
            viewModel.setConfiguration(configuration ?: return)
        }
    }

    private fun setupObservables() {
        viewModel.configuration.observe(viewLifecycleOwner) {
            binding?.layoutSetting?.updatePageData(it)
        }
        viewModel.information.observe(viewLifecycleOwner) {
            binding?.layoutInfo?.updatePageInfo(it)
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
        val green2 = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        val green = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN200)
        val gd = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(green2, green)
        )
        val corner = 16.toPx().toFloat()
        gd.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, corner, corner, corner, corner)
        viewBg.background = gd
    }

    private fun SmvcVoucherDetailVoucherTypeSectionBinding.setupLayoutType() {
        tpgEditAction.visible()
    }

    private fun SmvcVoucherDetailVoucherInfoSectionBinding.setupLayoutInfo() {
        tpgEditAction.visible()
    }

    private fun SmvcVoucherDetailVoucherSettingSectionBinding.setupLayoutSetting() {
        tpgEditAction.visible()
    }

    private fun SmvcVoucherDetailProductSectionBinding.setupLayoutProducts() {
        tpgEditAction.visible()
    }

    private fun SmvcFragmentSummarySubmissionBinding.setupLayoutSubmission() {
        cbTnc.text = MethodChecker.fromHtml(getString(R.string.smvc_summary_page_tnc_text))
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
            tpgVoucherNominalLabel.text = tpgDeductionType.text.toString() + " " + tpgPromoType.text
        }
    }

    private fun SmvcVoucherDetailVoucherInfoSectionBinding.updatePageInfo(
        information: VoucherInformation
    ) {
        val resources = root.context.resources
        with(information) {
            val targetItems = resources.getStringArray(R.array.target_items)
            val formatter = SimpleDateFormat(DEFAULT_VIEW_TIME_FORMAT, DEFAULT_LOCALE)
            tpgVoucherName.text = voucherName
            tpgVoucherCode.text = code
            tpgVoucherTarget.text = targetItems.getOrNull(target.ordinal)
            llVoucherCode.isVisible = target == VoucherTarget.PRIVATE
            tpgVoucherStartPeriod.text = formatter.format(startPeriod)
            tpgVoucherEndPeriod.text = formatter.format(endPeriod)
        }


    }
}
