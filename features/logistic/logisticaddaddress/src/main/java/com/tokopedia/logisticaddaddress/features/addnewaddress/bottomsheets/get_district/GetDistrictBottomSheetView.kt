package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.get_district

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictResponseUiModel

/**
 * Created by fwidjaja on 2019-05-16.
 */
interface GetDistrictBottomSheetView: CustomerView {
    fun onSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel)
}