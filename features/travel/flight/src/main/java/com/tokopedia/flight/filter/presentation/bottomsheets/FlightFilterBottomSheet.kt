package com.tokopedia.flight.filter.presentation.bottomsheets

import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by furqan on 19/02/2020
 */
class FlightFilterBottomSheet: BottomSheetUnify() {

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)

        FlightFilterAirlineBottomSheet.getInstance().show(manager, "NEW_TAG")
    }

}