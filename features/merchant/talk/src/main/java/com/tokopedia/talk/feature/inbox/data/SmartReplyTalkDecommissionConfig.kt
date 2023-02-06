package com.tokopedia.talk.feature.inbox.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SmartReplyTalkDecommissionConfig(
    @SerializedName("inbox_page")
    @Expose
    val inboxPage: InboxPage,
    @SerializedName("talk_setting_page")
    @Expose
    val talkSettingPage: TalkSettingPage,
    @SerializedName("smart_reply_page")
    @Expose
    val smartReplyPage: SmartReplyPage,
    @SerializedName("smart_reply_stock_page")
    @Expose
    val smartReplyStockPage: SmartReplyStockPage
) {
    data class InboxPage(
        @SerializedName("ticker_config")
        @Expose
        val tickerConfig: TickerConfig
    )

    data class TalkSettingPage(
        @SerializedName("show_smart_reply_entrypoint")
        @Expose
        val showSmartReplyEntryPoint: Boolean
    )

    data class SmartReplyPage(
        @SerializedName("ticker_config")
        @Expose
        val tickerConfig: TickerConfig
    )

    data class SmartReplyStockPage(
        @SerializedName("ticker_config")
        @Expose
        val tickerConfig: TickerConfig,
        @SerializedName("toaster_config")
        @Expose
        val toasterConfig: ToasterConfig
    )
}

data class TickerConfig(
    @SerializedName("show")
    @Expose
    val show: Boolean,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("text")
    @Expose
    val text: String
)

data class ToasterConfig(
    @SerializedName("enabled")
    @Expose
    val enabled: String,
    @SerializedName("text")
    @Expose
    val text: String,
    @SerializedName("cta")
    @Expose
    val cta: String
)
