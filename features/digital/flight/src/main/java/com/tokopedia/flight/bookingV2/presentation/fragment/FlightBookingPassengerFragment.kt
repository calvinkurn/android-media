package com.tokopedia.flight.bookingV2.presentation.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.common.travel.data.entity.TravelContactListModel
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode
import com.tokopedia.common.travel.widget.TravelContactArrayAdapter
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.constant.FlightBookingPassenger
import com.tokopedia.flight.booking.di.FlightBookingComponent
import com.tokopedia.flight.booking.view.activity.FlightBookingNationalityActivity
import com.tokopedia.flight.booking.view.adapter.FlightSimpleAdapter
import com.tokopedia.flight.booking.view.fragment.FlightBookingAmenityFragment
import com.tokopedia.flight.booking.view.fragment.FlightBookingNationalityFragment
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel
import com.tokopedia.flight.bookingV2.presentation.activity.FlightBookingPassengerActivity.Companion.EXTRA_DEPARTURE_DATE
import com.tokopedia.flight.bookingV2.presentation.activity.FlightBookingPassengerActivity.Companion.EXTRA_DEPATURE
import com.tokopedia.flight.bookingV2.presentation.activity.FlightBookingPassengerActivity.Companion.EXTRA_IS_AIRASIA
import com.tokopedia.flight.bookingV2.presentation.activity.FlightBookingPassengerActivity.Companion.EXTRA_IS_DOMESTIC
import com.tokopedia.flight.bookingV2.presentation.activity.FlightBookingPassengerActivity.Companion.EXTRA_LUGGAGES
import com.tokopedia.flight.bookingV2.presentation.activity.FlightBookingPassengerActivity.Companion.EXTRA_MEALS
import com.tokopedia.flight.bookingV2.presentation.activity.FlightBookingPassengerActivity.Companion.EXTRA_PASSENGER
import com.tokopedia.flight.bookingV2.presentation.activity.FlightBookingPassengerActivity.Companion.EXTRA_REQUEST_ID
import com.tokopedia.flight.bookingV2.presentation.activity.FlightBookingPassengerActivity.Companion.EXTRA_RETURN
import com.tokopedia.flight.bookingV2.viewmodel.FlightBookingViewModel
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.util.FlightPassengerInfoValidator
import kotlinx.android.synthetic.main.fragment_flight_booking_passenger.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * @author by jessica on 2019-09-05
 */

class FlightBookingPassengerFragment: BaseDaggerFragment() {

    lateinit var depatureId: String
    lateinit var passengerModel: FlightBookingPassengerViewModel
    lateinit var luggageModels: List<FlightBookingAmenityMetaViewModel>
    lateinit var mealModels: List<FlightBookingAmenityMetaViewModel>
    var isAirAsiaAirlines: Boolean = false
    lateinit var depatureDate: String
    lateinit var requestId: String
    var isDomestic: Boolean = false
    var returnId: String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bookingViewModel: FlightBookingViewModel

    lateinit var travelContactArrayAdapter: TravelContactArrayAdapter

    override fun initInjector() {
        getComponent(FlightBookingComponent::class.java).inject(this)
    }

    override fun getScreenName(): String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            passengerModel = it.getParcelable(EXTRA_PASSENGER)
            luggageModels = it.getParcelableArrayList(EXTRA_LUGGAGES)
            mealModels = it.getParcelableArrayList(EXTRA_MEALS)
            isAirAsiaAirlines = it.getBoolean(EXTRA_IS_AIRASIA)
            depatureDate = it.getString(EXTRA_DEPARTURE_DATE)
            requestId = it.getString(EXTRA_REQUEST_ID)
            isDomestic = it.getBoolean(EXTRA_IS_DOMESTIC)

        }

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            bookingViewModel = viewModelProvider.get(FlightBookingViewModel::class.java)
        }
        //masukkin observe validate data on submit
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_flight_booking_passenger, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        //getContactList

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bookingViewModel.contactListResult.observe(this, android.arch.lifecycle.Observer {
            contactList ->
            contactList?.let {
                travelContactArrayAdapter.updateItem(it.toMutableList())
            }
        })
    }

    private fun initView () {
        context?.let {
            //render layout!!
            tv_header.text = passengerModel.headerTitle

            renderViewBasedOnType()

            //autofill based on passenger models

            renderPassport()

            travelContactArrayAdapter = TravelContactArrayAdapter(it,
                    com.tokopedia.common.travel.R.layout.layout_travel_autocompletetv, arrayListOf(),
                    object : TravelContactArrayAdapter.ContactArrayListener {
                        override fun getFilterText(): String {
                            return et_first_name.text.toString()
                        }
                    })
            (et_first_name as AutoCompleteTextView).setAdapter(travelContactArrayAdapter)
            (et_first_name as AutoCompleteTextView).onItemClickListener = AdapterView.OnItemClickListener {
                _, _, position, _ -> autofillPassengerContact(travelContactArrayAdapter.getItem(position)) }

            button_submit.setOnClickListener {
                clearAllKeyboardFocus()
                // call viewmodel to validate fields and upsert!!
            }

            et_birth_date.setOnClickListener {
                onBirthdateClicked()
            }

            et_passport_expiration_date.setOnClickListener {
                onPassportExpiredClicked()
            }

            et_nationality.setOnClickListener {
                startActivityForResult(FlightBookingNationalityActivity.createIntent(context,
                        getString(R.string.flight_nationality_search_hint)), REQUEST_CODE_PICK_NATIONALITY)
            }

            et_passport_issuer_country.setOnClickListener {
                startActivityForResult(FlightBookingNationalityActivity.createIntent(context,
                        getString(R.string.flight_passport_search_hint)), REQUEST_CODE_PICK_ISSUER_COUNTRY)
            }
        }
    }

    fun onPassportExpiredClicked() {

        var minDate: Date
        var selectedDate: Date
        var maxDate: Date

        var tmpDepatureDate = FlightDateUtil.stringToDate(depatureDate)
        minDate = FlightDateUtil.addTimeToSpesificDate(tmpDepatureDate, Calendar.MONTH, PLUS_SIX)
        maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, PLUS_TWENTY)
        selectedDate = minDate

        //and also validate 6 month
        if (getPassportExpiryDate().isNotBlank()) {
            selectedDate = FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, getPassportExpiryDate())
        }

        showPassportExpiredDatePickerDialog(selectedDate, minDate, maxDate)
    }

    fun showPassportExpiredDatePickerDialog(selectedDate: Date, minDate: Date, maxDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        val datePicker = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener {
            view, year, month, dayOfMonth ->
            //on Passport expiry date change
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        val datePicker1 = datePicker.datePicker
        datePicker1.minDate = minDate.time
        datePicker1.maxDate = maxDate.time
        datePicker.show()
    }

    fun getPassportExpiryDate(): String {
        return et_passport_expiration_date.text.toString()
    }

    fun clearAllKeyboardFocus() {
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    fun renderPassengerMeals(flightBookingMealRouteModels: List<FlightBookingAmenityMetaViewModel>,
                             selecteds: List<FlightBookingAmenityMetaViewModel>) {
        meals_container.visibility = View.VISIBLE

        var models = arrayListOf<SimpleViewModel>()
        if (flightBookingMealRouteModels != null) {
            for (meal in flightBookingMealRouteModels) {
                val simpleModel = SimpleViewModel(meal.description, getString(R.string.flight_booking_passenger_choose_label))
                for (selected in selecteds) {
                    if (selected.key.equals(meal.key, true)) {
                        val selectedMeals = arrayListOf<String>()
                        for (amenity in selected.amenities) {
                            selectedMeals.add(amenity.title)
                        }
                        simpleModel.description = TextUtils.join(",", selectedMeals)
                        break
                    }
                }
                models.add(simpleModel)
            }
        }

        val mealAdapter = FlightSimpleAdapter()
        mealAdapter.setMarginTopDp(resources.getDimension(R.dimen.margin_4))
        mealAdapter.setMarginBottomDp(resources.getDimension(R.dimen.margin_4))
        mealAdapter.setArrowVisible(true)
        mealAdapter.setFontSize(resources.getDimension(R.dimen.sp_12))
        mealAdapter.setInteractionListener { adapterPosition, viewModel ->
            //on meal click
        }

        rv_meals.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_meals.setHasFixedSize(true)
        rv_meals.isNestedScrollingEnabled = false
        rv_meals.adapter = mealAdapter
        mealAdapter.setDescriptionTextColor(resources.getColor(R.color.colorPrimary))
        mealAdapter.setViewModels(models)
        mealAdapter.notifyDataSetChanged()
    }

    fun renderPassengerLuggages(flightBookingLuggageRouteViewModels: List<FlightBookingAmenityMetaViewModel>,
                                selecteds: List<FlightBookingAmenityMetaViewModel>) {
        luggage_container.visibility = View.VISIBLE

        val models = arrayListOf<SimpleViewModel>()
        if (flightBookingLuggageRouteViewModels != null) {
            for (luggage in flightBookingLuggageRouteViewModels) {
                val model = SimpleViewModel(luggage.description, getString(R.string.flight_booking_passenger_choose_label))
                for (selected in selecteds) {
                    if (selected.key.equals(luggage.key, true)) {
                        val selectedLuggages = arrayListOf<String>()
                        for (aminity in selected.amenities) {
                            selectedLuggages.add("${aminity.title} - ${aminity.price}")
                        }
                        model.description = TextUtils.join(",", selectedLuggages)
                        break
                    }
                }
                models.add(model)
            }
        }

        val luggageAdapter = FlightSimpleAdapter()
        luggageAdapter.setMarginTopDp(resources.getDimension(R.dimen.margin_4))
        luggageAdapter.setMarginBottomDp(resources.getDimension(R.dimen.margin_4))
        luggageAdapter.setArrowVisible(true)
        luggageAdapter.setFontSize(resources.getDimension(R.dimen.sp_12))
        luggageAdapter.setInteractionListener { adapterPosition, viewModel ->
            //on luggage click
        }

        rv_luggages.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_luggages.setHasFixedSize(true)
        rv_luggages.isNestedScrollingEnabled = false
        rv_luggages.adapter = luggageAdapter
        luggageAdapter.setDescriptionTextColor(resources.getColor(R.color.colorPrimary))
        luggageAdapter.setViewModels(models)
        luggageAdapter.notifyDataSetChanged()

    }

    fun renderPassport() {
        if (!isDomestic) {
            container_passport_data.visibility = View.VISIBLE
            if (passengerModel.passportNumber != null) et_passport_no.setText(passengerModel.passportNumber)
            if (passengerModel.passportExpiredDate != null) et_passport_expiration_date.setText(
                    FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT,
                            passengerModel.passportExpiredDate))
            if (passengerModel.passportNationality != null) et_nationality.setText(passengerModel.passportNationality.countryName)
            if (passengerModel.passportIssuerCountry != null) et_passport_issuer_country.setText(passengerModel.passportIssuerCountry.countryName)
        } else container_passport_data.visibility = View.GONE
    }

    fun renderViewBasedOnType() {
        if (isAdultPassenger()) {
            tv_subheader.text = getString(R.string.flight_booking_passenger_adult_subtitle)
            // render title for adult
            if (isMandatoryDoB() || isDomestic) til_birth_date.visibility = View.VISIBLE else View.GONE
        } else {
            // render title for child and infant
            til_birth_date.visibility = View.VISIBLE
            if (isChildPassenger()) tv_subheader.text = getString(R.string.flight_booking_passenger_child_subtitle)
            else getString(R.string.flight_booking_passenger_infant_subtitle)
        }

        if (isAdultPassenger() || isChildPassenger()) {
            if (luggageModels.isNotEmpty()) {}//render luggage
            if (mealModels.isNotEmpty()) {} // render meals
        }
    }

    fun onBirthdateClicked() {
        lateinit var maxDate: Date
        var minDate: Date? = null
        lateinit var selectedDate: Date
        val depatureDate = FlightDateUtil.stringToDate(depatureDate)

        if (isChildPassenger()) {
            // child age 2 - 12
            minDate = FlightDateUtil.addTimeToSpesificDate(depatureDate, Calendar.YEAR, MINUS_TWELVE)
            minDate = FlightDateUtil.addTimeToSpesificDate(minDate, Calendar.DATE, PLUS_ONE)
            maxDate = FlightDateUtil.addTimeToSpesificDate(depatureDate, Calendar.YEAR, MINUS_TWO)
            selectedDate = maxDate
        } else if (isAdultPassenger()) {
            maxDate = FlightDateUtil.addTimeToSpesificDate(depatureDate, Calendar.YEAR, MINUS_TWELVE)
            selectedDate = maxDate
        } else {
            // for Infant
            minDate = FlightDateUtil.addTimeToSpesificDate(depatureDate, Calendar.YEAR, MINUS_TWO)
            minDate = FlightDateUtil.addTimeToSpesificDate(depatureDate, Calendar.DATE, PLUS_ONE)
            maxDate = FlightDateUtil.getCurrentDate()
            selectedDate = maxDate
        }

        // if birthdate not empty -> selecteddate = birthdate edittext

        val currentTime = FlightDateUtil.getCurrentCalendar()
        currentTime.time = maxDate
        currentTime.set(Calendar.HOUR_OF_DAY, DEFAULT_LAST_HOUR_IN_DAY)
        currentTime.set(Calendar.MINUTE, DEFAULT_LAST_MIN_IN_DAY)
        currentTime.set(Calendar.SECOND, DEFAULT_LAST_SEC_IN_DAY)

        if (minDate != null) showBirthdatePickerDialog(selectedDate = selectedDate, minDate = minDate, maxDate = currentTime.time)
        else showBirthdatePickerDialog(selectedDate = selectedDate, maxDate = currentTime.time)
    }

    fun showBirthdatePickerDialog(selectedDate: Date, minDate: Date? = null, maxDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        val datePicker = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener {
            view, year, month, dayOfMonth ->
//            presenter.onBirthdateChange(year, month, dayOfMonth, minDate, maxDate)
            // VALIDATE DATE AND SHOW IN PAGES (call viewmodel)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        val datePicker1 = datePicker.datePicker
        if (minDate != null) datePicker1.minDate = minDate.time
        datePicker1.maxDate = maxDate.time
        datePicker.show()
    }

    fun autofillPassengerContact(contact:TravelContactListModel.Contact?) {
        if (contact != null) {

        }
    }

    fun isAdultPassenger(): Boolean {
        return passengerModel.type == FlightBookingPassenger.ADULT
    }

    fun isChildPassenger(): Boolean {
        return passengerModel.type == FlightBookingPassenger.CHILDREN
    }

    fun isInfantPassenger(): Boolean {
        return passengerModel.type == FlightBookingPassenger.INFANT
    }

    fun isMandatoryDoB(): Boolean = isAirAsiaAirlines

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        clearAllKeyboardFocus()
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_LUGGAGE -> {
                    if (data != null) {
                        val flightBookingLuggageMetaViewModel = data.getParcelableExtra<FlightBookingAmenityMetaViewModel>(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES)
//                        presenter.onLuggageDataChange(flightBookingLuggageMetaViewModel)
                    }
                }

                REQUEST_CODE_PICK_MEAL -> {
                    if (data != null) {
                        val flightBookingLuggageMetaViewModel = data.getParcelableExtra<FlightBookingAmenityMetaViewModel>(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES)
//                        presenter.onMealDataChange(flightBookingLuggageMetaViewModel)
                    }
                }

                REQUEST_CODE_PICK_NATIONALITY -> {
                    if (data != null) {
                        val flightPassportNationalityViewModel = data.getParcelableExtra<CountryPhoneCode>(FlightBookingNationalityFragment.EXTRA_SELECTED_COUNTRY)
//                        presenter.onNationalityChanged(flightPassportNationalityViewModel)
                    }
                }

                REQUEST_CODE_PICK_ISSUER_COUNTRY -> {
                    if (data != null) {
                        val flightPassportIssuerCountry = data.getParcelableExtra<CountryPhoneCode>(FlightBookingNationalityFragment.EXTRA_SELECTED_COUNTRY)
//                        presenter.onIssuerCountryChanged(flightPassportIssuerCountry)
                    }
                }
            }
        }
    }

    companion object {

        private val REQUEST_CODE_PICK_LUGGAGE = 1
        private val REQUEST_CODE_PICK_MEAL = 2
        private val REQUEST_CODE_PICK_NATIONALITY = 4
        private val REQUEST_CODE_PICK_ISSUER_COUNTRY = 5

        private val PLUS_ONE = 1
        private val MINUS_TWO = -2
        private val MINUS_TWELVE = -12
        private val PLUS_SIX = 6
        private val PLUS_TWENTY = 20

        private val DEFAULT_LAST_HOUR_IN_DAY = 23
        private val DEFAULT_LAST_MIN_IN_DAY = 59
        private val DEFAULT_LAST_SEC_IN_DAY = 59

        fun newInstance(depatureId: String,
                        passengerModel: FlightBookingPassengerViewModel,
                        luggageModels: List<FlightBookingAmenityMetaViewModel>,
                        mealModels: List<FlightBookingAmenityMetaViewModel>,
                        isAirAsiaAirlines: Boolean,
                        depatureDate: String,
                        requestId: String,
                        isDomestic: Boolean,
                        returnId: String? = null): FlightBookingPassengerFragment {
            val fragment = FlightBookingPassengerFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_DEPATURE, depatureId)
            bundle.putString(EXTRA_RETURN, returnId)
            bundle.putString(EXTRA_DEPARTURE_DATE, depatureDate)
            bundle.putBoolean(EXTRA_IS_AIRASIA, isAirAsiaAirlines)
            bundle.putParcelable(EXTRA_PASSENGER, passengerModel)
            bundle.putParcelableArrayList(EXTRA_LUGGAGES, luggageModels as ArrayList<out Parcelable>)
            bundle.putParcelableArrayList(EXTRA_MEALS, mealModels as ArrayList<out Parcelable>)
            bundle.putString(EXTRA_REQUEST_ID, requestId)
            bundle.putBoolean(EXTRA_IS_DOMESTIC, isDomestic)
            fragment.arguments = bundle
            return fragment
        }
    }

    interface OnFragmentInteractionListener {
        fun actionSuccessUpdatePassengerData(flightBookingPassengerViewModel: FlightBookingPassengerViewModel)

        fun updatePassengerViewModel(flightBookingPassengerViewModel: FlightBookingPassengerViewModel)

    }

}