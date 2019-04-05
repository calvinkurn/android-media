package com.tokopedia.notifcenter.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by stevenfredian on 25/03/19.
 */

data class NotifCenterSinglePojo(
        @Expose
        @SerializedName("notifcenter_singleList")
        val notifCenterList: NotifCenterList = NotifCenterList()
)

