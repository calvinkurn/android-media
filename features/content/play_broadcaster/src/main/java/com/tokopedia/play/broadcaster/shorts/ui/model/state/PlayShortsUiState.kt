package com.tokopedia.play.broadcaster.shorts.ui.model.state

import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
data class PlayShortsUiState(
    val shortsId: String,
    val mediaUri: String,
    val accountList: List<ContentAccountUiModel>,
    val selectedAccount: ContentAccountUiModel,
    val menuList: List<DynamicPreparationMenu>,

    val titleForm: PlayShortsTitleFormUiState,
)

data class PlayShortsTitleFormUiState(
    val title: String,
    val state: State,
) {

    companion object {
        val Empty: PlayShortsTitleFormUiState
            get() = PlayShortsTitleFormUiState(
                title = "",
                state = State.Unknown,
            )
    }

    enum class State {
        Editing, Loading, Unknown
    }
}
