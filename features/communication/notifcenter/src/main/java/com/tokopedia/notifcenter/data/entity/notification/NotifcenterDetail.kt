package com.tokopedia.notifcenter.data.entity.notification

import com.google.gson.annotations.SerializedName
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel

data class NotifcenterDetail(
        @SerializedName("empty_state_content")
        val emptyStateContent: EmptyStateContent = EmptyStateContent(),
        @SerializedName("list")
        val list: List<NotificationUiModel> = listOf(),
        @SerializedName("new_list")
        val newList: List<NotificationUiModel> = listOf(),
        @SerializedName("options")
        val options: Options = Options(),
        @SerializedName("paging")
        val paging: Paging = Paging(),
        @SerializedName("new_paging")
        val newPaging: Paging = Paging(),
        @SerializedName("user_info")
        val userInfo: UserInfo = UserInfo()
)