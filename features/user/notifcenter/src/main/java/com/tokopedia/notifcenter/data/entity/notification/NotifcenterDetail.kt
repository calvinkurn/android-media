package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class NotifcenterDetail(
        @SerializedName("empty_state_content")
        val emptyStateContent: EmptyStateContent = EmptyStateContent(),
        @SerializedName("list")
        val list: List<Notification> = listOf(),
        @SerializedName("new_list")
        val newList: List<Notification> = listOf(),
        @SerializedName("options")
        val options: Options = Options(),
        @SerializedName("paging")
        val paging: Paging = Paging(),
        @SerializedName("user_info")
        val userInfo: UserInfo = UserInfo()
)