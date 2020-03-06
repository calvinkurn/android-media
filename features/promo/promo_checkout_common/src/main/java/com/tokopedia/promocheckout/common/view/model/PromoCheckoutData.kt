package com.tokopedia.promocheckout.common.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView

/**
 * Created by fwidjaja on 2020-02-26.
 */
data class PromoCheckoutData (var promoLabel: String = "",
                              var promoUsageInfo: String = "",
                              var state: ButtonPromoCheckoutView.State = ButtonPromoCheckoutView.State.ACTIVE) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readParcelable(ButtonPromoCheckoutView.State::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(promoLabel)
        parcel.writeString(promoUsageInfo)
        parcel.writeParcelable(state, flags)
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

        fun promoLabel(promoLabel: String) = apply { this.promoLabel = promoLabel }
        fun promoUsageInfo(promoUsageInfo: String) = apply { this.promoUsageInfo = promoUsageInfo }
        fun state(state: ButtonPromoCheckoutView.State) = apply { this.state = state }

        fun build() = PromoCheckoutData(
                promoLabel,
                promoUsageInfo,
                state)
    }
}