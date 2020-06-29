package com.tokopedia.flight.searchV4.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.flight.search.presentation.model.FlightPriceModel

/**
 * @author by furqan on 15/04/2020
 */
class FlightSearchSelectedModel (
        val journeyModel: FlightJourneyModel,
        val priceModel: FlightPriceModel): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(FlightJourneyModel::class.java.classLoader),
            parcel.readParcelable(FlightPriceModel::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(journeyModel, flags)
        parcel.writeParcelable(priceModel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlightSearchSelectedModel> {
        override fun createFromParcel(parcel: Parcel): FlightSearchSelectedModel {
            return FlightSearchSelectedModel(parcel)
        }

        override fun newArray(size: Int): Array<FlightSearchSelectedModel?> {
            return arrayOfNulls(size)
        }
    }

}