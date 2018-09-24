package com.tokopedia.payment.setting.list.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataCreditCardList {

    @SerializedName("creditCard")
    @Expose
    var creditCard: CreditCard? = null

}
