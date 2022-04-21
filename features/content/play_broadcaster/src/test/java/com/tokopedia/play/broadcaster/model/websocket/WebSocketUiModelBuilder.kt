package com.tokopedia.play.broadcaster.model.websocket

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.domain.model.LiveDuration
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.EventUiModel
import com.tokopedia.play.broadcaster.ui.model.PlayMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalLikeUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalViewUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play_common.model.dto.interactive.InteractiveType
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.dto.interactive.PlayInteractiveTimeStatus
import com.tokopedia.play_common.model.ui.PlayChatUiModel

/**
 * Created By : Jonathan Darwin on February 21, 2022
 */
class WebSocketUiModelBuilder {

    /** New Metrics */
    fun buildNewMetricString(
        size: Int = 1,
        interval: Long = 1000,
        sentence: String = "<b>2</b> penonton <br> bergabung",
        type: String = "",
        icon: String = "",
    ): String {
        var data = ""
        List(size) {
            data += """
                {
                    "interval": $interval,
                    "sentence": "$sentence",
                    "type": "$type",
                    "icon": "$icon"
                }
            """.trimIndent()
            if(it != size-1) data += ","
        }

        return """
            {
                "type": "GENERAL_BULK_EVENT_NOTIF",
                "data": [
                    $data
                ]
            }
        """.trimIndent()
    }

    fun buildNewMetricModelList(
        size: Int = 1,
        iconUrl: String =  "",
        spannedSentence: String = "<b>2</b> penonton <br> bergabung",
        type: String = "",
        interval: Long = 1000L,
    ) = List(size) {
        PlayMetricUiModel(
            iconUrl = iconUrl,
            spannedSentence = spannedSentence,
            type = type,
            interval = interval,
        )
    }

    /** TOTAL_VIEW */
    fun buildTotalViewString(
        channelId: Int = 1,
        totalView: Long = 1,
        totalViewFmt: String = "1",
    ) = """
        {
            "type": "TOTAL_VIEW",
            "data": {
                "channel_id" : $channelId,
                "total_view" : $totalView,
                "total_view_formatted" : "$totalViewFmt"
            }
        }
    """.trimIndent()

    fun buildTotalViewModel(
        totalViewFmt: String = "1",
    ) = TotalViewUiModel(
        totalView = totalViewFmt,
    )

    /** TOTAL_LIKE */
    fun buildTotalLikeString(
        channelId: Int = 1,
        totalLike: Long = 1,
        totalLikeFmt: String = "1",
    ) = """
        {
            "type": "TOTAL_LIKE",
            "data": {
                "channel_id" : $channelId,
                "total_like" : $totalLike,
                "total_like_formatted" : "$totalLikeFmt"
            }
        }
    """.trimIndent()

    fun buildTotalLikeModel(
        totalLikeFmt: String = "1",
    ) = TotalLikeUiModel(
        totalLike = totalLikeFmt,
    )

    /** MESG */
    fun buildChatString(
        channelId: Int = 1,
        messageId: Int = 1,
        id: Int = 1,
        userId: Int = 1,
        name: String = "",
        image: String = "",
        message: String = "",
        timestamp: Long = 1579064126000,
    ) = """
        {
            "type": "MESG",
            "data": {
              "channel_id": $channelId,
              "msg_id": "$messageId",
              "user": {
                "id": $id,
                "user_id": $userId,
                "name": "$name",
                "image": "$image"
              },
              "message": "$message",
              "timestamp": $timestamp
            }
        }
    """.trimIndent()

    fun buildChatModel(
        messageId: String = "1",
        userId: String = "1",
        name: String = "",
        message: String = "",
        isSelfMessage: Boolean = false
    ) = PlayChatUiModel(
        messageId = messageId,
        userId = userId,
        name = name,
        message = message,
        isSelfMessage = isSelfMessage,
    )

    /** PINNED_MESSAGE */
    fun buildPinnedMessageString(
        channelId: Int = 1,
        pinnedMessageId: Int = 1,
        title: String = "",
        message: String = "",
        imageUrl: String = "",
        redirectUrl: String = "",
    ) = """
        {
            "type": "PINNED_MESSAGE",
            "data": {
            	"channel_id" : $channelId,
            	"pinned_message_id" : $pinnedMessageId,
            	"title" : "$title",
            	"message" : "$message",
                "imageUrl": "$imageUrl",
            	"redirect_url" : "$redirectUrl"
            }
        }
    """.trimIndent()

    fun buildPinnedMessageModel(
        id: String = "1",
        message: String = "",
        isActive: Boolean = true,
        editStatus: PinnedMessageEditStatus = PinnedMessageEditStatus.Nothing,
    ) = PinnedMessageUiModel(
        id = id,
        message = message,
        isActive = isActive,
        editStatus = editStatus,
    )

    /** LIVE_DURATION */
    fun buildLiveDurationString(
        durationSec: Long = 0,
        maxDurationSec: Long = 0,
        remainingSec: Long = 0,
        startTime: String = "",
        timeNow: String = "",
    ) = """
        {
          "type": "LIVE_DURATION",
          "data": {
            "duration_sec": $durationSec,
            "max_duration_sec": $maxDurationSec,
            "remaining_sec": $remainingSec,
            "start_time": "$startTime",
            "time_now": "$timeNow"
          }
        }
    """.trimIndent()

    fun buildLiveDurationModel(
        duration: Long = 0,
        maxDuration: Long = 0,
        remaining: Long = 0,
        startTime: String = "",
        timeNow: String = ""
    ) = LiveDuration(
        duration = duration,
        maxDuration = maxDuration,
        remaining = remaining,
        startTime = startTime,
        timeNow = timeNow
    )

    /** PRODUCT_TAG */
    fun buildProductTagString(
        sectionSize: Int = 1,
        productSize: Int = 1,
    ): String {
        var productList = ""
        for(i in 1..productSize) {
            productList += """
                {
                    "id": "$i",
                    "name": "Product $i",
                    "image_url": "",
                    "shop_id": "479887",
                    "original_price": 60000,
                    "original_price_formatted": "Rp 60.000",
                    "discount": 0,
                    "price": 0,
                    "price_formatted": "",
                    "quantity": 1,
                    "quantity_render": {
                        "show": false,
                        "copy": "",
                        "color": ""
                    },
                    "is_variant": false,
                    "is_available": false,
                    "order": 0,
                    "app_link": "tokopedia://product/15240013",
                    "web_link": "https://staging.tokopedia.com/hahastag/indomie-soto-lamongan",
                    "min_quantity": 1,
                    "is_free_shipping": true
                }
            """.trimIndent()

            if(i != productSize) productList += ","
        }

        var sectionList = ""
        for(i in 1..sectionSize) {
            sectionList += """
                {
                    "type": "active",
                    "title": "Section $i",
                    "countdown": {
                        "copy": "Berakhir dalam"
                    },
                    "background": {
                        "gradient": [
                            "#ff23de",
                            "#2244aa"
                        ],
                        "image_url": "https://via.placeholder.com/150"
                    },
                    "start_time": "2022-01-02T15:04:05Z07:00",
                    "end_time": "2022-01-02T16:04:05Z07:00",
                    "server_time": "2022-01-02T15:14:05Z07:00",
                    "products": [
                        $productList
                    ]
                }
            """.trimIndent()

            if(i != sectionSize) sectionList += ","
        }

        return """
            {
                "type": "PRODUCT_TAG_UPDATE",
                "data": {
                      "sections" : [
                            $sectionList
                      ]
                }
            }
        """.trimIndent()
    }

    fun buildProductTagModel(
        sectionSize: Int = 1,
        productSize: Int = 1,
    ) = List(sectionSize) {
        ProductTagSectionUiModel(
            name = "Section ${it + 1}",
            campaignStatus = CampaignStatus.Ongoing,
            products = List(productSize) { productIdx ->
                val idx = productIdx + 1
                ProductUiModel(
                    id = idx.toString(),
                    name = "Product $idx",
                    imageUrl = "",
                    stock = 1,
                    price = OriginalPrice(
                        price = "Rp 60.000",
                        priceNumber = 60000.0,
                    )
                )
            }
        )
    }

    /** FREEZE */
    fun buildFreezeString(
        channelId: Int = 1,
        isFreeze: Boolean = true,
        timestamp: Long = 1592392273000,
    ) = """
        {
            "type":"FREEZE",
            "data":{
              "channel_id": $channelId,
              "is_freeze": $isFreeze,
              "timestamp": $timestamp
            }
        }
    """.trimIndent()

    fun buildFreezeModel(
        freeze: Boolean = true,
        banned: Boolean = false,
        title: String = "",
        message: String = "",
        buttonTitle: String = "",
    ) = EventUiModel(
        freeze = freeze,
        banned = banned,
        title = title,
        message = message,
        buttonTitle = buttonTitle,
    )

    /** BANNED */
    fun buildBannedString(
        channelId: Int = 1,
        userId: Int = 1,
        name: String = "",
        image: String = "",
        isBanned: Boolean = true,
        timestamp: Long = 1579064126000,
    ) = """
        {
            "type": "MODERATE",
            "data": {
              "channel_id": $channelId,
              "user": {
                "id": $userId,
                "name": "$name",
                "image": "$image"
              },
              "is_banned": $isBanned,
              "timestamp": $timestamp
            }
        }
    """.trimIndent()

    fun buildBannedModel(
        freeze: Boolean = false,
        banned: Boolean = true,
        title: String = "",
        message: String = "",
        buttonTitle: String = "",
    ) = EventUiModel(
        freeze = freeze,
        banned = banned,
        title = title,
        message = message,
        buttonTitle = buttonTitle,
    )

    /** CHANNEL_INTERACTIVE */
    fun buildChannelInteractiveString(
        channelId: Long = 1,
        interactiveID: Long = 0,
        interactiveType: Int = -1,
        title: String = "",
        status: Int = 0,
        countdownStart: Int = 0,
        countdownEnd: Int = 1,
        countdownEndDelay: Int = 0,
        waitingDuration: Int = 0,
    ) = """
        {
            "type": "CHANNEL_INTERACTIVE",
            "data": {
              "channel_id": $channelId,
              "interactive_id": $interactiveID,
              "interactive_type": $interactiveType,
              "title": "$title",
              "status": $status,
              "countdown_start": $countdownStart,
              "countdown_end": $countdownEnd,
              "countdown_end_delay": $countdownEndDelay,
              "waiting_duration": $waitingDuration
            }
        }
    """.trimIndent()

    fun buildChannelInteractiveModel(
        id: Long = 0L,
        type: InteractiveType = InteractiveType.Unknown,
        title: String = "",
        timeStatus: PlayInteractiveTimeStatus = PlayInteractiveTimeStatus.Scheduled(
            timeToStartInMs = 0,
            interactiveDurationInMs = 1000,
        ),
        endGameDelayInMs: Long = 0L,
    ) = PlayCurrentInteractiveModel(
        id = id,
        type = type,
        title = title,
        timeStatus = timeStatus,
        endGameDelayInMs = endGameDelayInMs,
    )
}