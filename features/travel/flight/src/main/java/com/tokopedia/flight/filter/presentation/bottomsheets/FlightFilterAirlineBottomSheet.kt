package com.tokopedia.flight.filter.presentation.bottomsheets

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.flight.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * @author by furqan on 17/02/2020
 */
class FlightFilterAirlineBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showCloseIcon = false
        showKnob = true
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.airline))
        setAction(getString(R.string.reset)) {
            // TODO: Add Function for reset button
        }

        val view = View.inflate(requireContext(), R.layout.fragment_flight_filter_general, null)
        setChild(view)

    }

    companion object {
        const val TAG_FILTER_AIRLINE = "TagFilterAirlineBottomSheet"

        fun getInstance(): FlightFilterAirlineBottomSheet = FlightFilterAirlineBottomSheet()
    }
}