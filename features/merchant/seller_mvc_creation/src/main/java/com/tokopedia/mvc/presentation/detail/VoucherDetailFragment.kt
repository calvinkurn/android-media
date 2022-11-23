package com.tokopedia.mvc.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.campaign.utils.extension.showToaster
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.*
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import com.tokopedia.mvc.util.SharingUtil
import com.tokopedia.mvc.util.constant.*
import com.tokopedia.mvc.util.constant.VoucherTargetConstant.VOUCHER_TARGET_PUBLIC
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

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
    }

    // binding
    private var binding by autoClearedNullable<SmvcFragmentVoucherDetailBinding>()
    private var headerBinding by autoClearedNullable<SmvcVoucherDetailHeaderSectionBinding>()
    private var voucherTypeBinding by autoClearedNullable<SmvcVoucherDetailVoucherTypeSectionBinding>()
    private var voucherInfoBinding by autoClearedNullable<SmvcVoucherDetailVoucherInfoSectionBinding>()
    private var voucherSettingBinding by autoClearedNullable<SmvcVoucherDetailVoucherSettingSectionBinding>()
    private var voucherProductBinding by autoClearedNullable<SmvcVoucherDetailProductSectionBinding>()
    private var btnState1Binding by autoClearedNullable<SmvcVoucherDetailButtonSectionState1Binding>()
    private var btnState2Binding by autoClearedNullable<SmvcVoucherDetailButtonSectionState2Binding>()
    private var btnState3Binding by autoClearedNullable<SmvcVoucherDetailButtonSectionState3Binding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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
        getVoucherDetailData(voucherId)
    }

    private fun observeData() {
        viewModel.voucherDetail.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    setupView(result.data)
                }
                is Fail -> {
                }
            }
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
            if (layoutHeader.parent != null) {
                layoutHeader.inflate()
            }
        }
        headerBinding?.run {
            setupVoucherStatus(data)
            setupVoucherAction(data)
            imgVoucher.setImageUrl(data.voucherImageSquare)
            when (data.isVps) {
                FALSE -> {
                    labelVoucherSource.gone()
                    tpgVpsPackage.gone()
                }
                else -> {
                    labelVoucherSource.apply {
                        visible()
                        text = context.getString(R.string.smvc_promotion_package_label)
                    }
                    tpgVpsPackage.apply {
                        visible()
                        text = data.packageName
                    }
                }
            }
            val availableQuota = data.voucherQuota - data.remainingQuota
            tpgUsedVoucherQuota.text = availableQuota.toString()
            tpgAvailableVoucherQuota.text = getString(
                R.string.smvc_placeholder_voucher_quota,
                data.remainingQuota
            )
        }
    }

    private fun setupVoucherStatus(data: VoucherDetailData) {
        headerBinding?.run {
            when (data.voucherStatus) {
                VoucherStatusConstant.NOT_STARTED -> {
                    tpgVoucherStatus.apply {
                        text = getString(R.string.smvc_status_upcoming_label)
                        setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                    }
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREY)
                }
                VoucherStatusConstant.ONGOING -> {
                    tpgVoucherStatus.apply {
                        text = getString(R.string.smvc_status_ongoing_label)
                        setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Green_G500)
                    }
                    imgCampaignStatusIndicator.loadImage(ImageUrlConstant.IMAGE_URL_RIBBON_GREEN)
                }
                VoucherStatusConstant.STOPPED -> {
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
                VoucherStatusConstant.NOT_STARTED -> {
                    btnUbahKupon.visible()
                    timer.invisible()
                    tpgPeriodStop.invisible()
                }
                VoucherStatusConstant.ONGOING -> {
                    btnUbahKupon.invisible()
                    timer.visible()
                    tpgPeriodStop.invisible()
                    startTimer(data.voucherFinishTime.toDate(DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601))
                }
                VoucherStatusConstant.ENDED -> {
                    btnUbahKupon.invisible()
                    timer.invisible()
                    tpgPeriodStop.invisible()
                }
                else -> {
                    val format = SimpleDateFormat(
                        DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601,
                        Locale.getDefault()
                    )
                    val stoppedDate = format.parse(data.updateTime)
                    btnUbahKupon.invisible()
                    timer.invisible()
                    tpgPeriodStop.apply {
                        visible()
                        if (stoppedDate != null) {
                            text = getString(
                                R.string.smvc_placeholder_stopped_date,
                                stoppedDate.formatTo(DateConstant.DATE_YEAR_PRECISION)
                            )
                        }
                    }
                }
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
            tpgVoucherType.text = when (data.isLockToProduct) {
                TRUE -> getString(R.string.smvc_voucher_product_label)
                else -> getString(R.string.smvc_voucher_store_label)
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
                tpgVoucherTarget.text =  getString(R.string.smvc_voucher_public_label)
                llVoucherCode.gone()
            } else {
                tpgVoucherTarget.text =  getString(R.string.smvc_voucher_private_label)
                llVoucherCode.visible()
            }
            tpgVoucherName.text = data.voucherName
            tpgVoucherCode.text = data.voucherCode
            iconCopy.setOnClickListener {
                copyVoucherCode(data.voucherCode)
            }
            val format = SimpleDateFormat(
                DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601,
                Locale.getDefault()
            )
            val startPeriodDate = format.parse(data.voucherStartTime)
            val endPeriodDate = format.parse(data.voucherFinishTime)
            if (startPeriodDate != null) {
                tpgVoucherStartPeriod.text =
                    startPeriodDate.formatTo(DateConstant.DATE_YEAR_WITH_TIME)
            }
            if (endPeriodDate != null) {
                tpgVoucherEndPeriod.text = endPeriodDate.formatTo(DateConstant.DATE_YEAR_WITH_TIME)
            }
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

    private fun Typography.setPromoType(voucherType: Int) {
        this.text = when (voucherType) {
            PromoTypeConstant.SHIPPING -> getString(R.string.smvc_free_shipping_label)
            PromoTypeConstant.DISCOUNT -> getString(R.string.smvc_discount_label)
            else -> getString(R.string.smvc_cashback_label)
        }
    }

    private fun setDeductionType(data: VoucherDetailData) {
        voucherSettingBinding?.run {
            llDeductionType.visibility = when (data.voucherType) {
                PromoTypeConstant.SHIPPING -> View.GONE
                else -> View.VISIBLE
            }
            tpgDeductionType.text = when (data.voucherDiscountType) {
                DiscountTypeConstant.NOMINAL -> getString(R.string.smvc_nominal_label)
                else -> getString(R.string.smvc_percentage_label)
            }
        }
    }

    private fun setDeductionAmount(data: VoucherDetailData) {
        voucherSettingBinding?.run {
            when (data.voucherType) {
                PromoTypeConstant.SHIPPING -> {
                    tpgVoucherNominalLabel.text =
                        getString(R.string.smvc_nominal_free_shipping_label)
                    tpgVoucherNominal.text = data.voucherDiscountAmount.getCurrencyFormatted()
                }
                PromoTypeConstant.CASHBACK -> {
                    when (data.voucherDiscountType) {
                        DiscountTypeConstant.NOMINAL -> {
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
                        DiscountTypeConstant.NOMINAL -> {
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
                PromoTypeConstant.SHIPPING -> {
                    llVoucherMaxPriceDeduction.gone()
                }
                else -> {
                    when (data.voucherDiscountType) {
                        DiscountTypeConstant.PERCENTAGE -> {
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
            TargetBuyerConstant.ALL_USER -> getString(R.string.smvc_all_buyer_label)
            TargetBuyerConstant.NEW_FOLLOWER -> getString(R.string.smvc_new_follower_label)
            TargetBuyerConstant.NEW_USER -> getString(R.string.smvc_new_user_label)
            else -> getString(R.string.smvc_member_label)
        }
    }

    private fun setupProductListSection(data: VoucherDetailData) {
        if (data.isLockToProduct == TRUE) {
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
                tpgSeeProduct.setOnClickListener {
                    //TODO:go to product page
                }
            }
        }
    }

    private fun setupSpendingEstimationSection(data: VoucherDetailData) {
        val spendingEstimation = viewModel.getSpendingEstimation(data)
        val title = when (data.voucherStatus) {
            VoucherStatusConstant.ONGOING -> getString(R.string.smvc_spending_estimation_title_2)
            VoucherStatusConstant.ENDED -> getString(R.string.smvc_spending_estimation_title_3)
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
            }
        }
    }

    private fun setupButtonSection(data: VoucherDetailData) {
        binding?.run {
            when (data.voucherStatus) {
                VoucherStatusConstant.NOT_STARTED -> {
                    layoutButton.setOnInflateListener { _, view ->
                        btnState1Binding = SmvcVoucherDetailButtonSectionState1Binding.bind(view)
                    }
                    layoutButton.layoutResource =
                        R.layout.smvc_voucher_detail_button_section_state_1
                    if (layoutButton.parent != null) {
                        layoutButton.inflate()
                    }
                }
                VoucherStatusConstant.ONGOING -> {
                    layoutButton.setOnInflateListener { _, view ->
                        btnState2Binding = SmvcVoucherDetailButtonSectionState2Binding.bind(view)
                    }
                    layoutButton.layoutResource =
                        R.layout.smvc_voucher_detail_button_section_state_2
                    if (layoutButton.parent != null) {
                        layoutButton.inflate()
                    }
                }
                else -> {
                    layoutButton.setOnInflateListener { _, view ->
                        btnState3Binding = SmvcVoucherDetailButtonSectionState3Binding.bind(view)
                    }
                    layoutButton.layoutResource =
                        R.layout.smvc_voucher_detail_button_section_state_3
                    if (layoutButton.parent != null) {
                        layoutButton.inflate()
                    }
                }
            }
        }
        setupButtonAction()
    }

    private fun setupButtonAction() {
        btnState1Binding?.apply {
            btnThreeDots.setOnClickListener {
                //TODO:open bottom sheet
            }
            btnBroadcastChat.setOnClickListener {
                context?.let { ctx -> SharingUtil.shareToBroadCastChat(ctx, 14883692) }
            }
        }
        btnState2Binding?.apply {
            btnThreeDots.setOnClickListener {
                //TODO:open bottom sheet
            }
            btnBroadcastChat.setOnClickListener {
                context?.let { ctx -> SharingUtil.shareToBroadCastChat(ctx, 14883692) }
            }
            btnShare.setOnClickListener {
                //TODO:open share component
            }
        }
        btnState3Binding?.apply {
            btnThreeDots.setOnClickListener {
                //TODO:open bottom sheet
            }
            btnDuplicate.setOnClickListener {
                //TODO:go to summary page
            }
        }
    }

    private fun getVoucherDetailData(voucherId: Long) {
        viewModel.getVoucherDetail(voucherId)
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
}
