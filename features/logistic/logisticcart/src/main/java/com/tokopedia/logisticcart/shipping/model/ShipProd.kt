package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author anggaprasetiyo on 22/02/18.
 */
@Parcelize
class ShipProd: Parcelable {
    var shipProdId = 0
    var shipProdName = ""
    var shipGroupName = ""
    var shipGroupId = 0
    var additionalFee = 0
    var minimumWeight = 0
}