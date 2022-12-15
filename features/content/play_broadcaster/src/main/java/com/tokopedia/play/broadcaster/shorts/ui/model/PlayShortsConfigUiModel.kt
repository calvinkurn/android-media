package com.tokopedia.play.broadcaster.shorts.ui.model

import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel

/**
 * Created By : Jonathan Darwin on November 14, 2022
 */
data class PlayShortsConfigUiModel(
    val shortsId: String,
    val shortsAllowed: Boolean,
    val tncList: List<TermsAndConditionUiModel>,
    val maxTitleCharacter: Int,
    val maxTaggedProduct: Int,
    val shortsVideoSourceId: String
) {
    companion object {

        private const val DEFAULT_MAX_TITLE_CHARACTER = 24

        val Empty: PlayShortsConfigUiModel
            get() = PlayShortsConfigUiModel(
                shortsId = "",
                shortsAllowed = false,
                tncList = emptyList(),
                maxTitleCharacter = DEFAULT_MAX_TITLE_CHARACTER,
                maxTaggedProduct = 0,
                shortsVideoSourceId = ""
            )
    }
}
