package com.tokopedia.talk_old.talkdetails.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.talk_old.common.domain.pojo.TalkCommentItem
import com.tokopedia.talk_old.common.domain.pojo.TalkState

data class TalkDetailsPojo(
        @Expose
        @SerializedName("list")
        val list: List<TalkCommentItem> = ArrayList(),
        @Expose
        @SerializedName("talk")
        val talk: TalkDetailsItemPojo = TalkDetailsItemPojo()
)

data class TalkDetailsItemPojo(
        @Expose
        @SerializedName("talk_create_time")
        val talk_create_time: String = "",
        @Expose
        @SerializedName("talk_create_time_fmt")
        val talk_create_time_fmt: String = "",
        @Expose
        @SerializedName("talk_create_time_list")
        val talk_create_time_list: CreateTimeList = CreateTimeList(),
        @Expose
        @SerializedName("talk_follow_status")
        val talk_follow_status: Int = 0,
        @Expose
        @SerializedName("talk_id")
        val talk_id: String = "",
        @Expose
        @SerializedName("talk_inbox_id")
        val talk_inbox_id: String = "",
        @Expose
        @SerializedName("talk_message")
        val talk_message: String = "",
        @Expose
        @SerializedName("talk_own")
        val talk_own: Int = 0,
        @Expose
        @SerializedName("talk_product_id")
        val talk_product_id: String = "",
        @Expose
        @SerializedName("talk_product_image")
        val talk_product_image: String = "",
        @Expose
        @SerializedName("talk_product_name")
        val talk_product_name: String = "",
        @Expose
        @SerializedName("talk_product_url")
        val talk_product_url: String = "",
        @Expose
        @SerializedName("talk_product_status")
        val talk_product_status: Int = 0,
        @Expose
        @SerializedName("talk_raw_message")
        val talk_raw_message: String = "",
        @Expose
        @SerializedName("talk_read_status")
        val talk_read_status: Int = 0,
        @Expose
        @SerializedName("talk_shop_id")
        val talk_shop_id: String = "",
        @Expose
        @SerializedName("talk_state")
        val talk_state: TalkState = TalkState(),
        @Expose
        @SerializedName("talk_total_comment")
        val talk_total_comment: String = "",
        @Expose
        @SerializedName("talk_user_id")
        val talk_user_id: String = "",
        @Expose
        @SerializedName("talk_user_image")
        val talk_user_image: String = "",
        @Expose
        @SerializedName("talk_user_label")
        val talk_user_label: String = "",
        @Expose
        @SerializedName("talk_user_label_id")
        val talk_user_label_id: String = "",
        @Expose
        @SerializedName("talk_user_name")
        val talk_user_name: String = "",
        @Expose
        @SerializedName("talk_user_url")
        val talk_user_url: String = ""
)

data class CreateTimeList(
        @Expose
        @SerializedName("date_time_android")
        val date_time_android: String = "",
        @Expose
        @SerializedName("date_time_ios")
        val date_time_ios: String = ""
)