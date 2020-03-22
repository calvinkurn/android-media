package com.tokopedia.product.addedit.shipment.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by faisalramd on 2020-03-22.
 */

@Parcelize
data class ShipmentInputModel (
        var weight: Int = 0,
        var weightUnit: Int = 0,
        var isMustInsurance: Boolean = false
) : Parcelable