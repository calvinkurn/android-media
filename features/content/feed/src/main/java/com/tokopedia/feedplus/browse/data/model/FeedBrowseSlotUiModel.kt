package com.tokopedia.feedplus.browse.data.model

import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChannelListState
import com.tokopedia.feedplus.presentation.model.type.AuthorType
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by kenny.hadisaputra on 13/10/23
 */
internal sealed interface FeedBrowseSlotUiModel {

    val slotId: String
    val title: String

    data class ChannelsWithMenus(
        override val slotId: String,
        override val title: String,
        val group: String,
        val menus: Map<WidgetMenuModel, FeedBrowseChannelListState<PlayWidgetChannelUiModel>>,
        val selectedMenuId: String
    ) : FeedBrowseSlotUiModel {

        companion object {
            val Empty = ChannelsWithMenus(
                slotId = "",
                title = "",
                group = "",
                menus = emptyMap(),
                selectedMenuId = ""
            )
        }
    }

    data class InspirationBanner(
        override val slotId: String,
        override val title: String,
        val identifier: String,
        val bannerList: List<BannerWidgetModel>
    ) : FeedBrowseSlotUiModel

    data class Authors(
        override val slotId: String,
        override val title: String,
        val identifier: String,
        val authorList: List<AuthorWidgetModel>
    ) : FeedBrowseSlotUiModel

    data class StoryGroups(
        override val slotId: String,
        override val title: String,
        val storyList: List<StoryNodeModel>,
        val nextCursor: String,
        val source: String
    ) : FeedBrowseSlotUiModel
}

internal data class WidgetMenuModel(
    val id: String,
    val label: String,
    val group: String,
    val sourceType: String,
    val sourceId: String
) {

    val isValid: Boolean = id.isNotBlank()

    fun toRequest(nextCursor: String): WidgetRequestModel {
        return WidgetRequestModel(
            group = group,
            sourceType = sourceType,
            sourceId = sourceId,
            cursor = nextCursor
        )
    }

    companion object {
        val Empty = WidgetMenuModel(
            id = "",
            label = "",
            group = "",
            sourceType = "",
            sourceId = "",
        )
    }
}

internal data class BannerWidgetModel(
    val title: String,
    val imageUrl: String,
    val appLink: String
)

internal data class AuthorWidgetModel(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val coverUrl: String,
    val totalViewFmt: String,
    val appLink: String,
    val contentId: String,
    val contentAppLink: String,
    val channelType: String
)

internal data class StoryNodeModel(
    val id: String,
    val name: String,
    val authorType: AuthorType,
    val thumbnailUrl: String,
    val hasUnseenStory: Boolean,
    val appLink: String,
    val lastUpdatedAt: Long
)
