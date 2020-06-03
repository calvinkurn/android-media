package com.tokopedia.talk_old.common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 9/3/18.
 */

data class InboxTalkPojo(
        @Expose
        @SerializedName("unread_count")
        val unreadCount: UnreadCount = UnreadCount(),
        @Expose
        @SerializedName("list")
        val list: List<InboxTalkItemPojo> = ArrayList(),
        @Expose
        @SerializedName("paging")
        val paging: Paging = Paging()
)

data class InboxTalkItemPojo(
        @Expose
        @SerializedName("talk_comment_list")
        val list: List<TalkCommentItem> = ArrayList(),
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
        @SerializedName("talk_product_url")
        val talk_product_url: String = "",
        @Expose
        @SerializedName("talk_product_image")
        val talk_product_image: String = "",
        @Expose
        @SerializedName("talk_product_name")
        val talk_product_name: String = "",
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
        val talk_user_label_id: Int = 0,
        @Expose
        @SerializedName("talk_user_name")
        val talk_user_name: String = "",
        @Expose
        @SerializedName("talk_user_url")
        val talk_user_url: String = ""
)

data class TalkCommentItem(
        @Expose
        @SerializedName("comment_create_time")
        val comment_create_time: String = "",
        @Expose
        @SerializedName("comment_create_time_fmt")
        val comment_create_time_fmt: String = "",
        @Expose
        @SerializedName("comment_create_time_list")
        val comment_create_time_list: CreateTimeList = CreateTimeList(),
        @Expose
        @SerializedName("comment_id")
        val comment_id: String = "",
        @Expose
        @SerializedName("comment_is_moderator")
        val comment_is_moderator: Int = 0,
        @Expose
        @SerializedName("comment_is_owner")
        val comment_is_owner: Int = 0,
        @Expose
        @SerializedName("comment_is_seller")
        val comment_is_seller: Int = 0,
        @Expose
        @SerializedName("comment_message")
        val comment_message: String = "",
        @Expose
        @SerializedName("comment_products")
        val listProduct: List<CommentProduct> = ArrayList(),
        @Expose
        @SerializedName("comment_raw_message")
        val comment_raw_message: String = "",
        @Expose
        @SerializedName("comment_shop_id")
        val comment_shop_id: String = "",
        @Expose
        @SerializedName("comment_shop_image")
        val comment_shop_image: String = "",
        @Expose
        @SerializedName("comment_shop_name")
        val comment_shop_name: String = "",
        @Expose
        @SerializedName("comment_state")
        val comment_state: CommentState = CommentState(),
        @Expose
        @SerializedName("comment_talk_id")
        val comment_talk_id: String = "",
        @Expose
        @SerializedName("comment_user_image")
        val comment_user_image: String = "",
        @Expose
        @SerializedName("comment_user_id")
        val comment_user_id: String = "",
        @Expose
        @SerializedName("comment_user_label")
        val comment_user_label: String = "",
        @Expose
        @SerializedName("comment_user_label_id")
        val comment_user_label_id: Int = 0,
        @Expose
        @SerializedName("comment_user_name")
        val comment_user_name: String = ""
)

data class CommentProduct(
        @Expose
        @SerializedName("product_id")
        val product_id: Int = 0,
        @Expose
        @SerializedName("product_image")
        val product_image: String = "",
        @Expose
        @SerializedName("product_name")
        val product_name: String = "",
        @Expose
        @SerializedName("product_price")
        val product_price: String = "",
        @Expose
        @SerializedName("product_status")
        val product_status: Int = 0,
        @Expose
        @SerializedName("product_url")
        val product_url: String = ""
)

data class CreateTimeList(
        @Expose
        @SerializedName("date_time_android")
        val date_time_android: String = "",
        @Expose
        @SerializedName("date_time_ios")
        val date_time_ios: String = ""
)


data class TalkState(
        @Expose
        @SerializedName("allow_report")
        val allow_report: Boolean = false,
        @Expose
        @SerializedName("allow_delete")
        val allow_delete: Boolean = false,
        @Expose
        @SerializedName("allow_follow")
        val allow_follow: Boolean = false,
        @Expose
        @SerializedName("allow_unfollow")
        val allow_unfollow: Boolean = false,
        @Expose
        @SerializedName("allow_unmasked")
        val allow_unmasked: Boolean = false,
        @Expose
        @SerializedName("allow_reply")
        val allow_reply: Boolean = false,
        @Expose
        @SerializedName("reported")
        val reported: Boolean = false,
        @Expose
        @SerializedName("masked")
        val masked: Boolean = false
)

data class CommentState(
        @Expose
        @SerializedName("allow_report")
        val allow_report: Boolean = false,
        @Expose
        @SerializedName("allow_delete")
        val allow_delete: Boolean = false,
        @Expose
        @SerializedName("allow_follow")
        val allow_follow: Boolean = false,
        @Expose
        @SerializedName("allow_unfollow")
        val allow_unfollow: Boolean = false,
        @Expose
        @SerializedName("allow_unmasked")
        val allow_unmasked: Boolean = false,
        @Expose
        @SerializedName("allow_reply")
        val allow_reply: Boolean = false,
        @Expose
        @SerializedName("reported")
        val reported: Boolean = false,
        @Expose
        @SerializedName("masked")
        val masked: Boolean = false
)

data class Paging(
        @Expose
        @SerializedName("has_next")
        val has_next: Boolean = false,
        @Expose
        @SerializedName("page_id")
        val page_id: Int = 0
)


data class UnreadCount(
        @Expose
        @SerializedName("all")
        var all: Int = 0,
        @Expose
        @SerializedName("my_product")
        var my_product: Int = 0,
        @Expose
        @SerializedName("following")
        var following: Int = 0
)