package com.tokopedia.manageaddress.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

/**
 * Created by irpan on 20/06/22.
 */

data class DefaultAddressParam(
    @SerializedName("inputAddressId")
    val inputAddressId: Long,
    @SerializedName("setAsStateChosenAddress")
    val setAsStateChosenAddress: Boolean,
    @SerializedName("isTokonowRequest")
    val isTokonowRequest: Boolean
) : GqlParam
