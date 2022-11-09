package com.tokopedia.play.broadcaster.shorts.ui.model.action

import com.tokopedia.play.broadcaster.view.state.CoverSetupState

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
        val title: String
    ) : PlayShortsAction

    object CloseTitleForm : PlayShortsAction

    /** Cover Form */
    object OpenCoverForm : PlayShortsAction

    data class SetCover(
        val cover: CoverSetupState
    ) : PlayShortsAction

    object CloseCoverForm : PlayShortsAction

    object ClickNext : PlayShortsAction
}
