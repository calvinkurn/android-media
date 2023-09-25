package com.tokopedia.stories.analytic

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
    fun sendViewProductCardEvent(eventLabel: String, items: List<String>)
    fun sendClickProductCardEvent(eventLabel: String, itemList: String, items: List<String>)
    fun sendClickBuyButtonEvent(eventLabel: String, items: List<String>)
    fun sendClickAtcButtonEvent(eventLabel: String, items: List<String>)
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
