package com.tokopedia.salam.umrah.checkout.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UmrahCheckoutPaymentOptionsEntity(
        @SerializedName("umrahPaymentOptions")
        @Expose
        val umrahPaymentOptions: UmrahPaymentOptions = UmrahPaymentOptions()

)

class UmrahPaymentOptions(
        @SerializedName("paymentOptions")
        @Expose
        val paymentOptions: List<PaymentOptions> = emptyList(),
        @SerializedName("defaultOption")
        @Expose
        val defaultOption: Int = 0
)

class PaymentOptions(
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("desc")
        @Expose
        val desc: String = "",
        @SerializedName("defaultOption")
        @Expose
        val defaultOption: Int = 0,
        @SerializedName("schemes")
        @Expose
        val schemes: ArrayList<Schemes> = arrayListOf()
)

class Schemes(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("firstDueDate")
        @Expose
        val firstDueDate: String = "",
        @SerializedName("terms")
        @Expose
        val terms: List<Term> = arrayListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.createTypedArrayList(Term))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeInt(price)
        parcel.writeString(firstDueDate)
        parcel.writeTypedList(terms)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Schemes> {
        override fun createFromParcel(parcel: Parcel): Schemes {
            return Schemes(parcel)
        }

        override fun newArray(size: Int): Array<Schemes?> {
            return arrayOfNulls(size)
        }
    }
}

class Term(
        @SerializedName("type")
        @Expose
        val type: Int = 0,
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("dueDate")
        @Expose
        val dueDate: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeInt(price)
        parcel.writeString(dueDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Term> {
        override fun createFromParcel(parcel: Parcel): Term {
            return Term(parcel)
        }

        override fun newArray(size: Int): Array<Term?> {
            return arrayOfNulls(size)
        }
    }
}