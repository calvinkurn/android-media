package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryGeometryUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticdata.data.entity.response.Data

/**
 * Created by fwidjaja on 2019-05-09.
 */
interface PinpointMapListener : CustomerView {
    fun showLoading()
    fun onSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel)
    fun onSuccessAutofill(autofillDataUiModel: Data, errMsg: String)
    fun showFailedDialog()
    fun goToAddEditActivity(isMismatch: Boolean, isMismatchSolved: Boolean, isUnnamedRoad: Boolean, isZipCodeNull: Boolean)
    fun onSuccessGetDistrictBoundary(districtBoundaryGeometryUiModel: DistrictBoundaryGeometryUiModel)
    fun showAutoComplete(lat: Double, long: Double)
    fun finishBackToAddEdit(isMismatch: Boolean, isMismatchSolved: Boolean)
    fun showOutOfReachDialog()
    fun showUndetectedDialog()
}