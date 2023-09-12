package com.tokopedia.stories.analytic

interface StoriesAnalytic {

    fun sendImpressionStoriesContent(storiesId: String, authorId: String)
    fun sendViewStoryCircleEvent(
        entryPoint: String,
        storiesId: String,
        partnerId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
        templateTracker: String,
        promotions: List<StoriesEEModel>,
    )
    fun sendClickShopNameEvent(eventLabel: String)
    fun sendClickThreeDotsEvent(eventLabel: String)
    fun sendClickShoppingBagEvent(eventLabel: String)
    fun sendClickStoryCircleEvent(
        entryPoint: String,
        storiesId: String,
        partnerId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
        templateTracker: String,
        promotions: List<StoriesEEModel>,
    )
    fun sendClickRemoveStoryEvent(eventLabel: String)
    fun sendViewProductCardEvent(eventLabel: String, items: List<String>)
    fun sendClickProductCardEvent(eventLabel: String, itemList: String, items: List<String>)
    fun sendClickBuyButtonEvent(eventLabel: String, items: List<String>)
    fun sendClickAtcButtonEvent(eventLabel: String, items: List<String>)
    fun sendClickTapNextContentEvent(eventLabel: String)
    fun sendClickTapPreviousContentEvent(eventLabel: String)
    fun sendClickSwipeNextContentEvent(eventLabel: String)
    fun sendClickSwipePreviousContentEvent(eventLabel: String)
    fun sendClickExitStoryRoomEvent(eventLabel: String)

}
