package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author anggaprasetiyo on 22/02/18.
 */
@Parcelize
class ShipProd(
    var shipProdId: Int = 0,
    var shipProdName: String = "",
    var shipGroupName: String = "",
    var shipGroupId: Int = 0,
    var additionalFee: Int = 0,
    var minimumWeight: Int = 0
) : Parcelable
