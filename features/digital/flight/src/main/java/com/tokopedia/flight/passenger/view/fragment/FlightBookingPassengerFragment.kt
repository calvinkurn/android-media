package com.tokopedia.flight.passenger.view.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.common.travel.data.entity.TravelContactIdCard
import com.tokopedia.common.travel.data.entity.TravelContactListModel
import com.tokopedia.common.travel.data.entity.TravelUpsertContactModel
import com.tokopedia.common.travel.widget.TravelContactArrayAdapter
import com.tokopedia.common.travel.widget.filterchips.FilterChipAdapter
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.constant.FlightBookingPassenger
import com.tokopedia.flight.booking.view.activity.FlightBookingAmenityActivity
import com.tokopedia.flight.booking.view.activity.FlightBookingNationalityActivity
import com.tokopedia.flight.booking.view.adapter.FlightSimpleAdapter
import com.tokopedia.flight.booking.view.fragment.FlightBookingAmenityFragment
import com.tokopedia.flight.booking.view.fragment.FlightBookingNationalityFragment
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.util.FlightPassengerInfoValidator
import com.tokopedia.flight.common.util.FlightPassengerTitle
import com.tokopedia.flight.common.util.FlightPassengerTitleType
import com.tokopedia.flight.passenger.di.FlightPassengerComponent
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_DEPARTURE_DATE
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_DEPATURE
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_IS_AIRASIA
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_IS_DOMESTIC
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_LUGGAGES
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_MEALS
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_PASSENGER
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_REQUEST_ID
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_RETURN
import com.tokopedia.flight.passenger.viewmodel.FlightPassengerViewModel
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_flight_booking_passenger.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * @author by jessica on 2019-09-05
 */

class FlightBookingPassengerFragment : BaseDaggerFragment() {

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
    lateinit var passengerViewModel: FlightPassengerViewModel

    @Inject
    lateinit var flightPassengerInfoValidator: FlightPassengerInfoValidator

    lateinit var travelContactArrayAdapter: TravelContactArrayAdapter

    override fun initInjector() {
        getComponent(FlightPassengerComponent::class.java).inject(this)
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
            passengerViewModel = viewModelProvider.get(FlightPassengerViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(com.tokopedia.flight.R.layout.fragment_flight_booking_passenger, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        passengerViewModel.getContactList(GraphqlHelper.loadRawString(resources, com.tokopedia.common.travel.R.raw.query_get_travel_contact_list),
                getPassengerTypeString(passengerModel.type)
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        passengerViewModel.contactListResult.observe(this, androidx.lifecycle.Observer { contactList ->
            contactList?.let { travelContactArrayAdapter.updateItem(it.toMutableList()) }
        })

        passengerViewModel.nationalityData.observe(this, androidx.lifecycle.Observer { value ->
            value?.let { onNationalityChanged(it) }
        })

        passengerViewModel.passportIssuerCountryData.observe(this, androidx.lifecycle.Observer { value ->
            value?.let { onIssuerCountryChanged(it) }
        })
    }

    private fun initView() {
        context?.let {

            renderViewBasedOnType()
            renderPassengerData()
            renderPassport()
            initFirstNameAutoCompleteTv(it)

            button_submit.setOnClickListener {
                clearAllKeyboardFocus()
                onSubmitData()
            }
            et_birth_date.setOnClickListener { onBirthdateClicked() }
            et_passport_expiration_date.setOnClickListener { onPassportExpiredClicked() }
            et_nationality.setOnClickListener {
                startActivityForResult(FlightBookingNationalityActivity.createIntent(context,
                        getString(com.tokopedia.flight.R.string.flight_nationality_search_hint)), REQUEST_CODE_PICK_NATIONALITY)
            }
            et_passport_issuer_country.setOnClickListener {
                startActivityForResult(FlightBookingNationalityActivity.createIntent(context,
                        getString(com.tokopedia.flight.R.string.flight_passport_search_hint)), REQUEST_CODE_PICK_ISSUER_COUNTRY)
            }

            til_first_name.setErrorTextAppearance(com.tokopedia.common.travel.R.style.ErrorTextAppearance)
            til_last_name.setErrorTextAppearance(com.tokopedia.common.travel.R.style.ErrorTextAppearance)
            til_birth_date.setErrorTextAppearance(com.tokopedia.common.travel.R.style.ErrorTextAppearance)
            til_passport_no.setErrorTextAppearance(com.tokopedia.common.travel.R.style.ErrorTextAppearance)
            til_passport_expiration_date.setErrorTextAppearance(com.tokopedia.common.travel.R.style.ErrorTextAppearance)
            til_nationality.setErrorTextAppearance(com.tokopedia.common.travel.R.style.ErrorTextAppearance)
            til_passport_issuer_country.setErrorTextAppearance(com.tokopedia.common.travel.R.style.ErrorTextAppearance)

        }
    }

    fun initFirstNameAutoCompleteTv(context: Context) {
        travelContactArrayAdapter = TravelContactArrayAdapter(context,
                com.tokopedia.common.travel.R.layout.layout_travel_autocompletetv, arrayListOf(),
                object : TravelContactArrayAdapter.ContactArrayListener {
                    override fun getFilterText(): String {
                        return et_first_name.text.toString()
                    }
                })
        (et_first_name as AutoCompleteTextView).setAdapter(travelContactArrayAdapter)
        (et_first_name as AutoCompleteTextView).onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ -> autofillPassengerContact(travelContactArrayAdapter.getItem(position)) }

    }

    fun onSubmitData() {
        if (validateFields()) {
            passengerModel.passengerTitle = getPassengerTitle()
            passengerModel.passengerTitleId = getPassengerTitleId(getPassengerTitle())
            passengerModel.passengerFirstName = getFirstName()
            passengerModel.passengerLastName = getLastName()
            if (isMandatoryDoB() || !isDomestic) passengerModel.passengerBirthdate = FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, FlightDateUtil.DEFAULT_FORMAT, getPassengerBirthDate())
            if (!isDomestic) {
                passengerModel.passportNumber = getPassportNumber()
                passengerModel.passportExpiredDate = FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, FlightDateUtil.DEFAULT_FORMAT, getPassportExpiryDate())
            }

            upsertContactList()
            finishActivityWithData()
        }
    }

    fun upsertContactList() {
        val contact = TravelUpsertContactModel.Contact()
        contact.firstName = getFirstName()
        contact.lastName = getLastName()
        contact.title = getPassengerTitle()
        if (isMandatoryDoB() || !isDomestic) contact.birthDate = FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, FlightDateUtil.DEFAULT_FORMAT, getPassengerBirthDate())
        if (!isDomestic) {
            contact.nationality = passengerModel.passportNationality.countryId
            contact.idList = listOf(TravelContactIdCard(type = "passport", title = "Paspor",
                    number = getPassportNumber(), country = passengerModel.passportIssuerCountry.countryId,
                    expiry = FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, FlightDateUtil.DEFAULT_FORMAT, getPassportExpiryDate())))
        }

        passengerViewModel.updateContactList(GraphqlHelper.loadRawString(resources,
                com.tokopedia.common.travel.R.raw.query_upsert_travel_contact_list),
                contact)
    }

    fun finishActivityWithData() {
        activity?.run {
            intent.putExtra(EXTRA_PASSENGER, passengerModel)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    fun getFirstName(): String = et_first_name.text.toString()

    fun getLastName(): String = et_last_name.text.toString()

    fun getPassengerTitle(): String = rv_passenger_title.getFirstSelectedItem()

    fun getPassengerBirthDate(): String = et_birth_date.text.toString().trim()

    fun getPassportExpiryDate(): String = et_passport_expiration_date.text.toString().trim()

    fun getPassportNumber(): String = et_passport_no.text.toString().trim()

    fun renderPassengerData() {
        if (!passengerModel.passengerFirstName.isNullOrBlank()) {
            et_first_name.setText(passengerModel.passengerFirstName)
            et_last_name.setText(passengerModel.passengerLastName)
        }

        if (passengerModel.passengerBirthdate != null)
            et_birth_date.setText(FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT,
                    FlightDateUtil.DEFAULT_VIEW_FORMAT, passengerModel.passengerBirthdate))

        rv_passenger_title.listener = object : FilterChipAdapter.OnClickListener {
            override fun onChipClickListener(string: String, isSelected: Boolean) {
                if (isSelected) {
                    passengerModel.passengerTitle = string
                    passengerModel.passengerTitleId = getPassengerTitleId(string)
                }
            }
        }

        if (isAdultPassenger()) {
            val entries = resources.getStringArray(R.array.flight_adult_titles)
            rv_passenger_title.setItem(ArrayList(Arrays.asList(*entries)),
                    initialSelectedItemPos = if (passengerModel.passengerTitle != null) getPassengerTitleId(passengerModel.passengerTitle) - 1 else null)
        } else {
            val entries = resources.getStringArray(R.array.flight_child_infant_titles)
            rv_passenger_title.setItem(ArrayList(Arrays.asList(*entries)),
                    initialSelectedItemPos = if (passengerModel.passengerTitle != null) getPassengerTitleId(passengerModel.passengerTitle) - 1 else null)
        }
        rv_passenger_title.selectOnlyOneChip(true)
        rv_passenger_title.canDiselectAfterSelect(false)
    }

    private fun renderPassengerTitle(passengerTitle: String) {
        if (passengerTitle.equals(FlightPassengerTitle.TUAN, true))
            rv_passenger_title.selectChipByPosition(0)
        else if (passengerTitle.equals(FlightPassengerTitle.NYONYA, true))
            rv_passenger_title.selectChipByPosition(1)
        else if (passengerTitle.equals(FlightPassengerTitle.NONA, true))
            rv_passenger_title.selectChipByPosition(2)
        else rv_passenger_title.onResetChip()
    }

    fun onPassportExpiredClicked() {

        var minDate: Date
        var selectedDate: Date
        var maxDate: Date

        var tmpDepatureDate = FlightDateUtil.stringToDate(depatureDate)
        minDate = FlightDateUtil.addTimeToSpesificDate(tmpDepatureDate, Calendar.MONTH, PLUS_SIX)
        maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, PLUS_TWENTY)
        selectedDate = minDate

        if (getPassportExpiryDate().isNotBlank()) {
            selectedDate = FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, getPassportExpiryDate())
        }

        showCalendarPickerDialog(selectedDate, minDate, maxDate, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val calendar = FlightDateUtil.getCurrentCalendar()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DATE, dayOfMonth)
            val passportExpiryDate = calendar.time

            val passportExpiryDateStr = FlightDateUtil.dateToString(passportExpiryDate, FlightDateUtil.DEFAULT_VIEW_FORMAT)
            passengerModel.passportExpiredDate = FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT,
                    FlightDateUtil.DEFAULT_FORMAT, passportExpiryDateStr)
            et_passport_expiration_date.setText(passportExpiryDateStr)
        })
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
                val simpleModel = SimpleViewModel(meal.description, getString(com.tokopedia.flight.R.string.flight_booking_passenger_choose_label))
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
        mealAdapter.setMarginTopDp(resources.getDimension(com.tokopedia.flight.R.dimen.margin_4))
        mealAdapter.setMarginBottomDp(resources.getDimension(com.tokopedia.flight.R.dimen.margin_4))
        mealAdapter.setArrowVisible(true)
        mealAdapter.setFontSize(resources.getDimension(com.tokopedia.design.R.dimen.sp_12))
        mealAdapter.setInteractionListener { adapterPosition, viewModel ->
            val meal = flightBookingMealRouteModels.get(adapterPosition)
            var existingSelected: FlightBookingAmenityMetaViewModel? = null

            for (passengerMeal in passengerModel.flightBookingMealMetaViewModels) {
                if (passengerMeal.key.equals(meal.key, true)) {
                    existingSelected = passengerMeal
                    break
                }
            }

            if (existingSelected == null) {
                existingSelected = FlightBookingAmenityMetaViewModel()
                existingSelected.key = meal.key
                existingSelected.journeyId = meal.journeyId
                existingSelected.arrivalId = meal.arrivalId
                existingSelected.departureId = meal.departureId
                existingSelected.amenities = arrayListOf()
                existingSelected.description = meal.description
            }

            navigateToMealPicker(meal.amenities, existingSelected)
        }

        rv_meals.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rv_meals.setHasFixedSize(true)
        rv_meals.isNestedScrollingEnabled = false
        rv_meals.adapter = mealAdapter
        mealAdapter.setDescriptionTextColor(resources.getColor(com.tokopedia.design.R.color.bg_button_green_border_outline))
        mealAdapter.setViewModels(models)
        mealAdapter.notifyDataSetChanged()
    }

    fun navigateToMealPicker(meals: MutableList<FlightBookingAmenityViewModel>, selected: FlightBookingAmenityMetaViewModel) {
        val title = String.format("%s %s", getString(com.tokopedia.flight.R.string.flight_booking_meal_toolbar_title), selected.description)
        val intent = FlightBookingAmenityActivity.createIntent(activity, title, meals, selected)
        startActivityForResult(intent, REQUEST_CODE_PICK_MEAL)
    }

    fun navigateToLuggagePicker(luggages: MutableList<FlightBookingAmenityViewModel>, selected: FlightBookingAmenityMetaViewModel) {
        val title = String.format("%s %s", getString(com.tokopedia.flight.R.string.flight_booking_luggage_toolbar_title), selected.description)
        val intent = FlightBookingAmenityActivity.createIntent(activity, title, luggages, selected)
        startActivityForResult(intent, REQUEST_CODE_PICK_LUGGAGE)
    }

    fun renderPassengerLuggages(flightBookingLuggageRouteViewModels: List<FlightBookingAmenityMetaViewModel>,
                                selecteds: List<FlightBookingAmenityMetaViewModel>) {
        luggage_container.visibility = View.VISIBLE

        val models = arrayListOf<SimpleViewModel>()
        if (flightBookingLuggageRouteViewModels != null) {
            for (luggage in flightBookingLuggageRouteViewModels) {
                val model = SimpleViewModel(luggage.description, getString(com.tokopedia.flight.R.string.flight_booking_passenger_choose_label))
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
        luggageAdapter.setMarginTopDp(resources.getDimension(com.tokopedia.flight.R.dimen.margin_4))
        luggageAdapter.setMarginBottomDp(resources.getDimension(com.tokopedia.flight.R.dimen.margin_4))
        luggageAdapter.setArrowVisible(true)
        luggageAdapter.setFontSize(resources.getDimension(com.tokopedia.design.R.dimen.sp_12))
        luggageAdapter.setInteractionListener { adapterPosition, viewModel ->
            val luggage = flightBookingLuggageRouteViewModels.get(adapterPosition)

            var existingSelected: FlightBookingAmenityMetaViewModel? = null
            for (selected in passengerModel.flightBookingLuggageMetaViewModels) {
                if (selected.key.equals(luggage.key, true)) existingSelected = selected
            }
            if (existingSelected == null) {
                existingSelected = FlightBookingAmenityMetaViewModel()
                existingSelected.key = luggage.key
                existingSelected.journeyId = luggage.journeyId
                existingSelected.arrivalId = luggage.arrivalId
                existingSelected.departureId = luggage.departureId
                existingSelected.description = luggage.description
                existingSelected.amenities = arrayListOf()
            }
            navigateToLuggagePicker(luggage.amenities, existingSelected)
        }

        rv_luggages.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rv_luggages.setHasFixedSize(true)
        rv_luggages.isNestedScrollingEnabled = false
        rv_luggages.adapter = luggageAdapter
        luggageAdapter.setDescriptionTextColor(resources.getColor(com.tokopedia.design.R.color.bg_button_green_border_outline))
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
            (activity as FlightBookingPassengerActivity).updateTitle(getString(R.string.flight_booking_passenger_adult_title))
            if (isMandatoryDoB() || !isDomestic) {
                birthdate_helper_text.visibility = View.VISIBLE
                til_birth_date.visibility = View.VISIBLE
            } else {
                birthdate_helper_text.visibility = View.GONE
                til_birth_date.visibility = View.GONE
            }

            birthdate_helper_text.text = getString(R.string.flight_booking_passenger_birthdate_adult_helper_text)
        } else {
            if (isChildPassenger()) {
                (activity as FlightBookingPassengerActivity).updateTitle(getString(R.string.flight_booking_passenger_child_title))
                birthdate_helper_text.text = getString(R.string.flight_booking_passenger_birthdate_child_helper_text)
            } else {
                (activity as FlightBookingPassengerActivity).updateTitle(getString(R.string.flight_booking_passenger_infant_title))
                birthdate_helper_text.text = getString(R.string.flight_booking_passenger_birthdate_infant_helper_text)
            }
            til_birth_date.visibility = View.VISIBLE
        }

        if (isAdultPassenger() || isChildPassenger()) {
            if (luggageModels.isNotEmpty()) {
                renderPassengerLuggages(luggageModels, passengerModel.flightBookingLuggageMetaViewModels)
            }
            if (mealModels.isNotEmpty()) {
                renderPassengerMeals(mealModels, passengerModel.flightBookingMealMetaViewModels)
            }
        }
    }

    fun onBirthdateClicked() {
        lateinit var maxDate: Date
        var minDate: Date? = null
        lateinit var selectedDate: Date
        val depatureDate = FlightDateUtil.stringToDate(depatureDate)

        if (isChildPassenger()) {
            minDate = FlightDateUtil.addTimeToSpesificDate(depatureDate, Calendar.YEAR, MINUS_TWELVE)
            minDate = FlightDateUtil.addTimeToSpesificDate(minDate, Calendar.DATE, PLUS_ONE)
            maxDate = FlightDateUtil.addTimeToSpesificDate(depatureDate, Calendar.YEAR, MINUS_TWO)
            selectedDate = maxDate
        } else if (isAdultPassenger()) {
            maxDate = FlightDateUtil.addTimeToSpesificDate(depatureDate, Calendar.YEAR, MINUS_TWELVE)
            selectedDate = maxDate
        } else {
            minDate = FlightDateUtil.addTimeToSpesificDate(depatureDate, Calendar.YEAR, MINUS_TWO)
            minDate = FlightDateUtil.addTimeToSpesificDate(minDate, Calendar.DATE, PLUS_ONE)
            maxDate = FlightDateUtil.getCurrentDate()
            selectedDate = maxDate
        }

        if (et_birth_date.text.toString().isNotEmpty()) selectedDate = FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, et_birth_date.text.toString())

        val currentTime = FlightDateUtil.getCurrentCalendar()
        currentTime.time = maxDate
        currentTime.set(Calendar.HOUR_OF_DAY, DEFAULT_LAST_HOUR_IN_DAY)
        currentTime.set(Calendar.MINUTE, DEFAULT_LAST_MIN_IN_DAY)
        currentTime.set(Calendar.SECOND, DEFAULT_LAST_SEC_IN_DAY)

        var onDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val calendar = FlightDateUtil.getCurrentCalendar()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DATE, dayOfMonth)
            val birthDate = calendar.time

            val birthDateStr = FlightDateUtil.dateToString(birthDate, FlightDateUtil.DEFAULT_VIEW_FORMAT)
            passengerModel.passengerBirthdate = FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT,
                    FlightDateUtil.DEFAULT_FORMAT, birthDateStr)
            et_birth_date.setText(birthDateStr)
        }

        if (minDate != null) showCalendarPickerDialog(selectedDate = selectedDate, minDate = minDate, maxDate = currentTime.time, onDateSetListener = onDateSetListener)
        else showCalendarPickerDialog(selectedDate = selectedDate, maxDate = currentTime.time, onDateSetListener = onDateSetListener)
    }

    fun showCalendarPickerDialog(selectedDate: Date, minDate: Date? = null, maxDate: Date, onDateSetListener: DatePickerDialog.OnDateSetListener) {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        val datePicker = DatePickerDialog(activity!!, onDateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        val datePicker1 = datePicker.datePicker
        if (minDate != null) datePicker1.minDate = minDate.time
        datePicker1.maxDate = maxDate.time
        datePicker.show()
    }

    fun clearAllFields() {
        et_first_name.setText("")
        et_last_name.setText("")
        renderPassengerTitle("")
        et_birth_date.setText("")
        et_passport_no.setText("")
        et_passport_issuer_country.setText("")
        et_passport_expiration_date.setText("")
        et_nationality.setText("")
    }

    fun autofillPassengerContact(contact: TravelContactListModel.Contact?) {
        clearAllFields()
        if (contact != null) {
            if (contact.firstName.isNotBlank()) {
                passengerModel.passengerFirstName = contact.firstName
                et_first_name.setText(contact.firstName)
            }
            if (contact.lastName.isNotBlank()) {
                passengerModel.passengerLastName = contact.lastName
                et_last_name.setText(contact.lastName)
            }
            if (contact.title.isNotBlank()) {
                passengerModel.passengerTitleId = getPassengerTitleId(contact.title.toLowerCase())
                passengerModel.passengerTitle = contact.title
                renderPassengerTitle(contact.title.toLowerCase())
            }
            if (contact.birthDate.isNotBlank() && (!isDomestic || isMandatoryDoB())) {
                passengerModel.passengerBirthdate = contact.birthDate
                et_birth_date.setText(FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT,
                        FlightDateUtil.DEFAULT_VIEW_FORMAT, contact.birthDate))
            }

            if (contact.idList.isNotEmpty() && !isDomestic) {
                for (id in contact.idList) {
                    if (isPassportId(id)) {
                        passengerModel.passportNumber = id.number
                        passengerModel.passportExpiredDate = id.expiry
                        et_passport_no.setText(id.number)
                        et_passport_expiration_date.setText(FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT,
                                FlightDateUtil.DEFAULT_VIEW_FORMAT, id.expiry))
                        passengerViewModel.getNationalityById(GraphqlHelper.loadRawString(resources, com.tokopedia.travel.country_code.R.raw.query_travel_get_all_country), contact.nationality)
                        passengerViewModel.getPassportIssuerCountryById(GraphqlHelper.loadRawString(resources, com.tokopedia.travel.country_code.R.raw.query_travel_get_all_country), id.country)
                        break
                    }
                }
            }
        }
    }

    fun getPassengerTitleId(passengerTitle: String): Int {
        return when {
            passengerTitle.equals(FlightPassengerTitle.TUAN, true) -> FlightPassengerTitleType.TUAN
            passengerTitle.equals(FlightPassengerTitle.NYONYA, true) -> FlightPassengerTitleType.NYONYA
            else -> FlightPassengerTitleType.NONA
        }
    }

    private fun getPassengerTypeString(passengerType: Int): String = when (passengerType) {
        FlightBookingPassenger.ADULT -> "adult"
        FlightBookingPassenger.CHILDREN -> "child"
        FlightBookingPassenger.INFANT -> "infant"
        else -> ""
    }

    private fun isAdultPassenger(): Boolean = passengerModel.type == FlightBookingPassenger.ADULT

    private fun isChildPassenger(): Boolean = passengerModel.type == FlightBookingPassenger.CHILDREN

    private fun isInfantPassenger(): Boolean = passengerModel.type == FlightBookingPassenger.INFANT

    private fun isMandatoryDoB(): Boolean = isAirAsiaAirlines

    private fun isPassportId(id: TravelContactIdCard): Boolean = id.type.equals("passport", true)

    private fun onAmenitiesDataChange(flightBookingLuggageMetaViewModel: FlightBookingAmenityMetaViewModel, passengerModelAmenities: MutableList<FlightBookingAmenityMetaViewModel>): List<FlightBookingAmenityMetaViewModel> {
        val index = passengerModelAmenities.indexOf(flightBookingLuggageMetaViewModel)

        if (flightBookingLuggageMetaViewModel.amenities.size != 0) {
            if (index != -1) {
                passengerModelAmenities.set(index, flightBookingLuggageMetaViewModel)
            } else {
                passengerModelAmenities.add(flightBookingLuggageMetaViewModel)
            }
        } else {
            if (index != -1) {
                passengerModelAmenities.removeAt(index)
            }
        }

        return passengerModelAmenities
    }

    private fun onNationalityChanged(flightPassportNationalityViewModel: TravelCountryPhoneCode) {
        passengerModel.passportNationality = flightPassportNationalityViewModel
        et_nationality.setText(flightPassportNationalityViewModel.countryName)
    }

    private fun onIssuerCountryChanged(flightPassportIssuerCountry: TravelCountryPhoneCode) {
        passengerModel.passportIssuerCountry = flightPassportIssuerCountry
        et_passport_issuer_country.setText(flightPassportIssuerCountry.countryName)
    }

    private fun validateFields(): Boolean {
        var isValid = true

        val isNeedPassport = !isDomestic
        val twelveYearsAgo = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(depatureDate), Calendar.YEAR, MINUS_TWELVE)
        val twoYearsAgo = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(depatureDate), Calendar.YEAR, MINUS_TWO)
        val sixMonthFromDeparture = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.stringToDate(depatureDate),
                Calendar.MONTH, PLUS_SIX)
        val twentyYearsFromToday = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, PLUS_TWENTY)

        resetEditTextErrorText()

        if (flightPassengerInfoValidator.validateNameIsEmpty(getFirstName())) {
            isValid = false
            til_first_name.error = getString(com.tokopedia.flight.R.string.flight_booking_passenger_first_name_empty_error)
        } else if (flightPassengerInfoValidator.validateNameIsNotAlphabetAndSpaceOnly(getFirstName())) {
            isValid = false
            til_first_name.error = getString(R.string.flight_booking_passenger_first_name_alpha_space_error)
        } else if (flightPassengerInfoValidator.validateFirstNameIsMoreThanMaxLength(getFirstName())) {
            isValid = false
            til_first_name.error = getString(R.string.flight_booking_passenger_first_name_max_error)
        }

        if (flightPassengerInfoValidator.validateLastNameIsMoreThanMaxLength(getLastName())) {
            isValid = false
            til_last_name.error = getString(R.string.flight_booking_passenger_last_name_max_error)
        } else if (flightPassengerInfoValidator.validateNameIsEmpty(getLastName())) {
            isValid = false
            til_last_name.error = getString(com.tokopedia.flight.R.string.flight_booking_passenger_last_name_should_same_error)
        } else if (flightPassengerInfoValidator.validateLastNameIsLessThanMinLength(getLastName())) {
            isValid = false
            til_last_name.error = getString(com.tokopedia.flight.R.string.flight_booking_passenger_last_name_empty_error)
        } else if (flightPassengerInfoValidator.validateLastNameIsNotSingleWord(getLastName())) {
            isValid = false
            til_last_name.error = getString(com.tokopedia.flight.R.string.flight_booking_passenger_last_name_single_word_error)
        } else if (flightPassengerInfoValidator.validateNameIsNotAlphabetAndSpaceOnly(getLastName())) {
            isValid = false
            til_last_name.error = getString(com.tokopedia.flight.R.string.flight_booking_passenger_last_name_alpha_space_error)
        }

        if (flightPassengerInfoValidator.validateTitleIsEmpty(getPassengerTitle())) {
            isValid = false
            showMessageErrorInSnackBar(com.tokopedia.flight.R.string.flight_bookingpassenger_title_error)
        }

        if ((isChildPassenger() || isInfantPassenger()) && !flightPassengerInfoValidator.validateBirthdateNotEmpty(getPassengerBirthDate())) {
            isValid = false
            birthdate_helper_text.visibility = View.GONE
            til_birth_date.error = getString(R.string.flight_booking_passenger_birthdate_empty_error)
        } else if (isAdultPassenger() && !flightPassengerInfoValidator.validateBirthdateNotEmpty(
                        getPassengerBirthDate()) && (isMandatoryDoB() || !isDomestic)) {
            isValid = false
            birthdate_helper_text.visibility = View.GONE
            til_birth_date.error = getString(R.string.flight_booking_passenger_birthdate_empty_error)
        } else if (isAdultPassenger() && flightPassengerInfoValidator.validateBirthdateNotEmpty(
                        getPassengerBirthDate()) && (isMandatoryDoB() || !isDomestic) &&
                flightPassengerInfoValidator.validateDateMoreThan(getPassengerBirthDate(), twelveYearsAgo)) {
            isValid = false
            birthdate_helper_text.visibility = View.GONE
            til_birth_date.error = getString(R.string.flight_booking_passenger_birthdate_adult_shoud_more_than_twelve_years)
        } else if (isChildPassenger() && flightPassengerInfoValidator.validateDateMoreThan(
                        getPassengerBirthDate(), twoYearsAgo)) {
            isValid = false
            birthdate_helper_text.visibility = View.GONE
            til_birth_date.error = getString(R.string.flight_booking_passenger_birthdate_child_shoud_more_than_two_years)
        } else if (isChildPassenger() && flightPassengerInfoValidator.validateDateNotLessThan(
                        twelveYearsAgo,
                        getPassengerBirthDate())) {
            isValid = false
            til_birth_date.error = getString(com.tokopedia.flight.R.string.flight_booking_passenger_birthdate_child_sholud_lessthan_than_equal_12years)
        } else if (isInfantPassenger() && flightPassengerInfoValidator.validateDateLessThan(
                        getPassengerBirthDate(), twoYearsAgo)) {
            isValid = false
            birthdate_helper_text.visibility = View.GONE
            til_birth_date.error = getString(R.string.flight_booking_passenger_birthdate_infant_should_no_more_than_two_years)
        }

        if (isNeedPassport && !flightPassengerInfoValidator.validatePassportNumberNotEmpty(getPassportNumber())) {
            isValid = false
            til_passport_no.error = getString(com.tokopedia.flight.R.string.flight_booking_passport_number_empty_error)
        } else if (isNeedPassport && !flightPassengerInfoValidator.validatePassportNumberAlphaNumeric(getPassportNumber())) {
            isValid = false
            til_passport_no.error = getString(com.tokopedia.flight.R.string.flight_booking_passport_number_alphanumeric_error)
        }
        if (isNeedPassport && passengerModel.passportExpiredDate == null) {
            isValid = false
            passport_expiration_helper_text.visibility = View.GONE
            til_passport_expiration_date.error = getString(R.string.flight_booking_passport_expired_date_empty_error)
        } else if (isNeedPassport && !flightPassengerInfoValidator.validateExpiredDateOfPassportAtLeast6Month(
                        getPassportExpiryDate(), sixMonthFromDeparture)) {
            isValid = false
            passport_expiration_helper_text.visibility = View.GONE
            til_passport_expiration_date.error = getString(
                    com.tokopedia.flight.R.string.flight_passenger_passport_expired_date_less_than_6_month_error,
                    FlightDateUtil.dateToString(sixMonthFromDeparture, FlightDateUtil.DEFAULT_VIEW_FORMAT))
        } else if (isNeedPassport && !flightPassengerInfoValidator.validateExpiredDateOfPassportMax20Years(
                        getPassportExpiryDate(), twentyYearsFromToday)) {
            isValid = false
            passport_expiration_helper_text.visibility = View.GONE
            til_passport_expiration_date.error = getString(
                    com.tokopedia.flight.R.string.flight_passenger_passport_expired_date_more_than_20_year_error,
                    FlightDateUtil.dateToString(twentyYearsFromToday, FlightDateUtil.DEFAULT_VIEW_FORMAT))
        }
        if (isNeedPassport && passengerModel.passportNationality == null) {
            isValid = false
            til_nationality.error = getString(com.tokopedia.flight.R.string.flight_booking_passport_nationality_empty_error)
        }
        if (isNeedPassport && passengerModel.passportIssuerCountry == null) {
            isValid = false
            til_passport_issuer_country.error = getString(com.tokopedia.flight.R.string.flight_booking_passport_issuer_country_empty_error)
        }

        return isValid
    }

    fun resetEditTextErrorText() {
        til_first_name.error = ""
        til_last_name.error = ""
        til_birth_date.error = ""
        til_passport_no.error = ""
        til_passport_expiration_date.error = ""
        til_nationality.error = ""
        til_passport_issuer_country.error = ""
        if (isMandatoryDoB() || !isDomestic) birthdate_helper_text.visibility = View.VISIBLE
        passport_expiration_helper_text.visibility = View.VISIBLE
    }

    fun showMessageErrorInSnackBar(resId: Int) {
        view?.let {
            Toaster.showErrorWithAction(it, getString(resId), Snackbar.LENGTH_LONG, "OK", View.OnClickListener { /* do nothing */ })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        clearAllKeyboardFocus()
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_LUGGAGE -> {
                    if (data != null) {
                        val flightBookingLuggageMetaViewModel = data.getParcelableExtra<FlightBookingAmenityMetaViewModel>(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES)
                        renderPassengerLuggages(luggageModels, onAmenitiesDataChange(flightBookingLuggageMetaViewModel, passengerModel.flightBookingLuggageMetaViewModels))
                    }
                }

                REQUEST_CODE_PICK_MEAL -> {
                    if (data != null) {
                        val flightBookingMealMetaViewModel = data.getParcelableExtra<FlightBookingAmenityMetaViewModel>(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES)
                        renderPassengerMeals(mealModels, onAmenitiesDataChange(flightBookingMealMetaViewModel, passengerModel.flightBookingMealMetaViewModels))
                    }
                }

                REQUEST_CODE_PICK_NATIONALITY -> {
                    if (data != null) {
                        val flightPassportNationalityViewModel = data.getParcelableExtra<TravelCountryPhoneCode>(FlightBookingNationalityFragment.EXTRA_SELECTED_COUNTRY)
                        onNationalityChanged(flightPassportNationalityViewModel)
                    }
                }

                REQUEST_CODE_PICK_ISSUER_COUNTRY -> {
                    if (data != null) {
                        val flightPassportIssuerCountry = data.getParcelableExtra<TravelCountryPhoneCode>(FlightBookingNationalityFragment.EXTRA_SELECTED_COUNTRY)
                        onIssuerCountryChanged(flightPassportIssuerCountry)
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

}