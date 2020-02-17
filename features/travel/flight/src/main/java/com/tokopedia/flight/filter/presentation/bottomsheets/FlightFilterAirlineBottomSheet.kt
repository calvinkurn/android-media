package com.tokopedia.flight.filter.presentation.bottomsheets

import android.os.Bundle
import com.tokopedia.flight.R
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by furqan on 17/02/2020
 */
class FlightFilterAirlineBottomSheet : BottomSheetUnify() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showCloseIcon = false
        isFullpage = false
        showKnob = true
        isDragable = true
        setTitle(getString(R.string.airline))
        setAction(getString(R.string.reset)) {

        }

    }

    companion object {
        const val TAG_FILTER_AIRLINE = "TagFilterAirlineBottomSheet"

        fun getInstance(): FlightFilterAirlineBottomSheet = FlightFilterAirlineBottomSheet()
    }
}