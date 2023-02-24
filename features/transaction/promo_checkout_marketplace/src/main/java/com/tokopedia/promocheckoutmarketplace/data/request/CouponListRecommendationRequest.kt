package com.tokopedia.promocheckoutmarketplace.data.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest

data class CouponListRecommendationRequest(
    @SerializedName("promo")
    var promoRequest: PromoRequest = PromoRequest()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(PromoRequest::class.java.classLoader)
            ?: PromoRequest()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(promoRequest, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CouponListRecommendationRequest> {
        override fun createFromParcel(parcel: Parcel): CouponListRecommendationRequest {
            return CouponListRecommendationRequest(parcel)
        }

        override fun newArray(size: Int): Array<CouponListRecommendationRequest?> {
            return arrayOfNulls(size)
        }
    }
}
