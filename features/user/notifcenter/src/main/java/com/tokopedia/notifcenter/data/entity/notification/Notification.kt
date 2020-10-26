package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class Notification(
        @SerializedName("bottomsheet")
        val bottomsheet: Bottomsheet = Bottomsheet(),
        @SerializedName("button_text")
        val buttonText: String = "",
        @SerializedName("content")
        val content: String = "",
        @SerializedName("create_time")
        val createTime: String = "",
        @SerializedName("create_time_unix")
        val createTimeUnix: Int = 0,
        @SerializedName("data_notification")
        val dataNotification: DataNotification = DataNotification(),
        @SerializedName("expire_time")
        val expireTime: String = "",
        @SerializedName("expire_time_unix")
        val expireTimeUnix: Int = 0,
        @SerializedName("is_longer_content")
        val isLongerContent: Boolean = false,
        @SerializedName("notif_id")
        val notifId: String = "",
        @SerializedName("product_data")
        val productData: List<ProductData> = listOf(),
        @SerializedName("read_status")
        val readStatus: Int = 0,
        @SerializedName("section_icon")
        val sectionIcon: String = "",
        @SerializedName("section_id")
        val sectionId: String = "",
        @SerializedName("section_key")
        val sectionKey: String = "",
        @SerializedName("shop_id")
        val shopId: Int = 0,
        @SerializedName("short_description")
        val shortDescription: String = "",
        @SerializedName("short_description_html")
        val shortDescriptionHtml: String = "",
        @SerializedName("show_bottomsheet")
        val showBottomsheet: Boolean = false,
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("subsection_key")
        val subsectionKey: String = "",
        @SerializedName("template_key")
        val templateKey: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("total_product")
        val totalProduct: Int = 0,
        @SerializedName("type_bottomsheet")
        val typeBottomsheet: Int = 0,
        @SerializedName("type_link")
        val typeLink: Int = 0,
        @SerializedName("type_of_user")
        val typeOfUser: Int = 0,
        @SerializedName("update_time")
        val updateTime: String = "",
        @SerializedName("update_time_unix")
        val updateTimeUnix: Int = 0,
        @SerializedName("user_id")
        val userId: Int = 0
)