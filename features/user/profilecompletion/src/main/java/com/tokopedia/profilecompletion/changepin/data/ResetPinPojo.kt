package com.tokopedia.profilecompletion.changepin.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.profilecompletion.addpin.data.AddChangePinData

/**
 * Created by Yoris Prayogo on 23/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class ResetPinResponse(
    @SerializedName("reset_pin")
    @Expose
    var data: AddChangePinData = AddChangePinData()
)