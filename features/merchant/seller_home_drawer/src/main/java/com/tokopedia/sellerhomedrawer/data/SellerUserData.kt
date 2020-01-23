package com.tokopedia.sellerhomedrawer.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.core.drawer2.data.pojo.*

class SellerUserData {

    @SerializedName("shopInfoMoengage")
    @Expose
    var shopInfoMoengage: ShopInfoMoengage? = null
    @SerializedName("profile")
    @Expose
    var profile: Profile? = null
    @SerializedName("wallet")
    @Expose
    var wallet: Wallet? = null
    @SerializedName("balance")
    @Expose
    var saldo: Saldo? = null
    @SerializedName("paymentAdminProfile")
    @Expose
    var paymentAdminProfile: PaymentAdminProfile? = null
    @SerializedName("topadsDeposit")
    @Expose
    var topadsDeposit: TopadsDeposit? = null
    @SerializedName("notifications")
    @Expose
    var notifications: Notifications? = null
}