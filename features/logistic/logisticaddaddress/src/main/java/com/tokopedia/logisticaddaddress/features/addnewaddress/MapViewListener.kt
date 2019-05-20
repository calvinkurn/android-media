package com.tokopedia.logisticaddaddress.features.addnewaddress

import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autofill.AutofillDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel

/**
 * Created by fwidjaja on 2019-05-09.
 */
interface MapViewListener: CustomerView {
    fun moveMap(latLng: LatLng)
    fun onSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel)
    fun onSuccessAutofill(autofillDataUiModel: AutofillDataUiModel)
}