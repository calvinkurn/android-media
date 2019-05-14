package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets

import com.tokopedia.abstraction.base.view.listener.CustomerView

/**
 * Created by fwidjaja on 2019-05-14.
 */
interface MapSearchLocationBottomSheetView: CustomerView {

    fun hideListPointOfInterest()
    fun onSuccessGetListPointOfInterest()
}