package com.tokopedia.logisticaddaddress.features.addnewaddress

import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.base.view.listener.CustomerView

/**
 * Created by fwidjaja on 2019-05-09.
 */
interface AddNewAddressView: CustomerView {
    fun moveMap(latLng: LatLng)
}