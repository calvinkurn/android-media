package com.tokopedia.stories.view.model

import com.tokopedia.content.common.R
import com.tokopedia.content.common.report_content.model.FeedMenuIdentifier
import com.tokopedia.content.common.report_content.model.FeedMenuItem
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.universal_sharing.view.model.LinkProperties

data class StoriesUiModel(
    val selectedGroup: Int,
    val groups: List<StoriesGroupUiModel>,
)

data class StoriesGroupUiModel(
    val id: String,
    val image: String,
    val title: String,
    val selectedDetail: Int,
    val selected: Boolean,
    val details: List<StoriesDetailUiModel>,
)

data class StoriesDetailUiModel(
    val id: String,
    val selected: Int,
    val event: StoriesDetailUiEvent,
    val imageContent: String,
    val author: StoryAuthor,
    //TODO() rename, temp list
    val menus: List<FeedMenuItem> = defaultMenu,
    val share : Sharing = defaultSharing,
    val productCount : Int = 5,
) {

    enum class StoriesDetailUiEvent {
        PAUSE, START,
    }

    data class Sharing(
        val isShareable: Boolean,
        val metadata: LinkProperties,
    )
    companion object {
        val Empty = StoriesDetailUiModel(
            id = "0",
            selected = 1,
            event = StoriesDetailUiEvent.START,
            imageContent = "",
            author = StoryAuthor.Unknown,
        )
    }
}

//TODO() please remove
val defaultMenu = buildList<FeedMenuItem> {
    add(
        FeedMenuItem(
            iconUnify = IconUnify.WARNING,
            name = R.string.content_common_menu_report,
            type = FeedMenuIdentifier.Report
        )
    )
    add(
        FeedMenuItem(
            iconUnify = IconUnify.DELETE,
            name = com.tokopedia.stories.R.string.stories_delete_story_title,
            type = FeedMenuIdentifier.Delete,
        )
    )
}
val listProduct = List(5) {
    ContentTaggedProductUiModel(
        id = "1",
        parentID = "11",
        showGlobalVariant = true,
        shop = ContentTaggedProductUiModel.Shop("6", "Play"),
        title = "Product keren!",
        imageUrl = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
        price = ContentTaggedProductUiModel.NormalPrice(formattedPrice = "Rp.10.000", price = 10000.0),
        appLink = "",
        campaign = ContentTaggedProductUiModel.Campaign(ContentTaggedProductUiModel.CampaignType.NoCampaign, ContentTaggedProductUiModel.CampaignStatus.Unknown, isExclusiveForMember = false),
        affiliate = ContentTaggedProductUiModel.Affiliate(id = "", channel = "66"),
        stock = ContentTaggedProductUiModel.Stock.Available,
    )
}

val defaultSharing = StoriesDetailUiModel.Sharing (
    isShareable = true,
    metadata = LinkProperties(
        ogTitle = "Product keren!",
        ogDescription = "Ayo lihat barangnya dulu",
        deeplink = "",
        ogImageUrl = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
    ),
)
