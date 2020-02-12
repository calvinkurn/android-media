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
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.RechargeExpressCheckoutData
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.NULL_RESPONSE
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.data.REQUST_CODE_PROMO_LIST
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
abstract class BaseTopupBillsFragment: BaseDaggerFragment()  {

    lateinit var checkoutPassData: DigitalCheckoutPassData

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var topupBillsViewModel: TopupBillsViewModel

    var promoTicker: TickerPromoStackingCheckoutView? = null

    // Promo Checkout
    var promoCode: String = ""
    var isCoupon: Boolean = false
    var productId: Int? = null
    var price: Long? = null

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
                        showEnquiryError(throwable)
                    }
                }
            }
        })
        topupBillsViewModel.menuDetailData.observe(this, Observer {
            it.run {
                when (it) {
                    is Success -> processMenuDetail(it.data)
                    is Fail -> showMenuDetailError(it.throwable)
                }
            }
        })
        topupBillsViewModel.favNumberData.observe(this, Observer {
            it.run {
                when (it) {
                    is Success -> processFavoriteNumbers(it.data)
                    is Fail -> showFavoriteNumbersError(it.throwable)
                }
            }
        })
        topupBillsViewModel.expressCheckoutData.observe(this, Observer {
            it.run {
                when (it) {
                    is Success -> navigateToPayment(it.data)
                    is Fail -> showExpressCheckoutError(it.throwable)
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
            setupPromoTicker(TickerCheckoutView.State.EMPTY)
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
                REQUEST_CODE_PROMO_CHECKOUT_LIST, REQUEST_CODE_PROMO_CHECKOUT_DETAIL -> {
                    data?.let {
                        if (it.hasExtra(EXTRA_PROMO_DATA)) {
                            val itemPromoData = it.getParcelableExtra<PromoData>(EXTRA_PROMO_DATA)
                            promoCode = itemPromoData.promoCode
                            isCoupon = itemPromoData.typePromo == PromoData.TYPE_COUPON

                            when (itemPromoData.state) {
                                TickerCheckoutView.State.EMPTY -> {
                                    promoCode = ""
                                    setupPromoTicker(TickerCheckoutView.State.EMPTY)
                                }
                                TickerCheckoutView.State.FAILED -> {
                                    promoCode = ""
                                    setupPromoTicker(TickerCheckoutView.State.FAILED,
                                            itemPromoData?.title.toEmptyStringIfNull(),
                                            itemPromoData?.description.toEmptyStringIfNull())

                                }
                                TickerCheckoutView.State.ACTIVE -> {
                                    setupPromoTicker(TickerCheckoutView.State.ACTIVE,
                                            itemPromoData?.title.toEmptyStringIfNull(),
                                            itemPromoData?.description.toEmptyStringIfNull())
                                }
                                else -> {
                                    promoCode = ""
                                    setupPromoTicker(TickerCheckoutView.State.EMPTY)
                                }
                            }
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
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_DIGITAL)
                intent.putExtra(EXTRA_PROMO_DIGITAL_MODEL, getPromoDigitalModel())
                startActivityForResult(intent, REQUEST_CODE_LIST_PROMO)
            }

            override fun onResetPromoDiscount() {
                promoCode = ""
                setupPromoTicker(TickerCheckoutView.State.EMPTY)
            }

            override fun onDisablePromoDiscount() {
                promoCode = ""
                setupPromoTicker(TickerCheckoutView.State.EMPTY)
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
                        intent.putExtra(EXTRA_PROMO_DATA, getPromoDigitalModel())
                        requestCode = REQUST_CODE_PROMO_LIST
                    }
                    startActivityForResult(intent, requestCode)
                }
            }
        }
    }

    private fun getPromoDigitalModel(): PromoDigitalModel {
        val promoModel = PromoDigitalModel()
        productId?.run { promoModel.productId = this }
        price?.run { promoModel.price = this }
        return promoModel
    }

    private fun setupPromoTicker(state: TickerCheckoutView.State,
                                 title: String = "",
                                 description: String = "") {
        promoTicker?.let { ticker ->
            ticker.title = title
            ticker.desc = description
            ticker.state = when(state) {
                TickerCheckoutView.State.ACTIVE -> TickerPromoStackingCheckoutView.State.ACTIVE
                TickerCheckoutView.State.FAILED -> TickerPromoStackingCheckoutView.State.FAILED
                else -> TickerPromoStackingCheckoutView.State.EMPTY
            }
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

    fun getFavoriteNumbers(categoryId: Int) {
        topupBillsViewModel.getFavoriteNumbers(GraphqlHelper.loadRawString(resources, R.raw.query_fav_number_digital),
                topupBillsViewModel.createFavoriteNumbersParams(categoryId))
    }

    fun processExpressCheckout(inputs: Map<String, String>) {
        val _productId = productId
        val _price = price
        if (_productId != null && _price != null && inputs.isNotEmpty()) {
            topupBillsViewModel.processExpressCheckout(GraphqlHelper.loadRawString(resources, R.raw.query_recharge_express_checkout),
                    topupBillsViewModel.createExpressCheckoutParams(_productId, inputs, _price))
        }
    }

    abstract fun processEnquiry(data: TopupBillsEnquiryData)

    abstract fun processMenuDetail(data: TopupBillsMenuDetail)

    abstract fun processFavoriteNumbers(data: TopupBillsFavNumber)

    abstract fun showEnquiryError(t: Throwable)

    abstract fun showMenuDetailError(t: Throwable)

    abstract fun showFavoriteNumbersError(t: Throwable)

    abstract fun showExpressCheckoutError(t: Throwable)

    fun processToCart() {
        if (userSession.isLoggedIn) {
            navigateToCart()
        } else {
            navigateToLoginPage()
        }
    }

    private fun navigateToCart() {
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
        const val REQUEST_CODE_LOGIN = 1010
        const val REQUEST_CODE_CART_DIGITAL = 1090
        const val REQUEST_CODE_LIST_PROMO = 232
        const val REQUEST_CODE_PROMO_CHECKOUT_LIST = 3121
        const val REQUEST_CODE_PROMO_CHECKOUT_DETAIL = 3122
        const val EXTRA_PROMO_DIGITAL_MODEL = "EXTRA_PROMO_DIGITAL_MODEL"
        const val EXTRA_COUPON_ACTIVE = "EXTRA_COUPON_ACTIVE"
        const val EXTRA_PROMO_CODE = "EXTRA_PROMO_CODE"
        const val EXTRA_COUPON_CODE = "EXTRA_KUPON_CODE"
        const val EXTRA_IS_USE = "EXTRA_IS_USE"
        const val EXTRA_PROMO_DATA = "EXTRA_PROMO_DATA"
    }

}