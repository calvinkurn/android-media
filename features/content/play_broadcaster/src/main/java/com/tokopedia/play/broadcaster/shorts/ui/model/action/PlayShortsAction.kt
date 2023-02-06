package com.tokopedia.play.broadcaster.shorts.ui.model.action

import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.view.state.CoverSetupState

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
sealed interface PlayShortsAction {

    data class PreparePage(
        val preferredAccountType: String
    ) : PlayShortsAction

    /** Media */
    data class SetMedia(val mediaUri: String) : PlayShortsAction

    /** Account */
    object ClickSwitchAccount : PlayShortsAction
    data class SwitchAccount(val isRefreshAccountList: Boolean) : PlayShortsAction

    /** Title Form */
    object OpenTitleForm : PlayShortsAction

    data class UploadTitle(
        val title: String
    ) : PlayShortsAction

    object CloseTitleForm : PlayShortsAction

    /** Cover Form */
    object OpenCoverForm : PlayShortsAction

    data class SetCover(
        val cover: CoverSetupState
    ) : PlayShortsAction

    object CloseCoverForm : PlayShortsAction

    /** Product */
    data class SetProduct(
        val productSectionList: List<ProductTagSectionUiModel>
    ) : PlayShortsAction

    object ClickNext : PlayShortsAction

    /** Summary */
    object LoadTag : PlayShortsAction

    data class SelectTag(
        val tag: PlayTagUiModel,
    ) : PlayShortsAction

    object ClickUploadVideo : PlayShortsAction

    /** Others */
    object SetNotFirstSwitchAccount : PlayShortsAction
}
