package com.tokopedia.people.views.uimodel.event

import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

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
    data class ErrorFeedPosts(val throwable: Throwable) : UserProfileUiEvent
    data class ErrorVideoPosts(val throwable: Throwable) : UserProfileUiEvent
    data class ErrorLoadReview(val throwable: Throwable) : UserProfileUiEvent
    data class SuccessBlockUser(val isBlocking: Boolean) : UserProfileUiEvent
    data class ErrorBlockUser(val isBlocking: Boolean) : UserProfileUiEvent
    data class BlockingUserState(val throwable: Throwable) : UserProfileUiEvent

    object SuccessDeleteChannel : UserProfileUiEvent
    data class ErrorDeleteChannel(val throwable: Throwable) : UserProfileUiEvent

    data class OpenPlayVideoActionMenu(val channel: PlayWidgetChannelUiModel) : UserProfileUiEvent

    data class CopyLinkPlayVideo(val copyText: String) : UserProfileUiEvent

    data class OpenPerformancePlayChannel(val appLink: String) : UserProfileUiEvent

    data class ShowDeletePlayVideoConfirmationDialog(val channel: PlayWidgetChannelUiModel) : UserProfileUiEvent

    object ShowReviewOnboarding : UserProfileUiEvent

    data class ErrorLikeDislike(val throwable: Throwable) : UserProfileUiEvent

    data class OpenReviewMediaGalleryPage(
        val review: UserReviewUiModel.Review,
        val mediaPosition: Int,
    ) : UserProfileUiEvent
}
