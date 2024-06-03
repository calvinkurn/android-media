package com.tokopedia.topchat.chatroom.view.listener

interface TopChatRoomBroadcastProductListener {
    fun onImpressionBroadcastSeeMoreProduct(
        blastId: String,
        campaignStatus: String,
        campaignCountDown: String
    )
    fun onClickBroadcastSeeMoreProduct(
        blastId: String,
        campaignStatus: String,
        campaignCountDown: String,
        appLink: String
    )

    fun onImpressionBroadcastProduct(
        blastId: String,
        campaignStatus: String,
        campaignCountDown: String,
        productId: String,
        position: Int = 0,
        totalProduct: Int = 1
    )

    fun onClickBroadcastProduct(
        blastId: String,
        campaignStatus: String,
        campaignCountDown: String,
        productId: String,
        position: Int = 0,
        totalProduct: Int = 1,
        androidUrl: String,
        productUrl: String
    )
}

