package com.tokopedia.purchase_platform.express_checkout.data.entity.response.profile

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 18/01/19.
 */

data class ProfileListGqlResponse(
        @SerializedName("get_profile_expresscheckout")
        val data: ProfileResponse
)