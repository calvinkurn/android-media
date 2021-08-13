package com.tokopedia.product.addedit.shipment.presentation.model

import android.os.Parcelable
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.DEFAULT_WEIGHT_UNIT
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.DEFAULT_WEIGHT_VALUE
import kotlinx.android.parcel.Parcelize

/**
 * Created by faisalramd on 2020-03-22.
 */

@Parcelize
data class ShipmentInputModel (
        var weight: Int = DEFAULT_WEIGHT_VALUE,
        var weightUnit: Int = DEFAULT_WEIGHT_UNIT,
        var isMustInsurance: Boolean = true
) : Parcelable