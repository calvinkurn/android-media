package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailActionButtonModel(
        val id: Int,
        val label: String,
        val buttonType: String,
        val uri: String,
        val uriWeb: String,
        val mappingUrl: String,
        val method: String,
        val weight: Int
) : Parcelable