package com.tokopedia.notifcenter.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by alvinatin on 21/08/18.
 */
data class NotifCenterPojo(
        @Expose
        @SerializedName("notifcenter_list")
        val notifCenterList: NotifCenterList? = NotifCenterList()
)

data class NotifCenterList(
        @Expose
        @SerializedName("paging")
        val paging: Paging? = Paging(),
        @Expose
        @SerializedName("list")
        val list: List<UserNotification>? = ArrayList()
)

data class UserNotification(
        @SerializedName("notif_id")
        @Expose
        val notifId: String? = "",
        @SerializedName("user_id")
        @Expose
        val userId: Int? = 0,
        @SerializedName("shop_id")
        @Expose
        val shopId: Int? = 0,
        @SerializedName("section_key")
        @Expose
        val sectionKey: String? = "",
        @SerializedName("subsection_key")
        @Expose
        val subsectionKey: String? = "",
        @SerializedName("title")
        @Expose
        val title: String? = "",
        @SerializedName("short_description")
        @Expose
        val shortDescription: String? = "",
        @SerializedName("content")
        @Expose
        val content: String? = "",
        @SerializedName("type_of_user")
        @Expose
        val typeOfUser: Int? = 0,
        @SerializedName("create_time")
        @Expose
        val createTime: String? = "",
        @SerializedName("create_time_unix")
        @Expose
        val createTimeUnix: Int? = 0,
        @SerializedName("update_time")
        @Expose
        val updateTime: String? = "",
        @SerializedName("update_time_unix")
        @Expose
        val updateTimeUnix: Int? = 0,
        @SerializedName("status")
        @Expose
        val status: Int? = 0,
        @SerializedName("read_status")
        @Expose
        val readStatus: Int? = 0,
        @SerializedName("data_notification")
        @Expose
        val dataNotification: DataNotification? = DataNotification()
)

data class DataNotification(
        @SerializedName("app_link")
        @Expose
        val appLink: String? = "",
        @SerializedName("desktop_link")
        @Expose
        val desktopLink: String? = "",
        @SerializedName("info_thumbnail_url")
        @Expose
        val infoThumbnailUrl: String? = "",
        @SerializedName("mobile_link")
        @Expose
        val mobileLink: String? = ""
)

data class Paging(
        @SerializedName("has_next")
        @Expose
        val hasNext: Boolean? = false,
        @SerializedName("has_prev")
        @Expose
        val hasPrev: Boolean? = false
)