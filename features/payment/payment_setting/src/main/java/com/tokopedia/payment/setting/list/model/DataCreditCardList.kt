package com.tokopedia.payment.setting.list.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataCreditCardList {

    @SerializedName("creditCard")
    @Expose
    var creditCard: CreditCard? = null

}

data class CreditCard(
        @SerializedName("error")
        @Expose
        val error: String,
        @SerializedName("cards")
        @Expose
        var cards: List<SettingListPaymentModel>? = null)

