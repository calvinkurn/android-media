package com.tokopedia.flight.search_universal.presentation.bottomsheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.activity.FlightAirportPickerActivity
import com.tokopedia.flight.airport.view.fragment.FlightAirportPickerFragment
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.dashboard.view.activity.FlightClassesActivity
import com.tokopedia.flight.dashboard.view.activity.FlightSelectPassengerActivity
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel
import com.tokopedia.flight.dashboard.view.widget.FlightCalendarOneWayWidget
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.flight.search_universal.di.DaggerFlightSearchUniversalComponent
import com.tokopedia.flight.search_universal.di.FlightSearchUniversalComponent
import com.tokopedia.flight.search_universal.presentation.viewmodel.FlightSearchUniversalViewModel
import com.tokopedia.flight.search_universal.presentation.widget.FlightSearchFormView
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_flight_search_form.view.*
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 09/03/2020
 */
class FlightSearchUniversalBottomSheet : BottomSheetUnify(), FlightSearchFormView.FlightSearchFormListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightSearchUniversalViewModel: FlightSearchUniversalViewModel
    private lateinit var flightSearchUniversalComponent: FlightSearchUniversalComponent

    private lateinit var mChildView: View

    lateinit var listener: Listener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightSearchUniversalViewModel = viewModelProvider.get(FlightSearchUniversalViewModel::class.java)
        }

        initBottomSheet()
    }

    override fun onDepartureAirportClicked() {
        val intent = FlightAirportPickerActivity.createInstance(requireContext(), getString(R.string.flight_airportpicker_departure_title))
        startActivityForResult(intent, REQUEST_CODE_AIRPORT_DEPARTURE)
    }

    override fun onDestinationAirportClicked() {
        val intent = FlightAirportPickerActivity.createInstance(requireContext(), getString(R.string.flight_airportpicker_arrival_title))
        startActivityForResult(intent, REQUEST_CODE_AIRPORT_DESTINATION)
    }

    override fun onDepartureDateClicked(departureAirport: String, arrivalAirport: String, flightClassId: Int,
                                        departureDate: Date, returnDate: Date, isRoundTrip: Boolean) {
        val minMaxDate = flightSearchUniversalViewModel.generatePairOfMinAndMaxDateForDeparture()
        if (isRoundTrip) {
            // if round trip, use selected date asa mindate and return date as selected date
            setCalendarDatePicker(
                    returnDate,
                    departureDate,
                    minMaxDate.second,
                    getString(com.tokopedia.travelcalendar.R.string.travel_calendar_label_choose_departure_trip_date),
                    TAG_DEPARTURE_CALENDAR
            )
        } else {
            val flightCalendarDialog = FlightCalendarOneWayWidget.newInstance(
                    FlightDateUtil.dateToString(minMaxDate.first, FlightDateUtil.DEFAULT_FORMAT),
                    FlightDateUtil.dateToString(minMaxDate.second, FlightDateUtil.DEFAULT_FORMAT),
                    FlightDateUtil.dateToString(departureDate, FlightDateUtil.DEFAULT_FORMAT),
                    departureAirport,
                    arrivalAirport,
                    flightClassId
            )
            flightCalendarDialog.setListener(object : FlightCalendarOneWayWidget.ActionListener {
                override fun onDateSelected(dateSelected: Date) {
                    val errorResourceId = flightSearchUniversalViewModel.validateDepartureDate(dateSelected)
                    if (errorResourceId == -1) {
                        mChildView.flightSearchFormView.setDepartureDate(dateSelected)
                    } else {
                        showMessageErrorInSnackbar(errorResourceId)
                    }
                }

            })
            flightCalendarDialog.show(requireFragmentManager(), TAG_DEPARTURE_CALENDAR)
        }
    }

    override fun onReturnDateClicked(departureDate: Date, returnDate: Date) {
        val minMaxDate = flightSearchUniversalViewModel.generatePairOfMinAndMaxDateForReturn(departureDate)
        setCalendarDatePicker(null,
                minMaxDate.first,
                minMaxDate.second,
                getString(com.tokopedia.travelcalendar.R.string.travel_calendar_label_choose_return_trip_date),
                TAG_RETURN_CALENDAR)
    }

    override fun onPassengerClicked(passengerModel: FlightPassengerViewModel?) {
        passengerModel?.let {
            val intent = FlightSelectPassengerActivity.getCallingIntent(requireContext(), it)
            startActivityForResult(intent, REQUEST_CODE_SELECT_PASSENGER)
        }
    }

    override fun onClassClicked(flightClassId: Int) {
        val intent = FlightClassesActivity.getCallingIntent(requireContext(), flightClassId)
        startActivityForResult(intent, REQUEST_CODE_SELECT_CLASSES)
    }

    override fun onSaveSearch(flightSearchData: FlightSearchPassDataViewModel) {
        if (::listener.isInitialized) {
            listener.onSaveSearchParams(flightSearchData)
            dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mChildView.flightSearchFormView.removeFocus()
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_AIRPORT_DEPARTURE -> {
                    val departureAirport = data?.getParcelableExtra<FlightAirportViewModel>(FlightAirportPickerFragment.EXTRA_SELECTED_AIRPORT)
                    departureAirport?.let {
                        mChildView.flightSearchFormView.setOriginAirport(departureAirport)
                    }
                }
                REQUEST_CODE_AIRPORT_DESTINATION -> {
                    val arrivalAirport = data?.getParcelableExtra<FlightAirportViewModel>(FlightAirportPickerFragment.EXTRA_SELECTED_AIRPORT)
                    arrivalAirport?.let {
                        mChildView.flightSearchFormView.setDestinationAirport(arrivalAirport)
                    }
                }
                REQUEST_CODE_SELECT_PASSENGER -> {
                    val passengerModel = data?.getParcelableExtra<FlightPassengerViewModel>(FlightSelectPassengerActivity.EXTRA_PASS_DATA)
                    passengerModel?.let {
                        mChildView.flightSearchFormView.setPassengerView(it)
                    }
                }
                REQUEST_CODE_SELECT_CLASSES -> {
                    val classModel = data?.getParcelableExtra<FlightClassViewModel>(FlightClassesActivity.EXTRA_FLIGHT_CLASS)
                    classModel?.let {
                        mChildView.flightSearchFormView.setClassView(it)
                    }
                }
            }
        }
    }

    private fun setCalendarDatePicker(selectedDate: Date?, minDate: Date, maxDate: Date, title: String, tag: String) {
        val minDateStr = FlightDateUtil.dateToString(minDate, FlightDateUtil.DEFAULT_FORMAT)
        val selectedDateStr = if (selectedDate != null) FlightDateUtil.dateToString(selectedDate, FlightDateUtil.DEFAULT_FORMAT) else null

        val flightCalendarDialog = SelectionRangeCalendarWidget.getInstance(
                minDateStr, selectedDateStr,
                SelectionRangeCalendarWidget.DEFAULT_RANGE_CALENDAR_YEAR,
                SelectionRangeCalendarWidget.DEFAULT_RANGE_DATE_SELECTED.toLong(),
                getString(R.string.flight_min_date_label),
                getString(R.string.flight_max_date_label),
                SelectionRangeCalendarWidget.DEFAULT_MIN_SELECTED_DATE_TODAY,
                true
        )
        flightCalendarDialog.listener = object : SelectionRangeCalendarWidget.OnDateClickListener {
            override fun onDateClick(dateIn: Date, dateOut: Date) {
                val departureErrorResourceId = flightSearchUniversalViewModel.validateDepartureDate(dateIn)
                if (departureErrorResourceId == -1) {
                    mChildView.flightSearchFormView.setDepartureDate(dateIn)
                } else {
                    showMessageErrorInSnackbar(departureErrorResourceId)
                }

                val returnErrorResourceId = flightSearchUniversalViewModel.validateReturnDate(dateIn, dateOut)
                if (returnErrorResourceId == -1) {
                    mChildView.flightSearchFormView.setReturnDate(dateOut)
                } else {
                    showMessageErrorInSnackbar(returnErrorResourceId)
                }
            }
        }
        flightCalendarDialog.show(requireFragmentManager(), tag)
    }

    private fun initInjector() {
        if (!::flightSearchUniversalComponent.isInitialized) {
            flightSearchUniversalComponent = DaggerFlightSearchUniversalComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(requireActivity().application))
                    .build()
        }
        flightSearchUniversalComponent.inject(this)
    }

    private fun initBottomSheet() {
        showCloseIcon = false
        showKnob = true
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.flight_change_search_label))

        mChildView = View.inflate(requireContext(), R.layout.bottom_sheet_flight_search_form, null)
        setChild(mChildView)
        initSearchForm()
    }

    private fun initSearchForm() {
        mChildView.flightSearchFormView.listener = this
    }

    private fun showMessageErrorInSnackbar(resourceId: Int) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, getString(resourceId))
    }

    interface Listener {
        fun onSaveSearchParams(flightSearchParams: FlightSearchPassDataViewModel)
    }

    companion object {
        const val TAG_SEARCH_FORM = "TagFlightSearchFormBottomSheet"
        const val TAG_DEPARTURE_CALENDAR = "flightCalendarDeparture"
        const val TAG_RETURN_CALENDAR = "flightCalendarReturn"

        const val REQUEST_CODE_AIRPORT_DEPARTURE = 1
        const val REQUEST_CODE_AIRPORT_DESTINATION = 2
        const val REQUEST_CODE_SELECT_PASSENGER = 3
        const val REQUEST_CODE_SELECT_CLASSES = 4

        fun getInstance(): FlightSearchUniversalBottomSheet =
                FlightSearchUniversalBottomSheet()
    }

}