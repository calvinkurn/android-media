package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel

/**
 * Created by fwidjaja on 2019-05-22.
 */
interface AddEditView : CustomerView {
    fun onSuccessAddAddress(saveAddressDataModel: SaveAddressDataModel)
    fun showZipCodes(zipcodes: List<String>)
    fun showManualZipCodes()
    fun showError(t: Throwable?)
    fun moveMap(latitude: Double, longitude: Double)
}