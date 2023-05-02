package com.tokopedia.home_account.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.home_account.privacy_account.data.LinkStatusPojo
import com.tokopedia.navigation_common.model.DebitInstantModel
import com.tokopedia.navigation_common.model.ProfileModel

/**
 * Created by Yoris Prayogo on 20/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class UserAccountDataModel(
    @SerializedName("profile")
    var profile: ProfileModel = ProfileModel(),
    @SerializedName("openDebitSettings")
    var debitInstant: DebitInstantModel = DebitInstantModel(),
    var shortcutResponse: ShortcutResponse = ShortcutResponse(),
    var linkStatus: LinkStatusPojo = LinkStatusPojo(),
    var offerInterrupt: OfferInterruptData = OfferInterruptData()
)
