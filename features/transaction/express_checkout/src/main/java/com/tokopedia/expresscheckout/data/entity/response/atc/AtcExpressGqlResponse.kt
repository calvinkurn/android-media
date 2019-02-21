package com.tokopedia.expresscheckout.data.entity.response.atc

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 13/12/18.
 */

data class AtcExpressGqlResponse(
        @SerializedName("atcExpress")
        val atcExpress: AtcResponse
)
