package com.tokopedia.play.model

import com.google.gson.Gson
import com.tokopedia.play.data.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.ChannelInfoUiModel

/**
 * Created by jegul on 20/02/20
 */
class ModelBuilder {

    private val gson = Gson()

    private val channelJson = """
        {
        			"gc_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxNzIxMTA0OCwiaWQiOjE3MjExMDQ4LCJuYW1lIjoiS3VtZGphZmYiLCJkYXRhIjoiIiwicGFydG5lcl90eXBlIjowLCJwYXJ0bmVyX2lkIjowLCJhdWQiOiJLdW1kamFmZiIsImV4cCI6MTU4MjIwNjMyMiwianRpIjoiMTcyMTEwNDgiLCJpYXQiOjE1ODIxOTEzMjIsImlzcyI6InRva29wZWRpYV9wbGF5IiwibmJmIjoxNTgyMTkxMzIyLCJzdWIiOiJ0b2tvcGVkaWFfcGxheV90b2tlbl8xNzIxMTA0OF8xNTgyMTkxMzIyIn0.i0moJfXrq43AhFLtbczPuIIw2wI2v7H7BN7HSh1WhMk",
        			"partner_type": 1,
        			"partner_id": 2900759,
        			"channel_id": 2092,
        			"title": "Dropping Soon: Yefta Gunawan for Duma",
        			"description": "Nantikan Launching koleksi terbaru DUMA ekslukif di Tokopedia PLAY",
        			"cover_url": "https://ecs7.tokopedia.net/img/cache/1400/attachment/2020/2/13/1581564194887/1581564194887_8d202234-73c8-4623-947a-1fb7367cb250.jpg",
        			"start_time": 1581510480,
        			"end_time": 1581660000,
        			"total_view_formatted": "5.3k",
        			"is_published": false,
        			"is_active": true,
        			"is_freeze": false,
        			"moderator_id": "",
        			"moderator_name": "DUMA Official",
        			"moderator_thumb_url": "https://www.tokopedia.com/dumaofficial",
        			"content_id": 9572493,
        			"content_type": 29,
        			"like_type": 3,
        			"pinned_message": {
        				"pinned_message_id": 2871,
        				"title": "Follow us to get more updates",
        				"max_title_chars": 36,
        				"message": " ",
        				"max_message_chars": 72,
        				"image_url": "",
        				"redirect_url": "https://www.tokopedia.com/dumaofficial"
        			},
        			"quick_reply": ["😊", "😍", "😘", "❤", "👍", "👏", "🎉", "😂"],
        			"settings": {
        				"ping_interval": 5000,
        				"max_chars": 200,
        				"max_retries": 3,
        				"min_reconnect_delay": 3000
        			},
        			"banned": {
        				"msg": "Anda diblokir oleh admin karena melanggar syarat dan ketentuan Play, sehingga tidak dapat melihat konten ini.",
        				"title": "Akun Anda Terblokir",
        				"button_title": "OK"
        			},
        			"exit_msg": {
        				"title": "Keluar dari Play?",
        				"body": "Pastikan sudah vote dan mengikuti kuis untuk mendapatkan hadiah menarik."
        			},
        			"freeze_channel_state": {
        				"category": "",
        				"title": "Dropping Soon: Yefta Gunawan for Duma Telah Berakhir",
        				"desc": "Nantikan dan gabung di channel Dropping Soon: Yefta Gunawan for Duma selanjut nya.",
        				"btn_title": "Mulai Belanja",
        				"btn_app_link": "tokopedia://home"
        			},
        			"video_stream": {
        				"video_stream_id": 367,
        				"orientation": "vertical",
        				"type": "vod",
        				"is_live": false,
        				"config": {
        					"stream_url": "https://vod.tokopedia.com/view/adaptive.m3u8?id=6714234f6dca4c97ad494f77889e7713",
        					"is_auto_play": true
        				}
        			},
        			"chat_permit": {
        				"is_show_chat": true,
        				"error_chat_message": "Mohon maaf fitur chat dinonaktifkan untuk saat ini."
        			}
        }
    """.trimIndent()

    private val shopInfoJson = """
        {
				"shopCore": {
					"name": "DUMA Official",
					"shopID": "2900759"
				},
				"favoriteData": {
					"totalFavorite": 3495,
					"alreadyFavorited": 1
				}
        }
    """.trimIndent()

    private val newChatJson = """
        {
                "channel_id": "1387",
                "msg_id": "1241515151",
                "message": "Woaw!! Keren",
                "user": {
                    "id": "12314",
                    "name": "Kumdjaff",
                    "image": "https://www.google.com"
                }
        }
    """.trimIndent()

    private val totalLikeCount = """
        {
            "like": {
                "fmt": "48",
                "value": 48
            }
        }
    """.trimIndent()

    private val isLike = """
        {
            "isLike": true
        }
    """.trimIndent()

    fun buildChannel() = gson.fromJson(channelJson, Channel::class.java)

    fun buildShopInfo() = gson.fromJson(shopInfoJson, ShopInfo::class.java)

    fun buildNewChat() = gson.fromJson(shopInfoJson, PlayChat::class.java)

    fun buildTotalLike() = gson.fromJson(totalLikeCount, TotalLikeContent.Data::class.java)

    fun buildIsLike() = gson.fromJson(isLike, IsLikedContent.Data::class.java)

    fun buildChannelInfoUiModel(
            id: String = "1230",
            title: String = "Channel live",
            description: String = "Ini Channel live",
            channelType: PlayChannelType = PlayChannelType.Live,
            partnerId: Long = 123151,
            partnerType: PartnerType = PartnerType.ADMIN,
            moderatorName: String = "Lisa",
            contentId: Int = 1412,
            contentType: Int = 2,
            likeType: Int = 1
    ) = ChannelInfoUiModel(id, title, description, channelType, partnerId, partnerType,
            moderatorName, contentId, contentType, likeType)
}