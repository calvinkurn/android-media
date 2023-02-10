package com.tokopedia.people.views.uimodel.event

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
sealed interface UserProfileUiEvent {
    data class SuccessLoadTabs(val isEmptyContent: Boolean) : UserProfileUiEvent
    data class SuccessUpdateReminder(val message: String) : UserProfileUiEvent

    data class ErrorLoadProfile(val throwable: Throwable) : UserProfileUiEvent
    data class ErrorFollowUnfollow(val throwable: Throwable) : UserProfileUiEvent
    data class ErrorUpdateReminder(val throwable: Throwable) : UserProfileUiEvent
    data class ErrorGetProfileTab(val throwable: Throwable) : UserProfileUiEvent
    data class ErrorLoadNextPageShopRecom(val throwable: Throwable) : UserProfileUiEvent
    data class ErrorFeedPosts(val throwable: Throwable): UserProfileUiEvent
    data class ErrorVideoPosts(val throwable: Throwable): UserProfileUiEvent

    data class SuccessBlockUser(val isBlocking: Boolean) : UserProfileUiEvent
    data class ErrorBlockUser(val isBlocking: Boolean) : UserProfileUiEvent
    data class BlockingUserState(val throwable: Throwable): UserProfileUiEvent

    data class ErrorDeleteChannel(val throwable: Throwable) : UserProfileUiEvent
}
