package com.tokopedia.instantdebitbca.data.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 26/03/19.
 */
data class DebitRegisterBcaEntity (
    @SerializedName("callbackURL")
    @Expose
    val callbackUrl: String? = "",
    @SerializedName("debitData")
    @Expose
    val debitData: String? = ""
)
