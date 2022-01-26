package com.tokopedia.vouchercreation.product.create.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
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
import com.tokopedia.vouchercreation.common.utils.SharingUtil
import com.tokopedia.vouchercreation.databinding.FragmentProductCouponPreviewBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.BroadcastCouponBottomSheet
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.CouponPreviewBottomSheet
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.ExpenseEstimationBottomSheet
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.TermAndConditionBottomSheet
import com.tokopedia.vouchercreation.product.create.view.dialog.CreateProductCouponFailedDialog
import com.tokopedia.vouchercreation.product.create.view.dialog.UpdateProductCouponFailedDialog
import com.tokopedia.vouchercreation.product.create.view.viewmodel.ProductCouponPreviewViewModel
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherCreationStep
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject


class ProductCouponPreviewFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_COUPON = "coupon"
        private const val EMPTY_STRING = ""
        private const val SCREEN_NAME = "Product coupon preview page"
        private const val ZERO: Long = 0
        private const val ROTATION_ANGLE_ZERO = 0f
        private const val ROTATION_ANGLE_HALF_CIRCLE = 180f
        private const val ROTATION_ANIM_DURATION_IN_MILLIS : Long = 300

        fun newInstance(): ProductCouponPreviewFragment {
            return ProductCouponPreviewFragment()
        }

        fun newInstance(coupon: Coupon): ProductCouponPreviewFragment {
            val args = Bundle()
            args.putSerializable(BUNDLE_KEY_COUPON, coupon)
            val fragment = ProductCouponPreviewFragment()
            fragment.arguments = args
            return fragment
        }

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession : UserSessionInterface

    private var nullableBinding by autoClearedNullable<FragmentProductCouponPreviewBinding>()
    private val binding: FragmentProductCouponPreviewBinding
        get() = requireNotNull(nullableBinding)
    
    private var onNavigateToCouponInformationPage: () -> Unit = {}
    private var onNavigateToCouponSettingsPage: () -> Unit = {}
    private var onNavigateToProductListPage: () -> Unit = {}
    private var onUpdateCouponSuccess : ()-> Unit = {}
    private var couponSettings: CouponSettings? = null
    private var couponInformation: CouponInformation? = null
    private var couponProducts: List<CouponProduct> = emptyList()
    private var isCardExpanded = true
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductCouponPreviewViewModel::class.java) }
    private var couponId = -1
    private val createCouponErrorNotice by lazy {
        CreateProductCouponFailedDialog(requireActivity(), ::onRetryCreateCoupon, ::onRequestHelp)
    }

    private val updateCouponErrorNotice by lazy {
        UpdateProductCouponFailedDialog(requireActivity(), ::onRetryUpdateCoupon, ::onRequestHelp)
    }

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

    override fun getScreenName() = SCREEN_NAME
    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
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
        setupViews()
        observeValidCoupon()
        observeCreateCouponResult()
        observeShareMetaDataResult()
        observeUpdateCouponResult()

        if(isUpdateMode()) {
            changeToolbarTitle(getString(R.string.update_coupon_product))
            changeButtonBehavior()
            displayCouponDetail()
        }
    }

    private fun changeButtonBehavior() {
        binding.btnCreateCoupon.text = getString(R.string.save_changes)
        binding.btnCreateCoupon.setOnClickListener { updateCoupon() }
    }

    private fun isUpdateMode() : Boolean {
        val coupon : Coupon? = arguments?.getSerializable(BUNDLE_KEY_COUPON) as? Coupon
        return coupon != null
    }

    private fun displayCouponDetail() {
        val coupon : Coupon = arguments?.getSerializable(BUNDLE_KEY_COUPON) as? Coupon ?: return
        this.couponSettings = coupon.settings
        this.couponProducts = coupon.products
        this.couponInformation = coupon.information
    }

    private fun setupViews() {
        binding.tpgReadArticle.setOnClickListener { redirectToSellerEduPage() }
        binding.tpgCouponInformation.setOnClickListener { onNavigateToCouponInformationPage() }
        binding.tpgCouponSetting.setOnClickListener { onNavigateToCouponSettingsPage() }
        binding.tpgAddProduct.setOnClickListener { onNavigateToProductListPage() }
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

    private fun changeToolbarTitle(title : String) {
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
            if (result is Success) {
                this.couponId = result.data
                viewModel.getShareMetaData()
            } else {
                createCouponErrorNotice.show()
            }
        })
    }

    private fun observeShareMetaDataResult() {
        viewModel.shareMetadata.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    /*val startDate = Calendar.getInstance().apply { set(2022, 0, 25, 22, 30, 0) }
                    val endDate = Calendar.getInstance().apply {  set(2022, 0, 30, 22, 0, 0) }
                    val period = CouponInformation.Period(startDate.time, endDate.time)

                    showBroadCastVoucherBottomSheet(
                        CouponInformation(CouponInformation.Target.SPECIAL, "Kenangan", "KOPKEN", period),
                        result.data.promo,
                        result.data.shopName
                    )*/

                    showBroadCastVoucherBottomSheet(
                        couponInformation ?: return@observe,
                        result.data.promo,
                        result.data.shopName
                    )
                }
                is Fail -> {
                    showBroadCastVoucherBottomSheetWithoutShareToSocialMediaCapability()
                }
            }
        })
    }

    private fun observeUpdateCouponResult() {
        viewModel.updateCouponResult.observe(viewLifecycleOwner, { result ->
            when(result) {
                is Success -> {
                    onUpdateCouponSuccess()
                    Toaster.build(
                        binding.root,
                        getString(R.string.coupon_updated)
                    ).show()
                }
                is Fail -> {
                    updateCouponErrorNotice.show()
                }
            }
        })
    }


    fun setOnNavigateToCouponInformationPageListener(onNavigateToCouponInformationPage: () -> Unit) {
        this.onNavigateToCouponInformationPage = onNavigateToCouponInformationPage
    }

    fun setOnNavigateToCouponSettingsPageListener(onNavigateToCouponSettingsPage: () -> Unit) {
        this.onNavigateToCouponSettingsPage = onNavigateToCouponSettingsPage
    }

    fun setOnNavigateToProductListPageListener(onNavigateToProductListPage: () -> Unit) {
        this.onNavigateToProductListPage = onNavigateToProductListPage
    }

    fun setCouponSettingsData(couponSettings: CouponSettings) {
        this.couponSettings = couponSettings
    }

    fun setCouponProductsData(couponProducts : List<CouponProduct>) {
        this.couponProducts = couponProducts
    }

    fun setCouponInformationData(couponInformation : CouponInformation) {
        this.couponInformation =  couponInformation
    }

    fun setOnUpdateCouponSuccess(onUpdateCouponSuccess : ()-> Unit) {
        this.onUpdateCouponSuccess = onUpdateCouponSuccess
    }


    override fun onResume() {
        super.onResume()
        couponInformation?.let { coupon ->  refreshCouponInformationSection(coupon) }
        couponSettings?.let { coupon -> refreshCouponSettingsSection(coupon) }
        refreshProductsSection(couponProducts)

        viewModel.validateCoupon(couponSettings, couponInformation, couponProducts)
    }

    private fun refreshCouponInformationSection(coupon: CouponInformation) {
        binding.imgCopyToClipboard.visible()
        binding.labelCouponInformationCompleteStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
        binding.labelCouponInformationCompleteStatus.setLabel(getString(R.string.completed))

        val target = when (coupon.target) {
            CouponInformation.Target.PUBLIC -> getString(R.string.mvc_public)
            CouponInformation.Target.SPECIAL -> getString(R.string.mvc_special)
        }
        binding.tpgCouponTarget.text = target

        binding.tpgCouponName.text = coupon.name
        handleCouponCodeVisibility(coupon.code, coupon.target)

        val startDate = coupon.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val startHour = coupon.period.startDate.parseTo(DateTimeUtils.HOUR_FORMAT)
        val endDate = coupon.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val endHour = coupon.period.endDate.parseTo(DateTimeUtils.HOUR_FORMAT)

        val period = String.format(getString(R.string.placeholder_coupon_period), startDate, startHour, endDate, endHour)
        binding.tpgCouponPeriod.text = period
    }

    private fun handleCouponCodeVisibility(couponCode : String, target: CouponInformation.Target) {
        when (target) {
            CouponInformation.Target.PUBLIC -> binding.groupCouponCode.gone()
            CouponInformation.Target.SPECIAL -> binding.groupCouponCode.visible()
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
        handleDiscountAmount(coupon.type, coupon.discountType, coupon.discountAmount, coupon.discountPercentage)
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

            binding.tpgProductCount.text = String.format(getString(R.string.placeholder_registered_product), products.size)
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

    private fun handleMaximumDiscount(couponType : CouponType, discountType: DiscountType, maxDiscount: Int) {
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
            binding.groupMinimumPurchaseType.visible()
            binding.tpgMinimumPurchaseType.text = minimumPurchaseType.label
        }

    }


    private fun handleMinimumPurchase(couponType: CouponType, minimumPurchaseType: MinimumPurchaseType, minimumPurchase: Int) {
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
                couponType == CouponType.CASHBACK && minimumPurchaseType == MinimumPurchaseType.NOTHING -> getString(R.string.nothing)
                else -> EMPTY_STRING
            }

            binding.tpgMinimumPurchase.text = text

        } else {
            binding.groupMinimumPurchase.gone()
        }

    }

    private fun redirectToSellerEduPage() {
        if (!isAdded) return
        val url = UrlConstant.SELLER_HOSTNAME + UrlConstant.SELLER_EDU
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
        viewModel.createCoupon(
            ImageGeneratorConstants.ImageGeneratorSourceId.RILISAN_SPESIAL,
            couponInformation ?: return,
            couponSettings ?: return,
            couponProducts
        )
    }

    private fun updateCoupon() {
        viewModel.updateCoupon(
            ImageGeneratorConstants.ImageGeneratorSourceId.RILISAN_SPESIAL,
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
        updateCoupon()
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

    private fun showBroadCastVoucherBottomSheet(couponInformation: CouponInformation, freeBroadcastQuota : Int, shopName : String) {
        val bottomSheet = BroadcastCouponBottomSheet.newInstance(couponInformation, freeBroadcastQuota)
        bottomSheet.setOnBroadCastClickListener {
            VoucherCreationTracking.sendBroadCastChatClickTracking(
                category = VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.PAGE,
                shopId = userSession.shopId
            )
            SharingUtil.shareToBroadCastChat(requireContext(), couponId)
        }
        bottomSheet.setOnShareToSocialMediaClickListener { displayShareBottomSheet(shopName, couponInformation) }
        bottomSheet.setCloseClickListener {
            VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.REVIEW,
                action = VoucherCreationAnalyticConstant.EventAction.Click.VOUCHER_SUCCESS_CLICK_BACK_BUTTON,
                userId = userSession.userId
            )
            bottomSheet.dismiss()
        }
        bottomSheet.clearContentPadding = true
        bottomSheet.show(childFragmentManager)
    }

    private fun showBroadCastVoucherBottomSheetWithoutShareToSocialMediaCapability() {
        val modifiedCouponInformation = couponInformation?.copy(target = CouponInformation.Target.SPECIAL)
        showBroadCastVoucherBottomSheet(modifiedCouponInformation ?: return, Int.ZERO, EMPTY_STRING)
    }

    private fun displayTermAndConditionBottomSheet() {
        val bottomSheet = TermAndConditionBottomSheet.newInstance(requireActivity(), getString(R.string.coupon_tnc))
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun displayCouponPreviewBottomSheet() {
        val imageUrls = viewModel.getMostSoldProductImageUrls(couponProducts)
        val bottomSheet = CouponPreviewBottomSheet.newInstance(
            couponInformation ?: return,
            couponSettings ?: return,
            couponProducts.size,
            imageUrls
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun displayShareBottomSheet(shopName : String, couponInformation: CouponInformation) {
        //TODO implement share component
        val startDate = couponInformation.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT_DAY_MONTH)
        val endDate = couponInformation.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT_DAY_MONTH)

        val template = getString(R.string.placeholder_share_coupon_product_wording)
        val wording = String.format(template, shopName, startDate, endDate, "")

    }
}