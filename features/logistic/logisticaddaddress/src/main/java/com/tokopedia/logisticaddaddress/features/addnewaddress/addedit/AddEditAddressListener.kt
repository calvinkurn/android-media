package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel

/**
 * Created by fwidjaja on 2019-05-22.
 */
interface AddEditAddressListener : CustomerView {
    fun onSuccessAddAddress(saveAddressDataModel: SaveAddressDataModel)
    fun showZipCodes(zipcodes: List<String>)
    fun showManualZipCodes()
    fun showError(t: Throwable)
}