package com.tokopedia.stories.analytic

interface StoriesAnalytic {

    fun sendImpressionStoriesContent(storiesId: String, authorId: String)
    fun sendViewStoryCircleEvent(
        entryPoint: String,
        partnerId: String,
        currentCircle: String,
        promotions: List<StoriesEEModel>,
    )
    fun sendClickShopNameEvent(eventLabel: String)
    fun sendClickThreeDotsEvent(eventLabel: String)
    fun sendClickShoppingBagEvent(eventLabel: String)
    fun sendClickStoryCircleEvent(
        entryPoint: String,
        partnerId: String,
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
        partnerId: String,
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    )
    fun sendClickTapPreviousContentEvent(
        entryPoint: String,
        partnerId: String,
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    )
    fun sendClickMoveToOtherGroup(
        entryPoint: String,
        partnerId: String,
    )
    fun sendClickExitStoryRoomEvent(
        entryPoint: String,
        partnerId: String,
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    )

}
