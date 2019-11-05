package com.tokopedia.flight.bookingV3.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.common.travel.presentation.activity.TravelContactDataActivity
import com.tokopedia.common.travel.presentation.fragment.TravelContactDataFragment
import com.tokopedia.common.travel.presentation.model.TravelContactData
import com.tokopedia.common.travel.widget.TravellerInfoWidget
import com.tokopedia.flight.booking.di.FlightBookingComponent
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingParamViewModel
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel
import com.tokopedia.flight.bookingV3.data.FlightCart
import com.tokopedia.flight.bookingV3.data.FlightCartViewEntity
import com.tokopedia.flight.bookingV3.data.FlightPromoViewEntity
import com.tokopedia.flight.bookingV3.presentation.adapter.FlightBookingPassengerAdapter
import com.tokopedia.flight.bookingV3.presentation.adapter.FlightBookingPriceAdapter
import com.tokopedia.flight.bookingV3.presentation.adapter.FlightInsuranceAdapter
import com.tokopedia.flight.bookingV3.presentation.adapter.FlightJourneyAdapter
import com.tokopedia.flight.bookingV3.viewmodel.FlightBookingViewModel
import com.tokopedia.flight.common.util.FlightRequestUtil
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.data.REQUST_CODE_PROMO_LIST
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_flight_booking_v3.*
import kotlinx.android.synthetic.main.layout_flight_booking_v3_loading.*
import kotlinx.coroutines.*
import javax.inject.Inject


/**
 * @author by jessica on 2019-10-24
 */

class FlightBookingFragment : BaseDaggerFragment() {

    private val uiScope = CoroutineScope(Dispatchers.Main)
    var isCouponChanged = false
    val cartId = ""

    lateinit var flightRouteAdapter: FlightJourneyAdapter
    lateinit var flightInsuranceAdapter: FlightInsuranceAdapter
    lateinit var flightPassengerAdapter: FlightBookingPassengerAdapter
    lateinit var flightPriceAdapter: FlightBookingPriceAdapter

    var departureId: String = ""
    var returnId: String = ""
    private lateinit var paramViewModel: FlightBookingParamViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bookingViewModel: FlightBookingViewModel

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val args: Bundle = savedInstanceState ?: arguments
//        departureId = args.getString(EXTRA_FLIGHT_DEPARTURE_ID)
//        returnId = args.getString(EXTRA_FLIGHT_ARRIVAL_ID, "")
//        paramViewModel = FlightBookingParamViewModel()
//        paramViewModel.searchParam = args.getParcelable(EXTRA_SEARCH_PASS_DATA)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            bookingViewModel = viewModelProvider.get(FlightBookingViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bookingViewModel.flightCartResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    launchLoadingPageJob.cancel()
                    renderData(it.data)
                }
                is Fail -> {

                }
            }
        })

        bookingViewModel.flightPromoResult.observe(this, Observer {
            renderAutoApplyPromo(it)
        })

        bookingViewModel.profileResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    renderProfileData(it.data)
                }
                is Fail -> {

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
    }

    private fun renderData(cart: FlightCartViewEntity) {
        hideShimmering()

        if (!::flightRouteAdapter.isInitialized) flightRouteAdapter = FlightJourneyAdapter()
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rv_flight_booking_route_summary.layoutManager = layoutManager
        rv_flight_booking_route_summary.setHasFixedSize(true)
        rv_flight_booking_route_summary.adapter = flightRouteAdapter
        flightRouteAdapter.updateRoutes(cart.journeySummaries)

        renderInsuranceData(cart.insurances)
    }

    private fun renderInsuranceData(insurances: List<FlightCart.Insurance>) {
        if (!::flightInsuranceAdapter.isInitialized) {
            flightInsuranceAdapter = FlightInsuranceAdapter()
            val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            rv_insurance_list.layoutManager = layoutManager
            rv_insurance_list.setHasFixedSize(true)
            rv_insurance_list.adapter = flightInsuranceAdapter
            flightInsuranceAdapter.listener = object : FlightInsuranceAdapter.ViewHolder.ActionListener {
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
                }
            }
        }
        flightPriceAdapter.updateRoutePriceList(priceList)
    }

    private fun renderOtherPriceData(priceList: List<FlightCart.PriceDetail>) {
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
                    val requestId = if (returnId.isNotEmpty()) generateIdEmpotency("${departureId}_${returnId}") else generateIdEmpotency(departureId)
                    navigateToPassengerInfoDetail(passenger, true, "2020-02-02", requestId)
                }
            }
        }
        flightPassengerAdapter.updateList(passengers)
    }

    private fun navigateToPassengerInfoDetail(viewModel: FlightBookingPassengerViewModel, isMandatoryDoB: Boolean, departureDate: String, requestId: String) {
        startActivityForResult(
                FlightBookingPassengerActivity.getCallingIntent(
                        activity as Activity,
                        departureId,
                        returnId,
                        viewModel,
                        bookingViewModel.getLuggageViewModels(),
                        bookingViewModel.getMealViewModels(),
                        isMandatoryDoB,
                        departureDate,
                        requestId,
                        true //must be is domestic
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
        widget_traveller_info.setContactPhoneNum(profileInfo.phone)
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
        bookingViewModel.getCart(GraphqlHelper.loadRawString(resources, com.tokopedia.flight.R.raw.dummy_get_cart), "")
        bookingViewModel.getProfile(GraphqlHelper.loadRawString(resources, com.tokopedia.sessioncommon.R.raw.query_profile))
        hidePriceDetail()

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

        tv_see_detail_price.setOnClickListener { if (rv_flight_price_detail.isVisible) hidePriceDetail() else showPriceDetail() }
    }

    fun renderAutoApplyPromo(flightVoucher: FlightPromoViewEntity) {
        if (flightVoucher.isCouponEnable) showVoucherContainer() else hideVoucherContainer()
        renderPromoTicker(flightVoucher)
    }

    private fun renderPromoTicker(flightVoucher: FlightPromoViewEntity) {
        flight_promo_ticker_view.state = flightVoucher.promoData.state
        flight_promo_ticker_view.title = flightVoucher.promoData.title
        flight_promo_ticker_view.desc = flightVoucher.promoData.description
        flight_promo_ticker_view.actionListener = object : TickerCheckoutView.ActionListener {
            override fun onClickUsePromo() {
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_FLIGHT)
                intent.putExtra("EXTRA_COUPON_ACTIVE", flightVoucher.isCouponActive)
                intent.putExtra("EXTRA_CART_ID", cartId)
                startActivityForResult(intent, REQUST_CODE_PROMO_LIST)
            }

            override fun onDisablePromoDiscount() {
                isCouponChanged = true
                bookingViewModel.onCancelAppliedVoucher(GraphqlHelper.loadRawString(resources,
                        R.raw.promo_checkout_flight_cancel_voucher))
//        updateFinalTotal(null, getCurrentBookingReviewModel())
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
                    intent.putExtra("EXTRA_CART_ID", cartId)
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

    var launchLoadingPageJob = uiScope.launch {
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

    private fun hideShimmering() {
        if (layout_loading.isVisible) layout_loading.hide()
        if (layout_shimmering.isVisible) layout_shimmering.hide()
    }

    private fun hidePriceDetail() {
        thin_seperator_1.hide()
        rv_flight_price_detail.hide()
        tv_see_detail_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, com.tokopedia.flight.R.drawable.ic_arrow_down_detail_flight, 0)
    }

    private fun showPriceDetail() {
        thin_seperator_1.show()
        rv_flight_price_detail.show()
//        tv_see_detail_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, com.tokopedia.flight.R.drawable.ic_arrow_up_flight_expandable, 0)
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

            REQUST_CODE_PROMO_LIST, REQUEST_CODE_PROMO_DETAIL -> {
                data?.let {
                    var promoData = PromoData()
                    if (it.hasExtra(EXTRA_PROMO_DATA)) promoData = data.getParcelableExtra(EXTRA_PROMO_DATA)
                    when (promoData.state) {
                        TickerCheckoutView.State.EMPTY -> {
                            promoData.promoCode = ""
                            flight_promo_ticker_view.resetView()
                        }
                        TickerCheckoutView.State.FAILED -> {
                            promoData.promoCode = ""
                            bookingViewModel.updatePromoData(promoData)
                        }
                        TickerCheckoutView.State.ACTIVE -> {
                            bookingViewModel.updatePromoData(promoData)
                        }
                    }
                }
            }
        }
    }

    companion object {

        const val REQUEST_CODE_PASSENGER = 1
        const val REQUEST_CODE_CONTACT_FORM = 12

        private val EXTRA_SEARCH_PASS_DATA = "EXTRA_SEARCH_PASS_DATA"
        private val EXTRA_FLIGHT_DEPARTURE_ID = "EXTRA_FLIGHT_DEPARTURE_ID"
        private val EXTRA_FLIGHT_ARRIVAL_ID = "EXTRA_FLIGHT_ARRIVAL_ID"

        fun newInstance(): FlightBookingFragment {
            return FlightBookingFragment()
        }
    }

}