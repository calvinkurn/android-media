package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by alvinatin on 21/08/18.
 */

data class NotifCenterPojo(
        @Expose
        @SerializedName("notifcenter_list")
        val notifCenterList: NotifCenterList = NotifCenterList()
)

