package com.tokopedia.flight.booking.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common.travel.ticker.TravelTickerUtils
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.data.*
import com.tokopedia.flight.booking.data.mapper.FlightBookingErrorCodeMapper
import com.tokopedia.flight.booking.di.FlightBookingComponent
import com.tokopedia.flight.booking.presentation.activity.FlightBookingActivity
import com.tokopedia.flight.booking.presentation.adapter.FlightBookingPassengerAdapter
import com.tokopedia.flight.booking.presentation.adapter.FlightBookingPriceAdapter
import com.tokopedia.flight.booking.presentation.adapter.FlightInsuranceAdapter
import com.tokopedia.flight.booking.presentation.adapter.FlightJourneyAdapter
import com.tokopedia.flight.booking.viewmodel.FlightBookingViewModel
import com.tokopedia.flight.common.constant.FlightErrorConstant
import com.tokopedia.flight.common.constant.FlightFlowConstant
import com.tokopedia.flight.common.data.model.FlightError
import com.tokopedia.flight.common.util.*
import com.tokopedia.flight.detail.view.model.FlightDetailModel
import com.tokopedia.flight.detail.view.widget.FlightDetailBottomSheet
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity
import com.tokopedia.flight.passenger.view.model.FlightBookingPassengerModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckout.common.data.PromoCheckoutCommonQueryConst
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_LIST
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.travel.passenger.presentation.activity.TravelContactDataActivity
import com.tokopedia.travel.passenger.presentation.fragment.TravelContactDataFragment
import com.tokopedia.travel.passenger.presentation.model.TravelContactData
import com.tokopedia.travel.passenger.presentation.widget.TravellerInfoWidget
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_flight_booking_v3.*
import kotlinx.android.synthetic.main.layout_flight_booking_v3_error.view.*
import kotlinx.android.synthetic.main.layout_flight_booking_v3_loading.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


/**
 * @author by jessica on 2019-10-24
 */

class FlightBookingFragment : BaseDaggerFragment() {

    private val uiScope = CoroutineScope(Dispatchers.Main)
    var isCouponChanged = false
    var totalCartPrice: Int = 0

    private lateinit var flightRouteAdapter: FlightJourneyAdapter
    private lateinit var flightInsuranceAdapter: FlightInsuranceAdapter
    private lateinit var flightPassengerAdapter: FlightBookingPassengerAdapter
    private lateinit var flightPriceAdapter: FlightBookingPriceAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bookingViewModel: FlightBookingViewModel

    @Inject
    lateinit var flightAnalytics: FlightAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun getScreenName(): String = "/flight/booking"

    lateinit var loadingDialog: DialogUnify
    lateinit var loadingText: Typography

    private var needRefreshCart = false
    private var needToDoChangesOnFirstPassenger = true
    private var passengerAsTraveller = false
    private var orderDueTimeStampString: String = ""
    private var isFirstTime: Boolean = true

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
            val returnTerm = args.getString(EXTRA_FLIGHT_ARRIVAL_TERM, "")
            val searchParam: FlightSearchPassDataModel = args.getParcelable(EXTRA_SEARCH_PASS_DATA)
                    ?: FlightSearchPassDataModel()
            val flightPriceModel: FlightPriceModel = args.getParcelable(EXTRA_PRICE)
                    ?: FlightPriceModel()

            bookingViewModel.setSearchParam(departureId, returnId, departureTerm, returnTerm, searchParam, flightPriceModel)
        }

        bookingViewModel.fetchTickerData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(EXTRA_SEARCH_PASS_DATA, bookingViewModel.getSearchParam())
        outState.putString(EXTRA_FLIGHT_DEPARTURE_ID, bookingViewModel.getDepartureId())
        outState.putString(EXTRA_FLIGHT_ARRIVAL_ID, bookingViewModel.getReturnId())
        outState.putString(EXTRA_FLIGHT_DEPARTURE_TERM, bookingViewModel.getDepartureTerm())
        outState.putString(EXTRA_FLIGHT_ARRIVAL_TERM, bookingViewModel.getReturnTerm())
        outState.putParcelable(EXTRA_PRICE, bookingViewModel.getFlightPriceModel())
        outState.putString(EXTRA_CART_ID, bookingViewModel.getCartId())
        outState.putParcelable(EXTRA_FLIGHT_BOOKING_PARAM, bookingViewModel.getFlightBookingParam())
        outState.putString(EXTRA_ORDER_DUE, orderDueTimeStampString)
        outState.putParcelable(EXTRA_CONTACT_DATA, FlightContactData(widget_traveller_info.getContactName(),
                widget_traveller_info.getContactEmail(),
                widget_traveller_info.getContactPhoneNum(),
                widget_traveller_info.getContactPhoneCountry(),
                widget_traveller_info.getContactPhoneCode()))
        if (bookingViewModel.getPassengerModels().isNotEmpty()) outState.putParcelableArrayList(EXTRA_PASSENGER_MODELS, bookingViewModel.getPassengerModels() as ArrayList<out Parcelable>)
        if (bookingViewModel.getPriceData().isNotEmpty()) outState.putParcelableArrayList(EXTRA_PRICE_DATA, bookingViewModel.getPriceData() as ArrayList<out Parcelable>)
        if (bookingViewModel.getOtherPriceData().isNotEmpty()) outState.putParcelableArrayList(EXTRA_OTHER_PRICE_DATA, bookingViewModel.getOtherPriceData() as ArrayList<out Parcelable>)
        if (bookingViewModel.getAmenityPriceData().isNotEmpty()) outState.putParcelableArrayList(EXTRA_AMENITY_PRICE_DATA, bookingViewModel.getAmenityPriceData() as ArrayList<out Parcelable>)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bookingViewModel.flightCartResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (layout_loading.isVisible) launchLoadingPageJob.cancel()
                    if (!it.data.isRefreshCart || savedInstanceState != null) {
                        renderData(it.data)
                        sendAddToCartTracking()
                    }
                    setUpTimer(it.data.orderDueTimeStamp)
                }
                is Fail -> {
                    showErrorDialog(mapThrowableToFlightError(it.throwable.message
                            ?: ""), ::refreshCart)
                }
            }
            if (bookingViewModel.isStillLoading) showLoadingDialog()
            else if (bookingViewModel.getDepartureJourney() != null) hideShimmering()
        })

        bookingViewModel.flightPromoResult.observe(viewLifecycleOwner, Observer {
            renderAutoApplyPromo(it)
        })

        bookingViewModel.profileResult.observe(viewLifecycleOwner, Observer {
            if (it is Success) renderProfileData(it.data)
        })

        bookingViewModel.flightPassengersData.observe(viewLifecycleOwner, Observer {
            renderPassengerData(it)
        })

        bookingViewModel.flightPriceData.observe(viewLifecycleOwner, Observer {
            renderPriceData(it)
        })

        bookingViewModel.flightOtherPriceData.observe(viewLifecycleOwner, Observer {
            renderOtherPriceData(it)
        })

        bookingViewModel.flightAmenityPriceData.observe(viewLifecycleOwner, Observer {
            renderAmenityPriceData(it)
        })

        bookingViewModel.errorToastMessageData.observe(viewLifecycleOwner, Observer {
            if (it == 0) showLoadingDialog() else renderErrorToast(it)
        })

        bookingViewModel.flightCheckoutResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    sendCheckOutTracking(it.data.parameter.pid)
                    navigateToTopPay(it.data)
                }
                is Fail -> {
                    showErrorDialog(mapThrowableToFlightError(it.throwable.message
                            ?: ""), ::checkOutCart)
                }
            }
            if (bookingViewModel.isStillLoading) showLoadingDialog() else hideShimmering()
        })

        bookingViewModel.flightVerifyResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
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
                    showErrorDialog(mapThrowableToFlightError(it.throwable.message
                            ?: ""), ::verifyCart)
                }
            }
            if (bookingViewModel.isStillLoading) showLoadingDialog() else hideShimmering()
        })

        bookingViewModel.tickerData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.message.isNotEmpty()) {
                        renderTickerView(it.data)
                    } else {
                        hideTickerView()
                    }
                }
                is Fail -> {
                    hideTickerView()
                }
            }
        })

    }

    private fun setUpView() {
        hidePriceDetail()

        widget_traveller_info.setListener(object : TravellerInfoWidget.TravellerInfoWidgetListener {
            override fun onClickEdit() {
                context?.let {
                    startActivityForResult(TravelContactDataActivity.getCallingIntent(it,
                            TravelContactData(widget_traveller_info.getContactName(),
                                    widget_traveller_info.getContactEmail(),
                                    widget_traveller_info.getContactPhoneNum(),
                                    widget_traveller_info.getContactPhoneCode(),
                                    widget_traveller_info.getContactPhoneCountry()),
                            TravelContactDataActivity.FLIGHT),
                            REQUEST_CODE_CONTACT_FORM)
                }
            }
        })

        layout_see_detail_price.setOnClickListener { if (rv_flight_price_detail.isVisible) hidePriceDetail() else showPriceDetail() }
        switch_traveller_as_passenger.setOnCheckedChangeListener { _, on ->
            if (needToDoChangesOnFirstPassenger) {
                if (on) {
                    val firstPassenger = bookingViewModel.onTravellerAsPassenger(widget_traveller_info.getContactName())
                    passengerAsTraveller = false
                    if (isFirstTime) navigateToPassengerInfoDetail(firstPassenger, getRequestId(), firstPassenger.passengerFirstName)
                    isFirstTime = true
                } else {
                    bookingViewModel.resetFirstPassenger()
                }
            }
            needToDoChangesOnFirstPassenger = true

        }
        button_submit.setOnClickListener { verifyCart() }
    }

    private fun mapThrowableToFlightError(message: String): FlightError {
        return try {
            if (message == FlightErrorConstant.FLIGHT_ERROR_ON_CHECKOUT_GENERAL ||
                    message == FlightErrorConstant.FLIGHT_ERROR_GET_CART_EXCEED_MAX_RETRY ||
                    message == FlightErrorConstant.FLIGHT_ERROR_VERIFY_EXCEED_MAX_RETRY) {
                val error = FlightError(message)
                error.head = getString(R.string.flight_booking_general_error_title)
                error.message = getString(R.string.flight_booking_general_error_subtitle)
                error
            } else {
                val gson = Gson()
                val itemType = object : TypeToken<List<FlightError>>() {}.type
                gson.fromJson<List<FlightError>>(message, itemType)[0]
            }
        } catch (e: Exception) {
            val flightError = FlightError()
            flightError.status = message
            flightError
        }
    }

    private fun sendAddToCartTracking() {
        flightAnalytics.eventAddToCart(bookingViewModel.getSearchParam().flightClass,
                bookingViewModel.getDepartureJourney(),
                bookingViewModel.getReturnJourney(),
                bookingViewModel.getFlightPriceModel().comboKey,
                userSession.userId)
    }

    private fun sendCheckOutTracking(pid: String) {
        flightAnalytics.eventCheckoutClick(
                bookingViewModel.getDepartureJourney(),
                bookingViewModel.getReturnJourney(),
                bookingViewModel.getSearchParam(),
                bookingViewModel.getFlightPriceModel().comboKey,
                userSession.userId)

        flightAnalytics.eventBranchCheckoutFlight(
                "${bookingViewModel.getDepartureJourney()?.departureAirportCity}-${bookingViewModel.getDepartureJourney()?.arrivalAirportCity}",
                "",
                bookingViewModel.getInvoiceId(), pid,
                bookingViewModel.getUserId(),
                totalCartPrice.toString()
        )
    }

    private fun renderErrorToast(resId: Int) {
        view?.let {
            Toaster.build(it, getString(resId), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(R.string.flight_booking_action_okay),
                    View.OnClickListener { /* do nothing */ }).show()
        }
    }

    private fun renderRepricePrice(cart: FlightVerify.FlightVerifyCart) {
        bookingViewModel.updateFlightPriceData(cart.priceDetail)
        bookingViewModel.updateFlightDetailPriceData(cart.newPrice)
    }

    private fun showRepriceTag(cart: FlightVerify.FlightVerifyCart) {
        val bottomSheet = BottomSheetUnify()
        val viewBottomSheetDialog = View.inflate(context, R.layout.layout_flight_booking_reprice_bottom_sheet, null)
        bottomSheet.setTitle(getString(R.string.flight_booking_reprice_bottom_sheet_title))
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

        if (cart.promoEligibility.message.isNotEmpty()) {
            if (cart.promoEligibility.success) {
                tickerBookingPromo.tickerType = Ticker.TYPE_ANNOUNCEMENT
                tickerBookingPromo.tickerTitle = ""
            } else {
                tickerBookingPromo.tickerType = Ticker.TYPE_WARNING
                tickerBookingPromo.tickerTitle = getString(R.string.flight_booking_reprice_ticker_warning_title)
            }
            tickerBookingPromo.setTextDescription(cart.promoEligibility.message)
            tickerBookingPromo.tickerShape = Ticker.SHAPE_LOOSE
        } else tickerBookingPromo.visibility = View.GONE

        continueToPayButton.setOnClickListener {
            showCheckBookingDetailPopUp()
            bottomSheet.dismiss()
        }

        findNewTicketButton.setOnClickListener {
            bottomSheet.dismiss()
            finishActivityToSearchPage()
        }

        fragmentManager?.let {
            bottomSheet.show(it, getString(R.string.flight_booking_reprice_bottom_sheet_title))
        }
    }

    private fun showCheckBookingDetailPopUp() {
        if (activity != null) {
            val dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.flight_booking_popup_checkout_confirmation_title))
            dialog.setDescription(getString(R.string.flight_booking_popup_checkout_confirmation_description))
            dialog.setPrimaryCTAText(getString(R.string.flight_booking_action_proceed_checkout))
            dialog.setSecondaryCTAText(getString(R.string.flight_booking_action_recheck_data))

            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                checkOutCart()
            }

            dialog.setSecondaryCTAClickListener { dialog.dismiss() }
            dialog.show()
        }
    }

    private fun addToCart() {
        if (bookingViewModel.getCartId().isNotEmpty()) showLoadingDialog()
        bookingViewModel.addToCart(getAtcQuery(), getGetCartQuery(), getRequestId())
    }

    private fun setUpTimer(timeStamp: Date) {
        orderDueTimeStampString = FlightDateUtil.dateToString(timeStamp, FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)
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
        showLoadingDialog()
        bookingViewModel.getCart(getGetCartQuery(), bookingViewModel.getCartId(), isRefreshCart = true)
    }

    private fun renderData(cart: FlightCartViewEntity) {
        if (!::flightRouteAdapter.isInitialized) flightRouteAdapter = FlightJourneyAdapter()
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rv_flight_booking_route_summary.layoutManager = layoutManager
        rv_flight_booking_route_summary.setHasFixedSize(true)
        rv_flight_booking_route_summary.adapter = flightRouteAdapter
        flightRouteAdapter.listener = object : FlightJourneyAdapter.ViewHolder.ActionListener {
            override fun onClickRouteDetail(id: String, position: Int) {
                val route = if (position == 0) bookingViewModel.getDepartureJourney() else bookingViewModel.getReturnJourney()
                route?.let { navigateToDetailTrip(it) }
            }
        }
        flightRouteAdapter.updateRoutes(cart.journeySummaries)

        renderInsuranceData(cart.insurances)
    }

    fun navigateToDetailTrip(departureTrip: FlightDetailModel) {
        val flightDetailBottomSheet = FlightDetailBottomSheet.getInstance()
        flightDetailBottomSheet.setDetailModel(departureTrip)
        flightDetailBottomSheet.setShowSubmitButton(false)
        flightDetailBottomSheet.setShowListener { flightDetailBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        flightDetailBottomSheet.show(requireFragmentManager(), FlightDetailBottomSheet.TAG_FLIGHT_DETAIL_BOTTOM_SHEET)
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
                    RouteManager.route(activity, ApplinkConstInternalGlobal.WEBVIEW_TITLE, tncTitle, tncUrl)
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

    private fun renderPassengerData(passengers: List<FlightBookingPassengerModel>) {
        if (!::flightPassengerAdapter.isInitialized) {
            flightPassengerAdapter = FlightBookingPassengerAdapter()
            val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            rv_passengers_info.layoutManager = layoutManager
            rv_passengers_info.adapter = flightPassengerAdapter
            flightPassengerAdapter.listener = object : FlightBookingPassengerAdapter.PassengerViewHolderListener {
                override fun onClickEditPassengerListener(passenger: FlightBookingPassengerModel) {
                    passengerAsTraveller = switch_traveller_as_passenger.isChecked
                    navigateToPassengerInfoDetail(passenger, getRequestId())
                }
            }
        }
        flightPassengerAdapter.updateList(passengers)

        if (passengers.isNotEmpty() && switch_traveller_as_passenger.isChecked) {
            val firstPassenger = passengers.first()
            val fullName = "${firstPassenger.passengerFirstName} ${firstPassenger.passengerLastName}"
            if (!fullName.equals(widget_traveller_info.getContactName(), true) &&
                    !firstPassenger.passengerFirstName.equals(widget_traveller_info.getContactName(), true)) {
                needToDoChangesOnFirstPassenger = false
                switch_traveller_as_passenger.isChecked = false
            }
        }
    }

    private fun navigateToPassengerInfoDetail(model: FlightBookingPassengerModel, requestId: String, autofillName: String = "") {
        val departureDate = if (bookingViewModel.getSearchParam().isOneWay) {
            bookingViewModel.getSearchParam().departureDate
        } else {
            bookingViewModel.getSearchParam().returnDate
        }

        startActivityForResult(
                FlightBookingPassengerActivity.getCallingIntent(
                        activity as Activity,
                        bookingViewModel.getDepartureId(),
                        bookingViewModel.getReturnId(),
                        model,
                        bookingViewModel.getLuggageViewModels(),
                        bookingViewModel.getMealViewModels(),
                        bookingViewModel.getMandatoryDOB(),
                        departureDate,
                        requestId,
                        bookingViewModel.flightIsDomestic(),
                        autofillName
                ),
                REQUEST_CODE_PASSENGER
        )
    }

    private fun getRequestId(): String = if (getReturnId().isNotEmpty()) generateIdEmpotency("${getDepartureId()}_${getReturnId()}") else generateIdEmpotency(getDepartureId())

    private fun generateIdEmpotency(requestId: String): String {
        var userId = Math.random().toString()
        userId += userSession.userId
        val timeMillis = System.currentTimeMillis().toString()
        val token = FlightRequestUtil.md5(timeMillis)
        return userId + String.format(getString(com.tokopedia.flight.R.string.flight_booking_id_empotency_format),
                requestId, if (token.isEmpty()) timeMillis else token)
    }

    private fun renderProfileData(profileInfo: ProfileInfo) {
        widget_traveller_info.setContactName(profileInfo.fullName)
        widget_traveller_info.setContactPhoneNum(62, profileInfo.phone)
        widget_traveller_info.setContactEmail(profileInfo.email)
        widget_traveller_info.setContactPhoneCountry("ID")
    }

    private fun renderProfileData(profileInfo: FlightContactData) {
        widget_traveller_info.setContactName(profileInfo.name)
        widget_traveller_info.setContactPhoneNum(profileInfo.countryCode, profileInfo.phone)
        widget_traveller_info.setContactEmail(profileInfo.email)
        widget_traveller_info.setContactPhoneCountry(profileInfo.country)
    }

    override fun initInjector() {
        getComponent(FlightBookingComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(com.tokopedia.flight.R.layout.fragment_flight_booking_v3, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isFirstTime = savedInstanceState == null
        setUpView()

        if (savedInstanceState == null) {
            launchLoadingPageJob.start()
            initialize()
        } else {
            hideShimmering()
            renderUiFromBundle(savedInstanceState)
        }
    }

    private fun renderUiFromBundle(args: Bundle) {
        val flightBookingParam = args.getParcelable(EXTRA_FLIGHT_BOOKING_PARAM)
                ?: FlightBookingModel()
        bookingViewModel.setFlightBookingParam(flightBookingParam)

        val cartId = args.getString(EXTRA_CART_ID, "")
        bookingViewModel.setCartId(cartId)

        orderDueTimeStampString = args.getString(EXTRA_ORDER_DUE, "")
        if (orderDueTimeStampString.isNotEmpty()) setUpTimer(FlightDateUtil.stringToDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, orderDueTimeStampString))

        val profileData = args.getParcelable(EXTRA_CONTACT_DATA) ?: FlightContactData()
        renderProfileData(profileData)

        val passengerModels = args.getParcelableArrayList(EXTRA_PASSENGER_MODELS)
                ?: listOf<FlightBookingPassengerModel>()
        bookingViewModel.setPassengerModels(passengerModels)

        val priceData = args.getParcelableArrayList(EXTRA_PRICE_DATA)
                ?: listOf<FlightCart.PriceDetail>()
        bookingViewModel.setPriceData(priceData)
        val otherPriceData = args.getParcelableArrayList(EXTRA_OTHER_PRICE_DATA)
                ?: listOf<FlightCart.PriceDetail>()
        bookingViewModel.setOtherPriceData(otherPriceData)
        val amenityPriceData = args.getParcelableArrayList(EXTRA_AMENITY_PRICE_DATA)
                ?: listOf<FlightCart.PriceDetail>()
        bookingViewModel.setAmenityPriceData(amenityPriceData)

        refreshCart()
    }

    private fun initialize() {
        if (userSession.isMsisdnVerified) {
            addToCart()
            bookingViewModel.getProfile(getProfileQuery())
        } else if (userSession.isLoggedIn) {
            navigateToOtpPage()
        }
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
        finishActivityToHomepage()
    }

    private fun verifyCart() {
        bookingViewModel.validateDataAndVerifyCart(
                getVerifyCartQuery(),
                totalPrice = totalCartPrice,
                contactName = widget_traveller_info.getContactName(),
                contactEmail = widget_traveller_info.getContactEmail(),
                contactPhone = widget_traveller_info.getContactPhoneNum(),
                contactCountry = widget_traveller_info.getContactPhoneCountry(),
                checkVoucherQuery = getCheckVoucherQuery(),
                addToCartQuery = getAtcQuery(),
                idempotencyKey = getRequestId(),
                getCartQuery = getGetCartQuery())
    }

    private fun checkOutCart() {
        showLoadingDialog()
        bookingViewModel.checkOutCart(getCheckoutQuery(), totalCartPrice)
    }

    private fun renderAutoApplyPromo(flightVoucher: FlightPromoViewEntity) {
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
                bookingViewModel.updatePromoData(PromoData(state = TickerCheckoutView.State.EMPTY, title = "", description = "", promoCode = ""))
                bookingViewModel.onCancelAppliedVoucher(getCancelVoucherQuery())
            }

            override fun onClickUsePromo() {
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_FLIGHT)
                intent.putExtra(COUPON_EXTRA_COUPON_ACTIVE, flightVoucher.isCouponActive)
                intent.putExtra(COUPON_EXTRA_CART_ID, bookingViewModel.getCartId())
                startActivityForResult(intent, COUPON_EXTRA_LIST_ACTIVITY_RESULT)
            }

            override fun onDisablePromoDiscount() {
                bookingViewModel.updatePromoData(PromoData(state = TickerCheckoutView.State.EMPTY, title = "", description = "", promoCode = ""))
                bookingViewModel.onCancelAppliedVoucher(getCancelVoucherQuery())
            }

            override fun onClickDetailPromo() {
                val intent: Intent
                val promoCode = flightVoucher.promoData.promoCode
                if (!promoCode.isEmpty()) {
                    val requestCode: Int
                    if (flightVoucher.promoData.typePromo == PromoData.TYPE_VOUCHER) {
                        intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_FLIGHT)
                        intent.putExtra(COUPON_EXTRA_PROMO_CODE, promoCode)
                        intent.putExtra(COUPON_EXTRA_COUPON_ACTIVE, flightVoucher.isCouponActive)
                        requestCode = REQUEST_CODE_PROMO_LIST
                    } else {
                        intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_FLIGHT)
                        intent.putExtra(COUPON_EXTRA_IS_USE, true)
                        intent.putExtra(COUPON_EXTRA_COUPON_CODE, promoCode)
                        requestCode = REQUEST_CODE_PROMO_DETAIL
                    }
                    intent.putExtra(COUPON_EXTRA_CART_ID, bookingViewModel.getCartId())
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
        try {
            if (bookingViewModel.getCartId().isEmpty()) {
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
        } catch (e: Throwable) {
        }
    }

    private fun hideShimmering() {
        if (layout_loading.isVisible) layout_loading.hide()
        if (layout_shimmering.isVisible) layout_shimmering.hide()
        hideLoadingDialog()
    }

    private fun hideLoadingDialog() {
        if (::loadingDialog.isInitialized && loadingDialog.isShowing) loadingDialog.dismiss()
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

    private fun showErrorFullPage(e: FlightError) {
        val errorCode = FlightBookingErrorCodeMapper.mapToFlightErrorCode(e.id.toInt())
        layout_full_page_error.visibility = View.VISIBLE
        layout_full_page_error.iv_error_page.setImageResource(FlightBookingErrorCodeMapper.getErrorIcon(errorCode))
        layout_full_page_error.tv_error_title.text = e.head
        layout_full_page_error.tv_error_subtitle.text = e.message
        layout_full_page_error.button_error_action.text = getString(R.string.flight_booking_action_refind_ticket)
        layout_full_page_error.button_error_action.setOnClickListener { finishActivityToSearchPage() }
    }

    @SuppressLint("DialogUnifyUsage")
    private fun showErrorDialog(e: FlightError, action: () -> Unit) {
        if (activity != null) {
            if (e.id != null) {
                val errorCode = FlightBookingErrorCodeMapper.mapToFlightErrorCode(e.id.toInt())
                if (errorCode == FlightErrorConstant.FLIGHT_DUPLICATE_USER_NAME) renderErrorToast(R.string.flight_duplicate_user_error_toaster_text)
                else if (errorCode == FlightErrorConstant.FLIGHT_SOLD_OUT) {
                    showErrorFullPage(e)
                } else {
                    lateinit var dialog: DialogUnify
                    when (errorCode) {
                        FlightErrorConstant.INVALID_JSON -> {
                            dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
                            dialog.setPrimaryCTAText(getString(R.string.flight_booking_action_refind_ticket))
                            dialog.setPrimaryCTAClickListener {
                                dialog.dismiss()
                                showLoadingDialog()
                                finishActivityToSearchPage()
                            }
                        }
                        FlightErrorConstant.FAILED_ADD_FACILITY -> {
                            dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ICON)
                            dialog.setPrimaryCTAText(getString(R.string.flight_booking_action_proceed_checkout))
                            dialog.setPrimaryCTAClickListener {
                                dialog.dismiss()
                                proceedCheckoutWithoutLuggage()
                            }
                            dialog.setSecondaryCTAText(getString(R.string.flight_booking_action_cancel))
                            dialog.setSecondaryCTAClickListener { dialog.dismiss() }
                        }
                        FlightErrorConstant.ERROR_PROMO_CODE -> {
                            dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
                            dialog.setPrimaryCTAText(getString(R.string.flight_booking_action_check_promo_code))
                            dialog.setPrimaryCTAClickListener {
                                dialog.dismiss()
                                navigateToPromoPage()
                            }
                        }
                        FlightErrorConstant.FLIGHT_DUPLICATE_BOOKING -> {
                            dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ICON)
                            dialog.setPrimaryCTAText(getString(R.string.flight_booking_action_check_again))
                            dialog.setPrimaryCTAClickListener {
                                dialog.dismiss()
                                navigateToFlightOrderList()
                            }
                            dialog.setSecondaryCTAText(getString(R.string.flight_booking_action_book_other_ticket))
                            dialog.setSecondaryCTAClickListener {
                                dialog.dismiss()
                                finishActivityToHomepage()
                            }
                        }
                        FlightErrorConstant.FLIGHT_STILL_IN_PROCESS -> {
                            dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ICON)
                            dialog.setPrimaryCTAText(getString(R.string.flight_booking_action_book_other_ticket))
                            dialog.setPrimaryCTAClickListener {
                                dialog.dismiss()
                                finishActivityToHomepage()
                            }
                            dialog.setSecondaryCTAText(getString(R.string.flight_booking_action_okay))
                            dialog.setSecondaryCTAClickListener { dialog.dismiss() }
                        }
                        FlightErrorConstant.FLIGHT_ERROR_GET_CART_EXCEED_MAX_RETRY -> {
                            dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
                            dialog.setPrimaryCTAText(getString(R.string.flight_booking_action_retry))
                            dialog.setPrimaryCTAClickListener {
                                dialog.dismiss()
                                refreshCart()
                            }
                        }
                        FlightErrorConstant.FLIGHT_ERROR_VERIFY_EXCEED_MAX_RETRY -> {
                            dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
                            dialog.setPrimaryCTAText(getString(R.string.flight_booking_action_retry))
                            dialog.setPrimaryCTAClickListener {
                                dialog.dismiss()
                                verifyCart()
                            }
                        }
                        FlightErrorConstant.FLIGHT_ERROR_ON_CHECKOUT_GENERAL -> {
                            dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
                            dialog.setPrimaryCTAText(getString(R.string.flight_booking_action_retry))
                            dialog.setPrimaryCTAClickListener {
                                dialog.dismiss()
                                checkOutCart()
                            }
                        }
                        FlightErrorConstant.FLIGHT_INVALID_USER -> {
                            dialog = DialogUnify(activity as FlightBookingActivity, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
                            dialog.setPrimaryCTAText(getString(R.string.flight_booking_action_change_name))
                            dialog.setPrimaryCTAClickListener {
                                dialog.dismiss()
                            }
                        }
                    }
                    dialog.setCancelable(false)
                    dialog.setOverlayClose(false)
                    if (e.head.isNotEmpty()) dialog.setTitle(e.head) else dialog.setTitle(getString(R.string.flight_booking_general_error_title))
                    if (e.message.isNotEmpty()) dialog.setDescription(e.message) else dialog.setTitle(getString(R.string.flight_booking_general_error_subtitle))
                    dialog.setImageDrawable(FlightBookingErrorCodeMapper.getErrorIcon(errorCode))
                    dialog.show()
                }
            } else {
                NetworkErrorHelper.showEmptyState(activity, view) {
                    NetworkErrorHelper.hideEmptyState(view)
                    showLoadingDialog()
                    action()
                }
            }
        }
    }

    private fun proceedCheckoutWithoutLuggage() {
        showLoadingDialog()
        bookingViewModel.proceedCheckoutWithoutLuggage(
                getCheckVoucherQuery(),
                getVerifyCartQuery(),
                flightPriceAdapter.getTotalPriceWithoutAmenities(),
                widget_traveller_info.getContactName(),
                widget_traveller_info.getContactEmail(),
                widget_traveller_info.getContactPhoneNum(),
                widget_traveller_info.getContactPhoneCountry())
    }

    private fun navigateToPromoPage() {
        showLoadingDialog()
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_FLIGHT)
        intent.putExtra(COUPON_EXTRA_COUPON_ACTIVE, 0)
        intent.putExtra(COUPON_EXTRA_CART_ID, bookingViewModel.getCartId())
        startActivityForResult(intent, COUPON_EXTRA_LIST_ACTIVITY_RESULT)
    }

    private fun navigateToFlightOrderList() {
        showLoadingDialog()
        RouteManager.route(context, ApplinkConst.FLIGHT_ORDER)
        finishActivityToHomepage()
    }

    private fun navigateToOtpPage() {
        startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE), REQUEST_CODE_OTP)
    }

    private fun showLoadingDialog() {
        context?.let {
            val list = randomLoadingSubtitle()
            if (!::loadingDialog.isInitialized || !loadingDialog.isShowing) {
                loadingDialog = DialogUnify(it, 0, 0)
                loadingDialog.setUnlockVersion()
                loadingDialog.setCancelable(false)
                loadingDialog.setOverlayClose(false)

                val loadingView = View.inflate(context, R.layout.layout_flight_booking_loading, null)
                loadingDialog.setChild(loadingView)
                loadingText = loadingView.findViewById(R.id.tv_loading_subtitle)
                loadingText.text = list[0]

                loadingDialog.show()
            } else {
                loadingText.text = list[0]
            }
        }
    }

    private fun finishActivityToHomepage() {
        showLoadingDialog()
        activity?.let {
            FlightFlowUtil.actionSetResultAndClose(it, it.intent, FlightFlowConstant.EXPIRED_JOURNEY)
        }
    }

    private fun finishActivityToSearchPage() {
        showLoadingDialog()
        activity?.let {
            FlightFlowUtil.actionSetResultAndClose(it, it.intent, FlightFlowConstant.PRICE_CHANGE)
        }
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
                    widget_traveller_info.setContactPhoneCountry(contactData.phoneCountry)
                }
            }

            REQUEST_CODE_PASSENGER -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            val passengerViewModel = it.getParcelableExtra<FlightBookingPassengerModel>(FlightBookingPassengerActivity.EXTRA_PASSENGER)
                            bookingViewModel.onPassengerResultReceived(passengerViewModel)
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        needToDoChangesOnFirstPassenger = false
                        switch_traveller_as_passenger.isChecked = passengerAsTraveller
                        needToDoChangesOnFirstPassenger = true
                    }
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

            REQUEST_CODE_OTP -> if (resultCode == RESULT_OK) {
                initialize()
            } else {
                activity?.finish()
            }
        }

        if (needRefreshCart) {
            needRefreshCart = false
            refreshCart()
        }
    }

    private fun getDepartureId(): String = bookingViewModel.getDepartureId()
    private fun getReturnId(): String = bookingViewModel.getReturnId()
    private fun getAtcQuery(): String = FlightBookingQuery.QUERY_ADD_TO_CART
    private fun getGetCartQuery(): String = FlightBookingQuery.QUERY_GET_CART
    private fun getCheckVoucherQuery(): String = FlightBookingQuery.QUERY_CHECK_VOUCHER
    private fun getVerifyCartQuery(): String = FlightBookingQuery.QUERY_VERIFY_CART
    private fun getCheckoutQuery(): String = FlightBookingQuery.QUERY_CHECKOUT_CART
    private fun getProfileQuery(): String = GraphqlHelper.loadRawString(resources, com.tokopedia.sessioncommon.R.raw.query_profile)
    private fun getCancelVoucherQuery(): String = PromoCheckoutCommonQueryConst.QUERY_FLIGHT_CANCEL_VOUCHER

    private fun renderTickerView(travelTickerModel: TravelTickerModel) {
        TravelTickerUtils.buildUnifyTravelTicker(travelTickerModel, flightBookingTicker)
        if (travelTickerModel.url.isNotEmpty()) {
            flightBookingTicker.setOnClickListener {
                RouteManager.route(requireContext(), travelTickerModel.url)
            }
        }

        showTickerView()
    }

    private fun showTickerView() {
        flightBookingTicker.visibility = View.VISIBLE
    }

    private fun hideTickerView() {
        flightBookingTicker.visibility = View.GONE
    }

    companion object {

        const val COUPON_EXTRA_COUPON_ACTIVE = "EXTRA_COUPON_ACTIVE"
        const val COUPON_EXTRA_CART_ID = "EXTRA_CART_ID"
        const val COUPON_EXTRA_PROMO_CODE = "EXTRA_PROMO_CODE"
        const val COUPON_EXTRA_IS_USE = "EXTRA_IS_USE"
        const val COUPON_EXTRA_COUPON_CODE = "EXTRA_KUPON_CODE"
        const val COUPON_EXTRA_PROMO_DATA = "EXTRA_PROMO_DATA"
        const val EXTRA_SEARCH_PASS_DATA = "EXTRA_SEARCH_PASS_DATA"
        const val EXTRA_FLIGHT_DEPARTURE_ID = "EXTRA_FLIGHT_DEPARTURE_ID"
        const val EXTRA_FLIGHT_ARRIVAL_ID = "EXTRA_FLIGHT_ARRIVAL_ID"
        const val EXTRA_FLIGHT_DEPARTURE_TERM = "EXTRA_FLIGHT_DEPARTURE_TERM"
        const val EXTRA_FLIGHT_ARRIVAL_TERM = "EXTRA_FLIGHT_ARRIVAL_TERM"
        const val EXTRA_PRICE = "EXTRA_PRICE"
        const val EXTRA_PARAMETER_TOP_PAY_DATA = "EXTRA_PARAMETER_TOP_PAY_DATA"

        const val EXTRA_ORDER_DUE = "EXTRA_ORDER_DUE"
        const val EXTRA_CONTACT_DATA = "EXTRA_CONTACT_DATA"
        const val EXTRA_PASSENGER_MODELS = "EXTRA_PASSENGER_MODELS"
        const val EXTRA_PRICE_DATA = "EXTRA_PRICE_DATA"
        const val EXTRA_OTHER_PRICE_DATA = "EXTRA_OTHER_PRICE_DATA"
        const val EXTRA_AMENITY_PRICE_DATA = "EXTRA_AMENITY_PRICE_DATA"
        const val EXTRA_CART_ID = "EXTRA_BOOKING_CART_ID"
        const val EXTRA_FLIGHT_BOOKING_PARAM = "EXTRA_FLIGHT_BOOKING_PARAM"

        const val REQUEST_CODE_PASSENGER = 1
        const val REQUEST_CODE_CONTACT_FORM = 12
        const val COUPON_EXTRA_LIST_ACTIVITY_RESULT = 3121
        const val COUPON_EXTRA_DETAIL_ACTIVITY_RESULT = 3122
        const val REQUEST_CODE_OTP = 5

        fun newInstance(): FlightBookingFragment {
            return FlightBookingFragment()
        }

        fun newInstance(searchPassDataModel: FlightSearchPassDataModel,
                        departureId: String, returnId: String,
                        departureTerm: String, returnTerm: String,
                        priceModel: FlightPriceModel): FlightBookingFragment {
            val fragment = FlightBookingFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_SEARCH_PASS_DATA, searchPassDataModel)
            bundle.putString(EXTRA_FLIGHT_DEPARTURE_ID, departureId)
            bundle.putString(EXTRA_FLIGHT_ARRIVAL_ID, returnId)
            bundle.putString(EXTRA_FLIGHT_DEPARTURE_TERM, departureTerm)
            bundle.putString(EXTRA_FLIGHT_ARRIVAL_TERM, returnTerm)
            bundle.putParcelable(EXTRA_PRICE, priceModel)
            fragment.arguments = bundle
            return fragment
        }
    }

}