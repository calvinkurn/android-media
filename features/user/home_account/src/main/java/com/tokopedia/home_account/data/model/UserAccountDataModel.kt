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

        @SerializedName("openDebitSettings")
        @Expose
        var debitInstant: DebitInstantModel = DebitInstantModel(),

        @SerializedName("isAffiliate")
        @Expose
        var isAffiliate: Boolean = false,

        var shortcutResponse: ShortcutResponse = ShortcutResponse()
)