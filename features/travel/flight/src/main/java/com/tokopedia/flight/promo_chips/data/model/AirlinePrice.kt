package com.tokopedia.flight.promo_chips.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.promo_chips.presentation.adapter.FlightPromoChipsAdapterTypeFactory

data class AirlinePrice(
        @SerializedName("airlineID")
        @Expose
        val airlineID: String = "",
        
        @SerializedName("logo")
        @Expose
        val logo: String = "",

        @SerializedName("airlineName")
        @Expose
        val shortName: String = "",

        @SerializedName("price")
        @Expose
        val price: String = "",

        @SerializedName("priceNumeric")
        @Expose
        val priceNumeric: Int = 0
): Visitable<FlightPromoChipsAdapterTypeFactory>{
    override fun type(typeFactory: FlightPromoChipsAdapterTypeFactory): Int = typeFactory.type(this)
}