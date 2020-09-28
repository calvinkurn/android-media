package com.tokopedia.salam.umrah.checkout.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * @author by firman on 27/11/2019
 */
@Parcelize
class UmrahCheckoutPilgrims (
        @SerializedName("pilgrimsNumber")
        @Expose
        var pilgrimsNumber: Int = 0,
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("firstName")
        @Expose
        var firstName : String = "",
        @SerializedName("lastName")
        @Expose
        var lastName : String = "",
        @SerializedName("dateBirth")
        @Expose
        var dateBirth : String = ""

) : Parcelable