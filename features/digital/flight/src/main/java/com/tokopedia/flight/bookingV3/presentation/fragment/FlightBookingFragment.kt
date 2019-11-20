package com.tokopedia.flight.bookingV3.presentation.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common.travel.presentation.activity.TravelContactDataActivity
import com.tokopedia.common.travel.presentation.fragment.TravelContactDataFragment
import com.tokopedia.common.travel.presentation.model.TravelContactData
import com.tokopedia.common.travel.widget.TravellerInfoWidget
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.di.FlightBookingComponent
import com.tokopedia.flight.booking.view.activity.FlightInsuranceWebviewActivity
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel
import com.tokopedia.flight.bookingV3.data.*
import com.tokopedia.flight.bookingV3.data.mapper.FlightBookingErrorCodeMapper
import com.tokopedia.flight.bookingV3.presentation.activity.FlightBookingActivity
import com.tokopedia.flight.bookingV3.presentation.adapter.FlightBookingPassengerAdapter
import com.tokopedia.flight.bookingV3.presentation.adapter.FlightBookingPriceAdapter
import com.tokopedia.flight.bookingV3.presentation.adapter.FlightInsuranceAdapter
import com.tokopedia.flight.bookingV3.presentation.adapter.FlightJourneyAdapter
import com.tokopedia.flight.bookingV3.viewmodel.FlightBookingViewModel
import com.tokopedia.flight.common.constant.FlightErrorConstant
import com.tokopedia.flight.common.data.model.FlightError
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import com.tokopedia.flight.common.util.FlightRequestUtil
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.data.REQUST_CODE_PROMO_LIST
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_flight_booking.*
import kotlinx.android.synthetic.main.fragment_flight_booking_v3.*
import kotlinx.android.synthetic.main.fragment_flight_booking_v3.button_submit
import kotlinx.android.synthetic.main.layout_flight_booking_v3_error.view.*
import kotlinx.android.synthetic.main.layout_flight_booking_v3_loading.*
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject


/**
 * @author by jessica on 2019-10-24
 */

class FlightBookingFragment : BaseDaggerFragment() {

    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val uiScope2 = CoroutineScope(Dispatchers.Main)
    var isCouponChanged = false
    var totalCartPrice: Int = 0

    lateinit var flightRouteAdapter: FlightJourneyAdapter
    lateinit var flightInsuranceAdapter: FlightInsuranceAdapter
    lateinit var flightPassengerAdapter: FlightBookingPassengerAdapter
    lateinit var flightPriceAdapter: FlightBookingPriceAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bookingViewModel: FlightBookingViewModel

    @Inject
    lateinit var flightAnalytics: FlightAnalytics

    override fun getScreenName(): String = "/flight/booking"

    lateinit var loadingDialog: DialogUnify
    var needRefreshCart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            bookingViewModel = viewModelProvider.get(FlightBookingViewModel::class.java)
        }

        val args: Bundle? = savedInstanceState ?: arguments
        args?.let {
            val departureId = args.getString(EXTRA_FLIGHT_DEPARTURE_ID, "")
            val returnId = args.getString(EXTRA_FLIGHT_ARRIVAL_ID, "")
            val departureTerm = args.getString(EXTRA_FLIGHT_DEPARTURE_TERM, "")
            val returnTerm = args.getString(EXTRA_FLIGHT_DEPARTURE_TERM, "")
            val searchParam: FlightSearchPassDataViewModel = args.getParcelable(EXTRA_SEARCH_PASS_DATA)
                    ?: FlightSearchPassDataViewModel()
            val flightPriceViewModel: FlightPriceViewModel = args.getParcelable(EXTRA_PRICE)
                    ?: FlightPriceViewModel()

            bookingViewModel.setSearchParam(departureId, returnId, departureTerm, returnTerm, searchParam, flightPriceViewModel)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bookingViewModel.flightCartResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    if (layout_loading.isVisible) launchLoadingPageJob.cancel()
                    if (loadingDialog.isShowing) launchLoadingLayoutJob.cancel()
                    renderData(it.data)
                    setUpTimer(it.data.orderDueTimeStamp)
                    sendAddToCartTracking()
                }
                is Fail -> {
                    showErrorDialog(mapThrowableToFlightError(it.throwable.message ?: ""))
                }
            }
            hideShimmering()
        })

        bookingViewModel.flightPromoResult.observe(this, Observer {
            renderAutoApplyPromo(it)
        })

        bookingViewModel.profileResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    renderProfileData(it.data)
                }
            }
        })

        bookingViewModel.flightPassengersData.observe(this, Observer {
            renderPassengerData(it)
        })

        bookingViewModel.flightPriceData.observe(this, Observer {
            renderPriceData(it)
        })

        bookingViewModel.flightOtherPriceData.observe(this, Observer {
            renderOtherPriceData(it)
        })

        bookingViewModel.flightAmenityPriceData.observe(this, Observer {
            renderAmenityPriceData(it)
        })

        bookingViewModel.errorToastMessageData.observe(this, Observer {
            renderErrorToast(it)
        })

        bookingViewModel.flightCheckoutResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    if (loadingDialog.isShowing) launchLoadingLayoutJob.cancel()
                    navigateToTopPay(it.data)
                    sendCheckOutTracking(it.data.parameter.pid)
                }
                is Fail -> {
                    showErrorDialog(mapThrowableToFlightError(it.throwable.message ?: ""))
                }
            }
            hideShimmering()
        })

        bookingViewModel.flightVerifyResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    if (loadingDialog.isShowing) launchLoadingLayoutJob.cancel()
                    it.data.data.cartItems[0]?.let { cart ->
                        if (cart.newPrice.isNotEmpty()) {
                            showRepriceTag(cart)
                            renderRepricePrice(cart)
                        } else {
                            showCheckBookingDetailPopUp()
                        }
                    }
                }
                is Fail -> {
                    if (loadingDialog.isShowing) launchLoadingLayoutJob.cancel()
                    showErrorDialog(mapThrowableToFlightError(it.throwable.message ?: ""))
                }
            }
            hideShimmering()
        })

    }

    fun mapThrowableToFlightError(message: String): FlightError {
        val gson = Gson()
        val itemType = object : TypeToken<List<FlightError>>() {}.type
        return gson.fromJson<List<FlightError>>(message, itemType)[0]
    }

    private fun sendAddToCartTracking() {
        flightAnalytics.eventAddToCart(bookingViewModel.getSearchParam().flightClass,
                FlightBookingCartData(), 0,
                bookingViewModel.getRouteForFlightDetail(bookingViewModel.getDepartureId()),
                bookingViewModel.getRouteForFlightDetail(bookingViewModel.getReturnId()),
                bookingViewModel.getFlightPriceModel().comboKey)
    }

    private fun sendCheckOutTracking(pid: String) {
        flightAnalytics.eventCheckoutClick(
                bookingViewModel.getRouteForFlightDetail(bookingViewModel.getDepartureId()),
                bookingViewModel.getRouteForFlightDetail(bookingViewModel.getReturnId()),
                bookingViewModel.getSearchParam(),
                bookingViewModel.getFlightPriceModel().comboKey)

        flightAnalytics.eventBranchCheckoutFlight(
                "${bookingViewModel.getDepartureJourney()?.departureAirportCity}-${bookingViewModel.getDepartureJourney()?.arrivalAirportCity}",
                getTrackingJourneyId(),
                bookingViewModel.getInvoiceId(), pid,
                bookingViewModel.getUserId(),
                totalCartPrice.toString()
        )
    }

    fun getTrackingJourneyId(): String {
        if (bookingViewModel.getFlightPriceModel().comboKey.isNotEmpty()) return "${bookingViewModel.getFlightPriceModel().comboKey} ${bookingViewModel.getFlightPriceModel().comboKey}"
        else if (bookingViewModel.getReturnId().isNotEmpty()) return "${bookingViewModel.getDepartureId()} ${bookingViewModel.getReturnId()}"
        else return bookingViewModel.getDepartureId()
    }

    private fun renderErrorToast(resId: Int) {
        view?.let {
            Toaster.showErrorWithAction(it, getString(resId), Snackbar.LENGTH_LONG, "OK", View.OnClickListener { /* do nothing */ })
        }
    }

    private fun renderRepricePrice(cart: FlightVerify.FlightVerifyCart) {
        bookingViewModel.updateFlightPriceData(cart.priceDetail)
        bookingViewModel.updateFlightDetailPriceData(cart.newPrice)
    }

    private fun showRepriceTag(cart: FlightVerify.FlightVerifyCart) {
        val bottomSheet = BottomSheetUnify()
        val viewBottomSheetDialog = View.inflate(context, R.layout.layout_flight_booking_reprice_bottom_sheet, null)
        bottomSheet.setTitle("Harga tiket berubah")
        bottomSheet.setChild(viewBottomSheetDialog)
        bottomSheet.setCloseClickListener {
            bottomSheet.dismiss()
        }
        val continueToPayButton = viewBottomSheetDialog.findViewById(R.id.button_continue_pay) as UnifyButton
        val findNewTicketButton = viewBottomSheetDialog.findViewById(R.id.button_find_new_ticket) as UnifyButton
        val oldPriceTextView = viewBottomSheetDialog.findViewById(R.id.tv_before_reprice_amount) as AppCompatTextView
        val newPriceTextView = viewBottomSheetDialog.findViewById(R.id.tv_after_reprice_amount) as AppCompatTextView
        val tickerBookingPromo = viewBottomSheetDialog.findViewById(R.id.ticker_booking_promo) as Ticker

        oldPriceTextView.text = cart.oldPrice
        newPriceTextView.text = FlightCurrencyFormatUtil.convertToIdrPrice(cart.configuration.price)
        tickerBookingPromo.tickerType = if (cart.promoEligibility.success) Ticker.TYPE_ANNOUNCEMENT else Ticker.TYPE_WARNING
        tickerBookingPromo.setTextDescription(cart.promoEligibility.message)

        continueToPayButton.setOnClickListener {
            showCheckBookingDetailPopUp()
            bottomSheet.dismiss()
        }

        findNewTicketButton.setOnClickListener {
            bottomSheet.dismiss()
        }
        bottomSheet.show(fragmentManager, "harga tiket berubah")
    }

    private fun showCheckBookingDetailPopUp() {
        if (activity != null) {
            val dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle("Sudah cek detail pesananmu?")
            dialog.setDescription("Pastikan detail pesananmu benar, karena tidak bisa diubah setelah lanjut bayar")
            dialog.setPrimaryCTAText("Lanjut Bayar")
            dialog.setSecondaryCTAText("Cek Ulang")

            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                launchLoadingLayoutJob.start()
                bookingViewModel.checkOutCart(GraphqlHelper.loadRawString(resources, com.tokopedia.flight.R.raw.flight_gql_query_checkout_cart),
                        totalCartPrice,
                        GraphqlHelper.loadRawString(resources, com.tokopedia.flight.R.raw.dummy_checkout_data))
            }

            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun setUpTimer(timeStamp: Date) {
        countdown_timeout.setListener {
            if (context != null) {
                refreshCart()
            } else needRefreshCart = true
        }
        countdown_timeout.cancel()
        countdown_timeout.setExpiredDate(timeStamp)
        countdown_timeout.start()
    }

    private fun refreshCart() {
        launchLoadingLayoutJob.start()
        bookingViewModel.getCart(GraphqlHelper.loadRawString(resources, com.tokopedia.flight.R.raw.flight_gql_query_get_cart),
                bookingViewModel.getCartId(), "")
    }

    private fun renderData(cart: FlightCartViewEntity) {
        if (!::flightRouteAdapter.isInitialized) flightRouteAdapter = FlightJourneyAdapter()
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rv_flight_booking_route_summary.layoutManager = layoutManager
        rv_flight_booking_route_summary.setHasFixedSize(true)
        rv_flight_booking_route_summary.adapter = flightRouteAdapter
        flightRouteAdapter.listener = object : FlightJourneyAdapter.ViewHolder.ActionListener {
            override fun onClickRouteDetail(id: String) {
                bookingViewModel.getRouteForFlightDetail(id)?.let {
                    navigateToDetailTrip(it)
                }
            }
        }
        flightRouteAdapter.updateRoutes(cart.journeySummaries)

        renderInsuranceData(cart.insurances)
    }

    fun navigateToDetailTrip(departureTrip: FlightDetailViewModel) {
        startActivity(FlightDetailActivity.createIntent(activity, departureTrip, false))
    }

    private fun renderInsuranceData(insurances: List<FlightCart.Insurance>) {
        if (!::flightInsuranceAdapter.isInitialized) {
            flightInsuranceAdapter = FlightInsuranceAdapter()
            val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            rv_insurance_list.layoutManager = layoutManager
            rv_insurance_list.setHasFixedSize(true)
            rv_insurance_list.adapter = flightInsuranceAdapter
            flightInsuranceAdapter.listener = object : FlightInsuranceAdapter.ViewHolder.ActionListener {
                override fun onClickInsuranceTnc(tncUrl: String, tncTitle: String) {
                    startActivity(FlightInsuranceWebviewActivity.getCallingIntent(activity, tncUrl, tncTitle))
                }

                override fun onInsuranceChecked(insurance: FlightCart.Insurance, checked: Boolean) {
                    bookingViewModel.onInsuranceChanges(insurance, checked)
                }
            }
        }
        flightInsuranceAdapter.updateList(insurances)
    }

    private fun renderPriceData(priceList: List<FlightCart.PriceDetail>) {
        if (!::flightPriceAdapter.isInitialized) {
            flightPriceAdapter = FlightBookingPriceAdapter()
            val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            rv_flight_price_detail.layoutManager = layoutManager
            rv_flight_price_detail.setHasFixedSize(true)
            rv_flight_price_detail.adapter = flightPriceAdapter
            flightPriceAdapter.listener = object : FlightBookingPriceAdapter.PriceListener {
                override fun onPriceChangeListener(totalPrice: String, totalPriceNumeric: Int) {
                    tv_total_payment_amount.text = totalPrice
                    totalCartPrice = totalPriceNumeric
                }
            }
        }
        flightPriceAdapter.updateRoutePriceList(priceList)
    }

    private fun renderOtherPriceData(priceList: List<FlightCart.PriceDetail>) {
        if (::flightPriceAdapter.isInitialized) {
            flightPriceAdapter.updateOthersPriceList(priceList)
        }
    }

    private fun renderAmenityPriceData(priceList: List<FlightCart.PriceDetail>) {
        if (::flightPriceAdapter.isInitialized) {
            flightPriceAdapter.updateAmenityPriceList(priceList)
        }
    }

    private fun renderPassengerData(passengers: List<FlightBookingPassengerViewModel>) {
        if (!::flightPassengerAdapter.isInitialized) {
            flightPassengerAdapter = FlightBookingPassengerAdapter()
            val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            rv_passengers_info.layoutManager = layoutManager
            rv_passengers_info.adapter = flightPassengerAdapter
            flightPassengerAdapter.listener = object : FlightBookingPassengerAdapter.PassengerViewHolderListener {
                override fun onClickEditPassengerListener(passenger: FlightBookingPassengerViewModel) {
                    // if paramvm is oneoway, depaturedate = depaturedate else returndate
                    val requestId = if (getReturnId().isNotEmpty()) generateIdEmpotency("${getDepartureId()}_${getReturnId()}") else generateIdEmpotency(getDepartureId())
                    navigateToPassengerInfoDetail(passenger, bookingViewModel.getDepartureDate(), requestId)
                }
            }
        }
        flightPassengerAdapter.updateList(passengers)
    }

    private fun getDepartureId(): String = bookingViewModel.getDepartureId()
    private fun getReturnId(): String = bookingViewModel.getReturnId()

    private fun navigateToPassengerInfoDetail(viewModel: FlightBookingPassengerViewModel, departureDate: String, requestId: String) {
        startActivityForResult(
                FlightBookingPassengerActivity.getCallingIntent(
                        activity as Activity,
                        getDepartureId(),
                        getReturnId(),
                        viewModel,
                        bookingViewModel.getLuggageViewModels(),
                        bookingViewModel.getMealViewModels(),
                        bookingViewModel.getMandatoryDOB(),
                        departureDate,
                        requestId,
                        bookingViewModel.flightIsDomestic()
                ),
                REQUEST_CODE_PASSENGER
        )
    }


    private fun generateIdEmpotency(requestId: String): String {
        var userId = Math.random().toString()
        if (activity != null && activity!!.application is AbstractionRouter) {
            val userSession = UserSession(activity)
            userId += userSession.userId
        }
        val timeMillis = System.currentTimeMillis().toString()
        val token = FlightRequestUtil.md5(timeMillis)
        return userId + String.format(getString(com.tokopedia.flight.R.string.flight_booking_id_empotency_format),
                requestId, if (token.isEmpty()) timeMillis else token)
    }

    private fun renderProfileData(profileInfo: ProfileInfo) {
        widget_traveller_info.setContactName(profileInfo.fullName)
        widget_traveller_info.setContactPhoneNum(62, profileInfo.phone)
        widget_traveller_info.setContactEmail(profileInfo.email)
    }

    override fun initInjector() {
        getComponent(FlightBookingComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(com.tokopedia.flight.R.layout.fragment_flight_booking_v3, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchLoadingPageJob.start()
        setUpView()
        val requestId = if (getReturnId().isNotEmpty()) generateIdEmpotency("${getDepartureId()}_${getReturnId()}") else generateIdEmpotency(getDepartureId())
        bookingViewModel.addToCart(GraphqlHelper.loadRawString(resources, com.tokopedia.flight.R.raw.flight_gql_query_add_to_cart),
                GraphqlHelper.loadRawString(resources, com.tokopedia.flight.R.raw.flight_gql_query_get_cart),
                GraphqlHelper.loadRawString(resources, com.tokopedia.flight.R.raw.dummy_get_cart),
                requestId)
        bookingViewModel.getProfile(GraphqlHelper.loadRawString(resources, com.tokopedia.sessioncommon.R.raw.query_profile))
    }

    private fun navigateToTopPay(checkoutData: FlightCheckoutData) {
        countdown_timeout.cancel()
        val paymentPassData = PaymentPassData()
        paymentPassData.paymentId = checkoutData.parameter.pid
        paymentPassData.transactionId = checkoutData.parameter.transactionId
        paymentPassData.redirectUrl = checkoutData.redirectUrl
        paymentPassData.callbackFailedUrl = checkoutData.callbackURLFailed
        paymentPassData.callbackSuccessUrl = checkoutData.callbackUrlSuccess
        paymentPassData.queryString = checkoutData.queryString

        val intent = RouteManager.getIntent(context, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
        startActivity(intent)
        activity?.finish()
    }


    private fun setUpView() {
        hidePriceDetail()

        context?.let {
            loadingDialog = DialogUnify(it, 0, 0)
        }

        widget_traveller_info.setListener(object : TravellerInfoWidget.TravellerInfoWidgetListener {
            override fun onClickEdit() {
                context?.let {
                    startActivityForResult(TravelContactDataActivity.getCallingIntent(it,
                            TravelContactData(widget_traveller_info.getContactName(),
                                    widget_traveller_info.getContactEmail(),
                                    widget_traveller_info.getContactPhoneNum(),
                                    widget_traveller_info.getContactPhoneCode()),
                            TravelContactDataActivity.FLIGHT),
                            REQUEST_CODE_CONTACT_FORM)
                }
            }
        })

        layout_see_detail_price.setOnClickListener { if (rv_flight_price_detail.isVisible) hidePriceDetail() else showPriceDetail() }
        switch_traveller_as_passenger.setOnCheckedChangeListener { _, on ->
            bookingViewModel.onTravellerAsPassenger(on)
        }
        button_submit.setOnClickListener { verifyCart() }
    }

    private fun verifyCart() {
        launchLoadingLayoutJob.start()
        bookingViewModel.verifyCartData(
                GraphqlHelper.loadRawString(resources, com.tokopedia.flight.R.raw.flight_gql_query_verify_cart),
                totalPrice = totalCartPrice,
                contactName = widget_traveller_info.getContactName(),
                contactEmail = widget_traveller_info.getContactEmail(),
                contactPhone = widget_traveller_info.getContactPhoneNum(),
                contactCountry = "ID",
                dummy = GraphqlHelper.loadRawString(resources, com.tokopedia.flight.R.raw.dummy_verify_cart),
                checkVoucherQuery = GraphqlHelper.loadRawString(resources, com.tokopedia.flight.R.raw.flight_gql_query_check_voucher),
                dummyCheckVoucher = GraphqlHelper.loadRawString(resources, com.tokopedia.flight.R.raw.dummy_check_voucher))
    }

    fun renderAutoApplyPromo(flightVoucher: FlightPromoViewEntity) {
        if (flightVoucher.isCouponEnable) showVoucherContainer() else hideVoucherContainer()
        renderPromoTicker(flightVoucher)
    }

    private fun renderPromoTicker(flightVoucher: FlightPromoViewEntity) {
        flight_promo_ticker_view.state = when (flightVoucher.promoData.state) {
            TickerCheckoutView.State.EMPTY -> TickerPromoStackingCheckoutView.State.EMPTY
            TickerCheckoutView.State.ACTIVE -> TickerPromoStackingCheckoutView.State.ACTIVE
            TickerCheckoutView.State.FAILED -> TickerPromoStackingCheckoutView.State.FAILED
            else -> TickerPromoStackingCheckoutView.State.EMPTY
        }
        flight_promo_ticker_view.title = flightVoucher.promoData.title
        flight_promo_ticker_view.desc = flightVoucher.promoData.description
        flight_promo_ticker_view.actionListener = object : TickerPromoStackingCheckoutView.ActionListener {
            override fun onResetPromoDiscount() {
                isCouponChanged = true
                bookingViewModel.updatePromoData(PromoData(state = TickerCheckoutView.State.EMPTY, title = "", description = ""))
            }

            override fun onClickUsePromo() {
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_FLIGHT)
                intent.putExtra(COUPON_EXTRA_COUPON_ACTIVE, flightVoucher.isCouponActive)
                intent.putExtra(COUPON_EXTRA_CART_ID, bookingViewModel.getCartId())
                startActivityForResult(intent, COUPON_EXTRA_LIST_ACTIVITY_RESULT)
            }

            override fun onDisablePromoDiscount() {
                bookingViewModel.updatePromoData(PromoData(state = TickerCheckoutView.State.EMPTY, title = "", description = "", promoCode = ""))
                bookingViewModel.onCancelAppliedVoucher(GraphqlHelper.loadRawString(resources, R.raw.promo_checkout_flight_cancel_voucher))
            }

            override fun onClickDetailPromo() {
                val intent: Intent
                val promoCode = flightVoucher.promoData.promoCode
                if (!promoCode.isEmpty()) {
                    val requestCode: Int
                    if (flightVoucher.promoData.typePromo == PromoData.TYPE_VOUCHER) {
                        intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_FLIGHT)
                        intent.putExtra("EXTRA_PROMO_CODE", promoCode)
                        intent.putExtra("EXTRA_COUPON_ACTIVE", flightVoucher.isCouponActive)
                        requestCode = REQUST_CODE_PROMO_LIST
                    } else {
                        intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_FLIGHT)
                        intent.putExtra("EXTRA_IS_USE", true)
                        intent.putExtra("EXTRA_KUPON_CODE", promoCode)
                        requestCode = REQUEST_CODE_PROMO_DETAIL
                    }
                    intent.putExtra("EXTRA_CART_ID", bookingViewModel.getCartId())
                    startActivityForResult(intent, requestCode)
                } else {
                    Toast.makeText(activity, com.tokopedia.promocheckout.common.R.string.promo_none_applied, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun hideVoucherContainer() {
        flight_promo_ticker_view.hide()
        seperator_4.hide()
    }

    private fun showVoucherContainer() {
        flight_promo_ticker_view.show()
        seperator_4.show()
    }

    private fun randomLoadingSubtitle(): List<String> {
        var list = listOf(getString(com.tokopedia.flight.R.string.flight_booking_loading_text_1),
                getString(com.tokopedia.flight.R.string.flight_booking_loading_text_2),
                getString(com.tokopedia.flight.R.string.flight_booking_loading_text_3),
                getString(com.tokopedia.flight.R.string.flight_booking_loading_text_4))
        return list.shuffled()
    }

    private var launchLoadingPageJob = uiScope.launch {
        val list = randomLoadingSubtitle()
        layout_loading.visibility = View.VISIBLE
        tv_loading_subtitle.text = list[0]
        delay(2000L)
        tv_loading_subtitle.text = list[1]
        delay(2000L)
        tv_loading_subtitle.text = list[2]
        delay(2000L)
        layout_loading.visibility = View.GONE
        layout_shimmering.visibility = View.VISIBLE
    }

    private var launchLoadingLayoutJob = uiScope2.launch {
        val list = randomLoadingSubtitle()
        val view = View.inflate(context, R.layout.layout_flight_booking_loading, null)
        val loadingText = view.findViewById(R.id.tv_loading_subtitle) as Typography
        showLoadingDialog(view)
        loadingText.text = list[0]
        delay(2000L)
        loadingText.text = list[1]
        delay(2000L)
        loadingText.text = list[2]
        delay(2000L)
        loadingText.text = list[3]
        delay(2000L)
    }

    private fun hideShimmering() {
        if (layout_loading.isVisible) layout_loading.hide()
        if (layout_shimmering.isVisible) layout_shimmering.hide()
    }

    private fun hidePriceDetail() {
        thin_seperator_1.hide()
        rv_flight_price_detail.hide()
        iv_see_detail_price_arrow.setImageResource(com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_down_normal_24)
    }

    private fun showPriceDetail() {
        thin_seperator_1.show()
        rv_flight_price_detail.show()
        iv_see_detail_price_arrow.setImageResource(com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_up_normal_24)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_CONTACT_FORM -> if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val contactData: TravelContactData = it.getParcelableExtra(TravelContactDataFragment.EXTRA_CONTACT_DATA)
                    widget_traveller_info.setContactName(contactData.name)
                    widget_traveller_info.setContactEmail(contactData.email)
                    widget_traveller_info.setContactPhoneNum(contactData.phoneCode, contactData.phone)
                }
            }

            REQUEST_CODE_PASSENGER -> if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val passengerViewModel = it.getParcelableExtra<FlightBookingPassengerViewModel>(FlightBookingPassengerActivity.EXTRA_PASSENGER)
                    bookingViewModel.onPassengerResultReceived(passengerViewModel)
                }
            }

            COUPON_EXTRA_LIST_ACTIVITY_RESULT, COUPON_EXTRA_DETAIL_ACTIVITY_RESULT -> if (resultCode == RESULT_OK) {
                data?.let {
                    var promoData = PromoData()
                    if (it.hasExtra(EXTRA_PROMO_DATA)) promoData = data.getParcelableExtra(COUPON_EXTRA_PROMO_DATA)
                    when (promoData.state) {
                        TickerCheckoutView.State.EMPTY -> {
                            promoData.promoCode = ""
                            promoData.state = TickerCheckoutView.State.EMPTY
                            bookingViewModel.updatePromoData(promoData)
                        }
                        TickerCheckoutView.State.FAILED -> {
                            promoData.promoCode = ""
                            promoData.state = TickerCheckoutView.State.FAILED
                            bookingViewModel.updatePromoData(promoData)
                        }
                        TickerCheckoutView.State.ACTIVE -> {
                            promoData.state = TickerCheckoutView.State.ACTIVE
                            bookingViewModel.updatePromoData(promoData)
                        }
                        else -> {
                            promoData.promoCode = ""
                            promoData.state = TickerCheckoutView.State.EMPTY
                            bookingViewModel.updatePromoData(promoData)
                        }
                    }
                }
            }
        }

        if (needRefreshCart) {
            needRefreshCart = false
            refreshCart()
        }
    }

    private fun showErrorDialog(e: FlightError) {
        if (activity != null) {
            val errorCode = FlightBookingErrorCodeMapper.mapToFlightErrorCode(e.id.toInt())
            if (errorCode == FlightErrorConstant.FLIGHT_DUPLICATE_USER_NAME) renderErrorToast(R.string.flight_duplicate_user_error_toaster_text)
            else if (errorCode == FlightErrorConstant.FLIGHT_SOLD_OUT) {
                layout_full_page_error.visibility = View.VISIBLE
                layout_full_page_error.iv_error_page.setImageResource(R.drawable.ic_travel_no_ticket)
                layout_full_page_error.tv_error_title.text = FlightBookingErrorCodeMapper.getErrorTitle(errorCode)
                layout_full_page_error.tv_error_subtitle.text = FlightBookingErrorCodeMapper.getErrorSubtitle(errorCode)
                layout_full_page_error.button_error_action.text = "Cari Tiket Ulang"
                layout_full_page_error.button_error_action.setOnClickListener { navigateBackToSearch() }
            } else {
                lateinit var dialog: DialogUnify
                when (errorCode) {
                    FlightErrorConstant.INVALID_JSON -> {
                        dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
                        dialog.setImageDrawable(R.drawable.ic_flight_booking_error_refresh)
                        dialog.setPrimaryCTAText("Cari Tiket Ulang")
                        dialog.setPrimaryCTAClickListener {
                            dialog.dismiss()
                            navigateBackToSearch()
                        }
                    }
                    FlightErrorConstant.FAILED_ADD_FACILITY -> {
                        dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ICON)
                        dialog.setImageDrawable(R.drawable.ic_flight_booking_error_add_luggage)
                        dialog.setPrimaryCTAText("Lanjut Bayar")
                        dialog.setPrimaryCTAClickListener {
                            dialog.dismiss()
                            proceedCheckoutWithoutLuggage()
                        }
                        dialog.setSecondaryCTAText("Batalkan")
                        dialog.setSecondaryCTAClickListener { dialog.dismiss() }
                    }
                    FlightErrorConstant.ERROR_PROMO_CODE -> {
                        dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
                        dialog.setImageDrawable(R.drawable.ic_flight_booking_error_promo_code)
                        dialog.setPrimaryCTAText("Cek Kode Promo")
                        dialog.setPrimaryCTAClickListener {
                            dialog.dismiss()
                            navigateToPromoPage()
                        }
                    }
                    FlightErrorConstant.FLIGHT_DUPLICATE_BOOKING -> {
                        dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ICON)
                        dialog.setImageDrawable(R.drawable.ic_flight_booking_error_wait)
                        dialog.setPrimaryCTAText("Cek Pesanan")
                        dialog.setPrimaryCTAClickListener {
                            dialog.dismiss()
                            navigateToFlightOrderList()
                        }
                        dialog.setSecondaryCTAText("Pesan Tiket Lain")
                        dialog.setSecondaryCTAClickListener {
                            dialog.dismiss()
                            navigateBackToSearch()
                        }
                    }
                    FlightErrorConstant.FLIGHT_STILL_IN_PROCESS -> {
                        dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ICON)
                        dialog.setImageDrawable(R.drawable.ic_flight_booking_error_wait)
                        dialog.setPrimaryCTAText("Pesan Tiket Lain")
                        dialog.setPrimaryCTAClickListener {
                            dialog.dismiss()
                            navigateBackToSearch()
                        }
                        dialog.setSecondaryCTAText("Oke")
                        dialog.setSecondaryCTAClickListener { dialog.dismiss() }
                    }
                    FlightErrorConstant.FLIGHT_ERROR_GET_CART_EXCEED_MAX_RETRY -> {
                        dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
                        dialog.setImageDrawable(R.drawable.ic_flight_booking_error_refresh)
                        dialog.setPrimaryCTAText("Cari Tiket Ulang")
                        dialog.setPrimaryCTAClickListener {
                            dialog.dismiss()
                            refreshCart()
                        }
                    }
                    FlightErrorConstant.FLIGHT_ERROR_VERIFY_EXCEED_MAX_RETRY -> {
                        dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
                        dialog.setImageDrawable(R.drawable.ic_flight_booking_error_refresh)
                        dialog.setPrimaryCTAText("Coba Lagi")
                        dialog.setPrimaryCTAClickListener {
                            dialog.dismiss()
                            refreshCart()
                        }
                    }
                    else -> {
                        dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
                        dialog.setImageDrawable(R.drawable.ic_flight_booking_error_refresh)
                        dialog.setPrimaryCTAText("Cari Tiket Ulang")
                        dialog.setPrimaryCTAClickListener {
                            dialog.dismiss()
                            navigateBackToSearch()
                        }
                    }
                }
                dialog.setTitle(FlightBookingErrorCodeMapper.getErrorTitle(errorCode))
                dialog.setDescription(FlightBookingErrorCodeMapper.getErrorSubtitle(errorCode))
                dialog.show()
            }

        }
    }

    private fun navigateBackToSearch() {
        Log.d("HEHE", "navigateBackToSearch()")
        RouteManager.route(context, "tokopedia://pesawat")
        activity?.finish()
    }

    private fun proceedCheckoutWithoutLuggage() {
        Log.d("HEHE", "proceedCheckoutWoLuggage()")
        launchLoadingLayoutJob.start()
        bookingViewModel.proceedCheckoutWithoutLuggage(GraphqlHelper.loadRawString(resources, com.tokopedia.flight.R.raw.flight_gql_query_checkout_cart),
                totalCartPrice)
    }

    private fun navigateToPromoPage() {
        Log.d("HEHE", "navigateBackToPromoPage()")
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_FLIGHT)
        intent.putExtra(COUPON_EXTRA_COUPON_ACTIVE, 0)
        intent.putExtra(COUPON_EXTRA_CART_ID, bookingViewModel.getCartId())
        startActivityForResult(intent, COUPON_EXTRA_LIST_ACTIVITY_RESULT)
    }

    private fun navigateToFlightOrderList() {
        Log.d("HEHE", "navigateBackToFlightOL()")
        RouteManager.route(context, "tokopedia://pesawat/order")
        activity?.finish()
    }

    private fun showLoadingDialog(view: View) {
        if (context != null && ::loadingDialog.isInitialized) {
            loadingDialog.setUnlockVersion()
            loadingDialog.setChild(view)
            loadingDialog.setOverlayClose(false)
            if (launchLoadingPageJob.isCancelled) loadingDialog.show()
        }
    }

    companion object {

        const val COUPON_EXTRA_COUPON_ACTIVE = "EXTRA_COUPON_ACTIVE"
        const val COUPON_EXTRA_CART_ID = "EXTRA_CART_ID"
        const val COUPON_EXTRA_LIST_ACTIVITY_RESULT = 3121
        const val COUPON_EXTRA_DETAIL_ACTIVITY_RESULT = 3122
        const val COUPON_EXTRA_PROMO_DATA = "EXTRA_PROMO_DATA"
        const val EXTRA_SEARCH_PASS_DATA = "EXTRA_SEARCH_PASS_DATA"
        const val EXTRA_FLIGHT_DEPARTURE_ID = "EXTRA_FLIGHT_DEPARTURE_ID"
        const val EXTRA_FLIGHT_ARRIVAL_ID = "EXTRA_FLIGHT_ARRIVAL_ID"
        const val EXTRA_FLIGHT_DEPARTURE_TERM = "EXTRA_FLIGHT_DEPARTURE_TERM"
        const val EXTRA_FLIGHT_ARRIVAL_TERM = "EXTRA_FLIGHT_ARRIVAL_TERM"
        private val EXTRA_PRICE = "EXTRA_PRICE"

        const val REQUEST_CODE_PASSENGER = 1
        const val REQUEST_CODE_CONTACT_FORM = 12

        fun newInstance(): FlightBookingFragment {
            return FlightBookingFragment()
        }

        fun newInstance(searchPassDataViewModel: FlightSearchPassDataViewModel,
                        departureId: String, returnId: String,
                        departureTerm: String, returnTerm: String,
                        priceViewModel: FlightPriceViewModel): FlightBookingFragment {
            val fragment = FlightBookingFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_SEARCH_PASS_DATA, searchPassDataViewModel)
            bundle.putString(EXTRA_FLIGHT_DEPARTURE_ID, departureId)
            bundle.putString(EXTRA_FLIGHT_ARRIVAL_ID, returnId)
            bundle.putString(EXTRA_FLIGHT_DEPARTURE_TERM, departureTerm)
            bundle.putString(EXTRA_FLIGHT_ARRIVAL_TERM, returnTerm)
            bundle.putParcelable(EXTRA_PRICE, priceViewModel)
            fragment.arguments = bundle
            return fragment
        }

        private val EXTRA_PARAMETER_TOP_PAY_DATA = "EXTRA_PARAMETER_TOP_PAY_DATA"
    }

}