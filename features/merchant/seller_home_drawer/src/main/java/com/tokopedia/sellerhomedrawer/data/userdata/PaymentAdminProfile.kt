package com.tokopedia.sellerhomedrawer.data.userdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PaymentAdminProfile (

    @SerializedName("is_purchased_marketplace")
    @Expose
    val isPurchasedMarketplace: Boolean? = false,
    @SerializedName("is_purchased_digital")
    @Expose
    val isPurchasedDigital: Boolean? = false,
    @SerializedName("is_purchased_ticket")
    @Expose
    val isPurchasedTicket: Boolean? = false,
    @SerializedName("last_purchase_date")
    @Expose
    val lastPurchaseDate: String? = ""
)