package com.tokopedia.deals.checkout.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.common.payment.PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA
import com.tokopedia.common.payment.PaymentConstant.PAYMENT_SUCCESS
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_entertainment.data.DealsGeneral
import com.tokopedia.common_entertainment.data.DealsInstant
import com.tokopedia.common_entertainment.data.EventVerifyResponse
import com.tokopedia.common_entertainment.data.ItemMapResponse
import com.tokopedia.deals.checkout.di.DealsCheckoutComponent
import com.tokopedia.deals.checkout.ui.DealsCheckoutCallbacks
import com.tokopedia.deals.checkout.ui.activity.DealsCheckoutActivity
import com.tokopedia.deals.checkout.ui.activity.DealsCheckoutActivity.Companion.EXTRA_DEAL_DETAIL
import com.tokopedia.deals.checkout.ui.activity.DealsCheckoutActivity.Companion.EXTRA_DEAL_VERIFY
import com.tokopedia.deals.checkout.ui.mapper.DealsCheckoutMapper
import com.tokopedia.deals.checkout.ui.viewmodel.DealsCheckoutViewModel
import com.tokopedia.deals.common.analytics.DealsAnalytics
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.databinding.FragmentDealsCheckoutBinding
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class DealsCheckoutFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: DealsAnalytics

    @Inject
    lateinit var dealsCheckoutMapper: DealsCheckoutMapper

    private var binding by autoClearedNullable<FragmentDealsCheckoutBinding>()
    private val viewModel by viewModels<DealsCheckoutViewModel> { viewModelFactory }
    private var dealsCheckoutCallbacks: DealsCheckoutCallbacks? = null
    private var dealsDetail: ProductDetailData = ProductDetailData()
    private var dealsVerify: EventVerifyResponse = EventVerifyResponse()
    private var dealsItemMap: ItemMapResponse = ItemMapResponse()
    private var promoCode = ""
    private var voucherCode = ""
    private var couponCode = ""
    private var promoApplied = false

    private var toolbar: HeaderUnify? = null
    private var imgBrand: ImageUnify? = null
    private var tgBrandName: Typography? = null
    private var tgTitle: Typography? = null
    private var tgExpiredDate: Typography? = null
    private var tgNumbersLocation: Typography? = null
    private var tgAllLocation: Typography? = null
    private var etEmail: TextFieldUnify? = null
    private var tgMrpPerQuantity: Typography? = null
    private var tgSalesPricePerQty: Typography? = null
    private var tgSalesPriceAllQty: Typography? = null
    private var tgServiceFee: Typography? = null
    private var tgServiceFeeAmount: Typography? = null
    private var tgTotalAmount: Typography? = null
    private var tgNumberVoucher: Typography? = null
    private var btnPayment: UnifyButton? = null
    private var tickerPromoCode: TickerPromoStackingCheckoutView? = null
    private var clPromoDiscount: ConstraintLayout? = null
    private var tgPromoDiscount: Typography? = null
    private var progressBar: FrameLayout? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DealsCheckoutComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dealsDetail = it.getParcelable(EXTRA_DEAL_DETAIL) ?: ProductDetailData()
            dealsVerify = it.getParcelable(EXTRA_DEAL_VERIFY) ?: EventVerifyResponse()
            dealsItemMap = dealsVerify.metadata.itemMap.firstOrNull() ?: ItemMapResponse()
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        dealsCheckoutCallbacks = activity as DealsCheckoutActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDealsCheckoutBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupHeader()
        showUI()
        observeFlowData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOYALTY_ACTIVITY_REQUEST_CODE) {
            hideProgressBar()
            when (resultCode) {
                VOUCHER_RESULT_CODE -> {
                    val code = data?.extras?.getString(VOUCHER_CODE) ?: ""
                    val message = data?.extras?.getString(VOUCHER_MESSAGE) ?: ""
                    val amount = data?.extras?.getLong(VOUCHER_DISCOUNT_AMOUNT) ?: ZERO_LONG
                    val isCancel = data?.extras?.getBoolean(IS_CANCEL) ?: false
                    voucherCode = code
                    promoCode = code
                    showPromoSuccess(code, message, amount, isCancel)
                }

                COUPON_RESULT_CODE -> {
                    val code = data?.extras?.getString(COUPON_CODE) ?: ""
                    val message = data?.extras?.getString(COUPON_MESSAGE) ?: ""
                    val amount = data?.extras?.getLong(VOUCHER_DISCOUNT_AMOUNT) ?: ZERO_LONG
                    val isCancel = data?.extras?.getBoolean(IS_CANCEL) ?: false
                    couponCode = code
                    promoCode = code
                    showPromoSuccess(code, message, amount, isCancel)
                }
            }
        }
    }

    private fun observeFlowData() {
        observeCheckoutGeneral()
        observeCheckoutGeneralInstant()
    }

    private fun observeCheckoutGeneral() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowCheckoutGeneral.collect {
                when (it) {
                    is Success -> {
                        context?.let { context ->
                            hideProgressBar()
                            if (it.data.checkout.data.success == Int.ZERO) {
                                showErrorToaster(it.data.checkout.data.message)
                            } else {
                                val paymentData = it.data.checkout.data.data.queryString
                                val paymentURL: String = it.data.checkout.data.data.redirectUrl
                                if (!paymentData.isNullOrEmpty() || !paymentURL.isNullOrEmpty()) {
                                    val checkoutResultData = PaymentPassData()
                                    checkoutResultData.queryString = paymentData
                                    checkoutResultData.redirectUrl = paymentURL
                                    checkoutResultData.callbackSuccessUrl = ORDER_LIST_DEALS

                                    val paymentCheckoutString =
                                        ApplinkConstInternalPayment.PAYMENT_CHECKOUT
                                    val intent =
                                        RouteManager.getIntent(context, paymentCheckoutString)
                                    intent.putExtra(
                                        EXTRA_PARAMETER_TOP_PAY_DATA,
                                        checkoutResultData
                                    )
                                    intent.putExtra(ApplinkConstInternalPayment.CHECKOUT_TIMESTAMP, System.currentTimeMillis())
                                    startActivityForResult(intent, PAYMENT_SUCCESS)
                                } else {
                                    showErrorToaster(it.data.checkout.data.error)
                                }
                            }
                        }
                    }

                    is Fail -> {
                        hideProgressBar()
                        showErrorToaster(ErrorHandler.getErrorMessage(context, it.throwable))
                    }
                }
            }
        }
    }

    private fun observeCheckoutGeneralInstant() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowCheckoutGeneralInstant.collect {
                when (it) {
                    is Success -> {
                        context?.let { context ->
                            hideProgressBar()
                            if (it.data.checkout.data.success == Int.ZERO) {
                                showErrorToaster(it.data.checkout.data.message)
                            } else {
                                RouteManager.route(context, it.data.checkout.data.data.redirectUrl)
                            }
                        }
                    }

                    is Fail -> {
                        hideProgressBar()
                        showErrorToaster(ErrorHandler.getErrorMessage(context, it.throwable))
                    }
                }
            }
        }
    }

    private fun setupUI() {
        view?.apply {
            toolbar = binding?.toolbar
            imgBrand = binding?.imageViewBrand
            tgBrandName = binding?.tgBrandName
            tgTitle = binding?.tgDealDetails
            tgExpiredDate = binding?.tgExpiryDate
            tgNumbersLocation = binding?.tgNoLocations
            tgAllLocation = binding?.tgAvailableLocations
            etEmail = binding?.etEmail
            tgMrpPerQuantity = binding?.tgMrpPerQuantity
            tgSalesPricePerQty = binding?.tgSalesPricePerQuantity
            tgSalesPriceAllQty = binding?.tgSalesPriceAllQuantity
            tgServiceFee = binding?.tgServiceFee
            tgServiceFeeAmount = binding?.tgServiceFeeAmount
            tgTotalAmount = binding?.tgTotalAmount
            tgNumberVoucher = binding?.tgNumberVouchers
            tickerPromoCode = binding?.tickerPromocode
            clPromoDiscount = binding?.clPromo
            tgPromoDiscount = binding?.tgPromoDiscount
            progressBar = binding?.progressBarLayout
            btnPayment = binding?.btnSelectPaymentMethod
        }
    }

    private fun setupHeader() {
        toolbar?.headerTitle = context?.resources?.getString(com.tokopedia.deals.R.string.deals_checkout_title).orEmpty()
        (activity as DealsCheckoutActivity).setSupportActionBar(toolbar)
        (activity as DealsCheckoutActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showUI() {
        analytics.checkoutSendScreenName()
        showHeader()
        showOutlets()
        showEmail()
        showPrice()
        showPromo()
        showPayment()
    }

    private fun showErrorToaster(message: String) {
        view?.let { view ->
            Toaster.build(view, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                context?.resources?.getString(com.tokopedia.deals.R.string.deals_checkout_error_toaster).orEmpty()
            ).show()
        }
    }

    private fun showHeader() {
        imgBrand?.loadImage(dealsDetail.imageApp)
        tgBrandName?.text = dealsDetail.brand.title
        tgTitle?.text = dealsDetail.displayName
        tgExpiredDate?.text = String.format(
            getString(
                com.tokopedia.deals.R.string.deals_pdp_valid_through,
                DealsUtils.convertEpochToString(
                    dealsDetail.saleEndDate.toIntSafely()
                )
            )
        )
    }

    private fun showOutlets() {
        if (dealsDetail.outlets.isNullOrEmpty()) {
            tgAllLocation?.text = context?.resources?.getString(
                com.tokopedia.deals.R.string.deals_checkout_all_indonesia
            )
        }

        if (!dealsDetail.outlets.isNullOrEmpty()) {
            tgNumbersLocation?.text = String.format(
                getString(
                    com.tokopedia.deals.R.string.deals_checkout_number_of_locations,
                    dealsDetail.outlets.size
                )
            )
            tgNumbersLocation?.setOnClickListener { _ ->
                dealsCheckoutCallbacks?.onShowAllLocation(dealsDetail.outlets)
            }
        }
    }

    private fun showEmail() {
        etEmail?.textFieldInput?.setText(userSession.email)
        etEmail?.textFieldInput?.keyListener = null
    }

    private fun showPrice() {
        if (dealsDetail.mrp.toIntSafely() != Int.ZERO && dealsDetail.mrp != dealsDetail.salesPrice) {
            tgMrpPerQuantity?.apply {
                show()
                text = DealsUtils.convertToCurrencyString(dealsDetail.mrp.toIntSafely().toLong())
                paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        } else {
            tgMrpPerQuantity?.hide()
        }

        tgSalesPricePerQty?.text = DealsUtils.convertToCurrencyString(dealsItemMap.price)
        tgSalesPriceAllQty?.text =
            DealsUtils.convertToCurrencyString(dealsItemMap.price * dealsItemMap.quantity)

        if (dealsItemMap.commission <= Int.ZERO) {
            tgServiceFee?.gone()
            tgServiceFeeAmount?.gone()
        } else {
            tgServiceFeeAmount?.text =
                DealsUtils.convertToCurrencyString(dealsItemMap.commission.toLong())
        }

        tgTotalAmount?.text =
            DealsUtils.convertToCurrencyString(dealsItemMap.price * dealsItemMap.quantity.toLong() + dealsItemMap.commission.toLong())
        tgNumberVoucher?.text = context?.resources?.getString(
            com.tokopedia.deals.R.string.deals_checkout_number_of_vouchers,
            dealsItemMap.quantity
        )
    }

    private fun showPromo() {
        tickerPromoCode?.apply {
            enableView()
            actionListener = object : TickerPromoStackingCheckoutView.ActionListener {

                override fun onClickUsePromo() {
                    analytics.checkoutPromoClick(dealsDetail.brand.title, promoApplied)
                    goToPromoListDealsActivity()
                }

                override fun onResetPromoDiscount() {
                    setupPromoTicker(TickerCheckoutView.State.EMPTY, "", "")
                    showPromoSuccess("", "", ZERO_LONG, true)
                    promoApplied = false
                    promoCode = ""
                }

                override fun onClickDetailPromo() {
                    if (!couponCode.isNullOrEmpty()) {
                        goToPromoDetailDeals()
                    } else if (!voucherCode.isNullOrEmpty()) {
                        goToPromoListDealsWithVoucher()
                    }
                }

                override fun onDisablePromoDiscount() {
                    setupPromoTicker(TickerCheckoutView.State.EMPTY, "", "")
                    showPromoSuccess("", "", ZERO_LONG, true)
                    promoApplied = false
                    promoCode = ""
                }
            }
        }
    }

    private fun showPayment() {
        btnPayment?.setOnClickListener {
            analytics.checkoutProceedPaymentClick(dealsVerify.metadata.quantity, dealsDetail.categoryId,
                dealsDetail.id, dealsDetail.displayName, dealsDetail.brand.title, promoApplied, dealsDetail.salesPrice)
            if (dealsVerify.gatewayCode.isNullOrEmpty()) {
                viewModel.checkoutGeneral(validatePromoCodesCheckoutGeneral(listOf(promoCode)))
            } else {
                viewModel.checkoutGeneralInstant(validatePromoCodesCheckoutInstant(listOf(promoCode)))
            }
            showProgressBar()
        }
    }

    private fun setupPromoTicker(state: TickerCheckoutView.State, title: String, desc: String) {
        if (state == TickerCheckoutView.State.EMPTY) {
            tickerPromoCode?.title = title
            tickerPromoCode?.state = TickerPromoStackingCheckoutView.State.EMPTY
        } else if (state == TickerCheckoutView.State.ACTIVE) {
            tickerPromoCode?.title = title
            tickerPromoCode?.state = TickerPromoStackingCheckoutView.State.ACTIVE
            tickerPromoCode?.desc = desc
        }
    }

    private fun showPromoSuccess(
        title: String,
        message: String,
        discountAmount: Long,
        isCancel: Boolean
    ) {
        if (isCancel) {
            promoCode = ""
            tickerPromoCode?.state = TickerPromoStackingCheckoutView.State.EMPTY
            promoApplied = false
            tickerPromoCode?.title = ""
            tickerPromoCode?.desc = ""
        } else {
            tickerPromoCode?.state = TickerPromoStackingCheckoutView.State.ACTIVE
            tickerPromoCode?.title = title
            tickerPromoCode?.desc = message
            promoApplied = true
        }

        if (discountAmount != ZERO_LONG) {
            clPromoDiscount?.show()
            tgPromoDiscount?.text = DealsUtils.convertToCurrencyString(discountAmount)
        } else {
            clPromoDiscount?.gone()
        }

        updateAmount(discountAmount)
    }

    private fun updateAmount(discountAmount: Long) {
        tgTotalAmount?.text =
            DealsUtils.convertToCurrencyString(dealsItemMap.price * dealsItemMap.quantity.toLong() + dealsItemMap.commission.toLong() - discountAmount)
    }

    private fun validatePromoCodesCheckoutGeneral(promoCodes: List<String>): DealsGeneral {
        return if (promoCodes.isNotEmpty()) {
            if (promoCodes.first().isNotEmpty()) dealsCheckoutMapper.mapCheckoutDeals(
                dealsDetail,
                dealsVerify,
                listOf(promoCode)
            )
            else dealsCheckoutMapper.mapCheckoutDeals(dealsDetail, dealsVerify)
        } else {
            dealsCheckoutMapper.mapCheckoutDeals(dealsDetail, dealsVerify)
        }
    }

    private fun validatePromoCodesCheckoutInstant(promoCodes: List<String>): DealsInstant {
        return if (promoCodes.isNotEmpty()) {
            if (promoCodes.first().isNotEmpty()) dealsCheckoutMapper.mapCheckoutDealsInstant(
                dealsDetail,
                dealsVerify,
                listOf(promoCode)
            )
            else dealsCheckoutMapper.mapCheckoutDealsInstant(dealsDetail, dealsVerify)
        } else {
            dealsCheckoutMapper.mapCheckoutDealsInstant(dealsDetail, dealsVerify)
        }
    }

    private fun goToPromoListDealsActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalPromo.PROMO_LIST_DEALS)
        intent.putExtra(EXTRA_META_DATA, dealsCheckoutMapper.getMetaDataString(dealsVerify))
        intent.putExtra(EXTRA_CATEGORY_NAME, dealsVerify.metadata.categoryName)
        intent.putExtra(EXTRA_GRAND_TOTAL, dealsVerify.metadata.totalPrice)
        intent.putExtra(
            EXTRA_CATEGORYID,
            dealsDetail.catalog.digitalCategoryId.toIntSafely()
        )
        intent.putExtra(EXTRA_PRODUCTID, dealsItemMap.productId)
        startActivityForResult(intent, LOYALTY_ACTIVITY_REQUEST_CODE)
    }

    private fun goToPromoDetailDeals() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalPromo.PROMO_DETAIL_DEALS)
        intent.putExtra(EXTRA_META_DATA, dealsCheckoutMapper.getMetaDataString(dealsVerify))
        intent.putExtra(EXTRA_CATEGORY_NAME, dealsVerify.metadata.categoryName)
        intent.putExtra(EXTRA_GRAND_TOTAL, dealsVerify.metadata.totalPrice)
        intent.putExtra(COUPON_EXTRA_IS_USE, true)
        intent.putExtra(EXTRA_KUPON_CODE, couponCode)
        startActivityForResult(intent, LOYALTY_ACTIVITY_REQUEST_CODE)
    }

    private fun goToPromoListDealsWithVoucher() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalPromo.PROMO_LIST_DEALS)
        intent.putExtra(EXTRA_META_DATA, dealsCheckoutMapper.getMetaDataString(dealsVerify))
        intent.putExtra(EXTRA_CATEGORY_NAME, dealsVerify.metadata.categoryName)
        intent.putExtra(EXTRA_GRAND_TOTAL, dealsVerify.metadata.totalPrice)
        intent.putExtra(EXTRA_CATEGORYID, dealsDetail.catalog.digitalCategoryId.toIntSafely())
        intent.putExtra(EXTRA_PRODUCTID, dealsItemMap.productId)
        intent.putExtra(EXTRA_PROMO_CODE, voucherCode)
        startActivityForResult(intent, LOYALTY_ACTIVITY_REQUEST_CODE)
    }

    private fun showProgressBar() {
        progressBar?.show()
    }

    private fun hideProgressBar() {
        progressBar?.hide()
    }

    companion object {
        private const val ORDER_LIST_DEALS = "/order-list"
        private const val EXTRA_META_DATA = "EXTRA_META_DATA"
        private const val EXTRA_PRODUCTID = "EXTRA_PRODUCTID"
        private const val EXTRA_CATEGORYID = "EXTRA_CATEGORYID"
        private const val EXTRA_GRAND_TOTAL = "EXTRA_GRAND_TOTAL"
        private const val EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME"
        private const val COUPON_EXTRA_IS_USE = "EXTRA_IS_USE"
        private const val EXTRA_KUPON_CODE = "EXTRA_KUPON_CODE"
        private const val EXTRA_PROMO_CODE = "EXTRA_PROMO_CODE"
        private const val VOUCHER_CODE = "voucher_code"
        private const val COUPON_CODE = "coupon_code"
        private const val IS_CANCEL = "IS_CANCEL"
        private const val VOUCHER_DISCOUNT_AMOUNT = "VOUCHER_DISCOUNT_AMOUNT"
        private const val COUPON_DISCOUNT_AMOUNT = "COUPON_DISCOUNT_AMOUNT"
        private const val VOUCHER_MESSAGE = "voucher_message"
        private const val COUPON_MESSAGE = "coupon_message"

        private const val LOYALTY_ACTIVITY_REQUEST_CODE = 12345
        private const val VOUCHER_RESULT_CODE = 12
        private const val COUPON_RESULT_CODE = 15
        private const val ZERO_LONG = 0L

        fun createInstance(
            productDetailData: ProductDetailData?,
            verifyData: EventVerifyResponse?
        ): DealsCheckoutFragment {
            val fragment = DealsCheckoutFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(EXTRA_DEAL_DETAIL, productDetailData)
                putParcelable(EXTRA_DEAL_VERIFY, verifyData)
            }
            return fragment
        }
    }
}
