package com.tokopedia.flight.filter.presentation.bottomsheets

import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by furqan on 19/02/2020
 */
class FlightFilterBottomSheet : BottomSheetUnify() {

    companion object {
        const val TAG_FILTER = "TagFilterBottomSheet"

        fun getInstance(): FlightFilterBottomSheet = FlightFilterBottomSheet()
    }

}