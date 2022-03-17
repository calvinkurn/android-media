package com.tokopedia.vouchercreation.product.detail.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.ClipboardHandler
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.consts.*
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcError
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.extension.splitByThousand
import com.tokopedia.vouchercreation.common.tracker.CouponDetailTracker
import com.tokopedia.vouchercreation.common.tracker.SharingComponentTracker
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.common.utils.Timer
import com.tokopedia.vouchercreation.common.utils.setFragmentToUnifyBgColor
import com.tokopedia.vouchercreation.databinding.FragmentCouponDetailBinding
import com.tokopedia.vouchercreation.product.create.data.response.ProductId
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.ExpenseEstimationBottomSheet
import com.tokopedia.vouchercreation.product.detail.view.viewmodel.CouponDetailViewModel
import com.tokopedia.vouchercreation.product.download.CouponImageUiModel
import com.tokopedia.vouchercreation.product.download.DownloadCouponImageBottomSheet
import com.tokopedia.vouchercreation.product.duplicate.DuplicateCouponActivity
import com.tokopedia.vouchercreation.product.list.view.activity.ManageProductActivity
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_COUPON_SETTINGS
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_IS_VIEWING
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_MAX_PRODUCT_LIMIT
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_SELECTED_PRODUCT_IDS
import com.tokopedia.vouchercreation.product.share.LinkerDataGenerator
import com.tokopedia.vouchercreation.product.share.SharingComponentInstanceBuilder
import com.tokopedia.vouchercreation.shop.detail.view.component.StartEndVoucher
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import javax.inject.Inject

class CouponDetailFragment : BaseDaggerFragment() {


    companion object {
        private const val BUNDLE_KEY_COUPON_ID = "couponId"
        private const val EMPTY_STRING = ""
        private const val ZERO: Long = 0
        private const val PERCENT = 100
        private const val DISCOUNT_TYPE_NOMINAL = "idr"
        private const val PROGRESS_BAR_HEIGHT = 6

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

    @Inject
    lateinit var linkerDataGenerator: LinkerDataGenerator

    @Inject
    lateinit var tracker: CouponDetailTracker

    @Inject
    lateinit var sharingComponentTracker: SharingComponentTracker

    @Inject
    lateinit var shareComponentInstanceBuilder: SharingComponentInstanceBuilder

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CouponDetailViewModel::class.java) }
    private val couponId by lazy { arguments?.getLong(BUNDLE_KEY_COUPON_ID).orZero() }
    private var timer : Timer? = null
    private var bannerImageUrl : String = ""
    private var squareImageUrl : String = ""
    private var portraitImageUrl : String = ""
    private var shareComponentBottomSheet : UniversalShareBottomSheet? = null


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
        observeShopAndTopProducts()
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
                val couponStatus = binding.labelVoucherStatus.text.toString().trim()
                tracker.sendDownloadCouponImageClickEvent(couponStatus)
                downloadCoupon(
                    bannerImageUrl,
                    squareImageUrl,
                    portraitImageUrl
                )
            }
            tpgViewProducts.setOnClickListener {
                val couponStatus = binding.labelVoucherStatus.text.toString().trim()
                tracker.sendViewAllProductsClickEvent(couponStatus)
                viewModel.getCoupon()?.run {
                    val maxProductLimit = viewModel.getMaxProductLimit()
                    val couponSettings = viewModel.getCouponSettings(this)
                    val manageProductIntent = Intent(requireContext(), ManageProductActivity::class.java).apply {
                        putExtras(Bundle().apply {
                            putBoolean(BUNDLE_KEY_IS_VIEWING, true)
                            putInt(BUNDLE_KEY_MAX_PRODUCT_LIMIT, maxProductLimit)
                            putParcelable(BUNDLE_KEY_COUPON_SETTINGS, couponSettings)
                            val selectedProductIds = ArrayList<ProductId>()
                            selectedProductIds.addAll(viewModel.getCoupon()?.products ?: listOf())
                            putParcelableArrayList(BUNDLE_KEY_SELECTED_PRODUCT_IDS, selectedProductIds)
                        })
                    }
                    startActivity(manageProductIntent)
                }
            }
            header.setNavigationOnClickListener { activity?.onBackPressed() }
        }
    }

    private fun observeCouponDetail() {
        viewModel.couponDetail.observe(viewLifecycleOwner, { result ->
            hideLoading()
            if (result is Success) {
                showContent()
                displayCouponDetail(result.data.coupon, result.data.maxProduct)
                viewModel.setCoupon(result.data.coupon)
                viewModel.setMaxProductLimit(result.data.maxProduct)
            } else {
                hideContent()
                showError()
            }
        })
    }

    private fun observeShopAndTopProducts() {
        viewModel.shopWithTopProducts.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    displayShareBottomSheet(
                        viewModel.getCoupon() ?: return@observe,
                        result.data.topProductsImageUrl,
                        result.data.shop
                    )
                }
                is Fail -> {
                    showError(result.throwable)
                }
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
        refreshProductsSection(coupon.products.size, maxProduct)
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
                binding.tpgLabelMaxExpense.text = getString(R.string.mvc_spending_so_far)
                binding.card.visible()
                binding.labelCountdown.visible()
                binding.labelVoucherStatus.setLabel(getString(R.string.mvc_ongoing))
                binding.labelVoucherStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
                binding.button.text = getString(R.string.mvc_share)
                binding.button.setOnClickListener {
                    sharingComponentTracker.sendShareClickEvent(
                        ShareComponentConstant.ENTRY_POINT_DETAIL,
                        coupon.id.toString()
                    )
                    viewModel.getShopAndTopProducts(viewModel.getCoupon() ?: return@setOnClickListener)
                }
            }
            VoucherStatusConst.NOT_STARTED -> {
                binding.labelCountdown.gone()
                binding.labelVoucherStatus.setLabel(getString(R.string.mvc_future))
                binding.labelVoucherStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
            }
            VoucherStatusConst.STOPPED -> {
                binding.tpgCancelledAt.visible()
                val cancelledDate = coupon.updatedTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
                    .parseTo(DateTimeUtils.DATE_FORMAT)
                val formattedCancelledDate =
                    String.format(getString(R.string.mvc_placeholder_cancelled_at), cancelledDate)
                        .parseAsHtml()
                binding.tpgCancelledAt.text = formattedCancelledDate

                binding.labelCountdown.gone()
                binding.labelVoucherStatus.setLabel(getString(R.string.mvc_stopped))
                binding.labelVoucherStatus.setLabelType(Label.HIGHLIGHT_LIGHT_RED)

            }
            VoucherStatusConst.DELETED -> {
                binding.labelCountdown.gone()
                binding.labelVoucherStatus.setLabel(getString(R.string.mvc_deleted))
                binding.labelVoucherStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
            }
            VoucherStatusConst.ENDED -> {
                binding.card.visible()
                binding.tpgLabelMaxExpense.text = getString(R.string.mvc_total_expense)
                binding.labelCountdown.gone()
                binding.labelVoucherStatus.setLabel(getString(R.string.mvc_ended))
                binding.labelVoucherStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
                binding.button.text = getString(R.string.mvc_duplicate)
                binding.button.setOnClickListener {
                    tracker.sendDuplicateCouponClickEvent()
                    val couponId = viewModel.getCoupon()?.id.orZero().toLong()
                    DuplicateCouponActivity.start(requireActivity(), couponId)
                }
            }
            else -> {}
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
        if (viewModel.isOngoingCoupon(coupon.status)) {

            val progressBarValue = (coupon.confirmedQuota / coupon.quota) * PERCENT
            binding.progressBarQuotaUsage.setValue(progressBarValue, true)
            binding.progressBarQuotaUsage.progressBarHeight = requireActivity().pxToDp(PROGRESS_BAR_HEIGHT).toInt()

            binding.progressBarQuotaUsage.visible()
            binding.tpgTickerUsage.visible()
            binding.tpgTickerUsage.text = String.format(
                getString(R.string.placeholder_quota_usage),
                coupon.bookedQuota.toString()
            ).parseAsHtml()
            anchorQuotaCounterToQuotaProgressBar()
        } else {
            binding.progressBarQuotaUsage.gone()
            binding.tpgTickerUsage.gone()
            anchorQuotaCounterToQuotaLabel()
        }

        binding.tpgUsedQuota.text = coupon.confirmedQuota.toString()
        binding.tpgTotalQuota.text = String.format(
            getString(R.string.mvc_detail_total_quota).toBlankOrString(),
            coupon.quota.toString()
        )

        binding.tpgTickerUsage.setOnClickListener { displayExpenseEstimationDescription() }
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
        binding.content.visible()
    }

    private fun hideContent() {
        binding.card.gone()
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

    override fun onResume() {
        super.onResume()
        timer?.startCountdown()
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

    private fun displayShareBottomSheet(coupon: CouponUiModel, productImageUrls : List<String>, shop: ShopBasicDataResult) {
        val title = String.format(getString(R.string.placeholder_share_component_outgoing_title), shop.shopName)
        val endDate = coupon.finishTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
            .parseTo(DateTimeUtils.DATE_FORMAT)
        val endHour = coupon.finishTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
            .parseTo(DateTimeUtils.HOUR_FORMAT)
        val description = String.format(getString(R.string.placeholder_share_component_text_description), shop.shopName, endDate, endHour)


        shareComponentBottomSheet = shareComponentInstanceBuilder.build(
            coupon,
            shop.logo,
            shop.shopName,
            title,
            productImageUrls,
            onShareOptionsClicked = { shareModel ->
                sharingComponentTracker.sendSelectShareChannelClickEvent(
                    shareModel.channel.orEmpty(),
                    coupon.id.toString()
                )
                handleShareOptionSelection(
                    coupon.galadrielVoucherId,
                    shareModel,
                    title,
                    description,
                    shop.shopDomain
                )
            },
            onCloseOptionClicked = {
                sharingComponentTracker.sendShareBottomSheetDismissClickEvent(coupon.id.toString())
            })

        sharingComponentTracker.sendShareBottomSheetDisplayedEvent(coupon.id.toString())

        shareComponentBottomSheet?.show(childFragmentManager, shareComponentBottomSheet?.tag)
    }

    private fun showError(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(requireActivity(), throwable)
        Toaster.build(binding.root, errorMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }

    private fun handleShareOptionSelection(
        galadrielVoucherId: Long,
        shareModel: ShareModel,
        title: String,
        description: String,
        shopDomain: String
    ) {
        val shareCallback = object : ShareCallback {
            override fun urlCreated(linkerShareData: LinkerShareResult?) {
                val wording = "$description ${linkerShareData?.shareUri.orEmpty()}"
                SharingUtil.executeShareIntent(
                    shareModel,
                    linkerShareData,
                    activity,
                    view,
                    wording
                )
                shareComponentBottomSheet?.dismiss()
            }

            override fun onError(linkerError: LinkerError?) {}
        }

        val outgoingDescription = getString(R.string.share_component_outgoing_text_description)
        val linkerShareData = linkerDataGenerator.generate(
            galadrielVoucherId,
            userSession.shopId,
            shopDomain,
            shareModel,
            title,
            outgoingDescription
        )
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                NumberConstant.ZERO,
                linkerShareData,
                shareCallback
            )
        )
    }

    private fun anchorQuotaCounterToQuotaLabel() {
        val set = ConstraintSet()
        set.clone(binding.layout)
        set.connect(
            binding.tpgUsedQuota.id,
            ConstraintSet.TOP,
            binding.typographyQuota.id,
            ConstraintSet.TOP
        )
        set.connect(
            binding.tpgUsedQuota.id,
            ConstraintSet.BOTTOM,
            binding.typographyQuota.id,
            ConstraintSet.BOTTOM
        )
        set.applyTo(binding.layout)
    }

    private fun anchorQuotaCounterToQuotaProgressBar() {
        val set = ConstraintSet()
        set.clone(binding.layout)
        set.connect(
            binding.tpgUsedQuota.id,
            ConstraintSet.TOP,
            binding.progressBarQuotaUsage.id,
            ConstraintSet.TOP
        )
        set.connect(
            binding.tpgUsedQuota.id,
            ConstraintSet.BOTTOM,
            binding.progressBarQuotaUsage.id,
            ConstraintSet.BOTTOM
        )
        set.applyTo(binding.layout)
    }
}