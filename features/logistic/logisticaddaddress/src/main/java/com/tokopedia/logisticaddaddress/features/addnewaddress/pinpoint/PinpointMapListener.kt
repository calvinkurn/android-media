package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticdata.data.entity.response.Data

/**
 * Created by fwidjaja on 2019-05-09.
 */
interface PinpointMapListener : CustomerView {
    fun showLoading()
    fun onSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel)
    fun onSuccessAutofill(autofillDataUiModel: Data, errMsg: String)
    fun onSuccessGetDistrictBoundary(boundaries: List<LatLng>)
    fun showAutoComplete(lat: Double, long: Double)
    fun showOutOfReachDialog()
    fun showUndetectedDialog()
}