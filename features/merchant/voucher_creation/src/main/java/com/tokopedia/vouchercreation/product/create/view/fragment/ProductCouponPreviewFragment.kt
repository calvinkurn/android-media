package com.tokopedia.vouchercreation.product.create.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.Label
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.extension.splitByThousand
import com.tokopedia.vouchercreation.databinding.FragmentProductCouponPreviewBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.view.viewmodel.ProductCouponPreviewViewModel
import javax.inject.Inject


class ProductCouponPreviewFragment : BaseDaggerFragment() {

    companion object {
        private const val EMPTY_STRING = ""
        private const val SCREEN_NAME = "Product coupon preview page"
        private const val ZERO: Long = 0

        fun newInstance(): ProductCouponPreviewFragment {
            val args = Bundle()
            val fragment = ProductCouponPreviewFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private var binding by autoClearedNullable<FragmentProductCouponPreviewBinding>()
    private var onNavigateToCouponInformationPage: () -> Unit = {}
    private var onNavigateToCouponSettingsPage: () -> Unit = {}
    private var onNavigateToProductListPage: () -> Unit = {}
    private var couponSettings: CouponSettings? = null
    private var couponInformation: CouponInformation? = null
    private var couponProducts: List<CouponProduct>? = emptyList()

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
    }

    private fun setupViews() {
        binding?.tpgReadArticle?.setOnClickListener { onNavigateToCouponInformationPage() }
        binding?.tpgCouponInformation?.setOnClickListener { onNavigateToCouponInformationPage() }
        binding?.tpgCouponSetting?.setOnClickListener { onNavigateToCouponSettingsPage() }
        binding?.tpgAddProduct?.setOnClickListener { onNavigateToProductListPage() }
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
        couponSettings?.let { coupon -> refreshCouponSettingsSection(coupon) }
    }

    private fun refreshCouponSettingsSection(coupon: CouponSettings) {
        binding?.labelCouponSettingCompleteStatus?.setLabelType(Label.HIGHLIGHT_LIGHT_TEAL)
        binding?.labelCouponSettingCompleteStatus?.setLabel(getString(R.string.completed))
        binding?.tpgCouponType?.text = coupon.type.label
        binding?.tpgDiscountType?.text = coupon.discountType.label
        binding?.tpgMinimumPurchaseType?.text = coupon.minimumPurchaseType.label
        binding?.tpgCouponQouta?.text = coupon.quota.splitByThousand()

        handleDiscountAmount(coupon.discountType, coupon.discountAmount, coupon.discountPercentage)
        handleMaximumDiscount(coupon.discountType, coupon.maxDiscount)
        handleMinimumPurchase(coupon.minimumPurchaseType, coupon.minimumPurchase)
        handleEstimatedMaxExpense(coupon.estimatedMaxExpense)
    }

    private fun handleDiscountAmount(discountType: DiscountType, discountAmount: Int, discountPercentage: Int) {
        if (discountType == DiscountType.PERCENTAGE) {
            binding?.tpgDiscountAmount?.text = String.format(
                getString(R.string.placeholder_percent),
                discountPercentage.splitByThousand()
            )
        } else {
            binding?.tpgDiscountAmount?.text = String.format(
                getString(R.string.placeholder_rupiah),
                discountAmount.splitByThousand()
            )
        }
    }

    private fun handleMaximumDiscount(discountType: DiscountType, maxDiscount: Int) {
        if (discountType == DiscountType.PERCENTAGE) {
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

    private fun handleMinimumPurchase(
        minimumPurchaseType: MinimumPurchaseType,
        minimumPurchase: Int
    ) {
        val text = when (minimumPurchaseType) {
            MinimumPurchaseType.NONE -> EMPTY_STRING
            MinimumPurchaseType.NOMINAL -> String.format(
                getString(R.string.placeholder_rupiah),
                minimumPurchase.splitByThousand()
            )
            MinimumPurchaseType.QUANTITY -> String.format(
                getString(R.string.placeholder_quantity),
                minimumPurchase.splitByThousand()
            )
            MinimumPurchaseType.NOTHING -> getString(R.string.nothing)
        }

        binding?.tpgMinimumPurchase?.text = text
    }

}