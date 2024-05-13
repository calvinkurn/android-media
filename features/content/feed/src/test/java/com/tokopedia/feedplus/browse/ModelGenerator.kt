package com.tokopedia.feedplus.browse

import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.browse.data.model.BannerWidgetModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.StoryNodeModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.presentation.model.type.AuthorType
import java.util.*

internal class ModelGenerator {

    private val indexGenerator = generateSequence(1) { it + 1 }
    val widgetMenuModel = indexGenerator.map {
        WidgetMenuModel(
            id = it.toString(),
            label = "Menu $it",
            group = "Menu Group $it",
            sourceType = "Source type $it",
            sourceId = "Source Id $it"
        )
    }
    val bannerModel = indexGenerator.map {
        BannerWidgetModel(
            title = "Banner $it",
            imageUrl = "Url Banner $it",
            appLink = "AppLink Banner $it"
        )
    }
    val authorModel = indexGenerator.map {
        AuthorWidgetModel(
            id = it.toString(),
            contentId = it.toString(),
            name = "Author widget $it",
            avatarUrl = "Avatar $it",
            coverUrl = "Cover $it",
            totalViewFmt = "$it rb",
            appLink = "AppLink Author $it",
            contentAppLink = "Content AppLink $it",
            channelType = "live"
        )
    }

    val channelWithMenusSlot = indexGenerator.map {
        val randomSlotId = UUID.randomUUID().mostSignificantBits.toString()
        FeedBrowseSlotUiModel.ChannelsWithMenus(
            slotId = randomSlotId,
            title = "Channel with Menus $it",
            group = "Group $it",
            menus = emptyMap(),
            selectedMenuId = ""
        )
    }
    val bannerSlot = indexGenerator.map {
        val randomSlotId = UUID.randomUUID().mostSignificantBits.toString()
        FeedBrowseSlotUiModel.InspirationBanner(
            slotId = randomSlotId,
            title = "Banner Slot model $it",
            identifier = "banner_slot_$it",
            bannerList = emptyList()
        )
    }
    val authorSlot = indexGenerator.map {
        val randomSlotId = UUID.randomUUID().mostSignificantBits.toString()
        FeedBrowseSlotUiModel.Authors(
            slotId = randomSlotId,
            title = "Author Slot model $it",
            identifier = "author_slot_$it",
            authorList = emptyList()
        )
    }
    val storyGroupSlot = indexGenerator.map {
        val randomSlotId = UUID.randomUUID().mostSignificantBits.toString()
        FeedBrowseSlotUiModel.StoryGroups(
            slotId = randomSlotId,
            title = "Story Group $it",
            storyList = emptyList(),
            nextCursor = "",
            source = "",
        )
    }
}
