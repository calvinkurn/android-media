package com.tokopedia.people.views.uimodel.event

import com.tokopedia.people.views.uimodel.shoprecom.ShopRecomUiModelItem

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
sealed interface UserProfileUiEvent {
    data class ErrorLoadProfile(val throwable: Throwable) : UserProfileUiEvent

    data class ErrorFollowUnfollow(val message: String) : UserProfileUiEvent

    data class SuccessUpdateReminder(val message: String, val position: Int) : UserProfileUiEvent
    data class ErrorUpdateReminder(val throwable: Throwable) : UserProfileUiEvent

    data class SuccessFollowShopRecom(val data: ShopRecomUiModelItem): UserProfileUiEvent
}