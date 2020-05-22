package com.tokopedia.vouchercreation.create.view.fragment.step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherUrl
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.create.domain.model.CreateVoucherParam
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.domain.usecase.CreateVoucherUseCase
import com.tokopedia.vouchercreation.create.view.dialog.FailedCreateVoucherDialog
import com.tokopedia.vouchercreation.create.view.dialog.LoadingDialog
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.GeneralExpensesInfoBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.TermsAndConditionBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.VoucherDisplayBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.PostVoucherUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.ReviewVoucherViewModel
import com.tokopedia.vouchercreation.detail.model.*
import com.tokopedia.vouchercreation.detail.view.fragment.BaseDetailFragment
import java.text.SimpleDateFormat
import javax.inject.Inject

class ReviewVoucherFragment(private val getVoucherReviewUiModel: () -> VoucherReviewUiModel,
                            private val getToken: () -> String,
                            private val getIgPostVoucherUrl: () -> String,
                            private val onReturnToStep: (Int) -> Unit) : BaseDetailFragment() {

    companion object {
        @JvmStatic
        fun createInstance(getVoucherReviewUiModel: () -> VoucherReviewUiModel,
                           getToken: () -> String,
                           getIgPostVoucherUrl: () -> String,
                           onReturnToStep: (Int) -> Unit): ReviewVoucherFragment = ReviewVoucherFragment(getVoucherReviewUiModel, getToken, getIgPostVoucherUrl, onReturnToStep)

        private const val VOUCHER_INFO_DATA_KEY = "voucher_info"
        private const val VOUCHER_BENEFIT_DATA_KEY = "voucher_benefit"
        private const val PERIOD_DATA_KEY = "period"

        private const val VOUCHER_TIPS_INDEX = 1

        private const val DISPLAYED_DATE_FORMAT = "dd MMM yyyy"
        private const val RAW_DATE_FORMAT = "yyyy-MM-dd"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(ReviewVoucherViewModel::class.java)
    }

    private val termsAndConditionBottomSheet by lazy {
        context?.run {
            TermsAndConditionBottomSheetFragment.createInstance(this).apply {
                setCloseClickListener {
                    this.dismiss()
                }
            }
        }
    }

    private val generalExpenseBottomSheet by lazy {
        GeneralExpensesInfoBottomSheetFragment.createInstance(context)
    }

    private val publicVoucherTipsAndTrickBottomSheet by lazy {
        VoucherDisplayBottomSheetFragment.createInstance(context, ::getPublicVoucherDisplay)
    }

    private val failedCreateVoucherDialog by lazy {
        context?.run {
            FailedCreateVoucherDialog(this, ::onDialogTryAgain, ::onDialogRequestHelp)
        }
    }

    private val loadingDialog by lazy {
        context?.run {
            LoadingDialog(this)
        }
    }

    private val buttonUiModel by lazy {
        FooterButtonUiModel(context?.getString(R.string.mvc_add_voucher).toBlankOrString(), "")
    }

    private val rawDateFormatter by lazy {
        SimpleDateFormat(RAW_DATE_FORMAT, LocaleUtils.getIDLocale())
    }
    private val displayedDateFormatter by lazy {
        SimpleDateFormat(DISPLAYED_DATE_FORMAT, LocaleUtils.getIDLocale())
    }

    private var isWaitingForResult = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    override fun onResume() {
        super.onResume()
        renderReviewInformation(getVoucherReviewUiModel())
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {}

    override fun showDownloadBottomSheet() {}

    override fun showTipsAndTrickBottomSheet() {
        publicVoucherTipsAndTrickBottomSheet.run {
            setTitle(activity?.getString(R.string.mvc_create_public_voucher_display_title).toBlankOrString())
            show(this@ReviewVoucherFragment.childFragmentManager, VoucherDisplayBottomSheetFragment.TAG)
        }
    }

    override fun onInfoContainerCtaClick(dataKey: String) {
        val step = when(dataKey) {
            VOUCHER_INFO_DATA_KEY -> VoucherCreationStep.TARGET
            VOUCHER_BENEFIT_DATA_KEY -> VoucherCreationStep.BENEFIT
            PERIOD_DATA_KEY -> VoucherCreationStep.PERIOD
            else -> VoucherCreationStep.REVIEW
        }
        onReturnToStep(step)
    }

    override fun onFooterCtaTextClickListener() {
        termsAndConditionBottomSheet?.show(childFragmentManager, TermsAndConditionBottomSheetFragment.TAG)
    }

    override fun onTickerClicked() {
        generalExpenseBottomSheet.show(childFragmentManager, GeneralExpensesInfoBottomSheetFragment.TAG)
    }

    override fun onFooterButtonClickListener() {
        createVoucher()
    }

    private fun observeLiveData() {
        viewLifecycleOwner.observe(viewModel.createVoucherResponseLiveData) { result ->
            if (isWaitingForResult) {
                when(result) {
                    is Success -> {
                        if (result.data.status != CreateVoucherUseCase.STATUS_SUCCESS) {
                            failedCreateVoucherDialog?.show()
                        } else {
                            //Todo: Prompt to list page
                        }
                    }
                    is Fail -> {
                        failedCreateVoucherDialog?.show()
                    }
                }
                with(adapter) {
                    notifyItemChanged(data.indexOf(buttonUiModel))
                }
                loadingDialog?.dismiss()
            }
            isWaitingForResult = false
        }
    }

    private fun renderReviewInformation(voucherReviewUiModel: VoucherReviewUiModel) {
        val displayedDate =
                with(voucherReviewUiModel) {
                    getDisplayedDateString(startDate, startHour, endDate, endHour)
                }
        voucherReviewUiModel.run {
            val reviewInfoList = mutableListOf(
                    with(voucherReviewUiModel) {
                        getVoucherPreviewSection(voucherName, shopAvatarUrl, shopName, promoCode, displayedDate)
                    },
                    getVoucherInfoSection(targetType, voucherName, promoCode),
                    DividerUiModel(DividerUiModel.THIN),
                    getVoucherBenefitSection(voucherType, minPurchase, voucherQuota),
                    getExpenseEstimationSection(voucherType.value, voucherQuota),
                    DividerUiModel(DividerUiModel.THIN),
                    getPeriodSection(displayedDate),
                    DividerUiModel(DividerUiModel.THICK),
                    buttonUiModel,
                    FooterUiModel(
                            context?.getString(R.string.mvc_review_agreement).toBlankOrString(),
                            context?.getString(R.string.mvc_review_terms).toBlankOrString())
            )

            if (targetType == VoucherTargetType.PUBLIC) {
                context?.run {
                    val tipsUiModel = TipsUiModel(getString(R.string.mvc_detail_tips), getString(R.string.mvc_detail_tips_clickable))
                    reviewInfoList.add(VOUCHER_TIPS_INDEX, tipsUiModel)
                }
            }
            adapter.data.clear()
            renderList(reviewInfoList)
        }
    }

    private fun getDisplayedDateString(startDate: String,
                                       startHour: String,
                                       endDate: String,
                                       endHour: String): String {
        val formattedStartDate = startDate.formatToDisplayedDate()
        val formattedEndDate = endDate.formatToDisplayedDate()
        return String.format(context?.getString(R.string.mvc_displayed_date_format).toBlankOrString(),
                formattedStartDate, startHour, formattedEndDate, endHour)
    }

    private fun String.formatToDisplayedDate(): String {
        try {
            return displayedDateFormatter.format(rawDateFormatter.parse(this))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ""
    }

    private fun getVoucherPreviewSection(promoName: String,
                                         shopAvatar: String,
                                         shopName: String,
                                         promoCode: String,
                                         promoPeriod: String) : PostVoucherUiModel {
        return PostVoucherUiModel(
                VoucherImageType.FreeDelivery(0),
                promoName,
                shopAvatar,
                shopName,
                promoCode,
                promoPeriod,
                getIgPostVoucherUrl())
    }

    private fun getVoucherInfoSection(@VoucherTargetType targetType: Int,
                                      voucherName: String,
                                      promoCode: String) : InfoContainerUiModel {
        val targetTypeStringRes = when(targetType) {
            VoucherTargetType.PUBLIC -> R.string.mvc_create_target_public
            VoucherTargetType.PRIVATE -> R.string.mvc_create_target_private
            else -> R.string.mvc_create_target_public
        }
        val targetTypeString = context?.getString(targetTypeStringRes).toBlankOrString()

        val subInfoList = mutableListOf(
                SubInfoItemUiModel(R.string.mvc_voucher_target, targetTypeString),
                SubInfoItemUiModel(R.string.mvc_detail_voucher_name, voucherName)
        )
        if (targetType == VoucherTargetType.PRIVATE) {
            subInfoList.add(
                    SubInfoItemUiModel(R.string.mvc_detail_promo_code, promoCode)
            )
        }
        return InfoContainerUiModel(
                titleRes = R.string.mvc_detail_voucher_info,
                informationList = subInfoList,
                dataKey = VOUCHER_INFO_DATA_KEY,
                hasCta = true)
    }

    private fun getVoucherBenefitSection(voucherType: VoucherImageType,
                                         minPurchase: Int,
                                         voucherQuota: Int) : InfoContainerUiModel {
        val voucherTypeRes =
                when(voucherType) {
                    is VoucherImageType.FreeDelivery -> R.string.mvc_free_shipping
                    is VoucherImageType.Rupiah -> R.string.mvc_cashback
                    is VoucherImageType.Percentage -> R.string.mvc_cashback
                }
        val voucherTypeString = context?.getString(voucherTypeRes).toBlankOrString()
        val minPurchaseString = CurrencyFormatHelper.convertToRupiah(minPurchase.toString())
        val maxBenefitString = CurrencyFormatHelper.convertToRupiah(voucherType.value.toString())
        val quotaString = voucherQuota.toString()
        val purchaseTermString = String.format(context?.getString(R.string.mvc_detail_purchase_terms).toBlankOrString(), minPurchaseString, maxBenefitString)

        val subInfoList = mutableListOf(
                SubInfoItemUiModel(R.string.mvc_type_of_voucher, voucherTypeString)
        )
        if (voucherType is VoucherImageType.Percentage) {
            val discountPercentString = voucherType.percentage.toString()
            val discountAmountString = String.format(context?.getString(R.string.mvc_percent_value).toBlankOrString(), discountPercentString)
            subInfoList.add(SubInfoItemUiModel(R.string.mvc_detail_discount_amount, discountAmountString))
        }

        subInfoList.add(SubInfoItemUiModel(R.string.mvc_detail_quota, quotaString))
        subInfoList.add(SubInfoItemUiModel(R.string.mvc_detail_terms, purchaseTermString))

        return InfoContainerUiModel(
                titleRes = R.string.mvc_detail_voucher_benefit,
                informationList = subInfoList,
                dataKey = VOUCHER_BENEFIT_DATA_KEY,
                hasCta = true
        )
    }

    private fun getExpenseEstimationSection(benefitMax: Int,
                                            voucherQuota: Int) : VoucherTickerUiModel {
        val expenseEstimation = benefitMax * voucherQuota
        val formattedExpenseEstimation = CurrencyFormatHelper.convertToRupiah(expenseEstimation.toString())
        val expenseEstimationString = String.format(context?.getString(R.string.mvc_rp_value).toBlankOrString(), formattedExpenseEstimation)
        return VoucherTickerUiModel(
                nominalStr = expenseEstimationString,
                hasTooltip = true
        )
    }

    private fun getPeriodSection(dateString: String) : InfoContainerUiModel {
        val subInfoList = listOf(
                SubInfoItemUiModel(R.string.mvc_period, dateString)
        )

        return InfoContainerUiModel(
                titleRes = R.string.mvc_detail_voucher_period,
                informationList = subInfoList,
                dataKey = PERIOD_DATA_KEY,
                hasCta = true
        )
    }

    private fun getPublicVoucherDisplay() = VoucherTargetCardType.PUBLIC

    private fun createVoucher() {
        isWaitingForResult = true
        viewModel.createVoucher(
                CreateVoucherParam.mapToParam(
                        getVoucherReviewUiModel(), getToken()
                ))
    }

    private fun onDialogTryAgain() {
        failedCreateVoucherDialog?.dismiss()
        loadingDialog?.show()
        isWaitingForResult = true
        viewModel.createVoucher(
                CreateVoucherParam.mapToParam(
                        getVoucherReviewUiModel(), getToken()
                )
        )
    }

    private fun onDialogRequestHelp() {
        failedCreateVoucherDialog?.dismiss()
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, VoucherUrl.HELP_URL)
    }

}