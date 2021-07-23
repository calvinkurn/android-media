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
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common.travel.widget.filterchips.FilterChipAdapter
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightPassengerInfoValidator
import com.tokopedia.flight.common.view.enum.FlightPassengerTitle
import com.tokopedia.flight.detail.view.adapter.FlightSimpleAdapter
import com.tokopedia.flight.detail.view.model.SimpleModel
import com.tokopedia.flight.passenger.constant.FlightBookingPassenger
import com.tokopedia.flight.passenger.di.FlightPassengerComponent
import com.tokopedia.flight.passenger.view.activity.FlightBookingAmenityActivity
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_AUTOFILL_NAME
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_DEPARTURE_DATE
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_DEPATURE
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_IS_AIRASIA
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_IS_DOMESTIC
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_LUGGAGES
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_MEALS
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_PASSENGER
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_REQUEST_ID
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity.Companion.EXTRA_RETURN
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityMetaModel
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityModel
import com.tokopedia.flight.passenger.view.model.FlightBookingPassengerModel
import com.tokopedia.flight.passenger.view.viewmodel.FlightPassengerViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travel.country_code.presentation.activity.PhoneCodePickerActivity
import com.tokopedia.travel.country_code.presentation.fragment.PhoneCodePickerFragment
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.travel.country_code.util.TravelCountryCodeGqlQuery
import com.tokopedia.travel.passenger.data.entity.TravelContactIdCard
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.data.entity.TravelUpsertContactModel
import com.tokopedia.travel.passenger.presentation.adapter.TravelContactArrayAdapter
import com.tokopedia.travel.passenger.util.TravelPassengerGqlMutation
import com.tokopedia.travel.passenger.util.TravelPassengerGqlQuery
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString
import kotlinx.android.synthetic.main.fragment_flight_booking_passenger.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * @author by jessica on 2019-09-05
 */

class FlightBookingPassengerFragment : BaseDaggerFragment() {

    lateinit var passengerModel: FlightBookingPassengerModel
    lateinit var luggageModels: List<FlightBookingAmenityMetaModel>
    lateinit var mealModels: List<FlightBookingAmenityMetaModel>
    var isAirAsiaAirlines: Boolean = false
    lateinit var depatureDate: String
    lateinit var requestId: String
    var isDomestic: Boolean = false
    var returnId: String? = null
    var autofillName: String = ""

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
            passengerModel = it.getParcelable(EXTRA_PASSENGER) ?: FlightBookingPassengerModel()
            luggageModels = it.getParcelableArrayList(EXTRA_LUGGAGES) ?: arrayListOf()
            mealModels = it.getParcelableArrayList(EXTRA_MEALS) ?: arrayListOf()
            isAirAsiaAirlines = it.getBoolean(EXTRA_IS_AIRASIA)
            depatureDate = it.getString(EXTRA_DEPARTURE_DATE, "")
            requestId = it.getString(EXTRA_REQUEST_ID, "")
            isDomestic = it.getBoolean(EXTRA_IS_DOMESTIC)
            autofillName = it.getString(EXTRA_AUTOFILL_NAME, "")
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

        if (autofillName.isNotEmpty()) loading_screen.show() else loading_screen.hide()
        passengerViewModel.getContactList(TravelPassengerGqlQuery.CONTACT_LIST,
                getPassengerTypeString(passengerModel.type))
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        passengerViewModel.contactListResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer { contactList ->
            contactList?.let {
                travelContactArrayAdapter.updateItem(it.toMutableList())
                if (autofillName.isNotEmpty()) {
                    for ((index, item) in it.withIndex()) {
                        if (item.fullName.equals(autofillName, false)) {
                            autofillPassengerContact(item)
                            break
                        }
                    }
                    loading_screen.hide()
                }
            }
        })

        passengerViewModel.nationalityData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { value ->
            value?.let { onNationalityChanged(it) }
        })

        passengerViewModel.passportIssuerCountryData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { value ->
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

            til_birth_date.textFieldInput.isFocusable = false
            til_birth_date.textFieldInput.isClickable = true
            til_birth_date.textFieldInput.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    MethodChecker.getDrawable(requireContext(), com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_right_grayscale_24), null)
            til_birth_date.textFieldInput.setOnClickListener { onBirthdateClicked() }

            til_passport_expiration_date.textFieldInput.isFocusable = false
            til_passport_expiration_date.textFieldInput.isClickable = true
            til_passport_expiration_date.textFieldInput.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    MethodChecker.getDrawable(requireContext(), com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_right_grayscale_24), null)
            til_passport_expiration_date.textFieldInput.setOnClickListener { onPassportExpiredClicked() }

            til_nationality.textFieldInput.isFocusable = false
            til_nationality.textFieldInput.isClickable = true
            til_nationality.textFieldInput.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    MethodChecker.getDrawable(requireContext(), com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_right_grayscale_24), null)
            til_nationality.textFieldInput.setOnClickListener {
                startActivityForResult(PhoneCodePickerActivity.getCallingIntent(requireContext(),
                        getString(com.tokopedia.flight.R.string.flight_nationality_search_hint)), REQUEST_CODE_PICK_NATIONALITY)
            }

            til_passport_issuer_country.textFieldInput.isFocusable = false
            til_passport_issuer_country.textFieldInput.isClickable = true
            til_passport_issuer_country.textFieldInput.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    MethodChecker.getDrawable(requireContext(), com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_right_grayscale_24), null)
            til_passport_issuer_country.textFieldInput.setOnClickListener {
                startActivityForResult(PhoneCodePickerActivity.getCallingIntent(requireContext(),
                        getString(com.tokopedia.flight.R.string.flight_passport_search_hint)), REQUEST_CODE_PICK_ISSUER_COUNTRY)
            }

            til_first_name.setErrorTextAppearance(com.tokopedia.common.travel.R.style.ErrorTextAppearance)

            fragment_layout.setOnTouchListener { _, _ ->
                clearAllKeyboardFocus()
                true
            }

            rv_passenger_title.selectOnlyOneChip(true)
            rv_passenger_title.canDiselectAfterSelect(false)
        }
    }

    private fun initFirstNameAutoCompleteTv(context: Context) {
        travelContactArrayAdapter = TravelContactArrayAdapter(context,
                com.tokopedia.travel.passenger.R.layout.layout_travel_passenger_autocompletetv, arrayListOf(),
                object : TravelContactArrayAdapter.ContactArrayListener {
                    override fun getFilterText(): String {
                        return et_first_name.text.toString()
                    }
                })
        (et_first_name as AutoCompleteTextView).setAdapter(travelContactArrayAdapter)
        (et_first_name as AutoCompleteTextView).onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ -> autofillPassengerContact(travelContactArrayAdapter.getItem(position)) }
    }

    private fun onSubmitData() {
        if (validateAllFields()) {
            passengerModel.passengerTitle = getPassengerTitle()
            passengerModel.passengerTitleId = getPassengerTitleId(getPassengerTitle())
            passengerModel.passengerFirstName = getFirstName()
            passengerModel.passengerLastName = getLastName()
            if (isMandatoryDoB() || !isDomestic) passengerModel.passengerBirthdate = DateUtil
                    .formatDate(DateUtil.DEFAULT_VIEW_FORMAT, DateUtil.YYYY_MM_DD, getPassengerBirthDate())
            if (!isDomestic) {
                passengerModel.passportNumber = getPassportNumber()
                passengerModel.passportExpiredDate = DateUtil
                        .formatDate(DateUtil.DEFAULT_VIEW_FORMAT, DateUtil.YYYY_MM_DD, getPassportExpiryDate())
            }

            upsertContactList()
            finishActivityWithData()
        }
    }

    private fun upsertContactList() {
        val contact = TravelUpsertContactModel.Contact()
        contact.firstName = getFirstName()
        contact.lastName = getLastName()
        contact.title = getPassengerTitle()
        if (isMandatoryDoB() || !isDomestic)
            contact.birthDate = DateUtil.formatDate(DateUtil.DEFAULT_VIEW_FORMAT,
                    DateUtil.YYYY_MM_DD, getPassengerBirthDate())
        if (!isDomestic) {
            contact.nationality = passengerModel.passportNationality?.countryId ?: "ID"
            contact.idList = listOf(TravelContactIdCard(type = "passport", title = "Paspor",
                    number = getPassportNumber(), country = passengerModel.passportIssuerCountry?.countryId
                    ?: "ID",
                    expiry = DateUtil.formatDate(DateUtil.DEFAULT_VIEW_FORMAT, DateUtil.YYYY_MM_DD, getPassportExpiryDate())))
        }

        passengerViewModel.updateContactList(TravelPassengerGqlMutation.UPSERT_CONTACT, contact)
    }

    private fun finishActivityWithData() {
        activity?.run {
            intent.putExtra(EXTRA_PASSENGER, passengerModel)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun getFirstName(): String = et_first_name.text.toString()

    private fun getLastName(): String = til_last_name.textFieldInput.text.toString()

    private fun getPassengerTitle(): String = rv_passenger_title.getFirstSelectedItem()

    private fun getPassengerBirthDate(): String = til_birth_date.textFieldInput.text.toString().trim()

    private fun getPassportExpiryDate(): String = til_passport_expiration_date.textFieldInput.text.toString().trim()

    private fun getPassportNumber(): String = til_passport_no.textFieldInput.text.toString().trim()

    private fun renderPassengerData() {
        if (passengerModel.passengerFirstName.isNotEmpty()) {
            et_first_name.setText(passengerModel.passengerFirstName)
            til_last_name.textFieldInput.setText(passengerModel.passengerLastName)
        }

        if (passengerModel.passengerBirthdate.isNotEmpty())
            til_birth_date.textFieldInput.setText(DateUtil.formatDate(DateUtil.YYYY_MM_DD,
                    DateUtil.DEFAULT_VIEW_FORMAT, passengerModel.passengerBirthdate))

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
    }

    private fun renderPassengerTitle(passengerTitle: String) {
        when {
            passengerTitle.equals(FlightPassengerTitle.TUAN.salutation, true) -> rv_passenger_title.selectChipByPosition(0)
            passengerTitle.equals(FlightPassengerTitle.NYONYA.salutation, true) -> rv_passenger_title.selectChipByPosition(1)
            passengerTitle.equals(FlightPassengerTitle.NONA.salutation, true) -> rv_passenger_title.selectChipByPosition(2)
            else -> rv_passenger_title.onResetChip()
        }
    }

    private fun onPassportExpiredClicked() {

        val minDate: Date
        var selectedDate: Date

        val tmpDepatureDate = depatureDate.toDate(DateUtil.YYYY_MM_DD)
        minDate = tmpDepatureDate.addTimeToSpesificDate(Calendar.MONTH, PLUS_SIX)
        val maxDate: Date = DateUtil.getCurrentDate().addTimeToSpesificDate(Calendar.YEAR, PLUS_TWENTY)
        selectedDate = minDate

        if (getPassportExpiryDate().isNotBlank()) {
            selectedDate = getPassportExpiryDate().toDate(DateUtil.DEFAULT_VIEW_FORMAT)
        }

        showCalendarPickerDialog(selectedDate, minDate, maxDate, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val calendar = DateUtil.getCurrentCalendar()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DATE, dayOfMonth)
            val passportExpiryDate = calendar.time

            val passportExpiryDateStr = passportExpiryDate.toString(DateUtil.DEFAULT_VIEW_FORMAT)
            passengerModel.passportExpiredDate = DateUtil.formatDate(DateUtil.DEFAULT_VIEW_FORMAT,
                    DateUtil.YYYY_MM_DD, passportExpiryDateStr)
            til_passport_expiration_date.textFieldInput.setText(passportExpiryDateStr)
            validatePassportExpiredDate(true)
        })
    }

    private fun clearAllKeyboardFocus() {
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    private fun renderPassengerMeals(flightBookingMealRouteModels: List<FlightBookingAmenityMetaModel>,
                                     selecteds: List<FlightBookingAmenityMetaModel>) {
        meals_container.visibility = View.VISIBLE

        var models = arrayListOf<SimpleModel>()
        if (flightBookingMealRouteModels != null) {
            for (meal in flightBookingMealRouteModels) {
                val simpleModel = SimpleModel(meal.description, getString(R.string.flight_booking_passenger_choose_label))
                for (selected in selecteds) {
                    if (selected.key.equals(meal.key, true)) {
                        val selectedMeals = arrayListOf<String>()
                        for (amenity in selected.amenities) {
                            selectedMeals.add("${amenity.title} - ${amenity.price}")
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
        mealAdapter.setFontSize(resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.fontSize_lvl2))
        mealAdapter.setInteractionListener(object : FlightSimpleAdapter.OnAdapterInteractionListener {
            override fun onItemClick(adapterPosition: Int, viewModel: SimpleModel) {
                val meal = flightBookingMealRouteModels[adapterPosition]
                var existingSelected: FlightBookingAmenityMetaModel? = null

                for (passengerMeal in passengerModel.flightBookingMealMetaViewModels) {
                    if (passengerMeal.key.equals(meal.key, true)) {
                        existingSelected = passengerMeal
                        break
                    }
                }

                if (existingSelected == null) {
                    existingSelected = FlightBookingAmenityMetaModel()
                    existingSelected.key = meal.key
                    existingSelected.journeyId = meal.journeyId
                    existingSelected.arrivalId = meal.arrivalId
                    existingSelected.departureId = meal.departureId
                    existingSelected.amenities = arrayListOf()
                    existingSelected.description = meal.description
                }

                navigateToMealPicker(meal.amenities.toMutableList(), existingSelected)
            }
        })

        rv_meals.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rv_meals.setHasFixedSize(true)
        rv_meals.isNestedScrollingEnabled = false
        rv_meals.adapter = mealAdapter
        mealAdapter.setDescriptionTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500))
        mealAdapter.setViewModels(models)
        mealAdapter.notifyDataSetChanged()
    }

    fun navigateToMealPicker(meals: MutableList<FlightBookingAmenityModel>, selected: FlightBookingAmenityMetaModel) {
        val title = String.format("%s %s", getString(com.tokopedia.flight.R.string.flight_booking_meal_toolbar_title), selected.description)
        val intent = FlightBookingAmenityActivity.createIntent(activity, title, meals, selected)
        startActivityForResult(intent, REQUEST_CODE_PICK_MEAL)
    }

    private fun navigateToLuggagePicker(luggages: MutableList<FlightBookingAmenityModel>, selected: FlightBookingAmenityMetaModel) {
        val title = String.format("%s %s", getString(com.tokopedia.flight.R.string.flight_booking_luggage_toolbar_title), selected.description)
        val intent = FlightBookingAmenityActivity.createIntent(activity, title, luggages, selected)
        startActivityForResult(intent, REQUEST_CODE_PICK_LUGGAGE)
    }

    private fun renderPassengerLuggages(flightBookingLuggageRouteModels: List<FlightBookingAmenityMetaModel>,
                                        selecteds: List<FlightBookingAmenityMetaModel>) {
        luggage_container.visibility = View.VISIBLE

        val models = arrayListOf<SimpleModel>()
        for (luggage in flightBookingLuggageRouteModels) {
            val model = SimpleModel(luggage.description, getString(R.string.flight_booking_passenger_choose_label))
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

        val luggageAdapter = FlightSimpleAdapter()
        luggageAdapter.setMarginTopDp(resources.getDimension(com.tokopedia.flight.R.dimen.margin_4))
        luggageAdapter.setMarginBottomDp(resources.getDimension(com.tokopedia.flight.R.dimen.margin_4))
        luggageAdapter.setArrowVisible(true)
        luggageAdapter.setFontSize(resources.getDimension(com.tokopedia.unifycomponents.R.dimen.fontSize_lvl2))
        luggageAdapter.setInteractionListener(object : FlightSimpleAdapter.OnAdapterInteractionListener {
            override fun onItemClick(adapterPosition: Int, viewModel: SimpleModel) {
                val luggage = flightBookingLuggageRouteModels[adapterPosition]

                var existingSelected: FlightBookingAmenityMetaModel? = null
                for (selected in passengerModel.flightBookingLuggageMetaViewModels) {
                    if (selected.key.equals(luggage.key, true)) existingSelected = selected
                }
                if (existingSelected == null) {
                    existingSelected = FlightBookingAmenityMetaModel()
                    existingSelected.key = luggage.key
                    existingSelected.journeyId = luggage.journeyId
                    existingSelected.arrivalId = luggage.arrivalId
                    existingSelected.departureId = luggage.departureId
                    existingSelected.description = luggage.description
                    existingSelected.amenities = arrayListOf()
                }
                navigateToLuggagePicker(luggage.amenities.toMutableList(), existingSelected)
            }
        })

        rv_luggages.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rv_luggages.setHasFixedSize(true)
        rv_luggages.isNestedScrollingEnabled = false
        rv_luggages.adapter = luggageAdapter
        luggageAdapter.setDescriptionTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500))
        luggageAdapter.setViewModels(models)
        luggageAdapter.notifyDataSetChanged()

    }

    private fun renderPassport() {
        if (!isDomestic) {
            container_passport_data.visibility = View.VISIBLE
            passengerModel.passportNumber?.let {
                til_passport_no.textFieldInput.setText(it)
            }
            passengerModel.passportExpiredDate?.let {
                til_passport_expiration_date.textFieldInput.setText(DateUtil
                        .formatDate(DateUtil.YYYY_MM_DD,
                                DateUtil.DEFAULT_VIEW_FORMAT,
                                it))
            }
            passengerModel.passportNationality?.let {
                til_nationality.textFieldInput.setText(it.countryName)
            }
            passengerModel.passportIssuerCountry?.let {
                til_passport_issuer_country.textFieldInput.setText(it.countryName)
            }
        } else {
            container_passport_data.visibility = View.GONE
        }
    }

    private fun renderViewBasedOnType() {
        if (isAdultPassenger()) {
            (activity as FlightBookingPassengerActivity).updateTitle(getString(R.string.flight_booking_passenger_adult_title))
            if (isMandatoryDoB() || !isDomestic) {
                til_birth_date.visibility = View.VISIBLE
            } else {
                til_birth_date.visibility = View.GONE
            }

            til_birth_date.setMessage(getString(R.string.flight_booking_passenger_birthdate_adult_helper_text))
        } else {
            if (isChildPassenger()) {
                (activity as FlightBookingPassengerActivity).updateTitle(getString(R.string.flight_booking_passenger_child_title))
                til_birth_date.setMessage(getString(R.string.flight_booking_passenger_birthdate_child_helper_text))
            } else {
                (activity as FlightBookingPassengerActivity).updateTitle(getString(R.string.flight_booking_passenger_infant_title))
                til_birth_date.setMessage(getString(R.string.flight_booking_passenger_birthdate_infant_helper_text))
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

    private fun onBirthdateClicked() {
        lateinit var maxDate: Date
        var minDate: Date? = null
        lateinit var selectedDate: Date
        val departureDate = depatureDate.toDate(DateUtil.YYYY_MM_DD)

        when {
            isChildPassenger() -> {
                minDate = departureDate.addTimeToSpesificDate(Calendar.YEAR, MINUS_TWELVE)
                minDate = minDate.addTimeToSpesificDate(Calendar.DATE, PLUS_ONE)
                maxDate = departureDate.addTimeToSpesificDate(Calendar.YEAR, MINUS_TWO)
                selectedDate = maxDate
            }
            isAdultPassenger() -> {
                maxDate = departureDate.addTimeToSpesificDate(Calendar.YEAR, MINUS_TWELVE)
                selectedDate = maxDate
            }
            else -> {
                minDate = departureDate.addTimeToSpesificDate(Calendar.YEAR, MINUS_TWO)
                minDate = minDate.addTimeToSpesificDate(Calendar.DATE, PLUS_ONE)
                maxDate = DateUtil.getCurrentDate()
                selectedDate = maxDate
            }
        }

        if (til_birth_date.textFieldInput.text.toString().isNotEmpty()) selectedDate = til_birth_date.textFieldInput.text.toString().toDate(DateUtil.DEFAULT_VIEW_FORMAT)

        val currentTime = DateUtil.getCurrentCalendar()
        currentTime.time = maxDate
        currentTime.set(Calendar.HOUR_OF_DAY, DEFAULT_LAST_HOUR_IN_DAY)
        currentTime.set(Calendar.MINUTE, DEFAULT_LAST_MIN_IN_DAY)
        currentTime.set(Calendar.SECOND, DEFAULT_LAST_SEC_IN_DAY)

        var onDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val calendar = DateUtil.getCurrentCalendar()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DATE, dayOfMonth)
            val birthDate = calendar.time

            val birthDateStr = birthDate.toString(DateUtil.DEFAULT_VIEW_FORMAT)
            if (validateBirthDate(birthDateStr)) {
                passengerModel.passengerBirthdate = DateUtil.formatDate(DateUtil.DEFAULT_VIEW_FORMAT,
                        DateUtil.YYYY_MM_DD, birthDateStr)
                til_birth_date.textFieldInput.setText(birthDateStr)
            }
        }

        if (minDate != null) showCalendarPickerDialog(selectedDate = selectedDate, minDate = minDate, maxDate = currentTime.time, onDateSetListener = onDateSetListener)
        else showCalendarPickerDialog(selectedDate = selectedDate, maxDate = currentTime.time, onDateSetListener = onDateSetListener)
    }

    private fun showCalendarPickerDialog(selectedDate: Date, minDate: Date? = null, maxDate: Date, onDateSetListener: DatePickerDialog.OnDateSetListener) {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        val datePicker = DatePickerDialog(requireActivity(), onDateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        val datePicker1 = datePicker.datePicker
        if (minDate != null) datePicker1.minDate = minDate.time
        datePicker1.maxDate = maxDate.time
        datePicker.show()
    }

    private fun clearAllFields() {
        et_first_name.setText("")
        til_last_name.textFieldInput.setText("")
        renderPassengerTitle("")
        til_birth_date.textFieldInput.setText("")
        til_passport_no.textFieldInput.setText("")
        til_passport_issuer_country.textFieldInput.setText("")
        til_passport_expiration_date.textFieldInput.setText("")
        til_nationality.textFieldInput.setText("")
    }

    private fun autofillPassengerContact(contact: TravelContactListModel.Contact?) {
        clearAllFields()
        if (contact != null) {
            if (contact.firstName.isNotBlank()) {
                passengerModel.passengerFirstName = contact.firstName
                et_first_name.setText(contact.firstName)
            }
            if (contact.lastName.isNotBlank()) {
                passengerModel.passengerLastName = contact.lastName
                til_last_name.textFieldInput.setText(contact.lastName)
            }
            if (contact.title.isNotBlank()) {
                passengerModel.passengerTitleId = getPassengerTitleId(contact.title.toLowerCase())
                passengerModel.passengerTitle = contact.title
                renderPassengerTitle(contact.title.toLowerCase())
            }
            if (contact.birthDate.isNotBlank() && (!isDomestic || isMandatoryDoB())) {
                passengerModel.passengerBirthdate = contact.birthDate
                til_birth_date.textFieldInput.setText(DateUtil.formatDate(DateUtil.YYYY_MM_DD,
                        DateUtil.DEFAULT_VIEW_FORMAT, contact.birthDate))
            }

            if (contact.idList.isNotEmpty() && !isDomestic) {
                for (id in contact.idList) {
                    if (isPassportId(id)) {
                        passengerModel.passportNumber = id.number
                        passengerModel.passportExpiredDate = id.expiry
                        til_passport_no.textFieldInput.setText(id.number)
                        til_passport_expiration_date.textFieldInput.setText(DateUtil.formatDate(DateUtil.YYYY_MM_DD,
                                DateUtil.DEFAULT_VIEW_FORMAT, id.expiry))
                        passengerViewModel.getNationalityById(TravelCountryCodeGqlQuery.ALL_COUNTRY, contact.nationality)
                        passengerViewModel.getPassportIssuerCountryById(TravelCountryCodeGqlQuery.ALL_COUNTRY, id.country)
                        break
                    }
                }
            }
        }
    }

    fun getPassengerTitleId(passengerTitle: String): Int {
        return when {
            passengerTitle.equals(FlightPassengerTitle.TUAN.salutation, true) -> FlightPassengerTitle.TUAN.id
            passengerTitle.equals(FlightPassengerTitle.NYONYA.salutation, true) -> FlightPassengerTitle.NYONYA.id
            else -> FlightPassengerTitle.NONA.id
        }
    }

    private fun getPassengerTypeString(passengerType: Int): String = when (passengerType) {
        FlightBookingPassenger.ADULT.value -> "adult"
        FlightBookingPassenger.CHILDREN.value -> "child"
        FlightBookingPassenger.INFANT.value -> "infant"
        else -> ""
    }

    private fun isAdultPassenger(): Boolean = passengerModel.type == FlightBookingPassenger.ADULT.value

    private fun isChildPassenger(): Boolean = passengerModel.type == FlightBookingPassenger.CHILDREN.value

    private fun isInfantPassenger(): Boolean = passengerModel.type == FlightBookingPassenger.INFANT.value

    private fun isMandatoryDoB(): Boolean = isAirAsiaAirlines

    private fun isPassportId(id: TravelContactIdCard): Boolean = id.type.equals("passport", true)

    private fun onAmenitiesDataChange(flightBookingLuggageMetaModel: FlightBookingAmenityMetaModel, passengerModelAmenities: MutableList<FlightBookingAmenityMetaModel>): List<FlightBookingAmenityMetaModel> {
        val index = passengerModelAmenities.indexOf(flightBookingLuggageMetaModel)

        if (flightBookingLuggageMetaModel.amenities.isNotEmpty()) {
            if (index != -1) {
                passengerModelAmenities[index] = flightBookingLuggageMetaModel
            } else {
                passengerModelAmenities.add(flightBookingLuggageMetaModel)
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
        til_nationality.textFieldInput.setText(flightPassportNationalityViewModel.countryName)
    }

    private fun onIssuerCountryChanged(flightPassportIssuerCountry: TravelCountryPhoneCode) {
        passengerModel.passportIssuerCountry = flightPassportIssuerCountry
        til_passport_issuer_country.textFieldInput.setText(flightPassportIssuerCountry.countryName)
    }

    private fun validateAllFields(): Boolean {
        var isValid = true

        val isNeedPassport = !isDomestic

        resetEditTextErrorText()

        if (!validateFirstName()) isValid = false
        if (!validateLastName()) isValid = false
        if (!validatePassengerTitle()) isValid = false
        if (!validateBirthDate(getPassengerBirthDate())) isValid = false
        if (!validatePassportNumber(isNeedPassport)) isValid = false
        if (!validatePassportExpiredDate(isNeedPassport)) isValid = false
        if (!validatePassportNationality(isNeedPassport)) isValid = false
        if (!validatePassportIssuerCountry(isNeedPassport)) isValid = false

        return isValid
    }

    private fun validateFirstName(): Boolean =
            when {
                flightPassengerInfoValidator.validateNameIsEmpty(getFirstName()) -> {
                    til_first_name.error = getString(R.string.flight_booking_passenger_first_name_empty_error)
                    false
                }
                flightPassengerInfoValidator.validateNameIsNotAlphabetAndSpaceOnly(getFirstName()) -> {
                    til_first_name.error = getString(R.string.flight_booking_passenger_first_name_alpha_space_error)
                    false
                }
                flightPassengerInfoValidator.validateFirstNameIsMoreThanMaxLength(getFirstName()) -> {
                    til_first_name.error = getString(R.string.flight_booking_passenger_first_name_max_error)
                    false
                }
                else -> true
            }

    private fun validateLastName(): Boolean =
            when {
                flightPassengerInfoValidator.validateLastNameIsMoreThanMaxLength(getLastName()) -> {
                    til_last_name.setMessage(getString(R.string.flight_booking_passenger_last_name_max_error))
                    til_last_name.setError(true)
                    false
                }
                flightPassengerInfoValidator.validateNameIsEmpty(getLastName()) -> {
                    til_last_name.setMessage(getString(R.string.flight_booking_passenger_last_name_should_same_error))
                    til_last_name.setError(true)
                    false
                }
                flightPassengerInfoValidator.validateLastNameIsLessThanMinLength(getLastName()) -> {
                    til_last_name.setMessage(getString(R.string.flight_booking_passenger_last_name_empty_error))
                    til_last_name.setError(true)
                    false
                }
                flightPassengerInfoValidator.validateLastNameIsNotSingleWord(getLastName()) -> {
                    til_last_name.setMessage(getString(R.string.flight_booking_passenger_last_name_single_word_error))
                    til_last_name.setError(true)
                    false
                }
                flightPassengerInfoValidator.validateNameIsNotAlphabetAndSpaceOnly(getLastName()) -> {
                    til_last_name.setMessage(getString(R.string.flight_booking_passenger_last_name_alpha_space_error))
                    til_last_name.setError(true)
                    false
                }
                else -> true
            }

    private fun validatePassengerTitle(): Boolean =
            if (flightPassengerInfoValidator.validateTitleIsEmpty(getPassengerTitle())) {
                showMessageErrorInSnackBar(R.string.flight_bookingpassenger_title_error)
                false
            } else {
                true
            }

    private fun validateBirthDate(passengerBirthDate: String): Boolean {
        val twelveYearsAgo = depatureDate.toDate(DateUtil.YYYY_MM_DD)
                .addTimeToSpesificDate(Calendar.YEAR, MINUS_TWELVE)
                .addTimeToSpesificDate(Calendar.DATE, PLUS_ONE)
        val twoYearsAgo = depatureDate.toDate(DateUtil.YYYY_MM_DD)
                .addTimeToSpesificDate(Calendar.YEAR, MINUS_TWO)

        return if ((isChildPassenger() || isInfantPassenger()) && !flightPassengerInfoValidator.validateBirthdateNotEmpty(passengerBirthDate)) {

            til_birth_date.setMessage(getString(R.string.flight_booking_passenger_birthdate_empty_error))
            til_birth_date.setError(true)

            false
        } else if (isAdultPassenger() && !flightPassengerInfoValidator.validateBirthdateNotEmpty(
                        passengerBirthDate) && (isMandatoryDoB() || !isDomestic)) {
            til_birth_date.setMessage(getString(R.string.flight_booking_passenger_birthdate_empty_error))
            til_birth_date.setError(true)
            false
        } else if (isAdultPassenger() && flightPassengerInfoValidator.validateBirthdateNotEmpty(
                        passengerBirthDate) && (isMandatoryDoB() || !isDomestic) &&
                flightPassengerInfoValidator.validateDateMoreThan(passengerBirthDate, twelveYearsAgo)) {

            til_birth_date.setMessage(getString(R.string.flight_booking_passenger_birthdate_adult_shoud_more_than_twelve_years))
            til_birth_date.setError(true)
            false

        } else if (isChildPassenger() && flightPassengerInfoValidator.validateDateMoreThan(
                        passengerBirthDate, twoYearsAgo)) {
            til_birth_date.setMessage(getString(R.string.flight_booking_passenger_birthdate_child_shoud_more_than_two_years))
            til_birth_date.setError(true)
            false
        } else if (isChildPassenger() && flightPassengerInfoValidator.validateDateNotLessThan(
                        twelveYearsAgo,
                        passengerBirthDate)) {
            til_birth_date.setMessage(getString(R.string.flight_booking_passenger_birthdate_child_sholud_lessthan_than_equal_12years))
            til_birth_date.setError(true)
            false
        } else if (isInfantPassenger() && flightPassengerInfoValidator.validateDateLessThan(
                        passengerBirthDate, twoYearsAgo)) {
            til_birth_date.setMessage(getString(R.string.flight_booking_passenger_birthdate_infant_should_no_more_than_two_years))
            til_birth_date.setError(true)
            false
        } else {
            true
        }
    }

    private fun validatePassportNumber(isNeedPassport: Boolean): Boolean =
            if (isNeedPassport && !flightPassengerInfoValidator.validatePassportNumberNotEmpty(getPassportNumber())) {
                til_passport_no.setMessage(getString(R.string.flight_booking_passport_number_empty_error))
                til_passport_no.setError(true)
                false
            } else if (isNeedPassport && !flightPassengerInfoValidator.validatePassportNumberAlphaNumeric(getPassportNumber())) {
                til_passport_no.setMessage(getString(R.string.flight_booking_passport_number_alphanumeric_error))
                til_passport_no.setError(true)
                false
            } else if (isNeedPassport && !flightPassengerInfoValidator.validatePassportNumberAlphaAndNumeric(getPassportNumber())) {
                til_passport_no.setMessage(getString(R.string.flight_booking_passport_number_not_valid))
                til_passport_no.setError(true)
                false
            } else if (isNeedPassport && getPassportNumber().length > 10) {
                til_passport_no.setMessage(getString(R.string.flight_booking_passport_number_not_valid))
                til_passport_no.setError(true)
                false
            } else if (isNeedPassport && getPassportNumber().length < 6) {
                til_passport_no.setMessage(getString(R.string.flight_booking_passport_number_min_length))
                til_passport_no.setError(true)
                false
            } else {
                true
            }

    private fun validatePassportExpiredDate(isNeedPassport: Boolean): Boolean {
        val sixMonthFromDeparture = depatureDate.toDate(DateUtil.YYYY_MM_DD)
                .addTimeToSpesificDate(Calendar.MONTH, PLUS_SIX)
        val twentyYearsFromToday = DateUtil.getCurrentDate()
                .addTimeToSpesificDate(Calendar.YEAR, PLUS_TWENTY)

        return if (isNeedPassport && passengerModel.passportExpiredDate == null) {
            til_passport_expiration_date.setMessage(getString(R.string.flight_booking_passport_expired_date_empty_error))
            til_passport_expiration_date.setError(true)
            false
        } else if (isNeedPassport && !flightPassengerInfoValidator.validateExpiredDateOfPassportAtLeast6Month(
                        getPassportExpiryDate(), sixMonthFromDeparture)) {
            til_passport_expiration_date.setMessage(getString(R.string.flight_passenger_passport_expired_date_less_than_6_month_error))
            til_passport_expiration_date.setError(true)
            false
        } else if (isNeedPassport && !flightPassengerInfoValidator.validateExpiredDateOfPassportMax20Years(
                        getPassportExpiryDate(), twentyYearsFromToday)) {
            til_passport_expiration_date.setMessage(getString(
                    R.string.flight_passenger_passport_expired_date_more_than_20_year_error,
                    twentyYearsFromToday.toString(DateUtil.DEFAULT_VIEW_FORMAT)))
            til_passport_expiration_date.setError(true)
            false
        } else {
            til_passport_expiration_date.setError(false)
            true
        }
    }

    private fun validatePassportNationality(isNeedPassport: Boolean): Boolean =
            if (isNeedPassport && passengerModel.passportNationality == null) {
                til_nationality.setMessage(getString(R.string.flight_booking_passport_nationality_empty_error))
                til_nationality.setError(true)
                false
            } else {
                true
            }

    private fun validatePassportIssuerCountry(isNeedPassport: Boolean): Boolean =
            if (isNeedPassport && passengerModel.passportIssuerCountry == null) {
                til_passport_issuer_country.setMessage(getString(R.string.flight_booking_passport_issuer_country_empty_error))
                til_passport_issuer_country.setError(true)
                false
            } else {
                true
            }

    private fun resetEditTextErrorText() {
        til_first_name.error = ""

        til_last_name.setMessage("")
        til_last_name.setError(false)

        til_birth_date.setMessage("")
        til_birth_date.setError(false)

        til_passport_no.setMessage("")
        til_passport_no.setError(false)

        til_passport_expiration_date.setMessage("")
        til_passport_expiration_date.setError(false)

        til_nationality.setMessage("")
        til_nationality.setError(false)

        til_passport_issuer_country.setMessage("")
        til_passport_issuer_country.setError(false)

        when {
            isAdultPassenger() -> {
                if (isMandatoryDoB() || !isDomestic) til_birth_date.setMessage(getString(R.string.flight_booking_passenger_birthdate_adult_helper_text))
            }
            isChildPassenger() -> {
                til_birth_date.setMessage(getString(R.string.flight_booking_passenger_birthdate_child_helper_text))
            }
            else -> {
                til_birth_date.setMessage(getString(R.string.flight_booking_passenger_birthdate_infant_helper_text))
            }
        }

        til_passport_expiration_date.setMessage(getString(R.string.flight_booking_passenger_passport_expiry_helper_text))
        til_passport_expiration_date.setError(false)
    }

    private fun showMessageErrorInSnackBar(resId: Int) {
        view?.let {
            Toaster.build(it, getString(resId), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                    "OK") { /* do nothing */ }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        clearAllKeyboardFocus()
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_LUGGAGE -> {
                    if (data != null) {
                        val flightBookingLuggageMetaViewModel = data.getParcelableExtra<FlightBookingAmenityMetaModel>(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES)
                        flightBookingLuggageMetaViewModel?.let {
                            renderPassengerLuggages(luggageModels, onAmenitiesDataChange(flightBookingLuggageMetaViewModel, passengerModel.flightBookingLuggageMetaViewModels))
                        }
                    }
                }

                REQUEST_CODE_PICK_MEAL -> {
                    if (data != null) {
                        val flightBookingMealMetaViewModel = data.getParcelableExtra<FlightBookingAmenityMetaModel>(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES)
                        flightBookingMealMetaViewModel?.let {
                            renderPassengerMeals(mealModels, onAmenitiesDataChange(flightBookingMealMetaViewModel, passengerModel.flightBookingMealMetaViewModels))
                        }
                    }
                }

                REQUEST_CODE_PICK_NATIONALITY -> {
                    if (data != null) {
                        val flightPassportNationalityViewModel = data.getParcelableExtra<TravelCountryPhoneCode>(PhoneCodePickerFragment.EXTRA_SELECTED_PHONE_CODE)
                        flightPassportNationalityViewModel?.let {
                            onNationalityChanged(it)
                        }
                    }
                }

                REQUEST_CODE_PICK_ISSUER_COUNTRY -> {
                    if (data != null) {
                        val flightPassportIssuerCountry = data.getParcelableExtra<TravelCountryPhoneCode>(PhoneCodePickerFragment.EXTRA_SELECTED_PHONE_CODE)
                        flightPassportIssuerCountry?.let {
                            onIssuerCountryChanged(it)
                        }

                    }
                }
            }
        }
    }

    companion object {

        private const val REQUEST_CODE_PICK_LUGGAGE = 1
        private const val REQUEST_CODE_PICK_MEAL = 2
        private const val REQUEST_CODE_PICK_NATIONALITY = 4
        private const val REQUEST_CODE_PICK_ISSUER_COUNTRY = 5

        private const val PLUS_ONE = 1
        private const val MINUS_TWO = -2
        private const val MINUS_TWELVE = -12
        private const val PLUS_SIX = 6
        private const val PLUS_TWENTY = 20

        private const val DEFAULT_LAST_HOUR_IN_DAY = 23
        private const val DEFAULT_LAST_MIN_IN_DAY = 59
        private const val DEFAULT_LAST_SEC_IN_DAY = 59

        fun newInstance(depatureId: String,
                        passengerModel: FlightBookingPassengerModel,
                        luggageModels: List<FlightBookingAmenityMetaModel>,
                        mealModels: List<FlightBookingAmenityMetaModel>,
                        isAirAsiaAirlines: Boolean,
                        depatureDate: String,
                        requestId: String,
                        isDomestic: Boolean,
                        returnId: String? = null,
                        autofillName: String = ""): FlightBookingPassengerFragment {
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
            bundle.putString(EXTRA_AUTOFILL_NAME, autofillName)
            fragment.arguments = bundle
            return fragment
        }
    }

}