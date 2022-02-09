package com.tokopedia.vouchercreation.product.detail.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.ClipboardHandler
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.consts.VoucherCreationConst
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcError
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.extension.splitByThousand
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.common.utils.Timer
import com.tokopedia.vouchercreation.common.utils.setFragmentToUnifyBgColor
import com.tokopedia.vouchercreation.databinding.FragmentCouponDetailBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.ExpenseEstimationBottomSheet
import com.tokopedia.vouchercreation.product.detail.view.viewmodel.CouponDetailViewModel
import com.tokopedia.vouchercreation.product.download.CouponImageUiModel
import com.tokopedia.vouchercreation.product.download.DownloadCouponImageBottomSheet
import com.tokopedia.vouchercreation.shop.detail.view.component.StartEndVoucher
import javax.inject.Inject

class CouponDetailFragment : BaseDaggerFragment() {


    companion object {
        private const val BUNDLE_KEY_COUPON_ID = "couponId"
        private const val EMPTY_STRING = ""
        private const val ZERO: Long = 0
        private const val PERCENT = 100
        private const val DISCOUNT_TYPE_NOMINAL = "idr"

        @JvmStatic
        fun newInstance(couponId: Long): CouponDetailFragment {
            return CouponDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(BUNDLE_KEY_COUPON_ID, couponId)
                }
            }
        }
    }

    private val MinimumPurchaseType.label: String
        get() {
            return when (this) {
                MinimumPurchaseType.NOMINAL -> getString(R.string.nominal)
                MinimumPurchaseType.QUANTITY -> getString(R.string.item)
                MinimumPurchaseType.NOTHING -> getString(R.string.nothing)
                else -> EMPTY_STRING
            }
        }

    private val DiscountType.label: String
        get() {
            return when (this) {
                DiscountType.NOMINAL -> getString(R.string.nominal)
                DiscountType.PERCENTAGE -> getString(R.string.percentage)
                else -> EMPTY_STRING
            }
        }

    private var nullableBinding by autoClearedNullable<FragmentCouponDetailBinding>()
    private val binding: FragmentCouponDetailBinding
        get() = requireNotNull(nullableBinding)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CouponDetailViewModel::class.java) }
    private val couponId by lazy { arguments?.getLong(BUNDLE_KEY_COUPON_ID).orZero() }
    private var timer : Timer? = null
    private var bannerImageUrl : String = ""
    private var squareImageUrl : String = ""
    private var portraitImageUrl : String = ""


    override fun getScreenName() = CouponDetailFragment::class.simpleName

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nullableBinding = FragmentCouponDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentToUnifyBgColor()
        setupViews()
        observeCouponDetail()
        viewModel.getCouponDetail(couponId)
    }

    private fun setupViews() {
        with(binding) {
            imgExpenseEstimationDescription.setOnClickListener { displayExpenseEstimationDescription() }
            imgCopyToClipboard.setOnClickListener {
                val content = binding.tpgCouponCode.text.toString().trim()
                copyToClipboard(content)
            }
            btnDownload.setOnClickListener {
                downloadCoupon(
                    bannerImageUrl,
                    squareImageUrl,
                    portraitImageUrl
                )
            }
        }
    }

    private fun observeCouponDetail() {
        viewModel.couponDetail.observe(viewLifecycleOwner, { result ->
            hideLoading()
            if (result is Success) {
                showContent()
                displayCouponDetail(result.data.coupon, result.data.maxProduct)
            } else {
                hideContent()
                showError()
            }
        })
    }

    private fun displayCouponDetail(coupon: CouponUiModel, maxProduct: Int) {
        binding.header.headerView?.text = coupon.name
        displayCouponImage(coupon.imageSquare)
        displayCountdown(coupon.status, coupon.finishTime)
        displayCouponStatus(coupon)
        displayCouponInformationSection(
            coupon.name,
            coupon.code,
            coupon.isPublic,
            coupon.startTime,
            coupon.finishTime
        )
        displayCouponSettingsSection(coupon)
        displayQuotaUsage(coupon)
        refreshProductsSection(coupon.productIds.size, maxProduct)
        this.bannerImageUrl = coupon.image
        this.squareImageUrl = coupon.imageSquare
        this.portraitImageUrl = coupon.imagePortrait
    }

    private fun displayCouponImage(imageUrl: String) {
        binding.imgCoupon.loadImageRounded(
            url = imageUrl,
            resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
                .toFloat()
        )
    }

    private fun displayCountdown(couponStatus: Int, finishTime: String) {
        if (couponStatus == VoucherStatusConst.ONGOING) {
            binding.groupCountdown.isVisible = couponStatus == VoucherStatusConst.ONGOING
            startTimer(finishTime)
        }
    }


    private fun displayCouponInformationSection(
        couponName: String,
        couponCode: String,
        isPublic: Boolean,
        unformattedStartDate: String,
        unformattedSEndDate: String
    ) {
        val target =
            if (isPublic) getString(R.string.mvc_public) else getString(R.string.mvc_special)
        binding.tpgCouponTarget.text = target

        binding.tpgCouponName.text = couponName
        handleCouponCodeVisibility(couponCode, isPublic)

        val startDate = unformattedStartDate.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
            .parseTo(DateTimeUtils.DATE_FORMAT)
        val startHour = unformattedStartDate.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
            .parseTo(DateTimeUtils.HOUR_FORMAT)
        val endDate = unformattedSEndDate.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
            .parseTo(DateTimeUtils.DATE_FORMAT)
        val endHour = unformattedSEndDate.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
            .parseTo(DateTimeUtils.HOUR_FORMAT)

        binding.startEndVoucher.setStartTime(StartEndVoucher.Model(startDate, startHour))
        binding.startEndVoucher.setEndTime(StartEndVoucher.Model(endDate, endHour))

        val period = String.format(
            getString(R.string.placeholder_coupon_period),
            startDate,
            startHour,
            endDate,
            endHour
        )

        binding.tpgCouponPeriod.text = period
    }

    private fun displayCouponSettingsSection(coupon: CouponUiModel) {
        binding.tpgCouponType.text = coupon.typeFormatted
        binding.tpgCouponQouta.text = coupon.quota.splitByThousand()

        val couponType =
            if (coupon.type == VoucherTypeConst.FREE_ONGKIR) CouponType.FREE_SHIPPING else CouponType.CASHBACK
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL

        val discountType =
            if (coupon.discountTypeFormatted == DISCOUNT_TYPE_NOMINAL) DiscountType.NOMINAL else DiscountType.PERCENTAGE
        binding.tpgDiscountType.text = discountType.label

        handleDiscountType(couponType)
        handleDiscountAmount(
            couponType,
            discountType,
            coupon.discountAmt,
            coupon.discountAmt
        )
        handleMaximumDiscount(couponType, discountType, coupon.discountAmtMax)
        handleMinimumPurchaseType(couponType, minimumPurchaseType)
        handleMinimumPurchase(couponType, minimumPurchaseType, coupon.minimumAmt)

        val maxExpense = (coupon.quota * coupon.discountAmtMax).toLong()
        handleEstimatedMaxExpense(maxExpense)
    }

    private fun handleDiscountType(couponType: CouponType) {
        if (couponType == CouponType.FREE_SHIPPING) {
            binding.groupDiscountType.gone()
        } else {
            binding.groupDiscountType.visible()
        }
    }

    private fun refreshProductsSection(productCount: Int, maxProduct: Int) {
        if (productCount > ZERO) {
            binding.tpgProductCount.text =
                String.format(getString(R.string.placeholder_registered_product), productCount, maxProduct)
        } else {

            binding.tpgProductCount.text = getString(R.string.no_products)
        }
    }

    private fun handleDiscountAmount(
        couponType: CouponType,
        discountType: DiscountType,
        discountAmount: Int,
        discountPercentage: Int
    ) {
        if (discountAmount > ZERO) {
            binding.groupDiscountAmount.visible()
        } else {
            binding.groupDiscountAmount.gone()
        }

        val formattedDiscountAmount = when {
            couponType == CouponType.FREE_SHIPPING -> String.format(
                getString(R.string.placeholder_rupiah),
                discountAmount.splitByThousand()
            )
            couponType == CouponType.CASHBACK && discountType == DiscountType.PERCENTAGE ->
                String.format(
                    getString(R.string.placeholder_percent),
                    discountPercentage.splitByThousand()
                )
            else ->
                String.format(
                    getString(R.string.placeholder_rupiah),
                    discountAmount.splitByThousand()
                )
        }

        val discountAmountLabel = when (couponType) {
            CouponType.NONE -> EMPTY_STRING
            CouponType.CASHBACK -> getString(R.string.discount_amount)
            CouponType.FREE_SHIPPING -> getString(R.string.discount_amount_free_shipping)
        }

        binding.tpgDiscountAmountLabel.text = discountAmountLabel
        binding.tpgDiscountAmount.text = formattedDiscountAmount
    }

    private fun handleMaximumDiscount(
        couponType: CouponType,
        discountType: DiscountType,
        maxDiscount: Int
    ) {
        if (couponType == CouponType.CASHBACK && discountType == DiscountType.PERCENTAGE) {
            binding.groupMaxDiscount.visible()
            binding.tpgMaxDiscount.text =
                String.format(getString(R.string.placeholder_rupiah), maxDiscount.splitByThousand())
        } else {
            binding.groupMaxDiscount.gone()
        }
    }

    private fun handleEstimatedMaxExpense(estimatedMaxExpense: Long) {
        if (estimatedMaxExpense == ZERO) {
            binding.tpgExpenseAmount.text = getString(R.string.hyphen)
        } else {
            binding.tpgExpenseAmount.text = String.format(
                getString(R.string.placeholder_rupiah),
                estimatedMaxExpense.splitByThousand()
            )
        }
    }

    private fun handleMinimumPurchaseType(
        couponType: CouponType,
        minimumPurchaseType: MinimumPurchaseType
    ) {

        if (couponType == CouponType.FREE_SHIPPING) {
            binding.groupMinimumPurchaseType.gone()
        } else {
            binding.groupMinimumPurchaseType.gone()
            binding.tpgMinimumPurchaseType.text = minimumPurchaseType.label
        }

    }

    private fun handleCouponCodeVisibility(couponCode: String, isPublic: Boolean) {
        if (isPublic) {
            binding.imgCopyToClipboard.gone()
            binding.groupCouponCode.gone()
        } else {
            binding.imgCopyToClipboard.visible()
            binding.groupCouponCode.visible()
        }

        binding.tpgCouponCode.text = couponCode
    }

    private fun handleMinimumPurchase(
        couponType: CouponType,
        minimumPurchaseType: MinimumPurchaseType,
        minimumPurchase: Int
    ) {
        if (minimumPurchase > ZERO) {
            binding.groupMinimumPurchase.visible()
            val text = when {
                couponType == CouponType.FREE_SHIPPING -> String.format(
                    getString(R.string.placeholder_rupiah),
                    minimumPurchase.splitByThousand()
                )
                couponType == CouponType.CASHBACK && minimumPurchaseType == MinimumPurchaseType.NONE -> EMPTY_STRING
                couponType == CouponType.CASHBACK && minimumPurchaseType == MinimumPurchaseType.NOMINAL -> String.format(
                    getString(R.string.placeholder_rupiah),
                    minimumPurchase.splitByThousand()
                )
                couponType == CouponType.CASHBACK && minimumPurchaseType == MinimumPurchaseType.QUANTITY -> String.format(
                    getString(R.string.placeholder_quantity),
                    minimumPurchase.splitByThousand()
                )
                couponType == CouponType.CASHBACK && minimumPurchaseType == MinimumPurchaseType.NOTHING -> getString(
                    R.string.nothing
                )
                else -> EMPTY_STRING
            }

            binding.tpgMinimumPurchase.text = text

        } else {
            binding.groupMinimumPurchase.gone()
        }

    }

    private fun displayCouponStatus(coupon: CouponUiModel) {
        when (coupon.status) {
            VoucherStatusConst.ONGOING -> {
                binding.labelCountdown.visible()
                binding.labelVoucherStatus.setLabel(getString(R.string.mvc_ongoing))
                binding.labelVoucherStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
            }
            VoucherStatusConst.NOT_STARTED -> {
                binding.labelCountdown.gone()
                binding.labelVoucherStatus.setLabel(getString(R.string.mvc_future))
                binding.labelVoucherStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
            }
            VoucherStatusConst.STOPPED -> {
                binding.labelCountdown.gone()
                binding.labelVoucherStatus.setLabel(getString(R.string.mvc_stopped))
                binding.labelVoucherStatus.setLabelType(Label.HIGHLIGHT_LIGHT_RED)

            }
            VoucherStatusConst.DELETED -> {
                binding.labelCountdown.gone()
                binding.labelVoucherStatus.setLabel(getString(R.string.mvc_deleted))
                binding.labelVoucherStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
            }
            else -> {
                binding.labelCountdown.gone()
                binding.labelVoucherStatus.setLabel(getString(R.string.mvc_ended))
                binding.labelVoucherStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
            }
        }

    }

    private fun startTimer(unformattedEndDate: String) {
        binding.labelCountdown.visible()
        val endDate = unformattedEndDate.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
        timer = Timer(endDate)
        timer?.setOnTickListener { remainingTime ->
            binding.labelCountdown.text = remainingTime
        }
        timer?.startCountdown()
    }

    private fun displayQuotaUsage(coupon: CouponUiModel) {
        val progressBarValue = (coupon.confirmedQuota / coupon.quota) * PERCENT
        binding.progressBarQuotaUsage.setValue(progressBarValue, true)
        binding.progressBarQuotaUsage.progressBarHeight = requireActivity().pxToDp(6).toInt()

        binding.tpgUsedQuota.text = coupon.confirmedQuota.toString()
        binding.tpgTotalQuota.text = String.format(
            getString(R.string.mvc_detail_total_quota).toBlankOrString(),
            coupon.quota.toString()
        )

        binding.tpgTickerUsage.setOnClickListener { displayExpenseEstimationDescription() }

        if (coupon.isPublic && coupon.status == VoucherStatusConst.ONGOING) {
            binding.tpgTickerUsage.visible()
            binding.tpgTickerUsage.text = String.format(
                getString(R.string.placeholder_quota_usage),
                coupon.bookedQuota.toString()
            ).parseAsHtml()
        } else {
            binding.tpgTickerUsage.gone()
        }
    }

    private fun displayExpenseEstimationDescription() {
        if (!isAdded) return
        val bottomSheet = ExpenseEstimationBottomSheet.newInstance()
        bottomSheet.show(childFragmentManager)
    }

    private fun hideLoading() {
        binding.loader.gone()
    }

    private fun showContent() {
        binding.cardShare.visible()
        binding.content.visible()
    }

    private fun hideContent() {
        binding.cardShare.gone()
        binding.content.gone()
    }

    private fun showError() {
        Toaster.build(
            binding.root,
            getString(R.string.error_message_failed_get_coupon_detail),
            Snackbar.LENGTH_SHORT,
            Toaster.TYPE_ERROR
        ).show()
    }

    private fun copyToClipboard(content: String) {
        ClipboardHandler().copyToClipboard(requireActivity(), content)
        Toaster.build(
            binding.root,
            getString(R.string.coupon_code_copied_to_clipboard)
        ).show()
    }

    override fun onPause() {
        super.onPause()
        timer?.stopCountdown()
    }

    private fun downloadCoupon(bannerImageUrl : String, squareImageUrl : String, portraitImageUrl : String) {
        if (!isAdded) return
        val bottomSheet = DownloadCouponImageBottomSheet.newInstance(
            bannerImageUrl,
            squareImageUrl,
            portraitImageUrl,
            userSession.userId
        )
        bottomSheet.setOnDownloadClickListener { couponList -> checkDownloadPermission(couponList) }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun checkDownloadPermission(couponList: List<CouponImageUiModel>) {
        val listener = object : PermissionCheckerHelper.PermissionCheckListener {
            override fun onPermissionDenied(permissionText: String) {
                permissionCheckerHelper.onPermissionDenied(requireActivity(), permissionText)
                Toaster.build(
                    view = binding.root,
                    text = getString(R.string.mvc_storage_permission_enabled_needed),
                    duration = Toast.LENGTH_LONG
                ).show()
            }

            override fun onNeverAskAgain(permissionText: String) {
                permissionCheckerHelper.onNeverAskAgain(requireActivity(), permissionText)
            }

            override fun onPermissionGranted() {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    couponList.forEach {
                        downloadFiles(it.imageType.imageUrl)
                    }
                }
            }
        }

        permissionCheckerHelper.checkPermission(
            fragment = this,
            permission = PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
            listener = listener
        )

        VoucherCreationTracking.sendVoucherListClickTracking(
            action = VoucherCreationAnalyticConstant.EventAction.Click.DOWNLOAD_VOUCHER,
            isActive = false,
            userId = userSession.userId
        )
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun downloadFiles(uri: String) {
        val downloadCompleteListener = object : DownloadHelper.DownloadHelperListener {
            override fun onDownloadComplete() {
                showDownloadCompleteStatus(true)
            }
        }

        try {
            val helper = DownloadHelper(
                context = requireActivity(),
                uri = uri,
                filename = System.currentTimeMillis()
                    .toString() + VoucherCreationConst.JPEG_EXT,
                listener = downloadCompleteListener
            )
            helper.downloadFile { true }
        } catch (se: SecurityException) {
            MvcErrorHandler.logToCrashlytics(se, MvcError.ERROR_SECURITY)
            showDownloadCompleteStatus(
                isSuccess = false,
                isInternetProblem = false
            )
        } catch (iae: IllegalArgumentException) {
            MvcErrorHandler.logToCrashlytics(iae, MvcError.ERROR_URI)
            showDownloadCompleteStatus(
                isSuccess = false,
                isInternetProblem = false
            )
        } catch (ex: Exception) {
            MvcErrorHandler.logToCrashlytics(ex, MvcError.ERROR_DOWNLOAD)
            showDownloadCompleteStatus(
                isSuccess = false
            )
        }

    }

    fun showDownloadCompleteStatus(isSuccess: Boolean, isInternetProblem: Boolean = true) {
        val toasterType: Int
        val toasterMessage: String

        if (isSuccess) {
            toasterType = Toaster.TYPE_NORMAL
            toasterMessage = getString(R.string.download_coupon_product_success).toBlankOrString()
        } else {
            toasterType = Toaster.TYPE_ERROR
            val errorMessageSuffix =
                if (isInternetProblem) {
                    getString(R.string.mvc_fail_download_voucher_suffix).toBlankOrString()
                } else {
                    ""
                }
            toasterMessage = "${getString(R.string.download_coupon_product_failed).toBlankOrString()}$errorMessageSuffix"
        }

        longToaster(toasterMessage, toasterType)
    }


    private fun longToaster(text: String, toasterType: Int) {
        if (text.isEmpty()) return
        Toaster.build(
            binding.root,
            text,
            Toaster.LENGTH_LONG,
            toasterType,
            getString(R.string.mvc_oke)
        ).show()
    }

}