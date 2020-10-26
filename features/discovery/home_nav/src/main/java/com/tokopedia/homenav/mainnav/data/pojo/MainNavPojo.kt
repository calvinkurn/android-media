package com.tokopedia.homenav.mainnav.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceResponse
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo

data class MainNavPojo(
        @SerializedName("id")
        @Expose
        val id: String = "",
        var wallet: WalletBalanceModel = WalletBalanceModel(),
        var membership: MembershipPojo = MembershipPojo(),
        var shop: ShopInfoPojo = ShopInfoPojo()
)