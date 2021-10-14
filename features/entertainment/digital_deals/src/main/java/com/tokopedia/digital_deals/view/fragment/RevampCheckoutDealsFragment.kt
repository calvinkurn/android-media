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
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
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
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_checkout_deal.*
import javax.inject.Inject

class RevampCheckoutDealsFragment : BaseDaggerFragment() {

    private var dealsDetail: DealsDetailsResponse = DealsDetailsResponse()
    private var verifyData: EventVerifyResponse = EventVerifyResponse()
    private var itemMap: ItemMapResponse = ItemMapResponse()
    private var quantity = 1
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
            val intentHomeEvent = RouteManager.getIntent(context, ApplinkConstInternalEntertainment.EVENT_HOME)
            taskStackBuilder.addNextIntent(intentHomeEvent)
            taskStackBuilder.startActivities()

            val intent = RouteManager.getIntent(context, ApplinkConst.DEALS_ORDER)
            intent?.run {
                taskStackBuilder.addNextIntent(this)
                taskStackBuilder.startActivities()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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
        tv_sales_price_all_quantity?.text = Utils.convertToCurrencyString((itemMap.price * itemMap.quantity).toLong())

        if (itemMap.commission == 0) {
            tv_service_fee?.gone()
            tv_service_fee_amount?.gone()
        } else {
            tv_service_fee?.text = Utils.convertToCurrencyString(itemMap.commission.toLong())
        }

        tv_total_amount?.text = Utils.convertToCurrencyString((itemMap.price * itemMap.quantity + itemMap.commission).toLong())
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

        ticker_promocode?.gone()
        //TODO PROMO TRACKER

        ll_select_payment_method?.setOnClickListener {
            dealsAnalytics.sendEcommercePayment(dealsDetail.categoryId, dealsDetail.id, verifyData.metadata.quantity, dealsDetail.salesPrice,
                    dealsDetail.displayName, dealsDetail.brand.title, promoApplied, userSession.userId)
            if (verifyData.gatewayCode.isNullOrEmpty()) {
                viewModel.checkoutGeneral(viewModel.mapCheckoutDeals(dealsDetail, verifyData))
            } else {
                viewModel.checkoutGeneralInstant(viewModel.mapCheckoutDealsInstant(dealsDetail, verifyData))
            }
            showProgressBar()
        }
    }

    companion object {

        const val ORDER_LIST_DEALS = "/order-list"
        const val SCREEN_NAME = "/digital/deals/checkout"

        fun createInstance(bundle: Bundle): RevampCheckoutDealsFragment {
            return RevampCheckoutDealsFragment().also {
                it.arguments = bundle
            }
        }
    }
}