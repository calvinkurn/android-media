package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author anggaprasetiyo on 22/02/18.
 */
@Parcelize
class ShopShipment : Parcelable {
    var shipId = 0
    var shipName = ""
    var shipCode = ""
    var shipLogo = ""
    var shipProds: List<ShipProd> = emptyList()
    var isDropshipEnabled = false
}