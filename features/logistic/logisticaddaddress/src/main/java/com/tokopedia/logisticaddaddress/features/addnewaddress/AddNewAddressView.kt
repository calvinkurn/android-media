package com.tokopedia.logisticaddaddress.features.addnewaddress

import com.google.android.gms.maps.model.LatLng

/**
 * Created by fwidjaja on 2019-05-09.
 */
interface AddNewAddressView {
    fun moveMap(latLng: LatLng)
}