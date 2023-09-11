package com.tokopedia.stories.analytic

interface StoriesAnalytic {

    fun sendImpressionStoriesContent(storiesId: String)
    fun sendViewStoryCircleEvent(eventLabel: String, promotions: List<String>)
    fun sendClickShopNameEvent(eventLabel: String)
    fun sendClickThreeDotsEvent(eventLabel: String)
    fun sendClickShoppingBagEvent(eventLabel: String)
    fun sendClickStoryCircleEvent(eventLabel: String, promotions: List<String>)
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
