package com.tokopedia.payment.setting.list.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CreditCard {

    @SerializedName("cards")
    @Expose
    var cards: List<SettingListPaymentModel>? = null

}
