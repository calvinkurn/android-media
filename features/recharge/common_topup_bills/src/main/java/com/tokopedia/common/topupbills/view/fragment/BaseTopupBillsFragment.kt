package com.tokopedia.common.topupbills.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsAnalytics
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.catalog_plugin.RechargeCatalogPlugin
import com.tokopedia.common.topupbills.data.express_checkout.RechargeExpressCheckoutData
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlMutation
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.NULL_RESPONSE
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.utils.DeviceUtil
import com.tokopedia.common_digital.cart.DigitalCheckoutUtil
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_LIST
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by resakemal on 12/08/19.
 */
abstract class BaseTopupBillsFragment : BaseDaggerFragment() {

    lateinit var checkoutPassData: DigitalCheckoutPassData

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var topupBillsViewModel: TopupBillsViewModel

    lateinit var addToCartViewModel: DigitalAddToCartViewModel

    @Inject
    lateinit var rechargeAnalytics: RechargeAnalytics

    @Inject
    lateinit var commonTopupBillsAnalytics: CommonTopupBillsAnalytics

    open var promoTicker: TickerPromoStackingCheckoutView? = null

    open var menuId: Int = 0

    // Promo
    var promoCode: String = ""
    var isCoupon: Boolean = false
    open var categoryId: Int = 0
    open var productId: Int = 0
    var price: Int = 0
    var pendingPromoNavigation: String = ""

    // Express Checkout
    var isExpressCheckout = false
        set(value) {
            field = value
            promoTicker?.run {
                if (value) this.show() else this.hide()
            }
        }
    var isInstantCheckout = false
    var inputFields: Map<String, String> = mapOf()

    var categoryName = ""
    var operatorName = ""
    var productName = ""

    private fun subscribeUi() {
        addToCartViewModel.addToCartResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> navigateToCart()
                is Fail -> showErrorMessage(it.throwable)
            }
            onLoadingAtc(false)
        })

        topupBillsViewModel.enquiryData.observe(viewLifecycleOwner, Observer {
            it.run {
                when (it) {
                    is Success -> processEnquiry(it.data)
                    is Fail -> {
                        // Handle null response error
                        var throwable = it.throwable
                        if (throwable.message == NULL_RESPONSE) {
                            throwable = MessageErrorException(getString(R.string.common_topup_enquiry_error))
                        }
                        onEnquiryError(throwable)
                    }
                }
            }
        })

        topupBillsViewModel.menuDetailData.observe(viewLifecycleOwner, Observer {
            it.run {
                when (it) {
                    is Success -> processMenuDetail(it.data)
                    is Fail -> onMenuDetailError(it.throwable)
                }
            }
        })

        topupBillsViewModel.catalogPluginData.observe(viewLifecycleOwner, Observer {
            it.run {
                when (it) {
                    is Success -> processCatalogPluginData(it.data)
                    is Fail -> onCatalogPluginDataError(it.throwable)
                }
            }
        })

        topupBillsViewModel.favNumberData.observe(viewLifecycleOwner, Observer {
            it.run {
                when (it) {
                    is Success -> processFavoriteNumbers(it.data)
                    is Fail -> onFavoriteNumbersError(it.throwable)
                }
            }
        })

        topupBillsViewModel.checkVoucherData.observe(viewLifecycleOwner, Observer {
            it.run {
                promoTicker?.toggleLoading(false)
                when (it) {
                    is Success -> setupPromoTicker(it.data)
                    is Fail -> onCheckVoucherError(it.throwable)
                }
            }
        })

        topupBillsViewModel.expressCheckoutData.observe(viewLifecycleOwner, Observer {
            it.run {
                when (it) {
                    is Success -> {
                        // Navigate to otp if requested
                        if (it.data.needsOtp) {
                            requestOtp()
                        } else {
                            commonTopupBillsAnalytics.eventExpressCheckout(
                                    categoryName,
                                    operatorName,
                                    productId.toString(),
                                    productName,
                                    price,
                                    isInstantCheckout,
                                    promoCode.isNotEmpty()
                            )
                            navigateToPayment(it.data)
                        }
                    }
                    is Fail -> onExpressCheckoutError(it.throwable)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAddToCartViewModel()
        subscribeUi()

        savedInstanceState?.run {
            promoCode = getString(EXTRA_PROMO_CODE, promoCode)
            categoryId = getInt(EXTRA_CATEGORY_ID, categoryId)
            productId = getInt(EXTRA_PRODUCT_ID, productId)
            price = getInt(EXTRA_PRODUCT_PRICE, price)
            isExpressCheckout = getBoolean(EXTRA_IS_EXPRESS_CHECKOUT, isExpressCheckout)
        }

        val checkoutView = getCheckoutView()
        checkoutView?.run {
            promoTicker = getPromoTicker()
            promoTicker?.actionListener = getPromoListener()
            promoTicker?.resetPromoTicker()
        }

        if (promoCode.isNotEmpty() && categoryId > 0 && productId > 0) checkVoucher()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_PROMO_CODE, promoCode)
        outState.putInt(EXTRA_CATEGORY_ID, categoryId)
        outState.putInt(EXTRA_PRODUCT_ID, productId)
        outState.putBoolean(EXTRA_IS_EXPRESS_CHECKOUT, isExpressCheckout)
        outState.putInt(EXTRA_PRODUCT_PRICE, price)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LOGIN -> {
                    when (pendingPromoNavigation) {
                        NAVIGATION_PROMO_LIST -> navigateToPromoList()
                        NAVIGATION_PROMO_DETAIL -> navigateToPromoDetail()
                        else -> processTransaction()
                    }
                    pendingPromoNavigation = ""
                }
                REQUEST_CODE_CART_DIGITAL -> {
                    data?.getStringExtra(DigitalExtraParam.EXTRA_MESSAGE)?.let { message ->
                        view?.let {
                            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
                        }
                    }
                }
                REQUEST_CODE_PROMO_LIST, REQUEST_CODE_PROMO_DETAIL -> {
                    data?.let {
                        if (it.hasExtra(EXTRA_PROMO_DATA)) {
                            // Stop check voucher job to prevent previous promo override
                            topupBillsViewModel.stopCheckVoucher()
                            val promoData: PromoData = it.getParcelableExtra(EXTRA_PROMO_DATA)
                            setupPromoTicker(promoData)
                        }
                    }
                }
                REQUEST_CODE_OTP -> {
                    processExpressCheckout(true)
                }
            }
        }
    }

    abstract fun getCheckoutView(): TopupBillsCheckoutWidget?

    abstract fun initAddToCartViewModel()

    fun getPromoListener(): TickerPromoStackingCheckoutView.ActionListener {
        return object : TickerPromoStackingCheckoutView.ActionListener {
            override fun onClickUsePromo() {
                commonTopupBillsAnalytics.eventClickUsePromo()

                if (userSession.isLoggedIn) {
                    navigateToPromoList()
                } else {
                    pendingPromoNavigation = NAVIGATION_PROMO_LIST
                    navigateToLoginPage()
                }
            }

            override fun onResetPromoDiscount() {
                commonTopupBillsAnalytics.eventClickRemovePromo()
                promoCode = ""
                promoTicker?.resetPromoTicker()
            }

            override fun onDisablePromoDiscount() {
                promoCode = ""
                promoTicker?.resetPromoTicker()
            }

            override fun onClickDetailPromo() {
                if (userSession.isLoggedIn) {
                    navigateToPromoDetail()
                } else {
                    pendingPromoNavigation = NAVIGATION_PROMO_DETAIL
                    navigateToLoginPage()
                }
            }
        }
    }

    private fun navigateToPromoList() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_DIGITAL)
        intent.putExtra(EXTRA_PROMO_DIGITAL_MODEL, getPromoDigitalModel())
        startActivityForResult(intent, REQUEST_CODE_PROMO_LIST)
    }

    private fun navigateToPromoDetail() {
        val intent: Intent
        if (promoCode.isNotEmpty()) {
            val requestCode: Int
            if (isCoupon) {
                intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_DIGITAL)
                intent.putExtra(EXTRA_IS_USE, true)
                intent.putExtra(EXTRA_COUPON_CODE, promoCode)
                intent.putExtra(EXTRA_PROMO_DIGITAL_MODEL, getPromoDigitalModel())
                requestCode = REQUEST_CODE_PROMO_DETAIL
            } else {
                intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_DIGITAL)
                intent.putExtra(EXTRA_PROMO_CODE, promoCode)
                intent.putExtra(EXTRA_COUPON_ACTIVE, true)
                intent.putExtra(EXTRA_PROMO_DIGITAL_MODEL, getPromoDigitalModel())
                requestCode = REQUEST_CODE_PROMO_LIST
            }
            startActivityForResult(intent, requestCode)
        }
    }

    private fun getPromoDigitalModel(): PromoDigitalModel {
        val promoModel = PromoDigitalModel()
        promoModel.categoryName = categoryName
        promoModel.operatorName = operatorName
        promoModel.categoryId = if (categoryId > 0) categoryId else 0
        promoModel.productId = if (productId > 0) productId else 0
        promoModel.price = price.toLong()
        return promoModel
    }

    fun setupPromoTicker(data: PromoData) {
        promoTicker?.apply {
            promoCode = data.promoCode
            isCoupon = data.typePromo == PromoData.TYPE_COUPON
            title = data.title
            desc = data.description
            state = when (data.state) {
                TickerCheckoutView.State.ACTIVE -> TickerPromoStackingCheckoutView.State.ACTIVE
                TickerCheckoutView.State.FAILED -> TickerPromoStackingCheckoutView.State.FAILED
                else -> TickerPromoStackingCheckoutView.State.EMPTY
            }
        }
        if (promoTicker == null) onPromoTickerNull(data)
    }

    // Optional function if promo ticker can be null e.g General Template Promo
    open fun onPromoTickerNull(data: PromoData) {

    }

    private fun TickerPromoStackingCheckoutView.resetPromoTicker() {
        isCoupon = false
        title = ""
        desc = ""
        state = TickerPromoStackingCheckoutView.State.EMPTY
    }

    // Check voucher with delay; used on pdp with changeable products
    fun checkVoucherWithDelay() {
        if (promoCode.isNotEmpty()) {
            checkVoucher()
        }
    }

    fun getEnquiry(operatorId: String, productId: String, inputData: Map<String, String>) {
        topupBillsViewModel.getEnquiry(CommonTopupBillsGqlQuery.rechargeInquiry,
                topupBillsViewModel.createEnquiryParams(operatorId, productId, inputData))
    }

    fun getMenuDetail(menuId: Int) {
        onLoadingMenuDetail(true)
        topupBillsViewModel.getMenuDetail(CommonTopupBillsGqlQuery.catalogMenuDetail,
                topupBillsViewModel.createMenuDetailParams(menuId))
    }

    fun getCatalogPluginData(operatorId: Int, categoryId: Int) {
        if (operatorId > 0 && categoryId > 0) {
            topupBillsViewModel.getCatalogPluginData(CommonTopupBillsGqlQuery.rechargeCatalogPlugin,
                    topupBillsViewModel.createCatalogPluginParams(operatorId, categoryId))
        }
    }

    fun getFavoriteNumbers(categoryId: Int) {
        topupBillsViewModel.getFavoriteNumbers(
                CommonTopupBillsGqlMutation.favoriteNumber,
                topupBillsViewModel.createFavoriteNumbersParams(categoryId))
    }

    fun checkVoucher() {
        promoTicker?.toggleLoading(true)
        topupBillsViewModel.checkVoucher(promoCode,
                PromoDigitalModel(categoryId, categoryName, operatorName, productId, price = price.toLong())
        )
    }

    private fun showErrorMessage(error: Throwable) {
        view?.let { v ->
            Toaster.build(v, ErrorHandler.getErrorMessage(requireContext(), error)
                    ?: "", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    private fun processExpressCheckout(checkOtp: Boolean = false) {
        // Check if promo code is valid
        val voucherCode = promoTicker?.run {
            if (state == TickerPromoStackingCheckoutView.State.ACTIVE) promoCode else ""
        } ?: ""
        if (productId > 0) {
            topupBillsViewModel.processExpressCheckout(
                    CommonTopupBillsGqlMutation.rechargeExpressCheckout,
                    topupBillsViewModel.createExpressCheckoutParams(
                            productId,
                            inputFields,
                            price,
                            voucherCode,
                            checkOtp))
        }
    }

    abstract fun processEnquiry(data: TopupBillsEnquiryData)

    open fun processMenuDetail(data: TopupBillsMenuDetail) {
        onLoadingMenuDetail(false)
        isExpressCheckout = data.isExpressCheckout
        categoryName = data.catalog.label
        rechargeAnalytics.eventViewPdpPage(categoryName, userSession.userId)
    }

    open fun onMenuDetailError(error: Throwable) {
        onLoadingMenuDetail(false)
    }

    abstract fun onLoadingMenuDetail(showLoading: Boolean)

    abstract fun onLoadingAtc(showLoading: Boolean)

    abstract fun processFavoriteNumbers(data: TopupBillsFavNumber)

    abstract fun onEnquiryError(error: Throwable)

    abstract fun onCatalogPluginDataError(error: Throwable)

    abstract fun onFavoriteNumbersError(error: Throwable)

    abstract fun onCheckVoucherError(error: Throwable)

    abstract fun onExpressCheckoutError(error: Throwable)

    private fun processCatalogPluginData(data: RechargeCatalogPlugin) {
        isInstantCheckout = data.instantCheckout.isEnabled
    }

    fun processTransaction() {
        if (userSession.isLoggedIn) {
            if (isExpressCheckout) {
                processExpressCheckout()
            } else {
                addToCart()
            }
        } else {
            navigateToLoginPage()
        }
    }

    fun addToCart() {
        if (::checkoutPassData.isInitialized) {
            onLoadingAtc(true)
            checkoutPassData.idemPotencyKey = userSession.userId.generateRechargeCheckoutToken()
            checkoutPassData.voucherCodeCopied = promoCode
            addToCartViewModel.addToCart(checkoutPassData,
                    DeviceUtil.getDigitalIdentifierParam(requireActivity()),
                    DigitalSubscriptionParams())
        }
    }

    private fun navigateToCart() {
        context?.let { context ->
            if (::checkoutPassData.isInitialized) {
                val intent = RouteManager.getIntent(context, DigitalCheckoutUtil.getApplinkCartDigital(context))
                intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, checkoutPassData)
                startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL)
            }
        }
    }

    fun navigateToLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    private fun requestOtp() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.COTP)

        val bundle = Bundle()
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, userSession.phoneNumber)
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, userSession.email)
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_CHECKOUT_DIGITAL)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)

        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_OTP)
    }

    private fun navigateToPayment(checkoutData: RechargeExpressCheckoutData) {
        val paymentPassData = PaymentPassData()
        paymentPassData.convertToPaymenPassData(checkoutData)

        val intent = RouteManager.getIntent(context, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
        startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
    }

    protected fun getDefaultCheckoutPassDataBuilder(): DigitalCheckoutPassData.Builder {
        return DigitalCheckoutPassData.Builder()
                .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                .instantCheckout("0")
                .utmContent(GlobalConfig.VERSION_NAME)
                .idemPotencyKey(userSession.userId.generateRechargeCheckoutToken())
                .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                .voucherCodeCopied("")
                .isFromPDP(true)
    }

    companion object {
        const val REQUEST_CODE_LOGIN = 1010
        const val REQUEST_CODE_CART_DIGITAL = 1090
        const val REQUEST_CODE_OTP = 1001

        const val OTP_TYPE_CHECKOUT_DIGITAL = 16

        const val NAVIGATION_PROMO_LIST = "NAVIGATION_PROMO_LIST"
        const val NAVIGATION_PROMO_DETAIL = "NAVIGATION_PROMO_DETAIL"

        const val EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID"
        const val EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID"
        const val EXTRA_PRODUCT_PRICE = "EXTRA_PRODUCT_PRICE"
        const val EXTRA_PROMO_DIGITAL_MODEL = "EXTRA_PROMO_DIGITAL_MODEL"
        const val EXTRA_COUPON_ACTIVE = "EXTRA_COUPON_ACTIVE"
        const val EXTRA_PROMO_CODE = "EXTRA_PROMO_CODE"
        const val EXTRA_COUPON_CODE = "EXTRA_KUPON_CODE"
        const val EXTRA_IS_EXPRESS_CHECKOUT = "EXTRA_IS_EXPRESS_CHECKOUT"
        const val EXTRA_IS_USE = "EXTRA_IS_USE"
        const val EXTRA_PROMO_DATA = "EXTRA_PROMO_DATA"
    }

}