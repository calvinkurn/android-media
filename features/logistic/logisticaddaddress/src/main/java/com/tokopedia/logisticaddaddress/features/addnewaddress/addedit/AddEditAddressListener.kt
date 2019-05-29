package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.add_address.AddAddressDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel

/**
 * Created by fwidjaja on 2019-05-22.
 */
interface AddEditAddressListener: CustomerView {
    fun moveMap(latLng: LatLng)
    fun onSuccessAddAddress(addAddressDataUiModel: AddAddressDataUiModel, saveAddressDataModel: SaveAddressDataModel)
}