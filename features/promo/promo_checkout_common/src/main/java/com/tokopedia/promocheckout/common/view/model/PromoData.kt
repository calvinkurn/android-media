package com.tokopedia.promocheckout.common.view.model

import android.os.Parcelable
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import kotlinx.android.parcel.Parcelize

@Parcelize
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

    fun isActive(): Boolean {
        return state == TickerCheckoutView.State.ACTIVE
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

    companion object {
        @JvmField
        val TYPE_VOUCHER = 0
        @JvmField
        val TYPE_COUPON = 1
        @JvmField
        val VALUE_COUPON = 1
        @JvmField
        val VOUCHER_RESULT_CODE = 12
        @JvmField
        val COUPON_RESULT_CODE = 15
    }
}