package com.tokopedia.product.addedit.shipment.presentation.model

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.DEFAULT_WEIGHT_UNIT
import kotlinx.parcelize.Parcelize

/**
 * Created by faisalramd on 2020-03-22.
 */

@Parcelize
data class ShipmentInputModel(
    var weight: Int = Int.ZERO,
    var weightUnit: Int = DEFAULT_WEIGHT_UNIT,
    var isMustInsurance: Boolean = true,
    var cplModel: CPLModel = CPLModel(),
    var isUsingParentWeight: Boolean = false
) : Parcelable

@Parcelize
data class CPLModel(
    val cplParam: List<Long>? = null,
    var shipmentServicesIds: ArrayList<Long>? = null
) : Parcelable
