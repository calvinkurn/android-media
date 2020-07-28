package com.tokopedia.flight.cancellation.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationReasonAdapterTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 15/06/2020
 */
@Parcelize
data class FlightCancellationReasonModel(val id: String,
                                         val detail: String,
                                         val requiredDocs: List<FlightCancellationReasonRequiredDocsModel>?)
    : Parcelable, Visitable<FlightCancellationReasonAdapterTypeFactory> {

    override fun type(typeFactory: FlightCancellationReasonAdapterTypeFactory): Int =
            typeFactory.type(this)

}