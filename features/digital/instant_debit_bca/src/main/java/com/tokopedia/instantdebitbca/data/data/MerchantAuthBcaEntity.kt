package com.tokopedia.instantdebitbca.data.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 25/03/19.
 */
class MerchantAuthBcaEntity {
    @SerializedName("data")
    @Expose
    val dataToken: DataTokenBcaEntity? = null
}
