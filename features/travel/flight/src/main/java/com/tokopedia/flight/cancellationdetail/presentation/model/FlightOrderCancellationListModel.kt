package com.tokopedia.flight.cancellationdetail.presentation.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationListTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 06/01/2021
 */
@Parcelize
class FlightOrderCancellationListModel(
        val orderId: String = "",
        val cancellationDetail: FlightOrderCancellationDetailModel = FlightOrderCancellationDetailModel()
) : Parcelable, Visitable<FlightOrderCancellationListTypeFactory> {
    override fun type(typeFactory: FlightOrderCancellationListTypeFactory): Int =
            typeFactory.type(this)
}