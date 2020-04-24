package com.tokopedia.common.topupbills.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common.payment.model.TopPayBaseModel
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsAnalytics
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.catalog_plugin.RechargeCatalogPlugin
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.NULL_RESPONSE
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_LIST
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
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
    @Inject
    lateinit var commonTopupBillsAnalytics: CommonTopupBillsAnalytics

    open var promoTicker: TickerPromoStackingCheckoutView? = null

    open var menuId: Int = 0

    // Promo
    var promoCode: String = ""
    var isCoupon: Boolean = false
    open var categoryId: Int = 0
    var productId: Int = 0
    var price: Int = 0

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        topupBillsViewModel.enquiryData.observe(this, Observer {
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

        topupBillsViewModel.menuDetailData.observe(this, Observer {
            it.run {
                when (it) {
                    is Success -> processMenuDetail(it.data)
                    is Fail -> onMenuDetailError(it.throwable)
                }
            }
        })

        topupBillsViewModel.catalogPluginData.observe(this, Observer {
            it.run {
                when (it) {
                    is Success -> processCatalogPluginData(it.data)
                    is Fail -> onCatalogPluginDataError(it.throwable)
                }
            }
        })

        topupBillsViewModel.favNumberData.observe(this, Observer {
            it.run {
                when (it) {
                    is Success -> processFavoriteNumbers(it.data)
                    is Fail -> onFavoriteNumbersError(it.throwable)
                }
            }
        })

        topupBillsViewModel.checkVoucherData.observe(this, Observer {
            it.run {
                promoTicker?.toggleLoading(false)
                when (it) {
                    is Success -> setupPromoTicker(it.data)
                    is Fail -> onCheckVoucherError(it.throwable)
                }
            }
        })

        topupBillsViewModel.expressCheckoutData.observe(this, Observer {
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

        savedInstanceState?.run {
            promoCode = getString(EXTRA_PROMO_CODE, promoCode)
            categoryId = getInt(EXTRA_CATEGORY_ID, categoryId)
            productId = getInt(EXTRA_PRODUCT_ID, productId)
            price = getInt(EXTRA_PRODUCT_PRICE, price)
            isExpressCheckout = getBoolean(EXTRA_IS_EXPRESS_CHECKOUT, isExpressCheckout)
        }

        getMenuDetail(menuId)

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
                    processTransaction()
                }
                REQUEST_CODE_CART_DIGITAL -> {
                    data?.getStringExtra(DigitalExtraParam.EXTRA_MESSAGE)?.let {
                        NetworkErrorHelper.showSnackbar(activity, it)
                    }
                }
                REQUEST_CODE_PROMO_LIST, REQUEST_CODE_PROMO_DETAIL -> {
                    data?.let {
                        if (it.hasExtra(EXTRA_PROMO_DATA)) {
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

    fun getPromoListener(): TickerPromoStackingCheckoutView.ActionListener {
        return object : TickerPromoStackingCheckoutView.ActionListener {
            override fun onClickUsePromo() {
                commonTopupBillsAnalytics.eventClickUsePromo()

                val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_DIGITAL)
                intent.putExtra(EXTRA_PROMO_DIGITAL_MODEL, getPromoDigitalModel())
                startActivityForResult(intent, REQUEST_CODE_PROMO_LIST)
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
                val intent: Intent
                if (promoCode.isNotEmpty()) {
                    val requestCode: Int
                    if (isCoupon) {
                        intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_DIGITAL)
                        intent.putExtra(EXTRA_IS_USE, true)
                        intent.putExtra(EXTRA_COUPON_CODE, promoCode)
                        intent.putExtra(EXTRA_PROMO_DATA, getPromoDigitalModel())
                        requestCode = REQUEST_CODE_PROMO_DETAIL
                    } else {
                        intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_HOTEL)
                        intent.putExtra(EXTRA_PROMO_CODE, promoCode)
                        intent.putExtra(EXTRA_COUPON_ACTIVE, true)
                        intent.putExtra(EXTRA_PROMO_DIGITAL_MODEL, getPromoDigitalModel())
                        requestCode = REQUEST_CODE_PROMO_LIST
                    }
                    startActivityForResult(intent, requestCode)
                }
            }
        }
    }

    private fun getPromoDigitalModel(): PromoDigitalModel {
        val promoModel = PromoDigitalModel()
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
        topupBillsViewModel.getEnquiry(GraphqlHelper.loadRawString(resources, R.raw.query_recharge_inquiry),
                topupBillsViewModel.createEnquiryParams(operatorId, productId, inputData))
    }

    fun getMenuDetail(menuId: Int) {
        topupBillsViewModel.getMenuDetail(GraphqlHelper.loadRawString(resources, R.raw.query_menu_detail),
                topupBillsViewModel.createMenuDetailParams(menuId))
    }

    fun getCatalogPluginData(operatorId: Int, categoryId: Int) {
        if (operatorId > 0 && categoryId > 0) {
            topupBillsViewModel.getCatalogPluginData(GraphqlHelper.loadRawString(resources, R.raw.query_recharge_catalog_plugin),
                    topupBillsViewModel.createCatalogPluginParams(operatorId, categoryId))
        }
    }

    fun getFavoriteNumbers(categoryId: Int) {
        topupBillsViewModel.getFavoriteNumbers(
                GraphqlHelper.loadRawString(resources, R.raw.query_fav_number_digital),
                topupBillsViewModel.createFavoriteNumbersParams(categoryId))
    }

    fun checkVoucher() {
        promoTicker?.toggleLoading(true)
        topupBillsViewModel.checkVoucher(promoCode,
                PromoDigitalModel(categoryId, productId, price = price.toLong())
        )
    }

    private fun processExpressCheckout(checkOtp: Boolean = false) {
        // Check if promo code is valid
        val voucherCode = promoTicker?.run {
            if (state == TickerPromoStackingCheckoutView.State.ACTIVE) promoCode else ""
        } ?: ""
        if (productId > 0) {
            topupBillsViewModel.processExpressCheckout(
                    GraphqlHelper.loadRawString(resources, R.raw.query_recharge_express_checkout),
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
        isExpressCheckout = data.isExpressCheckout
        categoryName = data.catalog.label
    }

    abstract fun processFavoriteNumbers(data: TopupBillsFavNumber)

    abstract fun onEnquiryError(error: Throwable)

    abstract fun onMenuDetailError(error: Throwable)

    abstract fun onCatalogPluginDataError(error: Throwable)

    abstract fun onFavoriteNumbersError(error: Throwable)

    abstract fun onCheckVoucherError(error: Throwable)

    abstract fun onExpressCheckoutError(error: Throwable)

    private fun processCatalogPluginData(data: RechargeCatalogPlugin) {
        isInstantCheckout = data.instantCheckout.isEnabled
    }

    fun processTransaction() {
        if (userSession.isLoggedIn) {
            processCheckout()
        } else {
            navigateToLoginPage()
        }
    }

    open fun processCheckout() {
        if (isExpressCheckout) {
            processExpressCheckout()
        } else {
            navigateToCart()
        }
    }

    fun navigateToCart() {
        if (::checkoutPassData.isInitialized) {
            checkoutPassData.idemPotencyKey = userSession.userId.generateRechargeCheckoutToken()
            checkoutPassData.voucherCodeCopied = promoCode
            val intent = RouteManager.getIntent(context, ApplinkConsInternalDigital.CART_DIGITAL)
            intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, checkoutPassData)
            startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL)
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

    fun navigateToPayment(data: TopPayBaseModel) {
        val paymentPassData = PaymentPassData()
        paymentPassData.convertToPaymenPassData(data)

        val intent = RouteManager.getIntent(context, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
        startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
    }

    companion object {
        const val REQUEST_CODE_LOGIN = 1010
        const val REQUEST_CODE_CART_DIGITAL = 1090
        const val REQUEST_CODE_OTP = 1001

        const val OTP_TYPE_CHECKOUT_DIGITAL = 16

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