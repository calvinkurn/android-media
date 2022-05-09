package com.tokopedia.play.analytic.interactive

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel

/**
 * Created by jegul on 09/07/21
 */
interface PlayInteractiveAnalytic {

    fun clickFollowShopInteractive(
        channelId: String,
        channelType: PlayChannelType,
        interactiveId: String,
        isForQuiz: Boolean,
        shopId: String
    )

    fun clickWinnerBadge(channelId: String, channelType: PlayChannelType, shopId: String, isForQuiz: Boolean, interactiveId: String)

    fun clickTapTap(channelId: String, channelType: PlayChannelType, interactiveId: String)

    fun clickRefreshLeaderBoard(channelId: String, interactiveId: String, shopId: String)

    fun clickQuizOption(channelId: String, choiceAlphabet: String, interactiveId: String, shopId: String)

    fun clickActiveInteractive(channelId: String, interactiveId: String, shopId: String)
}