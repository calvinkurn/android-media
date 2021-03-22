package com.tokopedia.play.model

import com.google.gson.Gson
import com.tokopedia.play.data.ReportSummaries
import com.tokopedia.play.data.ShopInfo
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse

/**
 * Created by jegul on 09/02/21
 */
class PlayResponseBuilder {

    private val gson = Gson()

    private val reportSummariesResponse = """
        {
            "broadcasterReportSummariesBulk": {
                "reportData": [{
                    "channel": {
                        "metrics": {
                            "totalLike": "1",
                            "totalLikeFmt": "1",
                            "visitChannel": "720",
                            "visitChannelFmt": "720"
                        }
                    }
                }]
            }
        }
    """.trimIndent()

    private val channelJsonWithRecomResponse = """
        {
         "playGetChannelDetailsWithRecom": {
            "meta":{
               "cursor":"ENdOGdDIMaNpuvA/IJqGxYAG"
            },
            "data":[
               {
                  "id":"10071",
                  "title":"Test shop",
                  "is_live":false,
                  "partner":{
                     "id":"479887",
                     "type":"shop",
                     "name":"haha stag",
                     "thumbnail_url":"https://images.tokopedia.net/img/cache/215-square/shops-1/2017/7/31/479887/479887_b682302c-3c6b-4053-859e-027af3a97c68.png",
                     "badge_url":"https://images.tokopedia.net/img/official_store/badge_os.png",
                     "app_link":"tokopedia://shop/479887",
                     "web_link":"https://staging.tokopedia.com/hahastag"
                  },
                  "video":{
                     "id":"752",
                     "orientation":"vertical",
                     "type":"vod",
                     "stream_source":"https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                     "autoplay":true,
                     "buffer_control":{
                        "max_buffer_in_seconds":18,
                        "min_buffer_in_seconds":3,
                        "buffer_for_playback":2,
                        "buffer_for_playback_after_rebuffer":2
                     }
                  },
                  "pinned_message":{
                     "id":"225",
                     "title":"testing publish unpublish",
                     "redirect_url":"https://www.google.com/"
                  },
                  "quick_replies":[
                     "❤ ❤ ❤",
                     "☺",
                     "👽",
                     "✈"
                  ],
                  "configurations":{
                     "show_cart":false,
                     "show_pinned_product":false,
                     "ping_interval":10000,
                     "max_chars":200,
                     "max_retries":5,
                     "min_reconnect_delay":5000,
                     "has_promo":true,
                     "reminder":{
                        "is_set":false
                     },
                     "channel_freeze_screen":{
                        "title":"%s Telah Berakhir",
                        "description":"Yuk lanjut nonton berbagai video menarik lainnya di Tokopedia Play!",
                        "button_text":"Cek Channel Lain",
                        "button_app_link":"tokopedia://webview?pull_to_refresh=true\u0026titlebar=false\u0026autoplay=true\u0026url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F"
                     },
                     "channel_banned_message":{
                        "title":"Anda diblokir admin",
                        "message":"Anda diblokir oleh admin karena melanggar syarat dan ketentuan Channel, sehingga tidak dapat melihat konten ini.",
                        "button_text":"OK"
                     },
                     "chat_config":{
                        "chat_enabled":true,
                        "chat_disabled_message":"Maaf, fitur chat sedang dimatikan oleh admin."
                     },
                     "feeds_like_params":{
                        "content_type":29,
                        "content_id":"10071",
                        "like_type":3
                     },
                     "pinned_product_config":{
                        "pin_title":"Ayo belanja barang pilihan kami sebelum kehabisan!",
                        "bottom_sheet_title":"Produk Pilihan Seller"
                     },
                     "room_background":{
                        "image_url":""
                     }
                  },
                  "app_link":"tokopedia://play/10071",
                  "web_link":"https://www.tokopedia.com/play/update",
                  "share":{
                     "text":"\"Test shop\"\nYuk, nonton siaran dari haha stag di Tokopedia PLAY! Bakal seru banget lho!\n        ${'$'}{url}        ",
                     "redirect_url":"https://tokopedia.link/rEOLlsKdLab",
                     "use_short_url":false,
                     "meta_title":"Tokopedia PLAY seru!",
                     "meta_description":"Nonton siaran seru di Tokopedia PLAY!",
                     "is_show_button":true
                  }
               },
               {
                  "id":"10748",
                  "title":"Test - Tokopedia Play !",
                  "is_live":false,
                  "partner":{
                     "id":"479887",
                     "type":"shop",
                     "name":"haha stag",
                     "thumbnail_url":"https://images.tokopedia.net/img/cache/215-square/shops-1/2017/7/31/479887/479887_b682302c-3c6b-4053-859e-027af3a97c68.png",
                     "badge_url":"https://images.tokopedia.net/img/official_store/badge_os.png",
                     "app_link":"tokopedia://shop/479887",
                     "web_link":"https://staging.tokopedia.com/hahastag"
                  },
                  "video":{
                     "id":"1105",
                     "orientation":"horizontal",
                     "type":"vod",
                     "stream_source":"https://youtu.be/F1EsfKxZ2xs",
                     "autoplay":true,
                     "buffer_control":{
                        "max_buffer_in_seconds":18,
                        "min_buffer_in_seconds":3,
                        "buffer_for_playback":2,
                        "buffer_for_playback_after_rebuffer":2
                     }
                  },
                  "pinned_message":{
                     "id":"",
                     "title":"",
                     "redirect_url":""
                  },
                  "quick_replies":[
                     "❤ ❤ ❤",
                     "☺",
                     "👽",
                     "✈"
                  ],
                  "configurations":{
                     "show_cart":true,
                     "show_pinned_product":true,
                     "ping_interval":10000,
                     "max_chars":200,
                     "max_retries":5,
                     "min_reconnect_delay":5000,
                     "has_promo":false,
                     "reminder":{
                        "is_set":false
                     },
                     "channel_freeze_screen":{
                        "title":"%s Telah Berakhir",
                        "description":"Yuk lanjut nonton berbagai video menarik lainnya di Tokopedia Play!",
                        "button_text":"Cek Channel Lain",
                        "button_app_link":"tokopedia://webview?pull_to_refresh=true\u0026titlebar=false\u0026autoplay=true\u0026url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F"
                     },
                     "channel_banned_message":{
                        "title":"Anda diblokir admin",
                        "message":"Anda diblokir oleh admin karena melanggar syarat dan ketentuan Channel, sehingga tidak dapat melihat konten ini.",
                        "button_text":"OK"
                     },
                     "chat_config":{
                        "chat_enabled":true,
                        "chat_disabled_message":"Maaf, fitur chat sedang dimatikan oleh admin."
                     },
                     "feeds_like_params":{
                        "content_type":29,
                        "content_id":"10748",
                        "like_type":3
                     },
                     "pinned_product_config":{
                        "pin_title":"Ayo belanja barang pilihan kami sebelum kehabisan!",
                        "bottom_sheet_title":"Produk Pilihan Seller"
                     },
                     "room_background":{
                        "image_url":""
                     }
                  },
                  "app_link":"tokopedia://play/10748",
                  "web_link":"https://www.tokopedia.com/play/update",
                  "share":{
                     "text":"\"Test - Tokopedia Play !\"\nYuk, nonton siaran dari haha stag di Tokopedia PLAY! Bakal seru banget lho!\n        ${'$'}{url}        ",
                     "redirect_url":"https://tokopedia.link/RwsJ8Dcb2cb",
                     "use_short_url":false,
                     "meta_title":"Tokopedia PLAY seru!",
                     "meta_description":"Nonton siaran seru di Tokopedia PLAY!",
                     "is_show_button":true
                  }
               },
               {
                  "id":"10758",
                  "title":"Test - Tokopedia Play !",
                  "is_live":false,
                  "partner":{
                     "id":"479887",
                     "type":"shop",
                     "name":"haha stag",
                     "thumbnail_url":"https://images.tokopedia.net/img/cache/215-square/shops-1/2017/7/31/479887/479887_b682302c-3c6b-4053-859e-027af3a97c68.png",
                     "badge_url":"https://images.tokopedia.net/img/official_store/badge_os.png",
                     "app_link":"tokopedia://shop/479887",
                     "web_link":"https://staging.tokopedia.com/hahastag"
                  },
                  "video":{
                     "id":"1115",
                     "orientation":"horizontal",
                     "type":"vod",
                     "stream_source":"https://youtu.be/F1EsfKxZ2xs",
                     "autoplay":true,
                     "buffer_control":{
                        "max_buffer_in_seconds":18,
                        "min_buffer_in_seconds":3,
                        "buffer_for_playback":2,
                        "buffer_for_playback_after_rebuffer":2
                     }
                  },
                  "pinned_message":{
                     "id":"",
                     "title":"",
                     "redirect_url":""
                  },
                  "quick_replies":[
                     "❤ ❤ ❤",
                     "☺",
                     "👽",
                     "✈"
                  ],
                  "configurations":{
                     "show_cart":true,
                     "show_pinned_product":true,
                     "ping_interval":10000,
                     "max_chars":200,
                     "max_retries":5,
                     "min_reconnect_delay":5000,
                     "has_promo":false,
                     "reminder":{
                        "is_set":false
                     },
                     "channel_freeze_screen":{
                        "title":"%s Telah Berakhir",
                        "description":"Yuk lanjut nonton berbagai video menarik lainnya di Tokopedia Play!",
                        "button_text":"Cek Channel Lain",
                        "button_app_link":"tokopedia://webview?pull_to_refresh=true\u0026titlebar=false\u0026autoplay=true\u0026url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F"
                     },
                     "channel_banned_message":{
                        "title":"Anda diblokir admin",
                        "message":"Anda diblokir oleh admin karena melanggar syarat dan ketentuan Channel, sehingga tidak dapat melihat konten ini.",
                        "button_text":"OK"
                     },
                     "chat_config":{
                        "chat_enabled":false,
                        "chat_disabled_message":"Maaf, fitur chat sedang dimatikan oleh admin."
                     },
                     "feeds_like_params":{
                        "content_type":29,
                        "content_id":"10758",
                        "like_type":3
                     },
                     "pinned_product_config":{
                        "pin_title":"Ayo belanja barang pilihan kami sebelum kehabisan!",
                        "bottom_sheet_title":"Produk Pilihan Seller"
                     },
                     "room_background":{
                        "image_url":""
                     }
                  },
                  "app_link":"tokopedia://play/10758",
                  "web_link":"https://www.tokopedia.com/play/update",
                  "share":{
                     "text":"\"Test - Tokopedia Play !\"\nYuk, nonton siaran dari haha stag di Tokopedia PLAY! Bakal seru banget lho!\n        ${'$'}{url}        ",
                     "redirect_url":"https://tokopedia.link/phwM9Fyc2cb",
                     "use_short_url":false,
                     "meta_title":"Tokopedia PLAY seru!",
                     "meta_description":"Nonton siaran seru di Tokopedia PLAY!",
                     "is_show_button":true
                  }
               }
            ]
         }
      }
    """.trimIndent()

    private val partnerInfoResponse = """
        {
          "shopInfoByID": {
            "result": [
              {
                "shopCore": {
                  "name": "Bearhug-Id",
                  "shopID": "8377836"
                },
                "favoriteData": {
                  "totalFavorite": 3619,
                  "alreadyFavorited": 0
                }
              }
            ],
            "error": {
              "message": ""
            }
          }
        }
    """.trimIndent()

    fun buildReportSummariesResponse(): ReportSummaries {
        return gson.fromJson(reportSummariesResponse, ReportSummaries.Response::class.java).reportSummaries
    }

    fun buildChannelDetailsWithRecomResponse(): ChannelDetailsWithRecomResponse {
        return gson.fromJson(channelJsonWithRecomResponse, ChannelDetailsWithRecomResponse::class.java)
    }

    fun buildPartnerInfoResponse(): ShopInfo {
        return gson.fromJson(partnerInfoResponse, ShopInfo.Response::class.java).result.data.first()
    }

    fun buildCustomReportSummariesReponse(totalLike: String, totalView: String): ReportSummaries {
        return ReportSummaries(
                listOf(
                        ReportSummaries.Data(
                                ReportSummaries.Channel(
                                        ReportSummaries.Metric(
                                                totalLike = totalLike,
                                                totalLikeFmt = totalLike,
                                                totalView = totalView,
                                                totalViewFmt = totalView
                                        )
                                )
                        )
                )
        )
    }
}