package com.tokopedia.flight.bookingV2.presentation.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.flight.FlightModuleRouter
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.di.FlightBookingComponent
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo
import com.tokopedia.flight.booking.view.activity.FlightBookingPassengerActivity
import com.tokopedia.flight.booking.view.activity.FlightBookingPhoneCodeActivity
import com.tokopedia.flight.booking.view.activity.FlightInsuranceWebviewActivity
import com.tokopedia.flight.booking.view.adapter.*
import com.tokopedia.flight.booking.view.fragment.FLightBookingPhoneCodeFragment
import com.tokopedia.flight.booking.view.fragment.FlightBookingNewPriceDialogFragment
import com.tokopedia.flight.booking.view.viewmodel.*
import com.tokopedia.flight.booking.widget.FlightInsuranceView
import com.tokopedia.flight.bookingV2.presentation.contract.FlightBookingContract
import com.tokopedia.flight.bookingV2.presentation.presenter.FlightBookingPresenter
import com.tokopedia.flight.common.constant.FlightFlowConstant
import com.tokopedia.flight.common.constant.FlightFlowExtraConstant
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.util.FlightErrorUtil
import com.tokopedia.flight.common.util.FlightFlowUtil
import com.tokopedia.flight.common.util.FlightRequestUtil
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel
import com.tokopedia.flight.review.view.activity.FlightBookingReviewActivity
import com.tokopedia.flight.review.view.fragment.FlightBookingReviewFragment
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_flight_booking.*
import kotlinx.android.synthetic.main.partial_flight_booking_contact_form.*
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 04/03/19
 */
class FlightBookingFragment : BaseDaggerFragment(),
        FlightBookingContract.View, FlightBookingPassengerActionListener {

    lateinit var flightBookingPresenter: FlightBookingPresenter
        @Inject set

    private lateinit var priceListAdapter: FlightSimpleAdapter

    private lateinit var progressDialog: ProgressDialog

    private lateinit var departureId: String
    private lateinit var returnId: String
    private lateinit var flightPriceViewModel: FlightPriceViewModel
    private lateinit var paramViewModel: FlightBookingParamViewModel
    private lateinit var flightBookingCartData: FlightBookingCartData
    private lateinit var passengerAdapter: FlightBookingPassengerAdapter
    private lateinit var userBirthdate: String
    private lateinit var bookingCartId: String
    private var userGender: Int = 0
    private lateinit var expiredDate: Date
    private var flightInsuranceAdapter: FlightInsuranceAdapter? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(FlightBookingComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: Bundle = savedInstanceState ?: arguments!!

        departureId = args.getString(EXTRA_FLIGHT_DEPARTURE_ID)
        returnId = args.getString(EXTRA_FLIGHT_ARRIVAL_ID, "")
        paramViewModel = FlightBookingParamViewModel()
        paramViewModel.searchParam = args.getParcelable(EXTRA_SEARCH_PASS_DATA)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.flight_booking_loading_title))
        progressDialog.setCancelable(false)
        flightPriceViewModel = args.getParcelable(EXTRA_PRICE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_booking, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        flightBookingPresenter.attachView(this)
        if (savedInstanceState == null) {
            flightBookingCartData = FlightBookingCartData()
            flightBookingPresenter.initialize()
        } else {
            flightBookingCartData = savedInstanceState.getParcelable(KEY_CART_DATA)
            paramViewModel = savedInstanceState.getParcelable(KEY_PARAM_VIEW_MODEL_DATA)
            expiredDate = savedInstanceState.getSerializable(KEY_PARAM_EXPIRED_DATE) as Date
            flightPriceViewModel = savedInstanceState.getParcelable(KEY_PARAM_EXTRA_PRICE)
            hideFullPageLoading()
            flightBookingPresenter.renderUi(flightBookingCartData, true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        paramViewModel.contactName = getContactName()
        paramViewModel.contactPhone = getContactPhoneNumber()
        paramViewModel.contactEmail = getContactEmail()

        outState.putParcelable(EXTRA_SEARCH_PASS_DATA, paramViewModel.searchParam)
        outState.putString(EXTRA_FLIGHT_DEPARTURE_ID, departureId)
        outState.putString(EXTRA_FLIGHT_ARRIVAL_ID, returnId)
        outState.putParcelable(KEY_CART_DATA, flightBookingCartData)
        outState.putParcelable(KEY_PARAM_VIEW_MODEL_DATA, paramViewModel)
        outState.putSerializable(KEY_PARAM_EXPIRED_DATE, expiredDate)
        outState.putParcelable(KEY_PARAM_EXTRA_PRICE, flightPriceViewModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_PASSENGER -> if (resultCode == Activity.RESULT_OK) {
                val passengerViewModel = data!!.getParcelableExtra<FlightBookingPassengerViewModel>(FlightBookingPassengerActivity.EXTRA_PASSENGER)
                flightBookingPresenter.onPassengerResultReceived(passengerViewModel)
            }
            REQUEST_CODEP_PHONE_CODE -> if (resultCode == Activity.RESULT_OK) {
                val phoneCodeViewModel = data!!.getParcelableExtra<FlightBookingPhoneCodeViewModel>(FLightBookingPhoneCodeFragment.EXTRA_SELECTED_PHONE_CODE)
                flightBookingPresenter.onPhoneCodeResultReceived(phoneCodeViewModel)
            }
            REQUEST_CODE_NEW_PRICE_DIALOG -> if (resultCode != Activity.RESULT_OK) {
                FlightFlowUtil.actionSetResultAndClose(activity!!,
                        activity!!.intent,
                        FlightFlowConstant.PRICE_CHANGE
                )
            }
            REQUEST_CODE_REVIEW -> {
                var isCountdownRestarted = false
                if (data != null) {
                    if (data.getIntExtra(FlightFlowExtraConstant.EXTRA_FLOW_DATA, -1) != -1) {
                        FlightFlowUtil.actionSetResultAndClose(activity!!,
                                activity!!.intent,
                                data.getIntExtra(FlightFlowExtraConstant.EXTRA_FLOW_DATA, 0)
                        )
                    } else {
                        if (data.getBooleanExtra(FlightBookingReviewFragment.EXTRA_NEED_TO_REFRESH, false)) {
                            isCountdownRestarted = true
                            flightBookingPresenter.onGetCart(false, flightBookingCartData)
                        }

                        if (data.getParcelableExtra<Parcelable>(FlightBookingReviewFragment.EXTRA_COUPON_CHANGED) != null) {
                            flightBookingCartData.voucherViewModel = data.getParcelableExtra(
                                    FlightBookingReviewFragment.EXTRA_COUPON_CHANGED)
                        }
                    }

                }
                if (!isCountdownRestarted) countdown_finish_transaction.start()
            }
            REQUEST_CODE_OTP -> if (resultCode == Activity.RESULT_OK) {
                flightBookingPresenter.onReceiveOtpSuccessResult()
            } else {
                flightBookingPresenter.onReceiveOtpCancelResult()
            }
        }
    }

    override fun onDestroyView() {
        flightBookingPresenter.onDestroyView()
        super.onDestroyView()
    }

    override fun onChangePassengerData(viewModel: FlightBookingPassengerViewModel?) {
        val departureDate = if (!paramViewModel.searchParam.isOneWay) {
            paramViewModel.searchParam.returnDate
        } else {
            paramViewModel.searchParam.departureDate
        }

        if (viewModel != null) {
            flightBookingPresenter.onChangePassengerButtonClicked(viewModel, departureDate)
        }
    }

    override fun getContactName(): String = et_contact_name.text.toString().trim()

    override fun setContactName(fullname: String) {
        et_contact_name.setText(fullname)
    }

    override fun showContactNameEmptyError(resId: Int) {
        showMessageErrorInSnackBar(resId)
    }

    override fun showContactNameInvalidError(resId: Int) {
        showMessageErrorInSnackBar(resId)
    }

    override fun getContactEmail(): String = et_contact_email.text.toString().trim()

    override fun showContactEmailEmptyError(resId: Int) {
        showMessageErrorInSnackBar(resId)
    }

    override fun showContactEmailInvalidError(resId: Int) {
        showMessageErrorInSnackBar(resId)
    }

    override fun getContactPhoneNumber(): String = et_phone_number.text.toString().trim()

    override fun showContactPhoneNumberEmptyError(resId: Int) {
        showMessageErrorInSnackBar(resId)
    }

    override fun showContactPhoneNumberInvalidError(resId: Int) {
        showMessageErrorInSnackBar(resId)
    }

    override fun setContactEmail(email: String) {
        et_contact_email.setText(email)
    }

    override fun showContactEmailInvalidSymbolError(resId: Int) {
        showMessageErrorInSnackBar(resId)
    }

    override fun setContactPhoneNumber(phone: String) {
        et_phone_number.setText(phone)
    }

    override fun setContactBirthdate(birthdate: String) {
        userBirthdate = birthdate
    }

    override fun getContactBirthdate(): String = userBirthdate

    override fun setContactGender(gender: Int) {
        userGender = gender
    }

    override fun getContactGender(): Int = userGender

    override fun showPassengerInfoNotFullfilled(resId: Int) {
        showMessageErrorInSnackBar(resId)
    }

    override fun navigateToOtpPage() {
        if (activity!!.application is FlightModuleRouter) {
            val intent = (activity!!.application as FlightModuleRouter).getPhoneVerifIntent(activity)
            startActivityForResult(intent, REQUEST_CODE_OTP)
        }
    }

    override fun closePage() {
        activity!!.finish()
    }

    override fun getExpiredTransactionDate(): Date = expiredDate

    override fun showInsuranceLayout() {
        insurance_layout.visibility = View.VISIBLE
    }

    override fun hideInsuranceLayout() {
        insurance_layout.visibility = View.GONE
    }

    override fun renderInsurance(insurances: List<FlightInsuranceViewModel>) {
        if (flightInsuranceAdapter == null) {
            flightInsuranceAdapter = FlightInsuranceAdapter(insurances)
            flightInsuranceAdapter!!.setActionListener(object : FlightInsuranceView.ActionListener {
                override fun onInsuranceChecked(insurance: FlightInsuranceViewModel, checked: Boolean) {
                    flightBookingPresenter.onInsuranceChanges(insurance, checked)
                }

                override fun onMoreInfoClicked(tncUrl: String, title: String) {
                    startActivity(FlightInsuranceWebviewActivity.getCallingIntent(activity, tncUrl, title))
                    flightBookingPresenter.onMoreInsuranceInfoClicked()
                }

                override fun onBenefitExpanded() {
                    flightBookingPresenter.onInsuranceBenefitExpanded()
                }
            })
            rv_insurance.layoutManager = LinearLayoutManager(context)
            rv_insurance.adapter = flightInsuranceAdapter
        }
    }

    override fun getPriceViewModel(): FlightPriceViewModel = flightPriceViewModel

    override fun navigateToPassengerInfoDetail(viewModel: FlightBookingPassengerViewModel, isMandatoryDoB: Boolean, departureDate: String, requestId: String) {
        startActivityForResult(
                FlightBookingPassengerActivity.getCallingIntent(
                        activity,
                        getDepartureTripId(),
                        getReturnTripId(),
                        viewModel,
                        flightBookingCartData.luggageViewModels,
                        flightBookingCartData.mealViewModels,
                        isMandatoryDoB,
                        departureDate,
                        requestId,
                        flightBookingCartData.isDomestic
                ),
                REQUEST_CODE_PASSENGER
        )
    }

    override fun getProfileObservable(): Observable<ProfileInfo> {
        return if (activity!!.application is FlightModuleRouter && (activity!!
                        .application as FlightModuleRouter).profile != null) {
            (activity!!.application as FlightModuleRouter)
                    .profile
        } else Observable.empty()
    }

    override fun getCurrentBookingParamViewModel(): FlightBookingParamViewModel = paramViewModel

    override fun showAndRenderDepartureTripCardDetail(searchParam: FlightSearchPassDataViewModel, departureTrip: FlightDetailViewModel) {
        cwa_departure_info.visibility = View.VISIBLE
        cwa_departure_info.setContent(departureTrip.departureAirportCity + " - " + departureTrip.arrivalAirportCity)
        cwa_departure_info.setContentInfo("(" + FlightDateUtil.formatToUi(searchParam.departureDate) + ")")
        var airLineSection = ""
        var tripInfo = ""
        if (departureTrip.airlineDataList != null) {
            airLineSection = if (departureTrip.airlineDataList.size == 1) {
                departureTrip.airlineDataList[0].shortName
            } else {
                getString(R.string.flight_booking_multiple_airline_trip_card)
            }
        }
        tripInfo += if (departureTrip.totalTransit > 0) {
            String.format(getString(R.string.flight_booking_trip_info_format), departureTrip.totalTransit, getString(R.string.flight_booking_transit_trip_card))
        } else {
            String.format(getString(R.string.flight_booking_trip_info_format_without_count), getString(R.string.flight_booking_directly_trip_card))
        }
        cwa_departure_info.setSubContent(airLineSection)
        tripInfo += " " + String.format(getString(R.string.flight_booking_trip_info_airport_format), departureTrip.departureTime, departureTrip.arrivalTime)
        cwa_departure_info.setSubContentInfo(tripInfo)
    }

    override fun showAndRenderReturnTripCardDetail(searchParam: FlightSearchPassDataViewModel, returnTrip: FlightDetailViewModel) {
        cwa_return_info.visibility = View.VISIBLE
        cwa_return_info.setContent(returnTrip.departureAirportCity + " - " + returnTrip.arrivalAirportCity)
        cwa_return_info.setContentInfo("(" + FlightDateUtil.formatToUi(searchParam.returnDate) + ")")
        var airLineSection = ""
        var tripInfo = ""
        if (returnTrip.airlineDataList != null) {
            airLineSection = if (returnTrip.airlineDataList.size == 1) {
                returnTrip.airlineDataList[0].shortName
            } else {
                getString(R.string.flight_booking_multiple_airline_trip_card)
            }
        }
        tripInfo += if (returnTrip.totalTransit > 0) {
            String.format(getString(R.string.flight_booking_trip_info_format), returnTrip.totalTransit, getString(R.string.flight_booking_transit_trip_card))
        } else {
            String.format(getString(R.string.flight_booking_trip_info_format_without_count), getString(R.string.flight_booking_directly_trip_card))
        }
        cwa_return_info.setSubContent(airLineSection)
        tripInfo += " " + String.format(getString(R.string.flight_booking_trip_info_airport_format), returnTrip.departureTime, returnTrip.arrivalTime)
        cwa_return_info.setSubContentInfo(tripInfo)
    }

    override fun renderPassengersList(passengerViewModels: List<FlightBookingPassengerViewModel>) {
        passengerAdapter.clearAllElements()
        passengerAdapter.addElement(passengerViewModels)
    }

    override fun renderPhoneCodeView(countryPhoneCode: String) {
        et_phone_country_code.text = countryPhoneCode
    }

    override fun getDepartureTripId(): String = departureId

    override fun getReturnTripId(): String = returnId

    override fun navigateToDetailTrip(departureTrip: FlightDetailViewModel) {
        startActivity(FlightDetailActivity.createIntent(activity, departureTrip, false))
    }

    override fun getIdEmpotencyKey(tokenId: String): String = generateIdEmpotency(tokenId)

    override fun showFullPageLoading() {
        full_page_loading.visibility = View.VISIBLE
        container_full_page.visibility = View.GONE
    }

    override fun hideFullPageLoading() {
        full_page_loading.visibility = View.GONE
        container_full_page.visibility = View.VISIBLE
    }

    override fun setCartData(flightBookingCartData: FlightBookingCartData) {
        this.flightBookingCartData = flightBookingCartData
    }

    override fun getCurrentCartPassData(): FlightBookingCartData = flightBookingCartData

    override fun renderPriceListDetails(prices: List<SimpleViewModel>) {
        paramViewModel.priceListDetails = prices
        priceListAdapter.setViewModels(prices)
        priceListAdapter.notifyDataSetChanged()
    }

    override fun renderTotalPrices(totalPrice: String) {
        tv_total_price.text = totalPrice
    }

    override fun showGetCartDataErrorStateLayout(t: Throwable) {
        NetworkErrorHelper.showEmptyState(
                activity, view, FlightErrorUtil.getMessageFromException(activity, t)
        ) { flightBookingPresenter.onRetryGetCartData() }
    }

    override fun renderFinishTimeCountDown(date: Date) {
        expiredDate = date
        countdown_finish_transaction.setListener {
            flightBookingPresenter.onFinishTransactionTimeReached()
        }
        countdown_finish_transaction.cancel()
        countdown_finish_transaction.setExpiredDate(date)
        countdown_finish_transaction.start()
    }

    override fun showExpireTransactionDialog(message: String) {
        val dialog = AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(activity!!.getString(R.string.title_ok)
                ) { _, _ ->
                    FlightFlowUtil.actionSetResultAndClose(activity!!,
                            activity!!.intent,
                            FlightFlowConstant.EXPIRED_JOURNEY
                    )
                }
                .setCancelable(false)
                .create()
        dialog.show()
    }

    override fun setCartId(id: String) {
        bookingCartId = id
        flightBookingCartData.id = id
        getCurrentBookingParamViewModel().id = id
    }

    override fun showSoldOutDialog() {
        val dialog = AlertDialog.Builder(activity)
                .setMessage(R.string.flight_booking_sold_out_label)
                .setPositiveButton(activity!!.getString(R.string.title_ok)
                ) { _, _ ->
                    FlightFlowUtil.actionSetResultAndClose(activity!!,
                            activity!!.intent,
                            FlightFlowConstant.EXPIRED_JOURNEY
                    )
                }
                .setCancelable(false)
                .create()
        dialog.show()
    }

    override fun showPriceChangesDialog(newTotalPrice: String, oldTotalPrice: String) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        val previousDialog = fragmentManager!!.findFragmentByTag(INTERRUPT_DIALOG_TAG)
        if (previousDialog != null) {
            fragmentTransaction.remove(previousDialog)
        }
        fragmentTransaction.addToBackStack(null)
        val dialogFragment = FlightBookingNewPriceDialogFragment.newInstance(newTotalPrice, oldTotalPrice)
        dialogFragment.setTargetFragment(this, REQUEST_CODE_NEW_PRICE_DIALOG)
        dialogFragment.show(fragmentManager!!.beginTransaction(), INTERRUPT_DIALOG_TAG)
    }

    override fun getDepartureFlightDetailViewModel(): FlightDetailViewModel =
            flightBookingCartData.departureTrip

    override fun getReturnFlightDetailViewModel(): FlightDetailViewModel? =
            flightBookingCartData.returnTrip

    override fun getFlightBookingPassengers(): List<FlightBookingPassengerViewModel> =
            paramViewModel.passengerViewModels

    override fun navigateToReview(flightBookingReviewModel: FlightBookingReviewModel) {
        countdown_finish_transaction.cancel()
        startActivityForResult(FlightBookingReviewActivity.createIntent(activity, flightBookingReviewModel,
                flightPriceViewModel.comboKey), REQUEST_CODE_REVIEW)
    }

    override fun showUpdatePriceLoading() {
        progressDialog.show()
    }

    override fun hideUpdatePriceDialog() {
        progressDialog.dismiss()
    }

    override fun showUpdateDataErrorStateLayout(t: Throwable) {
        NetworkErrorHelper.showEmptyState(
                activity, view, FlightErrorUtil.getMessageFromException(activity, t)
        ) { flightBookingPresenter.onFinishTransactionTimeReached() }
    }

    override fun getCartId(): String = bookingCartId

    private fun generateIdEmpotency(requestId: String): String {
        var userId = Math.random().toString()
        if (activity != null && activity!!.application is AbstractionRouter) {
            val userSession = UserSession(activity)
            userId += userSession.userId
        }
        val timeMillis = System.currentTimeMillis().toString()
        val token = FlightRequestUtil.md5(timeMillis)
        return userId + String.format(getString(R.string.flight_booking_id_empotency_format),
                requestId, if (token.isEmpty()) timeMillis else token)
    }

    private fun showMessageErrorInSnackBar(resId: Int) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, getString(resId))
    }

    private fun initView() {
        et_phone_country_code.setOnClickListener {
            startActivityForResult(FlightBookingPhoneCodeActivity.getCallingIntent(activity),
                    REQUEST_CODEP_PHONE_CODE)
        }

        button_submit.setOnClickListener {
            flightBookingPresenter.onButtonSubmitClicked()
        }

        cwa_departure_info.setActionListener {
            flightBookingPresenter.onDepartureInfoClicked()
        }

        cwa_return_info.setActionListener {
            flightBookingPresenter.onReturnInfoClicked()
        }

        initializePassengerInfo()
        initializePriceList()
    }

    private fun initializePassengerInfo() {
        val adapterTypeFactory = FlightBookingPassengerAdapterTypeFactory(this)
        passengerAdapter = FlightBookingPassengerAdapter(adapterTypeFactory, arrayListOf())
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_passengers.layoutManager = layoutManager
        rv_passengers.setHasFixedSize(true)
        rv_passengers.isNestedScrollingEnabled = false
        rv_passengers.adapter = passengerAdapter
    }

    private fun initializePriceList() {
        priceListAdapter = FlightSimpleAdapter()
        priceListAdapter.setDescriptionTextColor(resources.getColor(R.color.font_black_secondary_54))
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_price_lists.layoutManager = layoutManager
        rv_price_lists.setHasFixedSize(true)
        rv_price_lists.isNestedScrollingEnabled = false
        rv_price_lists.adapter = priceListAdapter
    }

    companion object {
        private val EXTRA_SEARCH_PASS_DATA = "EXTRA_SEARCH_PASS_DATA"
        private val EXTRA_FLIGHT_DEPARTURE_ID = "EXTRA_FLIGHT_DEPARTURE_ID"
        private val EXTRA_FLIGHT_ARRIVAL_ID = "EXTRA_FLIGHT_ARRIVAL_ID"
        private val EXTRA_PRICE = "EXTRA_PRICE"
        private val INTERRUPT_DIALOG_TAG = "interrupt_dialog"
        private val KEY_CART_DATA = "KEY_CART_DATA"
        private val KEY_PARAM_VIEW_MODEL_DATA = "KEY_PARAM_VIEW_MODEL_DATA"
        private val KEY_PARAM_EXPIRED_DATE = "KEY_PARAM_EXPIRED_DATE"
        private val KEY_PARAM_EXTRA_PRICE = "KEY_PARAM_EXTRA_PRICE"

        private val REQUEST_CODE_PASSENGER = 1
        private val REQUEST_CODEP_PHONE_CODE = 2
        private val REQUEST_CODE_NEW_PRICE_DIALOG = 3
        private val REQUEST_CODE_REVIEW = 4
        private val REQUEST_CODE_OTP = 5

        fun newInstance(searchPassDataViewModel: FlightSearchPassDataViewModel,
                        departureId: String, returnId: String,
                        priceViewModel: FlightPriceViewModel): FlightBookingFragment {
            val fragment = FlightBookingFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_SEARCH_PASS_DATA, searchPassDataViewModel)
            bundle.putString(EXTRA_FLIGHT_DEPARTURE_ID, departureId)
            bundle.putString(EXTRA_FLIGHT_ARRIVAL_ID, returnId)
            bundle.putParcelable(EXTRA_PRICE, priceViewModel)
            fragment.arguments = bundle
            return fragment
        }
    }
}