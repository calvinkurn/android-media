package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.location_info

import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.logisticaddaddress.R

/**
 * Created by fwidjaja on 2019-06-18.
 */
class LocationInfoBottomSheetFragment: BottomSheets() {
    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_location_info
    }

    override fun initView(view: View?) {

    }

    override fun title(): String {
        return getString(R.string.undetected_location)
    }
}