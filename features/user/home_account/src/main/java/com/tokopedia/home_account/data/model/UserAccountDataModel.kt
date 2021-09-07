package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.navigation_common.model.DebitInstantModel
import com.tokopedia.navigation_common.model.ProfileModel
import com.tokopedia.navigation_common.model.WalletModel

/**
 * Created by Yoris Prayogo on 20/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class UserAccountDataModel(

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