package com.tokopedia.vouchercreation.product.create.view.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.view.bottomsheet.ClipboardHandler
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.UrlConstant
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.extension.splitByThousand
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.databinding.FragmentProductCouponPreviewBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.ExpenseEstimationBottomSheet
import com.tokopedia.vouchercreation.product.create.view.viewmodel.ProductCouponPreviewViewModel
import java.net.URLEncoder
import javax.inject.Inject


class ProductCouponPreviewFragment : BaseDaggerFragment() {

    companion object {
        private const val EMPTY_STRING = ""
        private const val SCREEN_NAME = "Product coupon preview page"
        private const val ZERO: Long = 0

        private const val ALPHA_VISIBLE : Float  = 1.0f
        private const val ALPHA_NOT_VISIBLE : Float  = 0.0f
        private const val NO_TRANSLATION : Float  = 0.0f

        fun newInstance(): ProductCouponPreviewFragment {
            return ProductCouponPreviewFragment()
        }

    }

    private var binding by autoClearedNullable<FragmentProductCouponPreviewBinding>()
    private var onNavigateToCouponInformationPage: () -> Unit = {}
    private var onNavigateToCouponSettingsPage: () -> Unit = {}
    private var onNavigateToProductListPage: () -> Unit = {}
    private var couponSettings: CouponSettings? = null
    private var couponInformation: CouponInformation? = null
    private var couponProducts: List<CouponProduct> = emptyList()
    private var isCardExpanded = true

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

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductCouponPreviewViewModel::class.java) }

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
        binding = FragmentProductCouponPreviewBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeValidCoupon()
        observeCreateCouponResult()
    }

    private fun setupViews() {
        binding?.tpgReadArticle?.setOnClickListener { redirectToSellerEduPage() }
        binding?.tpgCouponInformation?.setOnClickListener { onNavigateToCouponInformationPage() }
        binding?.tpgCouponSetting?.setOnClickListener { onNavigateToCouponSettingsPage() }
        binding?.tpgAddProduct?.setOnClickListener { onNavigateToProductListPage() }
        binding?.btnCreateCoupon?.setOnClickListener {
            viewModel.createCoupon(
                ImageGeneratorConstants.ImageGeneratorSourceId.RILISAN_SPESIAL,
                false,
                couponInformation ?: return@setOnClickListener,
                couponSettings ?: return@setOnClickListener,
                couponProducts,
            )
        }
        binding?.imgExpenseEstimationDescription?.setOnClickListener { displayExpenseEstimationDescription() }
        binding?.tpgTermAndConditions?.movementMethod = LinkMovementMethod.getInstance()
        binding?.imgDropdown?.setOnClickListener { handleCouponProductInformationVisibility() }
        binding?.imgCopyToClipboard?.setOnClickListener {
            val content = binding?.tpgCouponCode?.text.toString().trim()
            copyToClipboard(content)
        }

    }

    private fun observeValidCoupon() {
        viewModel.areInputValid.observe(viewLifecycleOwner, { areInputValid ->
            binding?.btnCreateCoupon?.isEnabled = areInputValid
            binding?.btnPreviewCouponImage?.isEnabled = areInputValid
        })
    }


    private fun observeCreateCouponResult() {
        viewModel.createCoupon.observe(viewLifecycleOwner, { result ->
            if (result is Success) {

            } else {

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


    override fun onResume() {
        super.onResume()
        couponInformation?.let { coupon ->  refreshCouponInformationSection(coupon) }
        couponSettings?.let { coupon -> refreshCouponSettingsSection(coupon) }
        refreshProductsSection(couponProducts)

        viewModel.validateCoupon(couponSettings, couponInformation, couponProducts)
    }

    private fun refreshCouponInformationSection(coupon: CouponInformation) {
        binding?.imgCopyToClipboard?.visible()
        binding?.labelCouponInformationCompleteStatus?.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
        binding?.labelCouponInformationCompleteStatus?.setLabel(getString(R.string.completed))

        val target = when (coupon.target) {
            CouponInformation.Target.PUBLIC -> getString(R.string.mvc_public)
            CouponInformation.Target.SPECIAL -> getString(R.string.mvc_special)
        }

        binding?.tpgCouponTarget?.text = target
        binding?.tpgCouponName?.text = coupon.name
        binding?.tpgCouponCode?.text = coupon.code

        val startDate = coupon.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val startHour = coupon.period.startDate.parseTo(DateTimeUtils.HOUR_FORMAT)
        val endDate = coupon.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val endHour = coupon.period.endDate.parseTo(DateTimeUtils.HOUR_FORMAT)

        val period = String.format(getString(R.string.placeholder_coupon_period), startDate, startHour, endDate, endHour)
        binding?.tpgCouponPeriod?.text = period
    }

    private fun refreshCouponSettingsSection(coupon: CouponSettings) {
        binding?.labelCouponSettingCompleteStatus?.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
        binding?.labelCouponSettingCompleteStatus?.setLabel(getString(R.string.completed))

        binding?.tpgCouponType?.text = coupon.type.label
        binding?.tpgDiscountType?.text = coupon.discountType.label

        binding?.tpgCouponQouta?.text = coupon.quota.splitByThousand()

        handleDiscountType(coupon.type)
        handleDiscountAmount(coupon.type, coupon.discountType, coupon.discountAmount, coupon.discountPercentage)
        handleMaximumDiscount(coupon.type, coupon.discountType, coupon.maxDiscount)
        handleMinimumPurchaseType(coupon.type, coupon.minimumPurchaseType)
        handleMinimumPurchase(coupon.type, coupon.minimumPurchaseType, coupon.minimumPurchase)
        handleEstimatedMaxExpense(coupon.estimatedMaxExpense)
    }

    private fun handleDiscountType(couponType: CouponType) {
        if (couponType == CouponType.FREE_SHIPPING) {
            binding?.groupDiscountType?.gone()
        } else {
            binding?.groupDiscountType?.visible()
        }
    }

    private fun refreshProductsSection(products: List<CouponProduct>) {
        binding?.tpgUpdateProduct?.isVisible = products.isNotEmpty()
        if (products.isNotEmpty()) {
            binding?.labelProductCompleteStatus?.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
            binding?.labelProductCompleteStatus?.setLabel(getString(R.string.completed))

            binding?.tpgProductCount?.text = String.format(getString(R.string.placeholder_registered_product), products.size)
        } else {
            binding?.labelProductCompleteStatus?.setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
            binding?.labelProductCompleteStatus?.setLabel(getString(R.string.incomplete))

            binding?.tpgProductCount?.text = getString(R.string.no_products)
        }
    }

    private fun handleDiscountAmount(
        couponType: CouponType,
        discountType: DiscountType,
        discountAmount: Int,
        discountPercentage: Int
    ) {
        if (discountAmount > ZERO) {
            binding?.groupDiscountAmount?.visible()
        } else {
            binding?.groupDiscountAmount?.gone()
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

        binding?.tpgDiscountAmountLabel?.text = discountAmountLabel
        binding?.tpgDiscountAmount?.text = formattedDiscountAmount
    }

    private fun handleMaximumDiscount(couponType : CouponType, discountType: DiscountType, maxDiscount: Int) {
        if (couponType == CouponType.CASHBACK && discountType == DiscountType.PERCENTAGE) {
            binding?.groupMaxDiscount?.visible()
            binding?.tpgMaxDiscount?.text =
                String.format(getString(R.string.placeholder_rupiah), maxDiscount.splitByThousand())
        } else {
            binding?.groupMaxDiscount?.gone()
        }
    }

    private fun handleEstimatedMaxExpense(estimatedMaxExpense: Long) {
        if (estimatedMaxExpense == ZERO) {
            binding?.tpgExpenseAmount?.text = getString(R.string.hyphen)
        } else {
            binding?.tpgExpenseAmount?.text = String.format(
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
            binding?.groupMinimumPurchaseType?.gone()
        } else {
            binding?.groupMinimumPurchaseType?.visible()
            binding?.tpgMinimumPurchaseType?.text = minimumPurchaseType.label
        }

    }


    private fun handleMinimumPurchase(couponType: CouponType, minimumPurchaseType: MinimumPurchaseType, minimumPurchase: Int) {
        if (minimumPurchase > ZERO) {
            binding?.groupMinimumPurchase?.visible()
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

            binding?.tpgMinimumPurchase?.text = text

        } else {
            binding?.groupMinimumPurchase?.gone()
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
            collapseCouponProductDescription()
        } else {
            expandCouponProductDescription()
        }

        isCardExpanded = !isCardExpanded
    }

    private fun copyToClipboard(content: String) {
        ClipboardHandler().copyToClipboard(requireActivity(), content)
        Toaster.build(
            binding?.root ?: return,
            getString(R.string.coupon_code_copied_to_clipboard)
        ).show()
    }

    private fun expandCouponProductDescription() {
        animateExpand()
        binding?.imgDropdown?.setImageResource(R.drawable.ic_chevron_up)
    }

    private fun collapseCouponProductDescription() {
        binding?.imgDropdown?.setImageResource(R.drawable.ic_mvc_chevron_down)
        animateCollapse()
    }

    private fun animateCollapse() {
        val view = binding?.groupCouponProductInformation ?: return
        view.animate()
            .translationY(NO_TRANSLATION)
            .alpha(ALPHA_NOT_VISIBLE)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    view.gone()
                }
            })
    }

    private fun animateExpand() {
        val view = binding?.groupCouponProductInformation ?: return
        view.alpha = ALPHA_NOT_VISIBLE

        view.animate()
            .translationY(view.height.toFloat())
            .alpha(ALPHA_VISIBLE)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    view.visible()
                }
            })
    }
}