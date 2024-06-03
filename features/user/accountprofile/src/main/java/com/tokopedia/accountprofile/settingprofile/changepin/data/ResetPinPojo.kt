package com.tokopedia.accountprofile.settingprofile.changepin.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.accountprofile.settingprofile.addpin.data.AddChangePinData

/**
 * Created by Yoris Prayogo on 23/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class ResetPinResponse(
    @SerializedName("reset_pin")
    var data: AddChangePinData = AddChangePinData()
)
