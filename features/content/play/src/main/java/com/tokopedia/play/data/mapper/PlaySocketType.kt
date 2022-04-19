package com.tokopedia.play.data.mapper


/**
 * Created by mzennis on 2019-12-12.
 */
enum class PlaySocketType(var value: String) {
    TotalLike("TOTAL_LIKE"),
    TotalView("TOTAL_VIEW"),
    ChatPeople("MESG"),
    PinnedMessage("PINNED_MESSAGE"),
    QuickReply("QUICK_REPLY"),
    EventBanned("BANNED"),
    EventFreeze("FREEZE"),
    ProductTag("PRODUCT_TAG_UPDATE"),
    MerchantVoucher("MERCHANT_VOUCHERS"),
    ChannelInteractiveStatus("CHANNEL_INTERACTIVE_STATUS"),
    ChannelInteractive("CHANNEL_INTERACTIVE"),
    RealTimeNotification("REAL_TIME_NOTIF"),
    UpdateConfigMultipleLike("UPDATE_CONFIG_MULTIPLE_LIKE"),
    UserWinnerStatus("USER_WINNER_STATUS")
}