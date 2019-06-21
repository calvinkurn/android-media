package com.tokopedia.notifcenter.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserNotification(
        @SerializedName("template_key")
        @Expose
        val templateKey: String = "",
        @SerializedName("notif_id")
        @Expose
        val notifId: String = "",
        @SerializedName("user_id")
        @Expose
        val userId: Int = 0,
        @SerializedName("shop_id")
        @Expose
        val shopId: Int = 0,
        @SerializedName("section_key")
        @Expose
        val sectionKey: String = "",
        @SerializedName("subsection_key")
        @Expose
        val subsectionKey: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("short_description")
        @Expose
        val shortDescription: String = "",
        @SerializedName("content")
        @Expose
        val content: String = "",
        @SerializedName("type_of_user")
        @Expose
        val typeOfUser: Int = 0,
        @SerializedName("create_time")
        @Expose
        val createTime: String = "",
        @SerializedName("create_time_unix")
        @Expose
        val createTimeUnix: Int = 0,
        @SerializedName("update_time")
        @Expose
        val updateTime: String = "",
        @SerializedName("update_time_unix")
        @Expose
        val updateTimeUnix: Int = 0,
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("read_status")
        @Expose
        val readStatus: Int = 0,
        @SerializedName("data_notification")
        @Expose
        val dataNotification: DataNotification = DataNotification()
)