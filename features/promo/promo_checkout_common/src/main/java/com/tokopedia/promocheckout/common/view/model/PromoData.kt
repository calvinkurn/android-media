package com.tokopedia.promocheckout.common.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView

data class PromoData(var typePromo: Int = 0,
                     var promoCode: String = "",
                     var description: String = "",
                     var title: String = "",
                     var amount: Int = 0,
                     var state: TickerCheckoutView.State = TickerCheckoutView.State.EMPTY) : Parcelable{

    fun getPromoCodeSafe() : String{
        if(state != TickerCheckoutView.State.EMPTY){
            return promoCode
        }else{
            return ""
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readParcelable(TickerCheckoutView.State::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(typePromo)
        parcel.writeString(promoCode)
        parcel.writeString(description)
        parcel.writeString(title)
        parcel.writeInt(amount)
        parcel.writeParcelable(state, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoData> {

        val TYPE_VOUCHER = 1
        val TYPE_COUPON = 2
        val VALUE_COUPON = 1

        override fun createFromParcel(parcel: Parcel): PromoData {
            return PromoData(parcel)
        }

        override fun newArray(size: Int): Array<PromoData?> {
            return arrayOfNulls(size)
        }
    }

    class Builder {
        private var typePromo: Int = 0
        var promoCode: String = ""
        var description: String = ""
        var title: String = ""
        var amount: Int = 0
        var state: TickerCheckoutView.State = TickerCheckoutView.State.EMPTY

        fun typePromo(typePromo: Int) = apply { this.typePromo = typePromo }
        fun promoCode(promoCode: String) = apply { this.promoCode = promoCode }
        fun description(description: String) = apply { this.description = description }
        fun title(title: String) = apply { this.title = title }
        fun amount(amount: Int) = apply { this.amount = amount }
        fun state(state: TickerCheckoutView.State) = apply { this.state = state }

        fun build() = PromoData(
                typePromo,
                promoCode,
                description,
                title,
                amount,
                state
        )
    }
}