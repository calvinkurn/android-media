package com.tokopedia.mvc.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.SellerApp.SELLER_MVC_LIST_HISTORY
import com.tokopedia.applink.RouteManager
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.campaign.utils.extension.showToaster
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.campaign.utils.extension.startLoading
import com.tokopedia.campaign.utils.extension.stopLoading
import com.tokopedia.campaign.utils.extension.toDate
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.getPercentFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toCalendar
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.R
import com.tokopedia.mvc.common.util.SharedPreferencesUtil
import com.tokopedia.mvc.databinding.SmvcFragmentVoucherDetailBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailButtonSectionState1Binding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailButtonSectionState2Binding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailButtonSectionState3Binding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailHeaderSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailProductSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherInfoSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherSettingSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherTypeSectionBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.GenerateVoucherImageMetadata
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.presentation.bottomsheet.ExpenseEstimationBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.moremenu.MoreMenuBottomSheet
import com.tokopedia.mvc.presentation.download.DownloadVoucherImageBottomSheet
import com.tokopedia.mvc.presentation.list.dialog.CallTokopediaCareDialog
import com.tokopedia.mvc.presentation.list.dialog.StopVoucherConfirmationDialog
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel
import com.tokopedia.mvc.presentation.product.list.ProductListActivity
import com.tokopedia.mvc.presentation.share.LinkerDataGenerator
import com.tokopedia.mvc.presentation.share.ShareComponentInstanceBuilder
import com.tokopedia.mvc.presentation.share.ShareCopyWritingGenerator
import com.tokopedia.mvc.presentation.summary.SummaryActivity
import com.tokopedia.mvc.util.SharingUtil
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.mvc.util.constant.ImageUrlConstant
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.mvc.util.constant.VoucherTargetConstant.VOUCHER_TARGET_PUBLIC
import com.tokopedia.mvc.util.tracker.ShareBottomSheetTracker
import com.tokopedia.mvc.util.tracker.VoucherDetailTracker
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.constants.BroadcastChannelType
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil as ShareComponentUtil

class VoucherDetailFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(voucherId: Long): VoucherDetailFragment {
            return VoucherDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(BundleConstant.BUNDLE_VOUCHER_ID, voucherId)
                }
            }
        }

        private const val TRUE = 1
        private const val FALSE = 0
        private const val COPY_PROMO_CODE_LABEL = "promo_code"
        private const val broadCastChatUrl =
            "https://m.tokopedia.com/broadcast-chat/create/content?voucher_id="
        private const val tncUrl =
            "https://www.tokopedia.com/help/seller/article/syarat-ketentuan-kupon-toko-saya"
        private const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"
        private const val TOKOPEDIA_CARE_PATH = "help"
    }

    // binding
    private var binding by autoClearedNullable<SmvcFragmentVoucherDetailBinding>()
    private var headerBinding by autoClearedNullable<SmvcVoucherDetailHeaderSectionBinding>()
    private var voucherTypeBinding by autoClearedNullable<SmvcVoucherDetailVoucherTypeSectionBinding>()
    private var voucherInfoBinding by autoClearedNullable<SmvcVoucherDetailVoucherInfoSectionBinding>()
    private var voucherSettingBinding by autoClearedNullable<SmvcVoucherDetailVoucherSettingSectionBinding>()
    private var voucherProductBinding by autoClearedNullable<SmvcVoucherDetailProductSectionBinding>()
    private var stateButtonBroadCastBinding by autoClearedNullable<SmvcVoucherDetailButtonSectionState1Binding>()
    private var stateButtonShareBinding by autoClearedNullable<SmvcVoucherDetailButtonSectionState2Binding>()
    private var stateButtonDuplicateBinding by autoClearedNullable<SmvcVoucherDetailButtonSectionState3Binding>()
    private var moreMenuBottomSheet: MoreMenuBottomSheet? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var shareComponentInstanceBuilder: ShareComponentInstanceBuilder

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var shareCopyWritingGenerator: ShareCopyWritingGenerator

    // bottomsheet
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null

    // dialog
    private var stopVoucherDialog: StopVoucherConfirmationDialog? = null

    // tracker
    @Inject
    lateinit var voucherDetailTracker: VoucherDetailTracker

    @Inject
    lateinit var tracker: ShareBottomSheetTracker

    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(VoucherDetailViewModel::class.java) }

    private val voucherId by lazy {
        arguments?.getLong(BundleConstant.BUNDLE_VOUCHER_ID).orZero()
    }

    override fun getScreenName(): String =
        VoucherDetailFragment::class.java.canonicalName.orEmpty()

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
        binding = SmvcFragmentVoucherDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        observeGenerateVoucherImageResult()
        observeOpenVoucherImageBottomSheetEvent()
        observeRedirectionToProductListPage()
        setupStopConfirmationDialog()
        getVoucherDetailData(voucherId)
    }

    private fun observeData() {
        viewModel.voucherDetail.observe(viewLifecycleOwner) { result ->
            hideLoading()
            when (result) {
                is Success -> {
                    setupView(result.data)
                }
                is Fail -> {
                    showGlobalError()
                }
            }
        }

        viewModel.updateVoucherStatusData.observe(viewLifecycleOwner) { result ->
            hideLoading()
            when (result) {
                is Success -> {
                    redirectToVoucherListPage()
                }
                is Fail -> {
                    binding?.layoutButtonGroup.showToasterError(result.throwable)
                }
            }
        }
    }

    private fun observeGenerateVoucherImageResult() {
        viewModel.generateVoucherImageMetadata.observe(viewLifecycleOwner) { result ->
            stateButtonShareBinding?.btnShare.stopLoading()
            when (result) {
                is Success -> {
                    displayShareBottomSheet(result.data)
                }
                is Fail -> {
                    binding?.nsvContent.showToasterError(result.throwable)
                }
            }
        }
    }

    private fun observeRedirectionToProductListPage() {
        viewModel.redirectToProductListPage.observe(viewLifecycleOwner) { voucherDetail ->
            redirectToProductListPage(voucherDetail)
        }
    }

    private fun observeOpenVoucherImageBottomSheetEvent() {
        viewModel.openDownloadVoucherImageBottomSheet.observe(viewLifecycleOwner) { voucherDetail ->
            displayDownloadVoucherImageBottomSheet(voucherDetail)
        }
    }

    private fun setupView(data: VoucherDetailData) {
        binding?.run {
            layoutHeader.setOnInflateListener { _, view ->
                headerBinding = SmvcVoucherDetailHeaderSectionBinding.bind(view)
            }
            layoutVoucherType.setOnInflateListener { _, view ->
                voucherTypeBinding = SmvcVoucherDetailVoucherTypeSectionBinding.bind(view)
            }
            layoutVoucherInfo.setOnInflateListener { _, view ->
                voucherInfoBinding = SmvcVoucherDetailVoucherInfoSectionBinding.bind(view)
            }
            layoutVoucherSetting.setOnInflateListener { _, view ->
                voucherSettingBinding = SmvcVoucherDetailVoucherSettingSectionBinding.bind(view)
            }
            layoutProductList.setOnInflateListener { _, view ->
                voucherProductBinding = SmvcVoucherDetailProductSectionBinding.bind(view)
            }
        }
        setupHeaderSection(data)
        setupVoucherTypeSection(data)
        setupVoucherInfoSection(data)
        setupVoucherSettingSection(data)
        setupProductListSection(data)
        setupSpendingEstimationSection(data)
        setupButtonSection(data)
    }

    private fun setupHeaderSection(data: VoucherDetailData) {
        binding?.run {
            header.headerTitle = data.voucherName
            header.setNavigationOnClickListener {
                voucherDetailTracker.sendClickArrowBackEvent(data)
                activity?.finish()
            }
            if (layoutHeader.parent != null) {
                layoutHeader.inflate()
            }
        }
        headerBinding?.run {
            setupVoucherStatus(data)
            setupVoucherAction(data)
            imgVoucher.setImageUrl(data.voucherImageSquare)
            tpgDateTimeStart.dateTime = data.voucherStartTime
            tpgDateTimeEnd.dateTime = data.voucherFinishTime
            setPackage(data)
            val availableQuota = data.voucherQuota - data.remainingQuota
            val usedQuotaPercentage = viewModel.getPercentage(availableQuota, data.remainingQuota)
            tpgUsedVoucherQuota.text = availableQuota.toString()
            tpgAvailableVoucherQuota.text = getString(
                R.string.smvc_placeholder_voucher_quota,
                data.remainingQuota
            )
            pgbUsedVoucher.setValue(usedQuotaPercentage, true)
            btnDownloadImageVoucher.apply {
                isVisible =
                    data.voucherStatus == VoucherStatus.NOT_STARTED || data.voucherStatus == VoucherStatus.ONGOING
                setOnClickListener {
                    viewModel.onTapDownloadVoucherImage()
                    voucherDetailTracker.sendClickDownloadEvent(data)
                }
            }
        }
    }

    private fun setPackage(data: VoucherDetailData) {
        headerBinding?.run {
            if (data.isVps == FALSE && data.isSubsidy == FALSE) {
                labelVoucherSource.invisible()
                tpgVpsPackage.gone()
            } else {
                setPackageName(data)
            }
        }
    }

    private fun setPackageName(data: VoucherDetailData) {
        headerBinding?.run {
            if (data.isVps == TRUE) {
                labelVoucherSource.apply {
                    visible()
                    text = getString(R.string.smvc_promotion_package_label)
                }
                tpgVpsPackage.apply {
                    visible()
                    text = data.packageName
                }
            } else {
                labelVoucherSource.apply {
                    visible()
                    text = getString(R.string.smvc_from_tokopedia_label)
                }
                tpgVpsPackage.gone()
            }
        }
    }

    private fun setupVoucherStatus(data: VoucherDetailData) {
        headerBinding?.run {
            when (data.voucherStatus) {
                VoucherStatus.NOT_STARTED -> {
                    tpgVoucherStatus.apply {
                        text = getString(R.string.smvc_status_upcoming_label)
                        setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                    }
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREY)
                }
                VoucherStatus.ONGOING -> {
                    tpgVoucherStatus.apply {
                        text = getString(R.string.smvc_status_ongoing_label)
                        setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Green_G500)
                    }
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREEN)
                }
                VoucherStatus.STOPPED -> {
                    tpgVoucherStatus.apply {
                        text = getString(R.string.smvc_status_stopped_label)
                        setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Red_R500)
                    }
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_RED)
                }
                else -> {
                    tpgVoucherStatus.apply {
                        text = getString(R.string.smvc_status_ended_label)
                        setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                    }
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREY)
                }
            }
        }
    }

    private fun setupVoucherAction(data: VoucherDetailData) {
        headerBinding?.run {
            when (data.voucherStatus) {
                VoucherStatus.NOT_STARTED -> {
                    btnUbahKupon.visible()
                    timer.invisible()
                    tpgPeriodStop.invisible()
                }
                VoucherStatus.ONGOING -> {
                    btnUbahKupon.invisible()
                    timer.visible()
                    tpgPeriodStop.invisible()
                    startTimer(data.voucherFinishTime.toDate(DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601))
                }
                VoucherStatus.ENDED -> {
                    btnUbahKupon.invisible()
                    timer.invisible()
                    tpgPeriodStop.invisible()
                }
                else -> {
                    val stoppedDate =
                        data.updateTime.toDate(DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601)
                    btnUbahKupon.invisible()
                    timer.invisible()
                    tpgPeriodStop.apply {
                        visible()
                        text = getString(
                            R.string.smvc_placeholder_stopped_date,
                            stoppedDate.formatTo(DateConstant.DATE_YEAR_PRECISION)
                        )
                    }
                }
            }
            if (data.isVps == TRUE || data.isSubsidy == TRUE || data.isChild == TRUE) {
                btnUbahKupon.invisible()
            }
            btnUbahKupon.setOnClickListener {
                val intent = SummaryActivity.buildEditModeIntent(context, data.voucherId)
                startActivity(intent)
                voucherDetailTracker.sendClickEditEvent(data)
            }
        }
    }

    private fun startTimer(endDate: Date) {
        headerBinding?.run {
            timer.timerFormat = TimerUnifySingle.FORMAT_AUTO
            timer.timerVariant = TimerUnifySingle.VARIANT_INFORMATIVE
            timer.targetDate = endDate.toCalendar()
        }
    }

    private fun setupVoucherTypeSection(data: VoucherDetailData) {
        binding?.run {
            if (layoutVoucherType.parent != null) {
                layoutVoucherType.inflate()
            }
        }
        voucherTypeBinding?.run {
            tpgVoucherType.text = if (data.isVoucherProduct) {
                getString(R.string.smvc_voucher_product_label)
            } else {
                getString(R.string.smvc_voucher_store_label)
            }
        }
    }

    private fun setupVoucherInfoSection(data: VoucherDetailData) {
        binding?.run {
            if (layoutVoucherInfo.parent != null) {
                layoutVoucherInfo.inflate()
            }
        }
        voucherInfoBinding?.run {
            if (data.isPublic == VOUCHER_TARGET_PUBLIC) {
                tpgVoucherTarget.text = getString(R.string.smvc_voucher_public_label)
                llVoucherCode.gone()
            } else {
                tpgVoucherTarget.text = getString(R.string.smvc_voucher_private_label)
                llVoucherCode.visible()
            }
            tpgVoucherName.text = data.voucherName.ifEmpty {
                getString(R.string.smvc_dash_label)
            }
            tpgVoucherCode.text = data.voucherCode.ifEmpty {
                getString(R.string.smvc_dash_label)
            }
            iconCopy.apply {
                isVisible = data.voucherCode.isNotEmpty()
                setOnClickListener {
                    copyVoucherCode(data.voucherCode)
                }
            }
        }
        setVoucherInfoDate(data)
    }

    private fun setVoucherInfoDate(data: VoucherDetailData) {
        voucherInfoBinding?.run {
            val startPeriodDate = data.voucherStartTime.toDate(
                DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601
            )
            val endPeriodDate = data.voucherFinishTime.toDate(
                DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601
            )
            tpgVoucherStartPeriod.text =
                startPeriodDate.formatTo(DateConstant.DATE_YEAR_WITH_TIME)
            tpgVoucherEndPeriod.text = endPeriodDate.formatTo(DateConstant.DATE_YEAR_WITH_TIME)
        }
    }

    private fun setupVoucherSettingSection(data: VoucherDetailData) {
        binding?.run {
            if (layoutVoucherSetting.parent != null) {
                layoutVoucherSetting.inflate()
            }
        }

        voucherSettingBinding?.run {
            tpgPromoType.setPromoType(data.voucherType)
            setDeductionType(data)
            setDeductionAmount(data)
            setMaxPriceDeduction(data)
            tpgVoucherQuota.text = data.voucherQuota.toString()
            tpgVoucherMinimumBuy.text = data.voucherDiscountAmountMin.getCurrencyFormatted()
            tpgVoucherTargetBuyer.setTargetBuyer(data)
        }
    }

    private fun Typography.setPromoType(voucherType: PromoType) {
        this.text = when (voucherType) {
            PromoType.FREE_SHIPPING -> getString(R.string.smvc_free_shipping_label)
            PromoType.DISCOUNT -> getString(R.string.smvc_discount_label)
            else -> getString(R.string.smvc_cashback_label)
        }
    }

    private fun setDeductionType(data: VoucherDetailData) {
        voucherSettingBinding?.run {
            llDeductionType.visibility = when (data.voucherType) {
                PromoType.FREE_SHIPPING -> View.GONE
                else -> View.VISIBLE
            }
            tpgDeductionType.text = when (data.voucherDiscountType) {
                BenefitType.NOMINAL -> getString(R.string.smvc_nominal_label)
                else -> getString(R.string.smvc_percentage_label)
            }
        }
    }

    private fun setDeductionAmount(data: VoucherDetailData) {
        voucherSettingBinding?.run {
            when (data.voucherType) {
                PromoType.FREE_SHIPPING -> {
                    tpgVoucherNominalLabel.text =
                        getString(R.string.smvc_nominal_free_shipping_label)
                    tpgVoucherNominal.text = data.voucherDiscountAmount.getCurrencyFormatted()
                }
                PromoType.CASHBACK -> {
                    when (data.voucherDiscountType) {
                        BenefitType.NOMINAL -> {
                            tpgVoucherNominalLabel.text =
                                getString(R.string.smvc_nominal_cashback_label)
                            tpgVoucherNominal.text =
                                data.voucherDiscountAmount.getCurrencyFormatted()
                        }
                        else -> {
                            tpgVoucherNominalLabel.text =
                                getString(R.string.smvc_percentage_cashback_label)
                            tpgVoucherNominal.text =
                                data.voucherDiscountAmount.getPercentFormatted()
                        }
                    }
                }
                else -> {
                    when (data.voucherDiscountType) {
                        BenefitType.NOMINAL -> {
                            tpgVoucherNominalLabel.text =
                                getString(R.string.smvc_nominal_discount_label)
                            tpgVoucherNominal.text =
                                data.voucherDiscountAmount.getCurrencyFormatted()
                        }
                        else -> {
                            tpgVoucherNominalLabel.text =
                                getString(R.string.smvc_percentage_discount_label)
                            tpgVoucherNominal.text =
                                data.voucherDiscountAmount.getPercentFormatted()
                        }
                    }
                }
            }
        }
    }

    private fun setMaxPriceDeduction(data: VoucherDetailData) {
        voucherSettingBinding?.run {
            when (data.voucherType) {
                PromoType.FREE_SHIPPING -> {
                    llVoucherMaxPriceDeduction.gone()
                }
                else -> {
                    when (data.voucherDiscountType) {
                        BenefitType.PERCENTAGE -> {
                            llVoucherMaxPriceDeduction.visible()
                            tpgVoucherMaxPriceDeduction.text =
                                data.voucherDiscountAmountMax.getCurrencyFormatted()
                        }
                        else -> {
                            llVoucherMaxPriceDeduction.gone()
                        }
                    }
                }
            }
        }
    }

    private fun Typography.setTargetBuyer(data: VoucherDetailData) {
        this.text = when (data.targetBuyer) {
            VoucherTargetBuyer.ALL_BUYER -> getString(R.string.smvc_all_buyer_label)
            VoucherTargetBuyer.NEW_FOLLOWER -> getString(R.string.smvc_new_follower_label)
            VoucherTargetBuyer.NEW_BUYER -> getString(R.string.smvc_new_user_label)
            else -> getString(R.string.smvc_member_label)
        }
    }

    private fun setupProductListSection(data: VoucherDetailData) {
        if (data.isVoucherProduct) {
            binding?.run {
                if (layoutProductList.parent != null) {
                    layoutProductList.inflate()
                }
            }
            voucherProductBinding?.run {
                tpgProductList.text = getString(
                    R.string.smvc_product_count_placeholder,
                    data.productIds.count()
                )
                tpgSeeProduct.setOnClickListener { viewModel.onTapViewAllProductCta() }
            }
        }
    }

    private fun setupSpendingEstimationSection(data: VoucherDetailData) {
        val spendingEstimation = viewModel.getSpendingEstimation(data)
        val title = when (data.voucherStatus) {
            VoucherStatus.ONGOING -> getString(R.string.smvc_spending_estimation_title_2)
            VoucherStatus.ENDED -> getString(R.string.smvc_spending_estimation_title_3)
            else -> getString(R.string.smvc_spending_estimation_title_1)
        }
        val description = when {
            data.isVps == TRUE -> getString(R.string.smvc_spending_estimation_description_2)
            data.isSubsidy == TRUE -> getString(R.string.smvc_spending_estimation_description_3)
            else -> getString(R.string.smvc_spending_estimation_description_1)
        }
        binding?.run {
            labelSpendingEstimation.apply {
                titleText = title
                descriptionText = description
                spendingEstimationText = spendingEstimation
                iconInfo?.setOnClickListener {
                    ExpenseEstimationBottomSheet.newInstance().show(childFragmentManager)
                }
            }
        }
    }

    private fun setupButtonSection(data: VoucherDetailData) {
        binding?.run {
            when (data.voucherStatus) {
                VoucherStatus.NOT_STARTED -> {
                    layoutButton.setOnInflateListener { _, view ->
                        stateButtonBroadCastBinding =
                            SmvcVoucherDetailButtonSectionState1Binding.bind(view)
                    }
                    layoutButton.layoutResource =
                        R.layout.smvc_voucher_detail_button_section_state_1
                    if (layoutButton.parent != null) {
                        layoutButton.inflate()
                    }
                }
                VoucherStatus.ONGOING -> {
                    layoutButton.setOnInflateListener { _, view ->
                        stateButtonShareBinding =
                            SmvcVoucherDetailButtonSectionState2Binding.bind(view)
                    }
                    layoutButton.layoutResource =
                        R.layout.smvc_voucher_detail_button_section_state_2
                    if (layoutButton.parent != null) {
                        layoutButton.inflate()
                    }
                }
                else -> {
                    layoutButton.setOnInflateListener { _, view ->
                        stateButtonDuplicateBinding =
                            SmvcVoucherDetailButtonSectionState3Binding.bind(view)
                    }
                    layoutButton.layoutResource =
                        R.layout.smvc_voucher_detail_button_section_state_3
                    if (layoutButton.parent != null) {
                        layoutButton.inflate()
                    }
                }
            }
        }
        setupButtonAction(data)
    }

    private fun setupButtonAction(data: VoucherDetailData) {
        stateButtonBroadCastBinding?.apply {
            btnThreeDots.setOnClickListener {
                openThreeDotsBottomSheet(data)
            }
            btnBroadcastChat.setOnClickListener {
                voucherDetailTracker.sendClickBroadCastChatEvent(data)
                shareToBroadcastChat(data.voucherId)
            }
        }
        stateButtonShareBinding?.apply {
            btnThreeDots.setOnClickListener {
                openThreeDotsBottomSheet(data)
            }
            btnBroadcastChat.setOnClickListener {
                voucherDetailTracker.sendClickBroadCastChatEvent(data)
                shareToBroadcastChat(data.voucherId)
            }
            btnShare.setOnClickListener {
                stateButtonShareBinding?.btnShare.startLoading()
                viewModel.generateVoucherImage()
                voucherDetailTracker.sendClickShareEvent(data)
            }
        }
        stateButtonDuplicateBinding?.apply {
            btnThreeDots.setOnClickListener {
                openThreeDotsBottomSheet(data)
            }
            btnDuplicate.setOnClickListener {
                voucherDetailTracker.sendClickDuplicateEvent(data)
                val intent = SummaryActivity.buildDuplicateModeIntent(context, data.voucherId)
                startActivity(intent)
            }
        }
    }

    private fun openThreeDotsBottomSheet(data: VoucherDetailData) {
        voucherDetailTracker.sendClick3DotsButtonEvent(data)
        val voucherStatus = viewModel.getThreeDotsBottomSheetType(data)
        activity?.let {
            moreMenuBottomSheet =
                MoreMenuBottomSheet.newInstance(
                    context = it,
                    voucher = null,
                    title = data.voucherName,
                    pageSource = VoucherDetailFragment::class.java.simpleName,
                    voucherStatus = voucherStatus
                )
            moreMenuBottomSheet?.setOnMenuClickListener { menu ->
                onClickListenerForMoreMenu(menu, data)
            }
            moreMenuBottomSheet?.setCloseClickListener {
                voucherDetailTracker.sendClickCloseMenuEvent(data)
                moreMenuBottomSheet?.dismiss()
            }
            moreMenuBottomSheet?.show(childFragmentManager, "")
        }
    }

    private fun onClickListenerForMoreMenu(menuUiModel: MoreMenuUiModel, data: VoucherDetailData) {
        moreMenuBottomSheet?.dismiss()
        when (menuUiModel) {
            is MoreMenuUiModel.TermsAndConditions -> {
                voucherDetailTracker.sendClickTNCEvent(data)
                openTncPage()
            }
            else -> deleteOrStopVoucher(data)
        }
    }

    private fun getVoucherDetailData(voucherId: Long) {
        showLoading()
        viewModel.getVoucherDetail(voucherId)
    }

    private fun updateVoucherStatusData(data: VoucherDetailData) {
        viewModel.updateVoucherStatus(data)
    }

    private fun copyVoucherCode(voucherCode: String) {
        context?.let { ctx ->
            SharingUtil.copyTextToClipboard(
                ctx,
                COPY_PROMO_CODE_LABEL,
                voucherCode
            )
        }
        binding?.run {
            layoutButtonGroup.showToaster(
                getString(R.string.coupon_code_copied_to_clipboard),
                getString(R.string.smvc_oke_label)
            )
        }
    }

    private fun showLoading() {
        binding?.run {
            loader.show()
            nsvContent.gone()
            layoutButtonGroup.gone()
            globalError.gone()
        }
    }

    private fun hideLoading() {
        binding?.run {
            loader.gone()
            nsvContent.show()
            layoutButtonGroup.show()
            globalError.gone()
        }
    }

    private fun showGlobalError() {
        binding?.run {
            loader.gone()
            nsvContent.gone()
            layoutButtonGroup.gone()
            globalError.apply {
                show()
                setType(GlobalError.SERVER_ERROR)
                setActionClickListener {
                    getVoucherDetailData(voucherId)
                }
            }
        }
    }

    private fun shareToBroadcastChat(voucherId: Long) {
        routeToUrl(broadCastChatUrl + voucherId.toString())
    }

    private fun openTncPage() {
        routeToUrl(tncUrl)
    }

    private fun displayShareBottomSheet(voucherImageMetadata: GenerateVoucherImageMetadata) {
        val voucherDetail = voucherImageMetadata.voucherDetail
        val voucherStartDate =
            voucherDetail.voucherStartTime.toDate(DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601)
        val voucherEndDate =
            voucherDetail.voucherFinishTime.toDate(DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601)
        val productImageUrls = if (voucherDetail.isVoucherProduct) {
            voucherImageMetadata.topSellingProductImageUrls
        } else {
            emptyList()
        }

        val shareComponentParam = ShareComponentInstanceBuilder.Param(
            isVoucherProduct = voucherDetail.isVoucherProduct,
            voucherId = voucherDetail.voucherId,
            isPublic = voucherDetail.isPublic == TRUE,
            voucherCode = voucherDetail.voucherCode,
            voucherStartDate = voucherStartDate,
            voucherEndDate = voucherEndDate,
            promoType = voucherDetail.voucherType,
            benefitType = voucherDetail.voucherDiscountType,
            shopLogo = voucherImageMetadata.shopData.logo,
            shopName = voucherImageMetadata.shopData.name,
            shopDomain = voucherImageMetadata.shopData.domain,
            discountAmount = voucherDetail.voucherDiscountAmount,
            discountAmountMax = voucherDetail.voucherDiscountAmountMax,
            productImageUrls = productImageUrls,
            discountPercentage = voucherDetail.voucherDiscountAmount.toInt(),
            targetBuyer = voucherDetail.targetBuyer
        )

        val formattedShopName = MethodChecker.fromHtml(shareComponentParam.shopName).toString()

        val titleTemplate = if (shareComponentParam.isVoucherProduct) {
            getString(R.string.smvc_placeholder_share_component_outgoing_title_product_voucher)
        } else {
            getString(R.string.smvc_placeholder_share_component_outgoing_title_shop_voucher)
        }

        val title = String.format(
            titleTemplate,
            formattedShopName
        )

        val copyWritingParam = ShareCopyWritingGenerator.Param(
            voucherStartDate,
            voucherEndDate,
            voucherImageMetadata.shopData.name,
            voucherDetail.voucherDiscountAmount,
            voucherDetail.voucherDiscountAmountMax,
            voucherDetail.voucherDiscountAmount.toInt()
        )

        val description = shareCopyWritingGenerator.findOutgoingDescription(
            voucherDetail.isVoucherProduct,
            voucherDetail.targetBuyer,
            voucherDetail.voucherType,
            voucherDetail.voucherDiscountType,
            copyWritingParam
        )

        universalShareBottomSheet = shareComponentInstanceBuilder.build(
            shareComponentParam,
            title,
            onShareOptionsClicked = { shareModel ->
                handleShareOptionSelection(
                    voucherDetail.isVoucherProduct,
                    shareComponentParam.voucherId,
                    shareModel,
                    title,
                    description,
                    shareComponentParam.shopDomain
                )

                tracker.sendClickShareToSocialMediaEvent(shareModel.socialMediaName.orEmpty())
            },
            onBottomSheetClosed = {}
        )

        val onBroadcastChatCardTapped = { tracker.sendClickBroadcastChatEvent() }
        universalShareBottomSheet?.setBroadcastChannel(
            activity ?: return,
            BroadcastChannelType.BLAST_PROMO,
            voucherDetail.voucherId.toString(),
            onBroadcastChatCardTapped
        )

        universalShareBottomSheet?.show(childFragmentManager, universalShareBottomSheet?.tag)
    }

    private fun handleShareOptionSelection(
        isProductVoucher: Boolean,
        voucherId: Long,
        shareModel: ShareModel,
        title: String,
        description: String,
        shopDomain: String
    ) {
        val shareCallback = object : ShareCallback {
            override fun urlCreated(linkerShareData: LinkerShareResult?) {
                val wording = "$description ${linkerShareData?.shareUri.orEmpty()}"
                ShareComponentUtil.executeShareIntent(
                    shareModel,
                    linkerShareData,
                    activity,
                    view,
                    wording
                )
                universalShareBottomSheet?.dismiss()
            }

            override fun onError(linkerError: LinkerError?) {}
        }

        val outgoingDescription = if (isProductVoucher) {
            getString(R.string.smvc_share_component_outgoing_text_description_product_voucher)
        } else {
            getString(R.string.smvc_share_component_outgoing_text_description_shop_voucher)
        }

        val linkerDataGenerator = LinkerDataGenerator()
        val linkerShareData = linkerDataGenerator.generate(
            voucherId,
            userSession.shopId,
            shopDomain,
            shareModel,
            title,
            outgoingDescription
        )
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                Int.ZERO,
                linkerShareData,
                shareCallback
            )
        )
    }

    private fun displayDownloadVoucherImageBottomSheet(voucherDetail: VoucherDetailData) {
        if (!isVisible) return

        val bottomSheet = DownloadVoucherImageBottomSheet.newInstance(
            voucherId,
            voucherDetail.voucherImage,
            voucherDetail.voucherImageSquare,
            voucherDetail.voucherImagePortrait
        )
        bottomSheet.setOnDownloadSuccess {
            binding?.layoutButtonGroup.showToaster(
                getString(
                    R.string.smvc_placeholder_download_voucher_image_success,
                    voucherDetail.voucherName
                ),
                getString(R.string.smvc_ok)
            )
        }
        bottomSheet.setOnDownloadError {
            binding?.layoutButtonGroup.showToaster(
                getString(R.string.smvc_download_voucher_image_failed),
                getString(R.string.smvc_ok)
            )
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun setupStopConfirmationDialog() {
        stopVoucherDialog = StopVoucherConfirmationDialog(context ?: return)
    }

    private fun deleteOrStopVoucher(data: VoucherDetailData) {
        if (data.isVps == TRUE || data.isSubsidy == TRUE) {
            showCallTokopediaCareDialog(data.voucherStatus)
        } else {
            showConfirmationStopVoucherDialog(data)
        }
    }

    private fun showConfirmationStopVoucherDialog(data: VoucherDetailData) {
        val voucherStatus = data.voucherStatus
        stopVoucherDialog?.let { dialog ->
            with(dialog) {
                setOnPositiveConfirmed {
                    updateVoucherStatusData(data)
                    sendDeleteOrStopVoucherTracker(data)
                    setDismissDialog()
                }
                show(
                    getTitleStopVoucherDialog(voucherStatus),
                    getStringDescStopVoucherDialog(voucherStatus, data.voucherName),
                    getStringPositiveCtaStopVoucherDialog(voucherStatus)
                )
            }
        }
    }

    private fun showCallTokopediaCareDialog(voucherStatus: VoucherStatus) {
        context?.let {
            val title = getTitleTokopediaCareDialog(voucherStatus)
            val desc = getDescTokopediaCareDialog(voucherStatus)
            CallTokopediaCareDialog(it).apply {
                setTitle(title)
                setDescription(desc)
                setOnPositiveConfirmed {
                    goToTokopediaCare()
                }
                show(getString(R.string.smvc_call_tokopedia_care), getString(R.string.smvc_back))
            }
        }
    }

    private fun sendDeleteOrStopVoucherTracker(data: VoucherDetailData) {
        if (data.voucherStatus == VoucherStatus.NOT_STARTED) {
            voucherDetailTracker.sendClickBatalkanEvent(data)
        } else {
            voucherDetailTracker.sendClickHentikanEvent(data)
        }
    }

    private fun getTitleStopVoucherDialog(voucherStatus: VoucherStatus): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_delete_voucher_confirmation_title_of_dialog)
        } else {
            getString(R.string.smvc_canceled_voucher_confirmation_title_of_dialog)
        }
    }

    private fun getStringDescStopVoucherDialog(
        voucherStatus: VoucherStatus,
        voucherName: String
    ): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_delete_voucher_confirmation_body_dialog)
        } else {
            getString(R.string.smvc_canceled_voucher_confirmation_body_dialog, voucherName)
        }
    }

    private fun getStringPositiveCtaStopVoucherDialog(voucherStatus: VoucherStatus): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_yes_deleted_voucher)
        } else {
            getString(R.string.smvc_yes_canceled_voucher)
        }
    }

    private fun getTitleTokopediaCareDialog(voucherStatus: VoucherStatus): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_cannot_deleted_call_tokopedia_care_title_dialog)
        } else {
            getString(R.string.smvc_cannot_canceled_call_tokopedia_care_title_dialog)
        }
    }

    private fun getDescTokopediaCareDialog(voucherStatus: VoucherStatus): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_cannot_deleted_call_tokopedia_care_desc_dialog)
        } else {
            getString(R.string.smvc_cannot_canceled_call_tokopedia_care_desc_dialog)
        }
    }

    private fun redirectToProductListPage(voucherDetail: VoucherDetailData) {
        val intent = ProductListActivity.buildIntentForVoucherDetailPage(
            context = activity ?: return,
            showCtaChangeProductOnToolbar = voucherDetail.voucherStatus == VoucherStatus.NOT_STARTED,
            voucherConfiguration = voucherDetail.toVoucherConfiguration(),
            selectedProducts = voucherDetail.toSelectedProducts(),
            selectedWarehouseId = voucherDetail.warehouseId
        )

        startActivityForResult(
            intent,
            NumberConstant.REQUEST_CODE_ADD_PRODUCT_TO_EXISTING_SELECTION
        )
    }

    private fun redirectToVoucherListPage() {
        val voucherDetailData = viewModel.getCurrentVoucherDetailData()
        context?.let { ctx ->
            voucherDetailData?.let { getStringSuccessStopVoucher(it.voucherStatus, it.voucherName) }
                ?.let { message ->
                    sharedPreferencesUtil.setUploadResult(
                        ctx,
                        message
                    )
                }
        }
        RouteManager.route(context, SELLER_MVC_LIST_HISTORY)
        activity?.finish()
    }

    private fun getStringSuccessStopVoucher(
        voucherStatus: VoucherStatus,
        voucherName: String
    ): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_success_to_deleted_voucher, voucherName)
        } else {
            getString(R.string.smvc_success_to_cancel_voucher, voucherName)
        }
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            activity,
            String.format(
                TOKOPEDIA_CARE_STRING_FORMAT,
                ApplinkConst.WEBVIEW,
                TokopediaUrl.getInstance().MOBILEWEB.plus(TOKOPEDIA_CARE_PATH)
            )
        )
    }
}
