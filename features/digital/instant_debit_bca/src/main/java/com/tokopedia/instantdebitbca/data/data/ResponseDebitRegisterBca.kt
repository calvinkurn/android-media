package com.tokopedia.instantdebitbca.data.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 26/03/19.
 */
class ResponseDebitRegisterBca {
    @SerializedName("notifyDebitRegister")
    @Expose
    val notifyDebitRegister: NotifyDebitRegisterBcaEntity? = null
}
