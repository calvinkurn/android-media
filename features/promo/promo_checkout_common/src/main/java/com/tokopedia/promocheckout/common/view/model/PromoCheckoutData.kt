package com.tokopedia.promocheckout.common.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView

/**
 * Created by fwidjaja on 2020-02-26.
 */
data class PromoCheckoutData (var promoLabel: String = "",
                              var promoUsageInfo: String = "",
                              var state: ButtonPromoCheckoutView.State = ButtonPromoCheckoutView.State.ACTIVE,
                              var codes: List<String> = emptyList(),
                              var totalBenefitLabel: String = "",
                              var totalBenefitAmountStr: String = "",
                              var listAllPromoCodes: List<String>) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readParcelable(ButtonPromoCheckoutView.State::class.java.classLoader),
            parcel.createStringArrayList(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.createStringArrayList()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(promoLabel)
        parcel.writeString(promoUsageInfo)
        parcel.writeParcelable(state, flags)
        parcel.writeStringList(codes)
        parcel.writeString(totalBenefitLabel)
        parcel.writeString(totalBenefitAmountStr)
        parcel.writeStringList(listAllPromoCodes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoCheckoutData> {
        override fun createFromParcel(parcel: Parcel): PromoCheckoutData {
            return PromoCheckoutData(parcel)
        }

        override fun newArray(size: Int): Array<PromoCheckoutData?> {
            return arrayOfNulls(size)
        }
    }

    class Builder {
        var promoLabel: String = ""
        var promoUsageInfo: String = ""
        var state: ButtonPromoCheckoutView.State = ButtonPromoCheckoutView.State.ACTIVE
        var codes: List<String> = emptyList()
        var totalBenefitLabel: String = ""
        var totalBenefitAmountStr: String = ""
        var listAllPromoCodes: List<String> = emptyList()

        fun promoLabel(promoLabel: String) = apply { this.promoLabel = promoLabel }
        fun promoUsageInfo(promoUsageInfo: String) = apply { this.promoUsageInfo = promoUsageInfo }
        fun state(state: ButtonPromoCheckoutView.State) = apply { this.state = state }
        fun codes(codes: List<String>) = apply { this.codes = codes }
        fun totalBenefitLabel(totalBenefitLabel: String) = apply { this.totalBenefitLabel = totalBenefitLabel }
        fun totalBenefitAmountStr(totalBenefitAmountStr: String) = apply { this.totalBenefitAmountStr = totalBenefitAmountStr }
        fun listAllPromoCodes(listAllPromoCodes: List<String>) = apply { this.listAllPromoCodes = listAllPromoCodes }

        fun build() = PromoCheckoutData(
                promoLabel,
                promoUsageInfo,
                state,
                codes,
                totalBenefitLabel,
                totalBenefitAmountStr,
                listAllPromoCodes)
    }
}