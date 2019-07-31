package com.tokopedia.navigation.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 11/04/19
 */
data class NotificationUpdateItem(

        @SerializedName("notif_id")
        @Expose
        var notifId: String = "",
        @SerializedName("user_id")
        @Expose
        var userId: String = "",
        @SerializedName("shop_id")
        @Expose
        var shopId: String = "",
        @SerializedName("section_id")
        @Expose
        var sectionId: String = "",
        @SerializedName("section_icon")
        @Expose
        var sectionIcon: String? = "",
        @SerializedName("section_key")
        @Expose
        var sectionKey: String = "",
        @SerializedName("subsection_key")
        @Expose
        var subsectionKey: String = "",
        @SerializedName("template_key")
        @Expose
        var templateKey: String = "",
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("short_description")
        @Expose
        var shortDescription: String = "",
        @SerializedName("content")
        @Expose
        var content: String = "",
        @SerializedName("type_of_user")
        @Expose
        var typeOfUser: Int = 0,
        @SerializedName("create_time")
        @Expose
        var createTime: String = "",
        @SerializedName("create_time_unix")
        @Expose
        var createTimeUnix: Int = 0,
        @SerializedName("update_time")
        @Expose
        var updateTime: String = "",
        @SerializedName("update_time_unix")
        @Expose
        var updateTimeUnix: Long = 0,
        @SerializedName("status")
        @Expose
        var status: Long = 0,
        @SerializedName("read_status")
        @Expose
        var readStatus: Long = 0,
        @SerializedName("data_notification")
        @Expose
        var dataNotification: DataNotification
)


data class DataNotification(
        @Expose
        @SerializedName("app_link")
        val appLink: String = "",
        @Expose
        @SerializedName("desktop_link")
        val desktopLink: String = "",
        @Expose
        @SerializedName("info_thumbnail_url")
        val infoThumbnailUrl: String = "",
        @Expose
        @SerializedName("mobile_link")
        val mobileLink: String = ""
)