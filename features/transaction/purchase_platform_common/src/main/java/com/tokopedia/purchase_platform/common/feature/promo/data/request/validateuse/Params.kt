package com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse

import android.os.Parcel
import android.os.Parcelable
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Params(

        @field:SerializedName("promo")
        var promo: ValidateUsePromoRequest = ValidateUsePromoRequest()
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable(ValidateUsePromoRequest::class.java.classLoader)
            ?: ValidateUsePromoRequest()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(promo, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Params> {
        override fun createFromParcel(parcel: Parcel): Params {
            return Params(parcel)
        }

        override fun newArray(size: Int): Array<Params?> {
            return arrayOfNulls(size)
        }
    }

}