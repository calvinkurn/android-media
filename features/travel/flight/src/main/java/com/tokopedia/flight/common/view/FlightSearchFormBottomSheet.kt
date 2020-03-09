package com.tokopedia.flight.common.view

import android.os.Bundle
import android.view.View
import com.tokopedia.flight.R
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by furqan on 09/03/2020
 */
class FlightSearchFormBottomSheet : BottomSheetUnify() {

    private lateinit var mChildView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomSheet()
    }

    private fun initBottomSheet() {
        showCloseIcon = false
        showKnob = true
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.flight_change_search_label))

        mChildView = View.inflate(requireContext(), R.layout.bottom_sheet_flight_search_form, null)
        setChild(mChildView)
    }

    companion object {
        const val TAG_SEARCH_FORM = "TagFlightSearchFormBottomSheet"

        fun getInstance(): FlightSearchFormBottomSheet =
                FlightSearchFormBottomSheet()
    }

}