package com.tokopedia.flight.search.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 15/04/2020
 */
@Parcelize
class FlightSearchSelectedModel (
        val journeyModel: FlightJourneyModel,
        val priceModel: FlightPriceModel): Parcelable