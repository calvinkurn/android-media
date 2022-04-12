package com.tokopedia.play.analytic.campaign

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel

/**
 * @author by astidhiyaa on 21/03/22
 */
interface PlayCampaignAnalytic {
    fun clickUpcomingReminder(sectionInfo: ProductSectionUiModel.Section, channelId: String, channelType: PlayChannelType)

    fun impressUpcomingReminder(sectionInfo: ProductSectionUiModel.Section, channelId: String, channelType: PlayChannelType)
}