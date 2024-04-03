package com.tokopedia.feedplus.browse.presentation.adapter

import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.InspirationCardViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.SlotInfo
import com.tokopedia.feedplus.browse.presentation.model.srp.FeedSearchResultContent
import kotlinx.coroutines.CoroutineScope

internal class FeedSearchResultAdapter(
    coroutineScope: CoroutineScope,
    inspirationCardListener: InspirationCardViewHolder.Item.Listener
): FeedBrowseItemAdapter<List<FeedSearchResultContent>>(
    scope = coroutineScope,
    inspirationCardListener = inspirationCardListener,
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
        var channelCount = 1

        return map {
            when (it) {
                is FeedSearchResultContent.Title -> {
                    FeedBrowseItemListModel.Title(
                        slotInfo = SlotInfo.Empty,
                        title = it.title
                    )
                }
                is FeedSearchResultContent.Channel -> {
                    FeedBrowseItemListModel.InspirationCard.Item(
                        slotInfo = SlotInfo.Empty,
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
