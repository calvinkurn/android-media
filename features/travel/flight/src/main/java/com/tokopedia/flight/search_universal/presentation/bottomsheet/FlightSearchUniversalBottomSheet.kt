package com.tokopedia.flight.search_universal.presentation.bottomsheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.activity.FlightAirportPickerActivity
import com.tokopedia.flight.airport.view.fragment.FlightAirportPickerFragment
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel
import com.tokopedia.flight.dashboard.view.activity.FlightClassesActivity
import com.tokopedia.flight.dashboard.view.activity.FlightSelectPassengerActivity
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel
import com.tokopedia.flight.search_universal.di.DaggerFlightSearchUniversalComponent
import com.tokopedia.flight.search_universal.di.FlightSearchUniversalComponent
import com.tokopedia.flight.search_universal.presentation.viewmodel.FlightSearchUniversalViewModel
import com.tokopedia.flight.search_universal.presentation.widget.FlightSearchFormView
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

    override fun onDepartureDateClicked(departureDate: Date?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onReturnDateClicked(returnDate: Date?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    companion object {
        const val TAG_SEARCH_FORM = "TagFlightSearchFormBottomSheet"

        const val REQUEST_CODE_AIRPORT_DEPARTURE = 1
        const val REQUEST_CODE_AIRPORT_DESTINATION = 2
        const val REQUEST_CODE_SELECT_PASSENGER = 3
        const val REQUEST_CODE_SELECT_CLASSES = 4

        fun getInstance(): FlightSearchUniversalBottomSheet =
                FlightSearchUniversalBottomSheet()
    }

}