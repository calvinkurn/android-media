package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author anggaprasetiyo on 22/02/18.
 */
@Parcelize
class ShopShipment(
    var shipId: Int = 0,
    var shipName: String = "",
    var shipCode: String = "",
    var shipLogo: String = "",
    var shipProds: List<ShipProd> = emptyList(),
    var isDropshipEnabled: Boolean = false
) : Parcelable
