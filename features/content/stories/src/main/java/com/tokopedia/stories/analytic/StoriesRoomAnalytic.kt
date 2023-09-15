package com.tokopedia.stories.analytic

interface StoriesRoomAnalytic {

    interface Factory {
        fun create(authorId: String): StoriesRoomAnalytic
    }

    fun sendImpressionStoriesContent(storiesId: String)
    fun sendViewStoryCircleEvent(
        entryPoint: String,
        currentCircle: String,
        promotions: List<StoriesEEModel>,
    )
    fun sendClickShopNameEvent(eventLabel: String)
    fun sendClickThreeDotsEvent(eventLabel: String)
    fun sendClickShoppingBagEvent(eventLabel: String)
    fun sendClickStoryCircleEvent(
        entryPoint: String,
        currentCircle: String,
        promotions: List<StoriesEEModel>,
    )
    fun sendClickRemoveStoryEvent(eventLabel: String)
    fun sendViewProductCardEvent(eventLabel: String, items: List<String>)
    fun sendClickProductCardEvent(eventLabel: String, itemList: String, items: List<String>)
    fun sendClickBuyButtonEvent(eventLabel: String, items: List<String>)
    fun sendClickAtcButtonEvent(eventLabel: String, items: List<String>)
    fun sendClickTapNextContentEvent(
        entryPoint: String,
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    )
    fun sendClickTapPreviousContentEvent(
        entryPoint: String,
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    )
    fun sendClickMoveToOtherGroup(
        entryPoint: String,
    )
    fun sendClickExitStoryRoomEvent(
        entryPoint: String,
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    )

}
