package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList
import com.tokopedia.navigation_common.model.*
import com.tokopedia.user_identification_common.domain.pojo.KycStatusPojo

/**
 * Created by Yoris Prayogo on 20/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class UserAccountDataModel(

        @SerializedName("wallet")
        @Expose
        var wallet: WalletModel = WalletModel(),

        @SerializedName("profile")
        @Expose
        var profile: ProfileModel = ProfileModel(),

        @SerializedName("tokopoints")
        @Expose
        var tokopoints: TokopointsModel = TokopointsModel(),

        @SerializedName("openDebitSettings")
        @Expose
        var debitInstant: DebitInstantModel = DebitInstantModel(),

        @SerializedName("vcc_user_status")
        @Expose
        var vccUserStatus: VccUserStatus = VccUserStatus(),

        @SerializedName("isAffiliate")
        @Expose
        var isAffiliate: Boolean = false,

        @SerializedName("balance")
        @Expose
        var saldo: DepositDataModel = DepositDataModel(),

        var shortcutResponse: ShortcutResponse = ShortcutResponse()
)