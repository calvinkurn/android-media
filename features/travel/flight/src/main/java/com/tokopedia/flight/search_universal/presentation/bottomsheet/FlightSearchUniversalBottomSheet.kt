package com.tokopedia.flight.search_universal.presentation.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.airportv2.presentation.bottomsheet.FlightAirportPickerBottomSheet
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.homepage.presentation.bottomsheet.FlightSelectClassBottomSheet
import com.tokopedia.flight.homepage.presentation.bottomsheet.FlightSelectPassengerBottomSheet
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.homepage.presentation.widget.FlightCalendarOneWayWidget
import com.tokopedia.flight.homepage.presentation.widget.FlightCalendarRoundTripWidget
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search_universal.di.DaggerFlightSearchUniversalComponent
import com.tokopedia.flight.search_universal.di.FlightSearchUniversalComponent
import com.tokopedia.flight.search_universal.presentation.viewmodel.FlightSearchUniversalViewModel
import com.tokopedia.flight.search_universal.presentation.widget.FlightSearchFormView
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.travelcalendar.singlecalendar.SinglePickCalendarWidget
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
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

    override fun onRoundTripSwitchChanged(isRoundTrip: Boolean) {
        // do nothing
    }

    override fun onDepartureAirportClicked() {
        val flightAirportPickerBottomSheet = FlightAirportPickerBottomSheet.getInstance()
        flightAirportPickerBottomSheet.listener = object : FlightAirportPickerBottomSheet.Listener {
            override fun onAirportSelected(selectedAirport: FlightAirportModel) {
                mChildView.flightSearchFormView.setOriginAirport(selectedAirport)
            }
        }
        fragmentManager?.let {
            flightAirportPickerBottomSheet.show(it, FlightAirportPickerBottomSheet.TAG_FLIGHT_AIRPORT_PICKER)
        }
    }

    override fun onDestinationAirportClicked() {
        val flightAirportPickerBottomSheet = FlightAirportPickerBottomSheet.getInstance()
        flightAirportPickerBottomSheet.listener = object : FlightAirportPickerBottomSheet.Listener {
            override fun onAirportSelected(selectedAirport: FlightAirportModel) {
                mChildView.flightSearchFormView.setDestinationAirport(selectedAirport)
            }
        }
        fragmentManager?.let {
            flightAirportPickerBottomSheet.show(it, FlightAirportPickerBottomSheet.TAG_FLIGHT_AIRPORT_PICKER)
        }
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
                    TAG_DEPARTURE_CALENDAR,
                    departureAirport,
                    arrivalAirport,
                    flightClassId
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
            flightCalendarDialog.setListener(object : SinglePickCalendarWidget.ActionListener {
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

    override fun onReturnDateClicked(departureDate: Date, returnDate: Date,
                                     departureAirport: String, arrivalAirport: String, flightClassId: Int) {
        val minMaxDate = flightSearchUniversalViewModel.generatePairOfMinAndMaxDateForReturn(departureDate)
        setCalendarDatePicker(null,
                minMaxDate.first,
                minMaxDate.second,
                getString(com.tokopedia.travelcalendar.R.string.travel_calendar_label_choose_return_trip_date),
                TAG_RETURN_CALENDAR,
                departureAirport,
                arrivalAirport, flightClassId)
    }

    override fun onPassengerClicked(passengerModel: FlightPassengerModel?) {
        val flightSelectPassengerBottomSheet = FlightSelectPassengerBottomSheet()
        flightSelectPassengerBottomSheet.listener = object : FlightSelectPassengerBottomSheet.Listener {
            override fun onSavedPassenger(passengerModel: FlightPassengerModel) {
                mChildView.flightSearchFormView.setPassengerView(passengerModel)
            }
        }
        flightSelectPassengerBottomSheet.passengerModel = passengerModel
        flightSelectPassengerBottomSheet.setShowListener { flightSelectPassengerBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        flightSelectPassengerBottomSheet.show(requireFragmentManager(), FlightSelectPassengerBottomSheet.TAG_SELECT_PASSENGER)
    }

    override fun onClassClicked(flightClassId: Int) {
        val flightSelectClassBottomSheet = FlightSelectClassBottomSheet()
        flightSelectClassBottomSheet.listener = object : FlightSelectClassBottomSheet.Listener {
            override fun onClassSelected(classEntity: FlightClassModel) {
                mChildView.flightSearchFormView.setClassView(classEntity)
            }
        }
        flightSelectClassBottomSheet.setSelectedClass(flightClassId)
        flightSelectClassBottomSheet.setShowListener { flightSelectClassBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        flightSelectClassBottomSheet.show(requireFragmentManager(), FlightSelectClassBottomSheet.TAG_SELECT_CLASS)
    }

    override fun onSaveSearch(flightSearchData: FlightSearchPassDataModel) {
        if (::listener.isInitialized) {
            listener.onSaveSearchParams(flightSearchData)
            dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mChildView.flightSearchFormView.removeFocus()
    }

    private fun setCalendarDatePicker(selectedDate: Date?, minDate: Date, maxDate: Date, title: String, tag: String,
                                      departureAirport: String, arrivalAirport: String, flightClassId: Int) {
        val minDateStr = FlightDateUtil.dateToString(minDate, FlightDateUtil.DEFAULT_FORMAT)
        val maxDateStr = FlightDateUtil.dateToString(maxDate, FlightDateUtil.DEFAULT_FORMAT)

        val selectedDateStr = if (selectedDate != null) FlightDateUtil.dateToString(selectedDate, FlightDateUtil.DEFAULT_FORMAT) else null

        val flightCalendarDialog = FlightCalendarRoundTripWidget.getInstance(
                minDateStr, selectedDateStr,
                SelectionRangeCalendarWidget.DEFAULT_RANGE_CALENDAR_YEAR,
                SelectionRangeCalendarWidget.DEFAULT_RANGE_DATE_SELECTED.toLong(),
                getString(R.string.flight_min_date_label),
                getString(R.string.flight_max_date_label),
                SelectionRangeCalendarWidget.DEFAULT_MIN_SELECTED_DATE_TODAY,
                true,
                departureAirport, arrivalAirport, flightClassId,
                maxDateStr
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
        Toaster.build(mChildView, getString(resourceId), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR,
                getString(R.string.flight_booking_action_okay)).show()
    }

    interface Listener {
        fun onSaveSearchParams(flightSearchParams: FlightSearchPassDataModel)
    }

    companion object {
        const val TAG_SEARCH_FORM = "TagFlightSearchFormBottomSheet"
        const val TAG_DEPARTURE_CALENDAR = "flightCalendarDeparture"
        const val TAG_RETURN_CALENDAR = "flightCalendarReturn"

        fun getInstance(): FlightSearchUniversalBottomSheet =
                FlightSearchUniversalBottomSheet()
    }

}