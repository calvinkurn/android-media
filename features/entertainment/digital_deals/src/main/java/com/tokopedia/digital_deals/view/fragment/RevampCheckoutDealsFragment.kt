package com.tokopedia.digital_deals.view.fragment

import android.app.Activity
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDeals
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.common.payment.PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA
import com.tokopedia.common.payment.PaymentConstant.PAYMENT_SUCCESS
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.digital_deals.data.EventVerifyResponse
import com.tokopedia.digital_deals.data.ItemMapResponse
import com.tokopedia.digital_deals.di.DealsComponent
import com.tokopedia.digital_deals.view.activity.CheckoutActivity
import com.tokopedia.digital_deals.view.activity.CheckoutActivity.EXTRA_DEALDETAIL
import com.tokopedia.digital_deals.view.activity.CheckoutActivity.EXTRA_VERIFY
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks
import com.tokopedia.digital_deals.view.utils.DealsAnalytics
import com.tokopedia.digital_deals.view.utils.Utils
import com.tokopedia.digital_deals.view.viewmodel.DealsCheckoutViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_checkout_deal.*
import javax.inject.Inject
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.loadImage

class RevampCheckoutDealsFragment : BaseDaggerFragment() {

    private var dealsDetail: DealsDetailsResponse = DealsDetailsResponse()
    private var verifyData: EventVerifyResponse = EventVerifyResponse()
    private var itemMap: ItemMapResponse = ItemMapResponse()
    private var quantity = 1
    private var promoCode = ""
    private var voucherCode = ""
    private var couponCode = ""
    private var promoApplied = false

    private lateinit var fragmentCallbacks: DealFragmentCallbacks

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModel: DealsCheckoutViewModel

    @Inject
    lateinit var dealsAnalytics: DealsAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dealsDetail = it.getParcelable(EXTRA_DEALDETAIL) ?: DealsDetailsResponse()
            verifyData = it.getParcelable(EXTRA_VERIFY) ?: EventVerifyResponse()
            itemMap = verifyData.metadata.itemMap.firstOrNull() ?: ItemMapResponse()
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        fragmentCallbacks = activity as CheckoutActivity
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DealsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.digital_deals.R.layout.fragment_checkout_deal, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLayout()
        observeData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == PAYMENT_SUCCESS) {
            val taskStackBuilder = TaskStackBuilder.create(context)
            val intentHomeEvent = RouteManager.getIntent(context, ApplinkConstInternalDeals.DEALS_HOMEPAGE)
            taskStackBuilder.addNextIntent(intentHomeEvent)
            taskStackBuilder.startActivities()

            val intent = RouteManager.getIntent(context, ApplinkConst.DEALS_ORDER)
            intent?.run {
                taskStackBuilder.addNextIntent(this)
                taskStackBuilder.startActivities()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOYALTY_ACTIVITY_REQUEST_CODE) {
            hideProgressBar()
            when (resultCode){
                VOUCHER_RESULT_CODE -> {
                    val code = data?.extras?.getString(VOUCHER_CODE) ?: ""
                    val message = data?.extras?.getString(VOUCHER_MESSAGE) ?: ""
                    val amount = data?.extras?.getInt(VOUCHER_DISCOUNT_AMOUNT) ?: 0
                    val isCancel = data?.extras?.getBoolean(IS_CANCEL) ?: false
                    voucherCode = code
                    promoCode = code
                    showPromoSuccess(code, message, amount.toLong(), isCancel)
                }

                COUPON_RESULT_CODE -> {
                    val code = data?.extras?.getString(COUPON_CODE) ?: ""
                    val message = data?.extras?.getString(COUPON_MESSAGE) ?: ""
                    val amount = data?.extras?.getInt(COUPON_DISCOUNT_AMOUNT) ?: 0
                    val isCancel = data?.extras?.getBoolean(IS_CANCEL) ?: false
                    couponCode = code
                    promoCode = code
                    showPromoSuccess(code, message, amount.toLong(), isCancel)
                }
            }
        }
    }

    private fun observeData() {
        observe(viewModel.dealsCheckoutResponse) { data ->
            context?.let {
                hideProgressBar()
                val context = it
                if (data.checkout.data.success == 0) {
                    view?.let {
                        Toaster.build(it, data.checkout.data.message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                                resources.getString(com.tokopedia.digital_deals.R.string.digital_deals_error_toaster)).show()
                    }
                } else {
                    val paymentData = data.checkout.data.data.queryString
                    val paymentURL: String = data.checkout.data.data.redirectUrl

                    if (!paymentData.isNullOrEmpty() || !paymentURL.isNullOrEmpty()) {

                        val checkoutResultData = PaymentPassData()
                        checkoutResultData.queryString = paymentData
                        checkoutResultData.redirectUrl = paymentURL
                        checkoutResultData.callbackSuccessUrl = ORDER_LIST_DEALS

                        val paymentCheckoutString = ApplinkConstInternalPayment.PAYMENT_CHECKOUT
                        val intent = RouteManager.getIntent(context, paymentCheckoutString)
                        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, checkoutResultData)
                        startActivityForResult(intent, PAYMENT_SUCCESS)

                    } else {
                        view?.let {
                            Toaster.build(it, data.checkout.data.error, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                                    resources.getString(com.tokopedia.digital_deals.R.string.digital_deals_error_toaster)).show()
                        }
                    }
                }
            }
        }

        observe(viewModel.dealsCheckoutInstantResponse) { data ->
            context?.let {
                hideProgressBar()
                val context = it
                if (data.checkout.data.success == 0) {
                    view?.let {
                        Toaster.build(it, data.checkout.data.message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                                resources.getString(com.tokopedia.digital_deals.R.string.digital_deals_error_toaster)).show()
                    }
                } else {
                    RouteManager.route(context, data.checkout.data.data.redirectUrl)
                }
            }
        }

        observe(viewModel.errorGeneralValue) { error ->
            view?.let {
                hideProgressBar()
                Toaster.build(it, ErrorHandler.getErrorMessage(context, error), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                        resources.getString(com.tokopedia.digital_deals.R.string.digital_deals_error_toaster)).show()
            }
        }
    }

    private fun showProgressBar() {
        progress_bar_layout?.show()
    }

    private fun hideProgressBar() {
        progress_bar_layout?.hide()
    }

    private fun showLayout() {
        dealsAnalytics.sendScreenNameEvent(SCREEN_NAME)
        quantity = dealsDetail.quantity
        image_view_brand?.loadImage(dealsDetail.imageWeb)
        tv_brand_name?.text = dealsDetail.brand.title
        tv_deal_details?.text = dealsDetail.displayName
        tv_expiry_date?.text = resources.getString(com.tokopedia.digital_deals.R.string.valid_through,
                Utils.convertEpochToString(dealsDetail.saleEndDate)
        )

        if (dealsDetail.outlets == null || dealsDetail.outlets.isEmpty()) {
            tv_available_locations?.text = resources.getString(com.tokopedia.digital_deals.R.string.deals_all_indonesia)
        }

        if (dealsDetail.mrp != 0 && dealsDetail.mrp != dealsDetail.salesPrice) {
            tv_mrp_per_quantity?.apply {
                show()
                text = Utils.convertToCurrencyString(dealsDetail.mrp.toLong())
                paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        } else {
            tv_mrp_per_quantity?.hide()
        }

        tv_sales_price_per_quantity?.text = Utils.convertToCurrencyString(itemMap.price.toLong())
        tv_sales_price_all_quantity?.text = Utils.convertToCurrencyString(itemMap.price.toLong() * itemMap.quantity.toLong())

        if (itemMap.commission == 0) {
            tv_service_fee?.gone()
            tv_service_fee_amount?.gone()
        } else {
            tv_service_fee?.text = Utils.convertToCurrencyString(itemMap.commission.toLong())
        }

        tv_total_amount?.text = Utils.convertToCurrencyString(itemMap.price.toLong() * itemMap.quantity.toLong() + itemMap.commission.toLong())
        tv_number_vouchers?.text = resources.getString(com.tokopedia.digital_deals.R.string.number_of_vouchers, itemMap.quantity)

        if (dealsDetail.outlets != null && dealsDetail.outlets.size > 0) {
            tv_no_locations?.text = resources.getString(com.tokopedia.digital_deals.R.string.number_of_locations, dealsDetail.outlets.size)
        }

        tv_email?.setText(userSession.email)
        tv_phone?.setText(userSession.phoneNumber)
        base_main_content?.show()
        cl_btn_payment?.show()

        tv_no_locations?.setOnClickListener {
            fragmentCallbacks.replaceFragment(dealsDetail.outlets, 0)
        }

        ticker_promocode?.apply {
            enableView()
            actionListener = object : TickerPromoStackingCheckoutView.ActionListener {
                override fun onClickUsePromo() {
                    dealsAnalytics.sendPromoCodeClickEvent(dealsDetail, userSession.userId, promoApplied)
                    goToPromoListDealsActivity()
                }

                override fun onResetPromoDiscount() {
                    setupPromoTicker(TickerCheckoutView.State.EMPTY, "", "")
                    showPromoSuccess("", "", 0, true)
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
                    showPromoSuccess("", "", 0, true)
                    promoApplied = false
                    promoCode = ""
                }
            }
        }

        ll_select_payment_method?.setOnClickListener {
            dealsAnalytics.sendEcommercePayment(dealsDetail.categoryId, dealsDetail.id, verifyData.metadata.quantity, dealsDetail.salesPrice,
                    dealsDetail.displayName, dealsDetail.brand.title, promoApplied, userSession.userId)
            if (verifyData.gatewayCode.isNullOrEmpty()) {
                viewModel.checkoutGeneral(viewModel.mapCheckoutDeals(dealsDetail, verifyData, listOf(promoCode)))
            } else {
                viewModel.checkoutGeneralInstant(viewModel.mapCheckoutDealsInstant(dealsDetail, verifyData, listOf(promoCode)))
            }
            showProgressBar()
        }
    }

    private fun setupPromoTicker(state: TickerCheckoutView.State, title: String, desc: String){
        if (state == TickerCheckoutView.State.EMPTY){
            ticker_promocode?.title = title
            ticker_promocode?.state = TickerPromoStackingCheckoutView.State.EMPTY
        } else if (state == TickerCheckoutView.State.ACTIVE){
            ticker_promocode?.title = title
            ticker_promocode?.state = TickerPromoStackingCheckoutView.State.ACTIVE
            ticker_promocode?.desc = desc
        }
    }

    private fun showPromoSuccess(title: String, message: String, discountAmount: Long, isCancel: Boolean){
        if (isCancel){
            promoCode = ""
            ticker_promocode?.state = TickerPromoStackingCheckoutView.State.EMPTY
            promoApplied = false
            ticker_promocode?.title = ""
            ticker_promocode?.desc = ""
        } else {
            ticker_promocode?.state = TickerPromoStackingCheckoutView.State.ACTIVE
            ticker_promocode?.title = title
            ticker_promocode?.desc = message
            promoApplied = true
        }

        if (discountAmount != 0L){
            cl_promo?.show()
            tv_promo_discount?.text = Utils.convertToCurrencyString(discountAmount)
        } else {
            cl_promo?.gone()
        }

        updateAmount(discountAmount)
    }

    private fun goToPromoListDealsActivity(){
        val intent = RouteManager.getIntent(context, ApplinkConstInternalPromo.PROMO_LIST_DEALS)
        intent.putExtra(EXTRA_META_DATA, viewModel.getMetaDataString(verifyData))
        intent.putExtra(EXTRA_CATEGORY_NAME, verifyData.metadata.categoryName)
        intent.putExtra(EXTRA_GRAND_TOTAL, verifyData.metadata.totalPrice)
        intent.putExtra(EXTRA_CATEGORYID, dealsDetail.catalog.digitalCategoryId)
        intent.putExtra(EXTRA_PRODUCTID, itemMap.productId)
        startActivityForResult(intent, LOYALTY_ACTIVITY_REQUEST_CODE)
    }

    private fun goToPromoListDealsWithVoucher(){
        val intent = RouteManager.getIntent(context, ApplinkConstInternalPromo.PROMO_LIST_DEALS)
        intent.putExtra(EXTRA_META_DATA, viewModel.getMetaDataString(verifyData))
        intent.putExtra(EXTRA_CATEGORY_NAME, verifyData.metadata.categoryName)
        intent.putExtra(EXTRA_GRAND_TOTAL, verifyData.metadata.totalPrice)
        intent.putExtra(EXTRA_CATEGORYID, dealsDetail.catalog.digitalCategoryId)
        intent.putExtra(EXTRA_PRODUCTID, itemMap.productId)
        intent.putExtra(EXTRA_PROMO_CODE, voucherCode)
        startActivityForResult(intent, LOYALTY_ACTIVITY_REQUEST_CODE)
    }

    private fun goToPromoDetailDeals(){
        val intent = RouteManager.getIntent(context, ApplinkConstInternalPromo.PROMO_DETAIL_DEALS)
        intent.putExtra(EXTRA_META_DATA, viewModel.getMetaDataString(verifyData))
        intent.putExtra(EXTRA_CATEGORY_NAME, verifyData.metadata.categoryName)
        intent.putExtra(EXTRA_GRAND_TOTAL, verifyData.metadata.totalPrice)
        intent.putExtra(COUPON_EXTRA_IS_USE, true)
        intent.putExtra(EXTRA_KUPON_CODE, couponCode)
        startActivityForResult(intent, LOYALTY_ACTIVITY_REQUEST_CODE)
    }

    private fun updateAmount(discountAmount: Long){
        tv_total_amount?.text = Utils.convertToCurrencyString(itemMap.price.toLong() * itemMap.quantity.toLong() + itemMap.commission.toLong() - discountAmount)
    }

    companion object {

        const val ORDER_LIST_DEALS = "/order-list"
        const val SCREEN_NAME = "/digital/deals/checkout"

        const val EXTRA_PRODUCTID = "EXTRA_PRODUCTID"
        const val EXTRA_CATEGORYID = "EXTRA_CATEGORYID"
        const val EXTRA_GRAND_TOTAL = "EXTRA_GRAND_TOTAL"
        const val EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME"
        val EXTRA_META_DATA = "EXTRA_META_DATA"
        const val VOUCHER_CODE = "voucher_code"
        const val COUPON_CODE = "coupon_code"
        const val IS_CANCEL = "IS_CANCEL"
        const val VOUCHER_DISCOUNT_AMOUNT = "VOUCHER_DISCOUNT_AMOUNT"
        const val COUPON_DISCOUNT_AMOUNT = "COUPON_DISCOUNT_AMOUNT"
        const val VOUCHER_MESSAGE = "voucher_message"
        const val COUPON_MESSAGE = "coupon_message"
        const val EXTRA_PROMO_CODE = "EXTRA_PROMO_CODE"
        const val COUPON_EXTRA_IS_USE = "EXTRA_IS_USE"
        const val EXTRA_KUPON_CODE = "EXTRA_KUPON_CODE"

        const val LOYALTY_ACTIVITY_REQUEST_CODE = 12345
        const val VOUCHER_RESULT_CODE = 12
        const val COUPON_RESULT_CODE = 15

        fun createInstance(bundle: Bundle): RevampCheckoutDealsFragment {
            return RevampCheckoutDealsFragment().also {
                it.arguments = bundle
            }
        }
    }
}