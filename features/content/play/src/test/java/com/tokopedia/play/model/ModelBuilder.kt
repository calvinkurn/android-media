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
      "gc_token": "",
      "partner_type": 0,
      "partner_id": 0,
      "channel_id": 2315,
      "title": "Test Channel Tanpa Shop",
      "description": "Test Description",
      "cover_url": "https://tokopedia.com",
      "start_time": 1580515200,
      "end_time": 1580536800,
      "total_view_formatted": "12",
      "is_published": false,
      "is_active": false,
      "is_freeze": false,
      "moderator_id": "",
      "moderator_name": "Admin",
      "moderator_thumb_url": "",
      "content_id": 0,
      "content_type": 0,
      "like_type": 1,
      "channel_type": 0,
      "is_show_cart": true,
      "is_show_product_tagging": true,
      "pinned_product": {
        "title_pinned": "Ayo belanja barang pilihan kami sebelum kehabisan!",
        "title_bottom_sheet": "Produk Pilihan Seller",
        "is_show_discount": false
      },
      "pinned_message": {
        "pinned_message_id": 3187,
        "title": "twretwqeyrtqywergsfdhgafdhgasfdtwqreyqwretqwebvsdbavsdlksdiwue12561526125175361537153",
        "max_title_chars": 36,
        "message": " ",
        "max_message_chars": 72,
        "image_url": "",
        "redirect_url": "tokopedia://shop/321513/etalase/19151054"
      },
      "quick_reply": [
        "😊",
        "😍",
        "😘",
        "❤",
        "👍",
        "👏",
        "🎉",
        "😂"
      ],
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
        "title": "Test Channel Tanpa Shop Telah Berakhir",
        "desc": "Nantikan dan gabung di channel Test Channel Tanpa Shop selanjut nya.",
        "btn_title": "Mulai Belanja",
        "btn_app_link": "tokopedia://home"
      },
      "video_stream": {
        "video_stream_id": 367,
        "orientation": "vertical",
        "type": "vod",
        "is_live": false,
        "config": {
          "stream_url": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
          "is_auto_play": false
        },
        "buffer_control": {
          "max_buffer_in_second": 50,
          "min_buffer_in_second": 15,
          "buffer_for_playback": 2,
          "buffer_for_playback_after_rebuffer": 5
        }
      },
      "chat_permit": {
        "is_show_chat": false,
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

    private val channelTagItemsJson = """
    {
      "products": [
        {
          "id": "15240013",
          "name": "Indomie Soto Lamongan",
          "image_url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2018/7/3/5511658/5511658_081f12a8-2229-4062-87d6-a405f17d5c90_500_500.jpg",
          "shopID": "479887",
          "original_price": 60000,
          "original_price_formatted": "Rp 60.000",
          "discount": 0,
          "price": 0,
          "price_formatted": "",
          "quantity": 9988,
          "is_variant": false,
          "is_available": false,
          "order": 0,
          "applink": "tokopedia://product/15240013",
          "web_link": "https://staging.tokopedia.com/hahastag/indomie-soto-lamongan",
          "min_quantity": 1
        }
      ],
      "vouchers": [
        {
          "voucher_id": "12",
          "voucher_name": "test date",
          "shop_id": "105407",
          "title": " ",
          "subtitle": "min. pembelian ",
          "voucher_type": 1,
          "voucher_image": "https://ecs7.tokopedia.net/img/attachment/2018/10/4/5480066/5480066_4a86d259-d8ce-4501-a1d8-17803320bc35",
          "voucher_image_square": "",
          "voucher_quota": 100,
          "voucher_finish_time": "2018-12-07T23:30:00Z"
        },
        {
          "voucher_id": "1470",
          "voucher_name": "VOUCHER 2020",
          "shop_id": "478804",
          "title": " ",
          "subtitle": "min. pembelian ",
          "voucher_type": 1,
          "voucher_image": "https://ecs7.tokopedia.net/img/attachment/2020/2/17/5479551/5479551_eb6e74a3-1ad9-4fd8-a17f-19d3197bb869",
          "voucher_image_square": "https://ecs7.tokopedia.net/img/attachment/2020/2/17/5479551/5479551_c3171bd3-1b75-4b46-a451-9228273072ca",
          "voucher_quota": 100,
          "voucher_finish_time": "2020-03-17T00:30:00Z"
        },
        {
          "voucher_id": "123",
          "voucher_name": "ini cashback",
          "shop_id": "479155",
          "title": " ",
          "subtitle": "min. pembelian ",
          "voucher_type": 3,
          "voucher_image": "https://ecs7.tokopedia.net/img/attachment/2018/10/15/5480709/5480709_318c7508-710f-4fb7-a55a-794730de70fb",
          "voucher_image_square": "",
          "voucher_quota": 100,
          "voucher_finish_time": "2018-12-05T23:30:00Z"
        }
      ]
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
            moderatorName, contentId, contentType, likeType, isShowCart = true)

    fun buildProductTagging() = gson.fromJson(channelTagItemsJson, ProductTagging::class.java)
}