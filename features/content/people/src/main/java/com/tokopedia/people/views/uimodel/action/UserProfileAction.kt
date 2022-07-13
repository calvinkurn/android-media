package com.tokopedia.people.views.uimodel.action

import com.tokopedia.people.views.uimodel.shoprecom.ShopRecomUiModelItem

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
sealed interface UserProfileAction {

    data class LoadProfile(val isRefresh: Boolean = false) : UserProfileAction

    data class LoadContent(val cursor: String) : UserProfileAction

    data class ClickFollowButton(val isFromLogin: Boolean) : UserProfileAction

    data class ClickUpdateReminder(val isFromLogin: Boolean) : UserProfileAction

    data class ClickFollowButtonShopRecom(val data: ShopRecomUiModelItem) : UserProfileAction

    data class SaveReminderActivityResult(
        val channelId: String,
        val position: Int,
        val isActive: Boolean,
    ) : UserProfileAction

    object RemoveReminderActivityResult : UserProfileAction
}