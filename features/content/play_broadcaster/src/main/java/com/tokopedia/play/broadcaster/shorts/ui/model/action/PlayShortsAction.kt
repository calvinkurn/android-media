package com.tokopedia.play.broadcaster.shorts.ui.model.action

import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel

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
    object SwitchAccount : PlayShortsAction

    /** Title Form */
    data class UploadTitle(
        val title: String
    ) : PlayShortsAction

    /** Cover Form */
    object UpdateCover: PlayShortsAction

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
    object SetShowSetupCoverCoachMark : PlayShortsAction
    data class SetCoverUploadedSource(
        val source: Int
    ) : PlayShortsAction
}
