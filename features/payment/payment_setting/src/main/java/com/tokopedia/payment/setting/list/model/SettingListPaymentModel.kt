package com.tokopedia.payment.setting.list.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.payment.setting.list.SettingListPaymentAdapterTypeFactory

open class SettingListPaymentModel : Visitable<SettingListPaymentAdapterTypeFactory> {
    override fun type(typeFactory: SettingListPaymentAdapterTypeFactory?): Int {
        return typeFactory?.type(this)?:0
    }

    @SerializedName("card_type")
    @Expose
    var cardType: String? = null
    @SerializedName("expiry_month")
    @Expose
    var expiryMonth: String? = null
    @SerializedName("expiry_year")
    @Expose
    var expiryYear: String? = null
    @SerializedName("masked_number")
    @Expose
    var maskedNumber: String? = null
    @SerializedName("token_id")
    @Expose
    var tokenId: String? = null

}
