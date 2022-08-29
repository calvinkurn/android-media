package com.tokopedia.play.broadcaster.ui.action

/**
 * Created By : Jonathan Darwin on January 25, 2022
 */
sealed class PlayBroadcastPreparationAction {
    /** Title Form */
    data class SubmitTitleForm(val title: String): PlayBroadcastPreparationAction()
}