package com.tokopedia.topads.dashboard.data.model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.dashboard.view.adapter.TopAdsCreditTypeFactory

/**
 * Created by zulfikarrahman on 11/4/16.
 */

data class DataCredit (
    @SerializedName("product_id")
    @Expose
    val productId: String = "",

    @SerializedName("product_type")
    @Expose
    val productType: String = "",

    @SerializedName("product_price")
    @Expose
    val productPrice: String = "",

    @SerializedName("product_bonus")
    @Expose
    val productBonus: String = "",

    @SerializedName("product_url")
    @Expose
    val productUrl: String = "",

    @SerializedName("default")
    @Expose
    val selected: Int = 0) : Parcelable, Visitable<TopAdsCreditTypeFactory>{


    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun type(typeFactory: TopAdsCreditTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productType)
        parcel.writeString(productPrice)
        parcel.writeString(productBonus)
        parcel.writeString(productUrl)
        parcel.writeInt(selected)
    }

    companion object CREATOR : Parcelable.Creator<DataCredit> {
        override fun createFromParcel(parcel: Parcel): DataCredit {
            return DataCredit(parcel)
        }

        override fun newArray(size: Int): Array<DataCredit?> {
            return arrayOfNulls(size)
        }
    }
}