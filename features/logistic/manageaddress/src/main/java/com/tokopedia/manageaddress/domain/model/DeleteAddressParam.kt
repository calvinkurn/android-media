package com.tokopedia.manageaddress.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

/**
 * Created by ekades on 24/09/22.
 */

data class DeleteAddressParam(
    @SerializedName("inputAddressId")
    val inputAddressId: Long,
    @SerializedName("isTokonowRequest")
    val isTokonowRequest: Boolean
) : GqlParam
