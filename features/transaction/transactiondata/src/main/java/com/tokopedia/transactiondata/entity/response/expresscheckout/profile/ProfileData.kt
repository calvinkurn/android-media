package com.tokopedia.transactiondata.entity.response.expresscheckout.profile

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class ProfileData(
        @SerializedName("profiles")
        val profiles: ArrayList<Profile>,

        @SerializedName("default_profile_id")
        val defaultProfileId: Int
)