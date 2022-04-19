package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticCommon.data.entity.response.Data

/**
 * Created by fwidjaja on 2019-05-09.
 */
interface PinpointMapView : CustomerView {
    fun showLoading()
    fun onSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel)
    fun onSuccessAutofill(autofillDataUiModel: Data)
    fun showBoundaries(boundaries: List<LatLng>)
    fun showAutoComplete(lat: Double, long: Double)
    fun showOutOfReachDialog()
    fun showUndetectedDialog()
    fun showLocationNotFoundCTA()
    fun goToAddNewAddressNegative()
}