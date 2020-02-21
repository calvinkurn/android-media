package com.tokopedia.common.topupbills.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
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
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.NULL_RESPONSE
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
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
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by resakemal on 12/08/19.
 */
abstract class BaseTopupBillsFragment: BaseDaggerFragment()  {

    lateinit var checkoutPassData: DigitalCheckoutPassData

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var topupBillsViewModelFactory: ViewModelProvider.Factory
    lateinit var topupBillsViewModel: TopupBillsViewModel
    @Inject
    lateinit var commonTopupBillsAnalytics: CommonTopupBillsAnalytics

    open var promoTicker: TickerPromoStackingCheckoutView? = null

    // Promo
    var promoCode: String = ""
    var isCoupon: Boolean = false
    open var categoryId: Int = 0
    var productId: Int = 0
    var price: Long? = null

    // Express Checkout
    var isExpressCheckout = false
    var isInstantCheckout = false

    var categoryName = ""
    var operatorName = ""
    var productName = ""

    private var checkVoucherJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, topupBillsViewModelFactory)
            topupBillsViewModel = viewModelProvider.get(TopupBillsViewModel::class.java)
        }

        savedInstanceState?.run {
            promoCode = this.getString(EXTRA_PROMO_CODE, "")
        }
    }

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
                promoTicker?.hideLoading()
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
                        commonTopupBillsAnalytics.eventBuy(
                                categoryName,
                                operatorName,
                                productId.toString(),
                                productName,
                                price.toString(),
                                isInstantCheckout,
                                promoCode.isNotEmpty()
                        )
                        navigateToPayment(it.data)
                    }
                    is Fail -> onExpressCheckoutError(it.throwable)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val checkoutView = getCheckoutView()
        checkoutView?.run {
            promoTicker = getPromoTicker()
            promoTicker?.actionListener = getPromoListener()
            resetPromoTicker()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LOGIN -> {
                    processToCart()
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
            }
        }
    }

    abstract fun getCheckoutView(): TopupBillsCheckoutWidget?

    fun getPromoListener(): TickerPromoStackingCheckoutView.ActionListener {
        return object: TickerPromoStackingCheckoutView.ActionListener {
            override fun onClickUsePromo() {
                if (checkVoucherJob?.isActive != true) {
                    commonTopupBillsAnalytics.eventClickUsePromo()

                    val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_DIGITAL)
                    intent.putExtra(EXTRA_PROMO_DIGITAL_MODEL, getPromoDigitalModel())
                    startActivityForResult(intent, REQUEST_CODE_PROMO_LIST)
                }
            }

            override fun onResetPromoDiscount() {
                if (checkVoucherJob?.isActive != true) {
                    commonTopupBillsAnalytics.eventClickRemovePromo()
                    resetPromoTicker()
                }
            }

            override fun onDisablePromoDiscount() {
                if (checkVoucherJob?.isActive != true) {
                    resetPromoTicker()
                }
            }

            override fun onClickDetailPromo() {
                if (checkVoucherJob?.isActive != true) {
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
                            intent.putExtra(EXTRA_PROMO_DATA, getPromoDigitalModel())
                            requestCode = REQUEST_CODE_PROMO_LIST
                        }
                        startActivityForResult(intent, requestCode)
                    }
                }
            }
        }
    }

    private fun getPromoDigitalModel(): PromoDigitalModel {
        val promoModel = PromoDigitalModel()
        promoModel.categoryId = if (categoryId > 0) categoryId else 0
        promoModel.productId = if (productId > 0) productId else 0
        price?.run { promoModel.price = this }
        return promoModel
    }

    fun setupPromoTicker(data: PromoData) {
        promoTicker?.apply {
            promoCode = if (data.isActive()) data.promoCode else ""
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

    private fun resetPromoTicker() {
        promoTicker?.run {
            promoCode = ""
            isCoupon = false
            title = ""
            desc = ""
            state = TickerPromoStackingCheckoutView.State.EMPTY
        }
    }

    fun checkPromo() {
        checkVoucherJob?.cancel()
        checkVoucherJob = CoroutineScope(Dispatchers.Main).launch{
            delay(CHECK_VOUCHER_DEBOUNCE_DELAY)
            promoTicker?.showLoading()
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
        topupBillsViewModel.getFavoriteNumbers(GraphqlHelper.loadRawString(resources, R.raw.query_fav_number_digital),
                topupBillsViewModel.createFavoriteNumbersParams(categoryId))
    }

    fun checkVoucher() {
        topupBillsViewModel.checkVoucher(promoCode, PromoDigitalModel(categoryId, productId, price = price ?: 0))
    }

    fun processExpressCheckout(inputs: Map<String, String>) {
        if (productId > 0 && inputs.isNotEmpty()) {
            topupBillsViewModel.processExpressCheckout(GraphqlHelper.loadRawString(resources, R.raw.query_recharge_express_checkout),
                    topupBillsViewModel.createExpressCheckoutParams(productId, inputs, price ?: 0, promoCode))
        }
    }

    abstract fun processEnquiry(data: TopupBillsEnquiryData)

    open fun processMenuDetail(data: TopupBillsMenuDetail) {
        isExpressCheckout = data.isExpressCheckout
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

    fun processToCart() {
        if (userSession.isLoggedIn) {
            navigateToCart()
        } else {
            navigateToLoginPage()
        }
    }

    fun navigateToCart() {
        if (::checkoutPassData.isInitialized) {
            checkoutPassData.idemPotencyKey = userSession.userId.generateRechargeCheckoutToken()
            val intent = RouteManager.getIntent(context, ApplinkConsInternalDigital.CART_DIGITAL)
            intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, checkoutPassData)
            startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL)
        }
    }

    fun navigateToLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    private fun navigateToPayment(checkoutData: RechargeExpressCheckoutData) {
        val paymentPassData = PaymentPassData()
        paymentPassData.convertToPaymenPassData(checkoutData)

        val intent = RouteManager.getIntent(context, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
        startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
    }

    companion object {
        const val CHECK_VOUCHER_DEBOUNCE_DELAY = 1000L
        const val REQUEST_CODE_LOGIN = 1010
        const val REQUEST_CODE_CART_DIGITAL = 1090
        const val EXTRA_PROMO_DIGITAL_MODEL = "EXTRA_PROMO_DIGITAL_MODEL"
        const val EXTRA_COUPON_ACTIVE = "EXTRA_COUPON_ACTIVE"
        const val EXTRA_PROMO_CODE = "EXTRA_PROMO_CODE"
        const val EXTRA_COUPON_CODE = "EXTRA_KUPON_CODE"
        const val EXTRA_IS_USE = "EXTRA_IS_USE"
        const val EXTRA_PROMO_DATA = "EXTRA_PROMO_DATA"
    }

}