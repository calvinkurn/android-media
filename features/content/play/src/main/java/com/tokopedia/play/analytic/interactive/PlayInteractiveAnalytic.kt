package com.tokopedia.play.analytic.interactive

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel

/**
 * @author by astidhiyaa on 17/06/22
 */
interface PlayInteractiveAnalytic {
    fun clickFollowShopInteractive(
        interactiveId: String,
        interactiveType: InteractiveUiModel,
        shopId: String,
        channelId: String,
        channelType: PlayChannelType,
    )

    fun impressFollowShopInteractive(
        shopId: String,
        interactiveType: InteractiveUiModel,
        channelId: String,
    )

    fun clickWinnerBadge(shopId: String, interactiveType: InteractiveUiModel, interactiveId: String,
                         channelId: String, channelType: PlayChannelType,
    )

    fun impressWinnerBadge(
        shopId: String,
        interactiveId: String,
        channelId: String,
    )

    fun clickTapTap(
        interactiveId: String,
        channelId: String,
        channelType: PlayChannelType,
    )

    fun clickRefreshLeaderBoard(
        interactiveId: String,
        shopId: String,
        channelId: String,
    )

    fun impressRefreshLeaderBoard(
        interactiveId: String,
        shopId: String,
        channelId: String,
    )

    fun clickQuizOption(
        choiceAlphabet: String,
        interactiveId: String,
        shopId: String,
        channelId: String,
    )

    fun impressQuizOptions(
        interactiveId: String,
        shopId: String,
        channelId: String,
    )

    fun clickActiveInteractive(
        interactiveId: String,
        shopId: String,
        interactiveType: InteractiveUiModel,
        channelId: String,
    )

    fun impressActiveInteractive(
        interactiveId: String,
        shopId: String,
        channelId: String
    )

    fun impressLeaderBoard(interactiveId: String, shopId: String, channelId: String)
}