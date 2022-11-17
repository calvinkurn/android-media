package com.tokopedia.play.broadcaster.shorts.ui.model

import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel

/**
 * Created By : Jonathan Darwin on November 14, 2022
 */
data class PlayShortsConfigUiModel(
    val shortsId: String,
    val shortsAllowed: Boolean,
    val tncList: List<TermsAndConditionUiModel>,
    val maxTitleCharacter: Int
) {
    companion object {
        val Empty: PlayShortsConfigUiModel
            get() = PlayShortsConfigUiModel(
                shortsId = "",
                shortsAllowed = false,
                tncList = emptyList(),
                maxTitleCharacter = 24
            )
    }
}
