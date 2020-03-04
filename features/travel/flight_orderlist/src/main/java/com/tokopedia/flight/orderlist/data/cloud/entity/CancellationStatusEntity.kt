package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/08/2019
 */
class CancellationStatusEntity(@SerializedName("status_str")
                               @Expose
                               val statusStr: String = "",
                               @SerializedName("status_type")
                               @Expose
                               val statusType: String = "",
                               @SerializedName("status")
                               @Expose
                               val status: Int = 0)