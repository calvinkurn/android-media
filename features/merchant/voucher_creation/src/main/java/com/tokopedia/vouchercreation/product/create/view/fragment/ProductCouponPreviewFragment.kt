package com.tokopedia.vouchercreation.product.create.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.ClipboardHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.consts.UrlConstant
import com.tokopedia.vouchercreation.common.consts.VoucherUrl
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.extension.splitByThousand
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.common.utils.HyperlinkClickHandler
import com.tokopedia.vouchercreation.common.utils.setFragmentToUnifyBgColor
import com.tokopedia.vouchercreation.databinding.FragmentProductCouponPreviewBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.CouponImagePreviewBottomSheet
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.ExpenseEstimationBottomSheet
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.TermAndConditionBottomSheet
import com.tokopedia.vouchercreation.product.create.view.dialog.CreateProductCouponFailedDialog
import com.tokopedia.vouchercreation.product.create.view.dialog.UpdateProductCouponFailedDialog
import com.tokopedia.vouchercreation.product.create.view.viewmodel.ProductCouponPreviewViewModel
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherCreationStep
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject


class ProductCouponPreviewFragment: BaseDaggerFragment() {

    companion object {
        private const val EMPTY_STRING = ""
        private const val SCREEN_NAME = "Product coupon preview page"
        private const val ZERO: Long = 0
        private const val ROTATION_ANGLE_ZERO = 0f
        private const val ROTATION_ANGLE_HALF_CIRCLE = 180f
        private const val ROTATION_ANIM_DURATION_IN_MILLIS: Long = 300
        const val COUPON_ID_NOT_YET_CREATED : Long = -1

        fun newInstance(
            onNavigateToCouponInformationPage: () -> Unit,
            onNavigateToCouponSettingsPage: () -> Unit,
            onNavigateToProductListPage: (Coupon) -> Unit,
            onCreateCouponSuccess: (Coupon) -> Unit,
            onUpdateCouponSuccess: () -> Unit,
            onDuplicateCouponSuccess: () -> Unit,
            couponId: Long,
            mode : Mode
        ): ProductCouponPreviewFragment {

            val fragment = ProductCouponPreviewFragment().apply {
                this.onNavigateToCouponInformationPage = onNavigateToCouponInformationPage
                this.onNavigateToCouponSettingsPage = onNavigateToCouponSettingsPage
                this.onNavigateToProductListPage = onNavigateToProductListPage
                this.onCreateCouponSuccess = onCreateCouponSuccess
                this.onUpdateCouponSuccess = onUpdateCouponSuccess
                this.onDuplicateCouponSuccess = onDuplicateCouponSuccess
                this.pageMode = mode
                this.couponId = couponId
            }

            return fragment
        }

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var nullableBinding by autoClearedNullable<FragmentProductCouponPreviewBinding>()
    private val binding: FragmentProductCouponPreviewBinding
        get() = requireNotNull(nullableBinding)

    private var onNavigateToCouponInformationPage: () -> Unit = {}
    private var onNavigateToCouponSettingsPage: () -> Unit = {}
    private var onNavigateToProductListPage: (Coupon) -> Unit = {}
    private var onDuplicateCouponSuccess: () -> Unit = {}
    private var onUpdateCouponSuccess: () -> Unit = {}
    private var onCreateCouponSuccess: (Coupon) -> Unit = {}
    private var couponSettings: CouponSettings? = null
    private var couponInformation: CouponInformation? = null
    private var couponProducts: List<CouponProduct> = emptyList()
    private var isCardExpanded = true
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductCouponPreviewViewModel::class.java) }
    private var couponId : Long = -1
    private var maxAllowedProduct = 0

    private val createCouponErrorNotice by lazy {
        CreateProductCouponFailedDialog(requireActivity(), ::onRetryCreateCoupon, ::onRequestHelp)
    }

    private val updateCouponErrorNotice by lazy {
        UpdateProductCouponFailedDialog(requireActivity(), ::onRetryUpdateCoupon, ::onRequestHelp)
    }

    private var pageMode = Mode.CREATE

    private val CouponType.label: String
        get() {
            return when (this) {
                CouponType.CASHBACK -> getString(R.string.mvc_cashback)
                CouponType.FREE_SHIPPING -> getString(R.string.mvc_free_shipping)
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

    private val MinimumPurchaseType.label: String
        get() {
            return when (this) {
                MinimumPurchaseType.NOMINAL -> getString(R.string.nominal)
                MinimumPurchaseType.QUANTITY -> getString(R.string.item)
                MinimumPurchaseType.NOTHING -> getString(R.string.nothing)
                else -> EMPTY_STRING
            }
        }

    enum class Mode {
        CREATE,
        UPDATE,
        DUPLICATE
    }

    override fun getScreenName() = SCREEN_NAME
    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when(pageMode) {
            Mode.CREATE -> {
                viewModel.validateCoupon(couponSettings, couponInformation, couponProducts)
            }
            Mode.UPDATE -> {
                viewModel.getCouponDetail(couponId)

            }
            Mode.DUPLICATE -> {
                viewModel.getCouponDetail(couponId)

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        nullableBinding = FragmentProductCouponPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentToUnifyBgColor()
        setupViews()
        observeCouponDetail()
        observeValidCoupon()
        observeCreateCouponResult()
        observeUpdateCouponResult()
        observeMaxAllowedProductResult()
        viewModel.getMaxAllowedProducts(pageMode)
        handlePageMode()
    }

    private fun observeCouponDetail() {
        viewModel.couponDetail.observe(viewLifecycleOwner, { result ->
            hideLoading()
            when(result) {
                is Success -> {
                    showContent()
                    this.couponInformation = result.data.information
                    this.couponProducts = result.data.products
                    this.couponSettings = result.data.settings

                    refreshCouponInformationSection(couponInformation ?: return@observe)
                    refreshCouponSettingsSection(couponSettings ?: return@observe)
                    refreshProductsSection(couponProducts)
                }
                is Fail -> {
                    hideLoading()
                    hideContent()
                    showError(result.throwable)
                }
            }
        })
    }

    private fun handlePageMode() {
        when(pageMode) {
            Mode.CREATE -> {
            }
            Mode.UPDATE -> {
                changeToolbarTitle(getString(R.string.update_coupon_product))
                changeButtonBehavior()
                enableSubmitButton()
                enableImagePreviewButton()
            }
            Mode.DUPLICATE -> {
                enableSubmitButton()
                enableImagePreviewButton()
            }
        }
    }

    private fun changeButtonBehavior() {
        binding.btnCreateCoupon.text = getString(R.string.save_changes)
        binding.btnCreateCoupon.setOnClickListener {
            updateCoupon(couponId)
        }
    }

    private fun enableImagePreviewButton() {
        binding.btnPreviewCouponImage.isEnabled = true
    }

    private fun enableSubmitButton() {
        binding.btnCreateCoupon.isEnabled = true
    }


    private fun setupViews() {
        binding.tpgReadArticle.setOnClickListener { redirectToSellerEduPage() }
        binding.tpgCouponInformation.setOnClickListener { onNavigateToCouponInformationPage() }
        binding.tpgCouponSetting.setOnClickListener { onNavigateToCouponSettingsPage() }
        binding.tpgAddProduct.setOnClickListener { navigateToProductListPage() }
        binding.btnCreateCoupon.setOnClickListener { createCoupon() }
        binding.btnPreviewCouponImage.setOnClickListener { displayCouponPreviewBottomSheet() }
        binding.imgExpenseEstimationDescription.setOnClickListener { displayExpenseEstimationDescription() }

        binding.tpgTermAndConditions.movementMethod = object : HyperlinkClickHandler() {
            override fun onLinkClick(url: String?) {
                displayTermAndConditionBottomSheet()
            }

        }

        binding.imgDropdown.setOnClickListener { handleCouponProductInformationVisibility() }
        binding.imgCopyToClipboard.setOnClickListener {
            val content = binding.tpgCouponCode.text.toString().trim()
            copyToClipboard(content)
        }

    }

    private fun changeToolbarTitle(title: String) {
        binding.header.headerView?.text = title
    }

    private fun observeValidCoupon() {
        viewModel.areInputValid.observe(viewLifecycleOwner, { areInputValid ->
            binding.btnCreateCoupon.isEnabled = areInputValid
            binding.btnPreviewCouponImage.isEnabled = areInputValid
        })
    }


    private fun observeCreateCouponResult() {
        viewModel.createCoupon.observe(viewLifecycleOwner, { result ->
            binding.btnCreateCoupon.isLoading = false
            when(result) {
                is Success -> {
                    this.couponId = result.data.toLong()
                    val coupon = Coupon(
                        result.data.toLong(),
                        couponInformation ?: return@observe,
                        couponSettings ?: return@observe,
                        couponProducts
                    )

                    if (pageMode == Mode.CREATE) {
                        onCreateCouponSuccess(coupon)
                    } else {
                        onDuplicateCouponSuccess()
                    }
                }
                is Fail -> {
                    showError(result.throwable)
                    createCouponErrorNotice.show()
                }
            }
        })
    }


    private fun observeUpdateCouponResult() {
        viewModel.updateCouponResult.observe(viewLifecycleOwner, { result ->
            binding.btnCreateCoupon.isLoading = false
            when (result) {
                is Success -> {
                    onUpdateCouponSuccess()
                }
                is Fail -> {
                    showError(result.throwable)
                    updateCouponErrorNotice.show()
                }
            }
        })
    }

    private fun observeMaxAllowedProductResult() {
        viewModel.maxAllowedProductCount.observe(viewLifecycleOwner, { result ->
            binding.loader.gone()
            when (result) {
                is Success -> {
                    binding.content.visible()
                    binding.tpgMaxProduct.text = String.format(getString(R.string.placeholder_max_product), result.data)
                    this.maxAllowedProduct = result.data
                }
                is Fail -> {
                    binding.content.gone()
                    showError(result.throwable)
                }
            }
        })
    }


    fun setCouponSettingsData(couponSettings: CouponSettings) {
        this.couponSettings = couponSettings
    }

    fun setCouponProductsData(couponProducts: List<CouponProduct>) {
        this.couponProducts = couponProducts
    }

    fun setCouponInformationData(couponInformation: CouponInformation) {
        this.couponInformation = couponInformation
    }

    fun getCouponInformationData() = this.couponInformation
    fun getCouponSettingsData() = this.couponSettings


    override fun onResume() {
        super.onResume()
        couponInformation?.let { coupon -> refreshCouponInformationSection(coupon) }
        couponSettings?.let { coupon -> refreshCouponSettingsSection(coupon) }
        refreshProductsSection(couponProducts)
    }

    private fun refreshCouponInformationSection(coupon: CouponInformation) {
        binding.imgCopyToClipboard.visible()

        if (viewModel.isCouponInformationValid(coupon)) {
            binding.labelCouponInformationCompleteStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
            binding.labelCouponInformationCompleteStatus.setLabel(getString(R.string.completed))
        }

        val target = when (coupon.target) {
            CouponInformation.Target.PUBLIC -> getString(R.string.mvc_public)
            CouponInformation.Target.PRIVATE -> getString(R.string.mvc_special)
            CouponInformation.Target.NOT_SELECTED -> getString(R.string.hyphen)
        }
        binding.tpgCouponTarget.text = target

        handleCouponName(coupon.target, coupon.name)
        handleCouponCodeVisibility(coupon.code, coupon.target)

        val startDate = coupon.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val startHour = coupon.period.startDate.parseTo(DateTimeUtils.HOUR_FORMAT)
        val endDate = coupon.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val endHour = coupon.period.endDate.parseTo(DateTimeUtils.HOUR_FORMAT)

        val period = String.format(
            getString(R.string.placeholder_coupon_period),
            startDate,
            startHour,
            endDate,
            endHour
        )
        binding.tpgCouponPeriod.text = period
    }

    private fun handleCouponName(target: CouponInformation.Target, couponName : String) {
        if (target == CouponInformation.Target.NOT_SELECTED) {
            binding.tpgCouponName.text = getString(R.string.hyphen)
        } else {
            binding.tpgCouponName.text = couponName
        }
    }

    private fun handleCouponCodeVisibility(couponCode: String, target: CouponInformation.Target) {
        when (target) {
            CouponInformation.Target.PUBLIC -> binding.groupCouponCode.gone()
            CouponInformation.Target.PRIVATE -> binding.groupCouponCode.visible()
            CouponInformation.Target.NOT_SELECTED -> binding.groupCouponCode.gone()
        }

        binding.tpgCouponCode.text = couponCode
    }

    private fun refreshCouponSettingsSection(coupon: CouponSettings) {
        binding.labelCouponSettingCompleteStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
        binding.labelCouponSettingCompleteStatus.setLabel(getString(R.string.completed))

        binding.tpgCouponType.text = coupon.type.label
        binding.tpgDiscountType.text = coupon.discountType.label

        binding.tpgCouponQouta.text = coupon.quota.splitByThousand()

        handleDiscountType(coupon.type)
        handleDiscountAmount(
            coupon.type,
            coupon.discountType,
            coupon.discountAmount,
            coupon.discountPercentage
        )
        handleMaximumDiscount(coupon.type, coupon.discountType, coupon.maxDiscount)
        handleMinimumPurchaseType(coupon.type, coupon.minimumPurchaseType)
        handleMinimumPurchase(coupon.type, coupon.minimumPurchaseType, coupon.minimumPurchase)
        handleEstimatedMaxExpense(coupon.estimatedMaxExpense)
    }

    private fun handleDiscountType(couponType: CouponType) {
        if (couponType == CouponType.FREE_SHIPPING) {
            binding.groupDiscountType.gone()
        } else {
            binding.groupDiscountType.visible()
        }
    }

    private fun refreshProductsSection(products: List<CouponProduct>) {
        binding.tpgUpdateProduct.isVisible = products.isNotEmpty()
        if (products.isNotEmpty()) {
            binding.labelProductCompleteStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
            binding.labelProductCompleteStatus.setLabel(getString(R.string.completed))

            binding.tpgProductCount.text =
                String.format(getString(R.string.placeholder_registered_product), products.size, maxAllowedProduct)
        } else {
            binding.labelProductCompleteStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
            binding.labelProductCompleteStatus.setLabel(getString(R.string.incomplete))

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

    private fun redirectToSellerEduPage() {
        if (!isAdded) return
        val url = UrlConstant.SELLER_HOSTNAME + UrlConstant.PRODUCT_COUPON
        val encodedUrl = URLEncoder.encode(url, "utf-8")
        val route = String.format("%s?url=%s", ApplinkConst.WEBVIEW, encodedUrl)
        RouteManager.route(requireActivity(), route)
    }

    private fun displayExpenseEstimationDescription() {
        if (!isAdded) return
        val bottomSheet = ExpenseEstimationBottomSheet.newInstance()
        bottomSheet.show(childFragmentManager)
    }

    private fun handleCouponProductInformationVisibility() {
        if (isCardExpanded) {
            binding.groupCouponProductInformation.gone()
            binding.imgDropdown.animate().rotation(ROTATION_ANGLE_HALF_CIRCLE).setDuration(
                ROTATION_ANIM_DURATION_IN_MILLIS
            ).start()
        } else {
            binding.groupCouponProductInformation.visible()
            binding.imgDropdown.animate().rotation(ROTATION_ANGLE_ZERO)
                .setDuration(ROTATION_ANIM_DURATION_IN_MILLIS).start()
        }

        isCardExpanded = !isCardExpanded
    }

    private fun copyToClipboard(content: String) {
        ClipboardHandler().copyToClipboard(requireActivity(), content)
        Toaster.build(
            binding.root,
            getString(R.string.coupon_code_copied_to_clipboard)
        ).show()
    }

    private fun createCoupon() {
        binding.btnCreateCoupon.isLoading = true
        binding.btnCreateCoupon.loadingText = getString(R.string.please_wait)

        viewModel.createCoupon(
            couponInformation ?: return,
            couponSettings ?: return,
            couponProducts
        )
    }

    private fun updateCoupon(couponId : Long) {
        binding.btnCreateCoupon.isLoading = true
        binding.btnCreateCoupon.loadingText = getString(R.string.please_wait)

        viewModel.updateCoupon(
            couponId,
            couponInformation ?: return,
            couponSettings ?: return,
            couponProducts
        )
    }


    private fun onRetryCreateCoupon() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
            step = VoucherCreationStep.REVIEW,
            action = VoucherCreationAnalyticConstant.EventAction.Click.FAILED_POP_UP_TRY_AGAIN,
            userId = userSession.userId
        )
        createCouponErrorNotice.dismiss()
        createCoupon()
    }

    private fun onRetryUpdateCoupon() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
            step = VoucherCreationStep.REVIEW,
            action = VoucherCreationAnalyticConstant.EventAction.Click.FAILED_POP_UP_TRY_AGAIN,
            userId = userSession.userId
        )
        updateCouponErrorNotice.dismiss()
        updateCoupon(couponId)
    }

    private fun onRequestHelp() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
            step = VoucherCreationStep.REVIEW,
            action = VoucherCreationAnalyticConstant.EventAction.Click.FAILED_POP_UP_HELP,
            userId = userSession.userId
        )
        createCouponErrorNotice.dismiss()
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, VoucherUrl.HELP_URL)
    }


    private fun displayTermAndConditionBottomSheet() {
        val bottomSheet = TermAndConditionBottomSheet.newInstance(
            requireActivity(),
            getString(R.string.coupon_tnc)
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun displayCouponPreviewBottomSheet() {
        val imageUrls = viewModel.findMostSoldProductImageUrls(couponProducts)
        val bottomSheet = CouponImagePreviewBottomSheet.newInstance(
            couponInformation ?: return,
            couponSettings ?: return,
            couponProducts.size,
            imageUrls
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }


    private fun showError(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(requireActivity(), throwable)
        Toaster.build(binding.root, errorMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }

    private fun navigateToProductListPage() {
        val coupon = Coupon(
            COUPON_ID_NOT_YET_CREATED,
            couponInformation ?: return,
            couponSettings ?: return,
            couponProducts
        )
        onNavigateToProductListPage(coupon)
    }


    private fun hideLoading() {
        binding.loader.gone()
    }

    private fun showContent() {
        binding.content.visible()
    }

    private fun hideContent() {
        binding.content.gone()
    }

}