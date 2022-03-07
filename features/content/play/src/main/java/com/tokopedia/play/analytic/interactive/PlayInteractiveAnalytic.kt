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
        interactiveId: String
    )

    fun clickWinnerBadge(channelId: String, channelType: PlayChannelType)

    fun clickTapTap(channelId: String, channelType: PlayChannelType, interactiveId: String)

    fun clickUpcomingReminder(sectionInfo: ProductSectionUiModel.Section, channelId: String, channelType: PlayChannelType)
}