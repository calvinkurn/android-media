package com.tokopedia.sellerhomedrawer.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import com.tokopedia.sellerhomedrawer.data.userdata.*

data class SellerUserData (
        @SerializedName("status")
        @Expose
        var status: String? = "",
        @SerializedName("shopInfoMoengage")
    @Expose
    var shopInfoMoengage: ShopInfoMoengage? = ShopInfoMoengage(),
        @SerializedName("profile")
    @Expose
    var profile: Profile? = Profile(),
        @SerializedName("wallet")
    @Expose
    var wallet: Wallet? = Wallet(),
        @SerializedName("balance")
    @Expose
    var saldo: Saldo? = Saldo(),
        @SerializedName("paymentAdminProfile")
    @Expose
    var paymentAdminProfile: PaymentAdminProfile? = PaymentAdminProfile(),
        @SerializedName("topadsDeposit")
    @Expose
    var topadsDeposit: TopadsDeposit? = TopadsDeposit(),
        @SerializedName("notifications")
    @Expose
    var notifications: Notifications? = Notifications()
)