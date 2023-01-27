package com.tokopedia.play.broadcaster.shorts.ui.model.state

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsMediaUiModel
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play_common.model.result.NetworkResult

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
data class PlayShortsUiState(
    val globalLoader: Boolean,
    val config: PlayShortsConfigUiModel,
    val media: PlayShortsMediaUiModel,
    val accountList: List<ContentAccountUiModel>,
    val selectedAccount: ContentAccountUiModel,
    val menuList: List<DynamicPreparationMenu>,

    val titleForm: PlayShortsTitleFormUiState,
    val coverForm: PlayShortsCoverFormUiState,
    val productSectionList: List<ProductTagSectionUiModel>,

    val tags: NetworkResult<Set<PlayTagUiModel>>,
    val uploadState: PlayShortsUploadUiState,
)

data class PlayShortsTitleFormUiState(
    val title: String,
    val state: State
) {

    companion object {
        val Empty: PlayShortsTitleFormUiState
            get() = PlayShortsTitleFormUiState(
                title = "",
                state = State.Unknown
            )
    }

    enum class State {
        Editing, Loading, Unknown
    }
}

data class PlayShortsCoverFormUiState(
    val cover: CoverSetupState,
    val state: State
) {
    val coverUri: String
        get() {
            if (cover is CoverSetupState.Cropped.Uploaded) {
                return if (cover.coverImage.toString().isNotEmpty() &&
                    cover.coverImage.toString().contains("http")
                ) {
                    cover.coverImage.toString()
                } else if (!cover.localImage?.toString().isNullOrEmpty()) {
                    cover.localImage.toString()
                } else {
                    ""
                }
            }

            return ""
        }

    companion object {
        val Empty: PlayShortsCoverFormUiState
            get() = PlayShortsCoverFormUiState(
                cover = CoverSetupState.Blank,
                state = State.Unknown
            )
    }

    enum class State {
        Editing, Unknown
    }
}

sealed interface PlayShortsUploadUiState {

    object Unknown : PlayShortsUploadUiState

    object Loading : PlayShortsUploadUiState

    object Success : PlayShortsUploadUiState

    data class Error(val throwable: Throwable) : PlayShortsUploadUiState
}
