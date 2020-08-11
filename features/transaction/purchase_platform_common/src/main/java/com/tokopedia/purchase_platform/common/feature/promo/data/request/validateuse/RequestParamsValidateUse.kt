package com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse

import android.os.Parcel
import android.os.Parcelable
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class RequestParamsValidateUse(

        @field:SerializedName("params")
        var params: Params = Params()

) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable(Params::class.java.classLoader)
            ?: Params()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(params, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RequestParamsValidateUse> {
        override fun createFromParcel(parcel: Parcel): RequestParamsValidateUse {
            return RequestParamsValidateUse(parcel)
        }

        override fun newArray(size: Int): Array<RequestParamsValidateUse?> {
            return arrayOfNulls(size)
        }
    }

}