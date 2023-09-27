package com.tokopedia.stories.analytics

import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.view.model.StoriesArgsModel

interface StoriesRoomAnalytic {

    interface Factory {
        fun create(args: StoriesArgsModel): StoriesRoomAnalytic
    }

    fun sendImpressionStoriesContent(storiesId: String)
    fun sendViewStoryCircleEvent(
        currentCircle: String,
        promotions: List<StoriesEEModel>,
    )
    fun sendClickShopNameEvent(eventLabel: String)
    fun sendClickThreeDotsEvent(eventLabel: String)
    fun sendClickShoppingBagEvent(eventLabel: String)
    fun sendClickStoryCircleEvent(
        currentCircle: String,
        promotions: List<StoriesEEModel>,
    )
    fun sendClickRemoveStoryEvent(eventLabel: String)
    fun sendViewProductCardEvent(eventLabel: String, items: Map<ContentTaggedProductUiModel, Int>)
    fun sendClickProductCardEvent(eventLabel: String, itemList: String, items: List<ContentTaggedProductUiModel>, position: Int)
    fun sendClickBuyButtonEvent(eventLabel: String, items: List<ContentTaggedProductUiModel>, position: Int, shopName: String)
    fun sendClickAtcButtonEvent(eventLabel: String, items: List<ContentTaggedProductUiModel>, position: Int, shopName: String)
    fun sendClickTapNextContentEvent(
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    )
    fun sendClickTapPreviousContentEvent(
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    )
    fun sendClickMoveToOtherGroup()
    fun sendClickExitStoryRoomEvent(
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    )

}
