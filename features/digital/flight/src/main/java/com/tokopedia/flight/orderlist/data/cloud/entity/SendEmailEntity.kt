package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by alvarisi on 2/7/18.
 */

class SendEmailEntity(
        @SerializedName("meta")
        var meta: MetaEntity) {

    inner class MetaEntity {
        @SerializedName("status")
        var status: String? = null
    }
}
