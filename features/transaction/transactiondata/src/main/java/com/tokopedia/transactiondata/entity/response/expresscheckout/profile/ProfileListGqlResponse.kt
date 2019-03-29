package com.tokopedia.transactiondata.entity.response.expresscheckout.profile

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 18/01/19.
 */

data class ProfileListGqlResponse(
        @SerializedName("get_profile_expresscheckout")
        val data: ProfileResponse
)