package com.tokopedia.feedplus.browse.presentation.adapter

import com.tokopedia.content.common.util.WindowSizeClass
import com.tokopedia.content.common.util.WindowWidthSizeClass
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.InspirationCardViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.SlotInfo
import com.tokopedia.feedplus.browse.presentation.model.srp.FeedSearchResultContent
import kotlinx.coroutines.CoroutineScope

internal class FeedSearchResultAdapter(
    coroutineScope: CoroutineScope,
    sizeClass: WindowSizeClass,
    inspirationCardListener: InspirationCardViewHolder.Item.Listener,
): FeedBrowseItemAdapter<List<FeedSearchResultContent>>(
    scope = coroutineScope,
    inspirationCardListener = inspirationCardListener,
    spanCount = when (sizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 2
        WindowWidthSizeClass.Medium -> 3
        WindowWidthSizeClass.Expanded -> 4
    }
) {

    fun setShimmer() {
        val placeholder = listOf<FeedBrowseItemListModel>(
            FeedBrowseItemListModel.InspirationCard.Placeholder,
            FeedBrowseItemListModel.InspirationCard.Placeholder,
            FeedBrowseItemListModel.InspirationCard.Placeholder,
            FeedBrowseItemListModel.InspirationCard.Placeholder
        )

        submitList(placeholder)
    }

    override fun List<FeedSearchResultContent>.mapToItems(): List<FeedBrowseItemListModel> {
        var channelCount = 0
        val title = this
            .filterIsInstance<FeedSearchResultContent.Title>()
            .firstOrNull()
            ?.title
            .orEmpty()

        return map {
            when (it) {
                is FeedSearchResultContent.Title -> {
                    FeedBrowseItemListModel.Title(
                        slotInfo = SlotInfo.Empty.copy(title = title),
                        title = it.title
                    )
                }
                is FeedSearchResultContent.Channel -> {
                    FeedBrowseItemListModel.InspirationCard.Item(
                        slotInfo = SlotInfo.Empty.copy(title = title),
                        item = it.channel,
                        config = it.config,
                        index = channelCount++,
                    )
                }
                is FeedSearchResultContent.Loading -> {
                    FeedBrowseItemListModel.LoadingModel
                }
            }
        }
    }
}
