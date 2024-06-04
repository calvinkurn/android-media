package com.tokopedia.feedplus.browse.data.tracker

import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.browse.data.model.BannerWidgetModel
import com.tokopedia.feedplus.browse.data.model.StoryNodeModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.model.SlotInfo
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel

/**
 * Created by meyta.taliti on 01/09/23.
 */

/**
 * thanos link: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4134
 */
internal interface FeedBrowseTracker {

    fun viewChannelCard(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        slotInfo: SlotInfo,
        channelPositionInList: Int
    )

    fun clickChannelCard(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        slotInfo: SlotInfo,
        channelPositionInList: Int
    )

    fun viewChipsWidget(
        item: WidgetMenuModel,
        slotInfo: SlotInfo,
        chipPositionInList: Int
    )

    fun clickChipsWidget(
        item: WidgetMenuModel,
        slotInfo: SlotInfo,
        chipPositionInList: Int
    )

    fun clickBackExitBrowsePage()

    fun viewInspirationBanner(
        item: BannerWidgetModel,
        slotInfo: SlotInfo,
        bannerPositionInList: Int
    )

    fun clickInspirationBanner(
        item: BannerWidgetModel,
        slotInfo: SlotInfo,
        bannerPositionInList: Int
    )

    fun viewAuthorWidget(
        item: AuthorWidgetModel,
        slotInfo: SlotInfo,
        widgetPositionInList: Int
    )

    fun clickAuthorChannelCard(
        item: AuthorWidgetModel,
        slotInfo: SlotInfo,
        widgetPositionInList: Int
    )

    fun clickAuthorName(
        item: AuthorWidgetModel,
        slotInfo: SlotInfo,
        widgetPositionInList: Int
    )

    fun clickBackExit()

    fun openScreenBrowseFeedPage()

    fun viewStoryWidget(
        item: StoryNodeModel,
        slotInfo: SlotInfo,
        widgetPositionInList: Int
    )

    fun clickStoryWidget(
        item: StoryNodeModel,
        slotInfo: SlotInfo,
        widgetPositionInList: Int
    )

    fun clickSearchbar()
}
