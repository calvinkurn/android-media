package com.tokopedia.wallet.ovoactivation.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 24/09/18.
 */
class ResponseCheckPhoneEntity (
    @SerializedName("check_phone")
    @Expose
    val checkPhoneOvoEntity: CheckPhoneOvoEntity? = null)
