package com.tokopedia.play.broadcaster.shorts.ui.model.action

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
sealed interface PlayShortsAction {

    data class PreparePage(
        val preferredAccountType: String
    ) : PlayShortsAction

    /** Title Form */
    object OpenTitleForm : PlayShortsAction

    data class SubmitTitle(
        val title: String,
    ) : PlayShortsAction

    object CloseTitleForm : PlayShortsAction
}
