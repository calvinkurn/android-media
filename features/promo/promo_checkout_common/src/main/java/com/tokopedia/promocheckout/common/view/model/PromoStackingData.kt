package com.tokopedia.promocheckout.common.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView

data class PromoStackingData(var typePromo: Int = 0,
                             var promoCode: String = "",
                             var description: String = "",
                             var title: String = "",
                             var amount: Int = 0,
                             var state: TickerPromoStackingCheckoutView.State = TickerPromoStackingCheckoutView.State.EMPTY,
                             var variant: TickerPromoStackingCheckoutView.Variant = TickerPromoStackingCheckoutView.Variant.GLOBAL) : Parcelable{

    fun getPromoCodeSafe() : String{
        if(state != TickerPromoStackingCheckoutView.State.EMPTY){
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
            parcel.readParcelable(TickerPromoStackingCheckoutView.State::class.java.classLoader),
            parcel.readParcelable(TickerPromoStackingCheckoutView.Variant::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(typePromo)
        parcel.writeString(promoCode)
        parcel.writeString(description)
        parcel.writeString(title)
        parcel.writeInt(amount)
        parcel.writeParcelable(state, flags)
        parcel.writeParcelable(variant, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoStackingData> {

        val TYPE_VOUCHER = 1
        val TYPE_COUPON = 2
        val VALUE_COUPON = 1

        override fun createFromParcel(parcel: Parcel): PromoStackingData {
            return PromoStackingData(parcel)
        }

        override fun newArray(size: Int): Array<PromoStackingData?> {
            return arrayOfNulls(size)
        }
    }

    class Builder {
        private var typePromo: Int = 0
        var promoCode: String = ""
        var description: String = ""
        var title: String = ""
        var amount: Int = 0
        var state: TickerPromoStackingCheckoutView.State = TickerPromoStackingCheckoutView.State.EMPTY
        var variant: TickerPromoStackingCheckoutView.Variant = TickerPromoStackingCheckoutView.Variant.GLOBAL

        fun typePromo(typePromo: Int) = apply { this.typePromo = typePromo }
        fun promoCode(promoCode: String) = apply { this.promoCode = promoCode }
        fun description(description: String) = apply { this.description = description }
        fun title(title: String) = apply { this.title = title }
        fun amount(amount: Int) = apply { this.amount = amount }
        fun state(state: TickerPromoStackingCheckoutView.State) = apply { this.state = state }
        fun variant(variant: TickerPromoStackingCheckoutView.Variant) = apply { this.variant = variant }

        fun build() = PromoStackingData(
                typePromo,
                promoCode,
                description,
                title,
                amount,
                state,
                variant
        )
    }
}