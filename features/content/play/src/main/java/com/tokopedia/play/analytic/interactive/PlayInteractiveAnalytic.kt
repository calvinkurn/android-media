package com.tokopedia.play.analytic.interactive

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel

/**
 * Created by jegul on 09/07/21
 */
interface PlayInteractiveAnalytic {

    fun clickFollowShopInteractive(
        channelId: String,
        channelType: PlayChannelType,
        interactiveId: String,
        interactiveType: InteractiveUiModel,
        shopId: String,
    )

    fun impressFollowShopInteractive(
        channelId: String,
        interactiveId: String,
        shopId: String,
    )

    fun clickWinnerBadge(channelId: String, channelType: PlayChannelType, shopId: String, interactiveType: InteractiveUiModel, interactiveId: String)

    fun impressWinnerBadge(channelId: String, shopId: String, interactiveId: String)

    fun clickTapTap(channelId: String, channelType: PlayChannelType, interactiveId: String)

    fun clickRefreshLeaderBoard(channelId: String, interactiveId: String, shopId: String)

    fun impressRefreshLeaderBoard(channelId: String, interactiveId: String, shopId: String)

    fun clickQuizOption(channelId: String, choiceAlphabet: String, interactiveId: String, shopId: String)

    fun impressQuizOptions(channelId: String, interactiveId: String, shopId: String)

    fun clickActiveInteractive(channelId: String, interactiveId: String, shopId: String)

    fun impressActiveInteractive(channelId: String, interactiveId: String, shopId: String)

    fun impressLeaderBoard(channelId: String, interactiveId: String, shopId: String)
}