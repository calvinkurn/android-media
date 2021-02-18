package com.tokopedia.flight.promo_chips.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.promo_chips.adapter.FlightPromoChipsAdapterTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AirlinePrice(
        @SerializedName("airlineID")
    val airlineID: String = "",
        @SerializedName("logo")
    val logo: String? = null,
        @SerializedName("shortName")
    val shortName: String = "",
        @SerializedName("price")
    val price: String = "",
        @SerializedName("priceNumeric")
    val priceNumeric: Int = 0
): Visitable<FlightPromoChipsAdapterTypeFactory>, Parcelable {

    override fun type(typeFactory: FlightPromoChipsAdapterTypeFactory): Int = typeFactory.type(this)
}