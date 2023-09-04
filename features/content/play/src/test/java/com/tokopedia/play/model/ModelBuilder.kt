package com.tokopedia.play.model

import com.google.gson.Gson
import com.tokopedia.content.common.report_content.model.UserReportOptions
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.play.data.*
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.recom.PlayShareInfoUiModel
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.model.ui.PlayChatUiModel

/**
 * Created by jegul on 20/02/20
 */
class ModelBuilder {

    private val gson = Gson()

    private val channelJsonWithRecom = """
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
               },
               {
                  "id":"10756",
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
                     "id":"1113",
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
                        "content_id":"10756",
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
                  "app_link":"tokopedia://play/10756",
                  "web_link":"https://www.tokopedia.com/play/update",
                  "share":{
                     "text":"\"Test - Tokopedia Play !\"\nYuk, nonton siaran dari haha stag di Tokopedia PLAY! Bakal seru banget lho!\n        ${'$'}{url}        ",
                     "redirect_url":"https://tokopedia.link/us2iYzyc2cb",
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
               }
            ]
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
        "broadcasterReportSummariesBulk": {
          "reportData": [
            {
              "channel": {
                "metrics": {
                  "totalLike": "48",
                  "totalLikeFmt": "48"
                }
              }
            }
          ]
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
             "playGetTagsItemSection": {
               "sections": [
                 {
                   "type": "other",
                   "title": "",
                   "countdown": {
                     "copy": ""
                   },
                   "background": {
                     "gradient": [],
                     "image_url": ""
                   },
                   "start_time": "1970-01-01T07:00:00+07:00",
                   "end_time": "1970-01-01T07:00:00+07:00",
                   "server_time": "2023-05-08T10:24:04+07:00",
                   "source_id": "0",
                   "products": [
                     {
                       "id": "2148348755",
                       "name": "ku3 smil3y",
                       "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2022/3/16/64f663b5-48bd-4808-8b33-51e45c63e1e7.jpg",
                       "shop_id": "479541",
                       "original_price": 680952,
                       "original_price_formatted": "Rp 680.952",
                       "discount": 0,
                       "price": 0,
                       "price_formatted": "Rp 0",
                       "quantity": 1234,
                       "is_variant": false,
                       "is_available": false,
                       "order": 0,
                       "app_link": "tokopedia://product/2148348755",
                       "web_link": "https://staging.tokopedia.com/ituajakak/ku3-smil3y",
                       "min_quantity": 1,
                       "is_free_shipping": false,
                       "is_toko_now": false,
                       "is_pinned": false,
                       "available_buttons": [
                         {
                           "text": "+ Keranjang",
                           "color": "SECONDARY_DISABLED",
                           "button_type": "ATC"
                         },
                         {
                           "text": "Beli",
                           "color": "PRIMARY_DISABLED",
                           "button_type": "GCR"
                         }
                       ],
                       "rating": "",
                       "sold_quantity": "",
                       "social_proof_rank": "Diminati",
                        "social_proof_tag_color": ["#E02954", "#FF7182"],
                        "social_proof_raw_value": 1,
                        "social_proof_type_value": "diminati"
                     }
                   ]
                 }
               ],
               "vouchers": [
                {
                   "voucher_id": "6137135",
                   "voucher_name": "",
                   "shop_id": "0",
                   "title": "Gratis Ongkir Rp20.000",
                   "subtitle": "Transaksi min. Rp100.000",
                   "voucher_type": 1,
                   "voucher_image": "",
                   "voucher_image_square": "",
                   "voucher_finish_time": "2022-09-13T23:59:59Z",
                   "voucher_code": "KONGT074OKE",
                   "is_quota_available": 1,
                   "is_highlighted": true,
                   "is_copyable": true,
                   "is_private": true
                 },
                 {
                   "voucher_id": "11968111",
                   "voucher_name": "free ongkir",
                   "shop_id": "1961630",
                   "title": "Gratis Ongkir Rp20.000",
                   "subtitle": "Transaksi min. Rp100.000",
                   "voucher_type": 1,
                   "voucher_image": "https://images.tokopedia.net/img/BTJGre/2022/8/31/876cf223-e031-4410-a61e-74d6d3e9c8bd.jpg",
                   "voucher_image_square": "https://images.tokopedia.net/img/nNLhqY/2022/8/31/f47c1320-e630-4fec-9216-b9bb48de647a.jpg",
                   "voucher_finish_time": "2022-09-30T23:30:00Z",
                   "voucher_code": "KONGON40HU",
                   "is_quota_available": 1,
                   "is_highlighted": false,
                   "is_copyable": false,
                   "is_private": true
                 },
                 {
                   "voucher_id": "11968048",
                   "voucher_name": "voucher testing",
                   "shop_id": "1961630",
                   "title": "Gratis Ongkir Rp20.000",
                   "subtitle": "Transaksi min. Rp100.000",
                   "voucher_type": 1,
                   "voucher_image": "https://images.tokopedia.net/img/BTJGre/2022/8/31/a9660bff-3632-4929-a8ff-c056c81dc769.jpg",
                   "voucher_image_square": "https://images.tokopedia.net/img/nNLhqY/2022/8/31/0997fcca-3a69-4dc3-bc9e-db646c4296b1.jpg",
                   "voucher_finish_time": "2022-09-30T23:30:00Z",
                   "voucher_code": "KONGGWPFRO",
                   "is_quota_available": 1,
                   "is_highlighted": false,
                   "is_copyable": false,
                   "is_private": true
                 },
                 {
                   "voucher_id": "11968115",
                   "voucher_name": "cashback",
                   "shop_id": "1961630",
                   "title": "Cashback Rp20.000",
                   "subtitle": "Transaksi min. Rp287.500",
                   "voucher_type": 1,
                   "voucher_image": "https://images.tokopedia.net/img/BTJGre/2022/8/31/79a73fb7-12ae-4292-80b7-8c8b5a85393b.jpg",
                   "voucher_image_square": "https://images.tokopedia.net/img/nNLhqY/2022/8/31/f2a68a0e-70da-43ec-8dc2-d991eba517ff.jpg",
                   "voucher_finish_time": "2022-09-30T23:30:00Z",
                   "voucher_code": "KONG0NBOUE",
                   "is_quota_available": 1,
                   "is_highlighted": false,
                   "is_copyable": false,
                   "is_private": true
                 },
                 {
                   "voucher_id": "4946142",
                   "voucher_name": "ONGKIRFREE",
                   "shop_id": "1961630",
                   "title": "Gratis Ongkir Rp20.000",
                   "subtitle": "Transaksi min. Rp50.000",
                   "voucher_type": 1,
                   "voucher_image": "https://images.tokopedia.net/img/BTJGre/2021/9/23/f71f1cce-47ac-4e0d-ab6f-e3810ed5e344.jpg",
                   "voucher_image_square": "https://images.tokopedia.net/img/BTJGre/2021/9/23/f71f1cce-47ac-4e0d-ab6f-e3810ed5e344.jpg",
                   "voucher_quota": 25,
                   "voucher_finish_time": "2050-12-31T23:59:00Z",
                   "voucher_code": "KONGP0FL5B",
                   "is_quota_available": 1,
                   "is_highlighted": false,
                   "is_copyable": false,
                   "is_private": true
                 },
                 {
                   "voucher_id": "11968045",
                   "voucher_name": "voucher untuk trackeran",
                   "shop_id": "1961630",
                   "title": "Cashback Rp20.000",
                   "subtitle": "Transaksi min. Rp287.500",
                   "voucher_type": 1,
                   "voucher_image": "https://images.tokopedia.net/img/BTJGre/2022/8/31/2b25f48d-b8c1-4c89-ab25-82fc5c8146b1.jpg",
                   "voucher_image_square": "https://images.tokopedia.net/img/nNLhqY/2022/8/31/140e27e4-6f0d-4f70-a458-a386a91066f3.jpg",
                   "voucher_quota": 15,
                   "voucher_finish_time": "2022-09-30T23:30:00Z",
                   "voucher_code": "KONG3LPOBC",
                   "is_quota_available": 1,
                   "is_highlighted": false,
                   "is_copyable": false,
                   "is_private": true
                 },
                 {
                   "voucher_id": "11968042",
                   "voucher_name": "monthly voucher",
                   "shop_id": "1961630",
                   "title": "Gratis Ongkir Rp20.000",
                   "subtitle": "Transaksi min. Rp100.000",
                   "voucher_type": 1,
                   "voucher_image": "https://images.tokopedia.net/img/BTJGre/2022/8/31/e4a7b074-3d7d-43ee-afd3-890e3cbc9d26.jpg",
                   "voucher_image_square": "https://images.tokopedia.net/img/nNLhqY/2022/8/31/be163b96-589f-4a41-8394-788d15d2ce26.jpg",
                   "voucher_quota": 25,
                   "voucher_finish_time": "2022-09-30T23:30:00Z",
                   "voucher_code": "KONG92U02Z",
                   "is_quota_available": 1,
                   "is_highlighted": false,
                   "is_copyable": false,
                   "is_private": false
                 }

               ],
               "config": {
                 "peek_product_count": 15,
                 "title_bottomsheet": "Promo dan Barang Pilihan"
               }
             }
           }
    """.trimIndent()

    private val channelNoPublicVoucher = """
        {
      "playGetTagsItemSection": {
        "sections": [
          {
            "type": "other",
            "title": "",
            "countdown": {
              "copy": ""
            },
            "background": {
              "gradient": [],
              "image_url": ""
            },
            "start_time": "1970-01-01T07:00:00+07:00",
            "end_time": "1970-01-01T07:00:00+07:00",
            "server_time": "2023-05-08T10:24:04+07:00",
            "source_id": "0",
            "products": [
              {
                "id": "2148348755",
                "name": "ku3 smil3y",
                "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2022/3/16/64f663b5-48bd-4808-8b33-51e45c63e1e7.jpg",
                "shop_id": "479541",
                "original_price": 680952,
                "original_price_formatted": "Rp 680.952",
                "discount": 0,
                "price": 0,
                "price_formatted": "Rp 0",
                "quantity": 1234,
                "is_variant": false,
                "is_available": false,
                "order": 0,
                "app_link": "tokopedia://product/2148348755",
                "web_link": "https://staging.tokopedia.com/ituajakak/ku3-smil3y",
                "min_quantity": 1,
                "is_free_shipping": false,
                "is_toko_now": false,
                "is_pinned": false,
                "available_buttons": [
                  {
                    "text": "+ Keranjang",
                    "color": "SECONDARY_DISABLED",
                    "button_type": "ATC"
                  },
                  {
                    "text": "Beli",
                    "color": "PRIMARY_DISABLED",
                    "button_type": "GCR"
                  }
                ],
                "rating": "",
                "sold_quantity": "",
                "social_proof_rank": "Diminati",
                "social_proof_tag_color": ["#E02954", "#FF7182"],
                "social_proof_raw_value": 1,
                "social_proof_type_value": "diminati"
              }
            ]
          }
        ],
        "vouchers": [
         {
            "voucher_id": "6137135",
            "voucher_name": "",
            "shop_id": "0",
            "title": "Gratis Ongkir Rp20.000",
            "subtitle": "Transaksi min. Rp100.000",
            "voucher_type": 1,
            "voucher_image": "",
            "voucher_image_square": "",
            "voucher_finish_time": "2022-09-13T23:59:59Z",
            "voucher_code": "KONGT074OKE",
            "is_quota_available": 1,
            "is_highlighted": true,
            "is_copyable": true,
            "is_private": true
          },
          {
            "voucher_id": "11968111",
            "voucher_name": "free ongkir",
            "shop_id": "1961630",
            "title": "Gratis Ongkir Rp20.000",
            "subtitle": "Transaksi min. Rp100.000",
            "voucher_type": 1,
            "voucher_image": "https://images.tokopedia.net/img/BTJGre/2022/8/31/876cf223-e031-4410-a61e-74d6d3e9c8bd.jpg",
            "voucher_image_square": "https://images.tokopedia.net/img/nNLhqY/2022/8/31/f47c1320-e630-4fec-9216-b9bb48de647a.jpg",
            "voucher_finish_time": "2022-09-30T23:30:00Z",
            "voucher_code": "KONGON40HU",
            "is_quota_available": 1,
            "is_highlighted": false,
            "is_copyable": false,
            "is_private": true
          },
          {
            "voucher_id": "11968048",
            "voucher_name": "voucher testing",
            "shop_id": "1961630",
            "title": "Gratis Ongkir Rp20.000",
            "subtitle": "Transaksi min. Rp100.000",
            "voucher_type": 1,
            "voucher_image": "https://images.tokopedia.net/img/BTJGre/2022/8/31/a9660bff-3632-4929-a8ff-c056c81dc769.jpg",
            "voucher_image_square": "https://images.tokopedia.net/img/nNLhqY/2022/8/31/0997fcca-3a69-4dc3-bc9e-db646c4296b1.jpg",
            "voucher_finish_time": "2022-09-30T23:30:00Z",
            "voucher_code": "KONGGWPFRO",
            "is_quota_available": 1,
            "is_highlighted": false,
            "is_copyable": false,
            "is_private": true
          },
          {
            "voucher_id": "11968115",
            "voucher_name": "cashback",
            "shop_id": "1961630",
            "title": "Cashback Rp20.000",
            "subtitle": "Transaksi min. Rp287.500",
            "voucher_type": 1,
            "voucher_image": "https://images.tokopedia.net/img/BTJGre/2022/8/31/79a73fb7-12ae-4292-80b7-8c8b5a85393b.jpg",
            "voucher_image_square": "https://images.tokopedia.net/img/nNLhqY/2022/8/31/f2a68a0e-70da-43ec-8dc2-d991eba517ff.jpg",
            "voucher_finish_time": "2022-09-30T23:30:00Z",
            "voucher_code": "KONG0NBOUE",
            "is_quota_available": 1,
            "is_highlighted": false,
            "is_copyable": false,
            "is_private": true
          },
          {
            "voucher_id": "4946142",
            "voucher_name": "ONGKIRFREE",
            "shop_id": "1961630",
            "title": "Gratis Ongkir Rp20.000",
            "subtitle": "Transaksi min. Rp50.000",
            "voucher_type": 1,
            "voucher_image": "https://images.tokopedia.net/img/BTJGre/2021/9/23/f71f1cce-47ac-4e0d-ab6f-e3810ed5e344.jpg",
            "voucher_image_square": "https://images.tokopedia.net/img/BTJGre/2021/9/23/f71f1cce-47ac-4e0d-ab6f-e3810ed5e344.jpg",
            "voucher_quota": 25,
            "voucher_finish_time": "2050-12-31T23:59:00Z",
            "voucher_code": "KONGP0FL5B",
            "is_quota_available": 1,
            "is_highlighted": false,
            "is_copyable": false,
            "is_private": true
          },
          {
            "voucher_id": "11968045",
            "voucher_name": "voucher untuk trackeran",
            "shop_id": "1961630",
            "title": "Cashback Rp20.000",
            "subtitle": "Transaksi min. Rp287.500",
            "voucher_type": 1,
            "voucher_image": "https://images.tokopedia.net/img/BTJGre/2022/8/31/2b25f48d-b8c1-4c89-ab25-82fc5c8146b1.jpg",
            "voucher_image_square": "https://images.tokopedia.net/img/nNLhqY/2022/8/31/140e27e4-6f0d-4f70-a458-a386a91066f3.jpg",
            "voucher_quota": 15,
            "voucher_finish_time": "2022-09-30T23:30:00Z",
            "voucher_code": "KONG3LPOBC",
            "is_quota_available": 1,
            "is_highlighted": false,
            "is_copyable": false,
            "is_private": true
          },
          {
            "voucher_id": "11968042",
            "voucher_name": "monthly voucher",
            "shop_id": "1961630",
            "title": "Gratis Ongkir Rp20.000",
            "subtitle": "Transaksi min. Rp100.000",
            "voucher_type": 1,
            "voucher_image": "https://images.tokopedia.net/img/BTJGre/2022/8/31/e4a7b074-3d7d-43ee-afd3-890e3cbc9d26.jpg",
            "voucher_image_square": "https://images.tokopedia.net/img/nNLhqY/2022/8/31/be163b96-589f-4a41-8394-788d15d2ce26.jpg",
            "voucher_quota": 25,
            "voucher_finish_time": "2022-09-30T23:30:00Z",
            "voucher_code": "KONG92U02Z",
            "is_quota_available": 1,
            "is_highlighted": false,
            "is_copyable": false,
            "is_private": true
          }

        ],
        "config": {
          "peek_product_count": 15,
          "title_bottomsheet": "Promo dan Barang Pilihan"
        }
      }
    }
    """.trimIndent()

    private val productVariant = """
        {
        	"getProductVariant": {
        		"parentID": "745647988",
        		"defaultChild": "745647992",
        		"sizeChart": "",
        		"alwaysAvailable": false,
        		"stock": 52,
        		"variant": [{
        			"name": "warna",
        			"identifier": "colour",
        			"unitName": "",
        			"position": 1,
        			"variantID": "1",
        			"variantUnitID": 0,
        			"productVariantID": "15125086",
        			"option": [{
        					"value": "Merah",
        					"hex": "#ff0016",
        					"productVariantOptionID": 47372624,
        					"variantUnitValueID": 9,
        					"picture": {
        						"url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420",
        						"url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420"
        					}
        				},
        				{
        					"value": "Biru",
        					"hex": "#1d6cbb",
        					"productVariantOptionID": 47372625,
        					"variantUnitValueID": 5,
        					"picture": {
        						"url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491",
        						"url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491"
        					}
        				},
        				{
        					"value": "Hijau",
        					"hex": "#006400",
        					"productVariantOptionID": 47372626,
        					"variantUnitValueID": 18,
        					"picture": {
        						"url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450",
        						"url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450"
        					}
        				},
        				{
        					"value": "Hitam",
        					"hex": "#000000",
        					"productVariantOptionID": 47372627,
        					"variantUnitValueID": 2,
        					"picture": {
        						"url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000",
        						"url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000"
        					}
        				},
        				{
        					"value": "Biru Muda",
        					"hex": "#8ad1e8",
        					"productVariantOptionID": 47372628,
        					"variantUnitValueID": 6,
        					"picture": {
        						"url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105",
        						"url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105"
        					}
        				},
        				{
        					"value": "Cokelat",
        					"hex": "#8b4513",
        					"productVariantOptionID": 47372629,
        					"variantUnitValueID": 16,
        					"picture": {
        						"url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860",
        						"url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860"
        					}
        				}
        			]
        		}],
        		"children": [{
        				"productID": "745647991",
        				"price": 110000,
        				"priceFmt": "Rp 110.000",
        				"sku": "",
        				"optionID": [
        					47372624
        				],
        				"productName": "starterpokemonberdasarkanwarna - Merah",
        				"productURL": "https://www.tokopedia.com/ostactical/starterpokemonberdasarkanwarna-merah",
        				"isCOD": false,
        				"isWishlist": false,
        				"campaignInfo": {
        					"campaignID": "0",
        					"isActive": false,
        					"originalPrice": 0,
        					"originalPriceFmt": "",
        					"discountPercentage": 0,
        					"discountPrice": 0,
        					"discountPriceFmt": "",
        					"campaignType": 0,
        					"campaignTypeName": "",
        					"startDate": "",
        					"endDate": "",
        					"stock": 0,
        					"appLinks": "",
        					"isAppsOnly": false
        				},
        				"stock": {
        					"isLimitedStock": false,
        					"stockWording": "Stok tersisa <5, beli segera!",
        					"stockWordingHTML": "Stok <b style='color:red'>tersisa &lt;5,</b> beli segera!",
        					"otherVariantStock": "available",
        					"minimumOrder": 1,
        					"maximumOrder": 0,
        					"isBuyable": true,
        					"alwaysAvailable": false
        				},
        				"picture": {
        					"url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420",
        					"url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420"
        				}
        			},
        			{
        				"productID": "745647992",
        				"price": 120000,
        				"priceFmt": "Rp 120.000",
        				"sku": "",
        				"optionID": [
        					47372625
        				],
        				"productName": "starterpokemonberdasarkanwarna - Biru",
        				"productURL": "https://www.tokopedia.com/ostactical/starterpokemonberdasarkanwarna-biru",
        				"isCOD": false,
        				"isWishlist": false,
        				"campaignInfo": {
        					"campaignID": "0",
        					"isActive": false,
        					"originalPrice": 0,
        					"originalPriceFmt": "",
        					"discountPercentage": 0,
        					"discountPrice": 0,
        					"discountPriceFmt": "",
        					"campaignType": 0,
        					"campaignTypeName": "",
        					"startDate": "",
        					"endDate": "",
        					"stock": 0,
        					"appLinks": "",
        					"isAppsOnly": false
        				},
        				"stock": {
        					"isLimitedStock": false,
        					"stockWording": "Stok tinggal <20, beli segera!",
        					"stockWordingHTML": "Stok tinggal &lt;20, beli segera!",
        					"otherVariantStock": "available",
        					"minimumOrder": 1,
        					"maximumOrder": 0,
        					"isBuyable": true,
        					"alwaysAvailable": false
        				},
        				"picture": {
        					"url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491",
        					"url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491"
        				}
        			},
        			{
        				"productID": "745647993",
        				"price": 130000,
        				"priceFmt": "Rp 130.000",
        				"sku": "",
        				"optionID": [
        					47372626
        				],
        				"productName": "starterpokemonberdasarkanwarna - Hijau",
        				"productURL": "https://www.tokopedia.com/ostactical/starterpokemonberdasarkanwarna-hijau",
        				"isCOD": false,
        				"isWishlist": false,
        				"campaignInfo": {
        					"campaignID": "0",
        					"isActive": false,
        					"originalPrice": 0,
        					"originalPriceFmt": "",
        					"discountPercentage": 0,
        					"discountPrice": 0,
        					"discountPriceFmt": "",
        					"campaignType": 0,
        					"campaignTypeName": "",
        					"startDate": "",
        					"endDate": "",
        					"stock": 0,
        					"appLinks": "",
        					"isAppsOnly": false
        				},
        				"stock": {
        					"isLimitedStock": false,
        					"stockWording": "Tersedia Untuk Varian Lain",
        					"stockWordingHTML": "Tersedia Untuk Varian Lain",
        					"otherVariantStock": "available",
        					"minimumOrder": 1,
        					"maximumOrder": 0,
        					"isBuyable": false,
        					"alwaysAvailable": false
        				},
        				"picture": {
        					"url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450",
        					"url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450"
        				}
        			},
        			{
        				"productID": "745647994",
        				"price": 300000,
        				"priceFmt": "Rp 300.000",
        				"sku": "",
        				"optionID": [
        					47372627
        				],
        				"productName": "starterpokemonberdasarkanwarna - Hitam",
        				"productURL": "https://www.tokopedia.com/ostactical/starterpokemonberdasarkanwarna-hitam",
        				"isCOD": false,
        				"isWishlist": false,
        				"campaignInfo": {
        					"campaignID": "0",
        					"isActive": false,
        					"originalPrice": 0,
        					"originalPriceFmt": "",
        					"discountPercentage": 0,
        					"discountPrice": 0,
        					"discountPriceFmt": "",
        					"campaignType": 0,
        					"campaignTypeName": "",
        					"startDate": "",
        					"endDate": "",
        					"stock": 0,
        					"appLinks": "",
        					"isAppsOnly": false
        				},
        				"stock": {
        					"isLimitedStock": false,
        					"stockWording": "Stok tinggal <20, beli segera!",
        					"stockWordingHTML": "Stok tinggal &lt;20, beli segera!",
        					"otherVariantStock": "available",
        					"minimumOrder": 1,
        					"maximumOrder": 0,
        					"isBuyable": true,
        					"alwaysAvailable": false
        				},
        				"picture": {
        					"url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000",
        					"url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000"
        				}
        			},
        			{
        				"productID": "745647995",
        				"price": 500000,
        				"priceFmt": "Rp 500.000",
        				"sku": "",
        				"optionID": [
        					47372628
        				],
        				"productName": "starterpokemonberdasarkanwarna - Biru Muda",
        				"productURL": "https://www.tokopedia.com/ostactical/starterpokemonberdasarkanwarna-biru-muda",
        				"isCOD": false,
        				"isWishlist": false,
        				"campaignInfo": {
        					"campaignID": "0",
        					"isActive": false,
        					"originalPrice": 0,
        					"originalPriceFmt": "",
        					"discountPercentage": 0,
        					"discountPrice": 0,
        					"discountPriceFmt": "",
        					"campaignType": 0,
        					"campaignTypeName": "",
        					"startDate": "",
        					"endDate": "",
        					"stock": 0,
        					"appLinks": "",
        					"isAppsOnly": false
        				},
        				"stock": {
        					"isLimitedStock": false,
        					"stockWording": "Tersedia Untuk Varian Lain",
        					"stockWordingHTML": "Tersedia Untuk Varian Lain",
        					"otherVariantStock": "available",
        					"minimumOrder": 1,
        					"maximumOrder": 0,
        					"isBuyable": false,
        					"alwaysAvailable": false
        				},
        				"picture": {
        					"url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105",
        					"url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105"
        				}
        			},
        			{
        				"productID": "745647996",
        				"price": 190000,
        				"priceFmt": "Rp 190.000",
        				"sku": "",
        				"optionID": [
        					47372629
        				],
        				"productName": "starterpokemonberdasarkanwarna - Cokelat",
        				"productURL": "https://www.tokopedia.com/ostactical/starterpokemonberdasarkanwarna-cokelat",
        				"isCOD": false,
        				"isWishlist": false,
        				"campaignInfo": {
        					"campaignID": "0",
        					"isActive": false,
        					"originalPrice": 0,
        					"originalPriceFmt": "",
        					"discountPercentage": 0,
        					"discountPrice": 0,
        					"discountPriceFmt": "",
        					"campaignType": 0,
        					"campaignTypeName": "",
        					"startDate": "",
        					"endDate": "",
        					"stock": 0,
        					"appLinks": "",
        					"isAppsOnly": false
        				},
        				"stock": {
        					"isLimitedStock": false,
        					"stockWording": "Tersedia Untuk Varian Lain",
        					"stockWordingHTML": "Tersedia Untuk Varian Lain",
        					"otherVariantStock": "available",
        					"minimumOrder": 1,
        					"maximumOrder": 0,
        					"isBuyable": false,
        					"alwaysAvailable": false
        				},
        				"picture": {
        					"url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860",
        					"url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860"
        				}
        			}
        		]
        	}
        }
    """.trimIndent()

    private val product = """
    {
       "id": "748953246",
       "name": "digimonroyalknightsberdasarkanwarna",
       "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/20/82764734/82764734_5759a072-79f9-4b66-8cf8-65c0e94721e3_779_779",
       "shop_id": "7307032",
       "original_price": 1000000,
       "original_price_formatted": "Rp 1.000.000",
       "discount": 0,
       "price": 0,
       "price_formatted": "Rp 0",
       "quantity": 0,
       "is_variant": true,
       "is_available": true,
       "order": 2,
       "app_link": "tokopedia://product/748953246",
       "web_link": "https://www.tokopedia.com/ostactical/digimonroyalknightsberdasarkanwarna",
       "min_quantity": 1,
       "is_free_shipping": true
     }
    """.trimIndent()

    private val socketCredential = """
        {
            "gc_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxOTAxNjg3MSwiaWQiOjE5MDE2ODcxLCJuYW1lIjoiRmF1emFub2ZhbWkgU2F0dSBEdWEiLCJkYXRhIjoiIiwicGFydG5lcl90eXBlIjowLCJwYXJ0bmVyX2lkIjowLCJhdWQiOiJGYXV6YW5vZmFtaSBTYXR1IER1YSIsImV4cCI6MTU5NzI0MjI1MCwianRpIjoiMTkwMTY4NzEiLCJpYXQiOjE1OTcyMjcyNTAsImlzcyI6InRva29wZWRpYV9wbGF5IiwibmJmIjoxNTk3MjI3MjUwLCJzdWIiOiJ0b2tvcGVkaWFfcGxheV90b2tlbl8xOTAxNjg3MV8xNTk3MjI3MjUwIn0.akrNYluXcNogqxk83H9Gr1ZlqpH4eam1UlwRK6xD7H0",
            "setting": {
                "ping_interval": 10000,
                "max_chars": 200,
                "max_retries": 5,
                "min_reconnect_delay": 5000
              }
        }
    """.trimIndent()

    fun buildChannel(): ChannelDetailsWithRecomResponse =
        gson.fromJson(channelJsonWithRecom, ChannelDetailsWithRecomResponse::class.java)

    fun buildSocketCredential(): SocketCredential =
        gson.fromJson(socketCredential, SocketCredential::class.java)

    fun buildChannelWithShop(): ChannelDetailsWithRecomResponse =
        gson.fromJson(channelJsonWithRecom, ChannelDetailsWithRecomResponse::class.java)

    fun buildShopInfo(): ShopInfo = gson.fromJson(shopInfoJson, ShopInfo::class.java)

    fun buildNewChat(): PlayChat = gson.fromJson(newChatJson, PlayChat::class.java)

    fun buildTotalLike(): TotalLikeContent.Response =
        gson.fromJson(totalLikeCount, TotalLikeContent.Response::class.java)

    fun buildIsLike(): IsLikedContent.Data = gson.fromJson(isLike, IsLikedContent.Data::class.java)

    fun buildProductTagging(): ProductSection.Response =
        gson.fromJson(channelTagItemsJson, ProductSection.Response::class.java)

    fun buildNonPublic(): ProductSection.Response =
        gson.fromJson(channelNoPublicVoucher, ProductSection.Response::class.java)

    fun buildProduct(): Product = gson.fromJson(product, Product::class.java)

    fun buildAddToCartModelResponseSuccess() = CartFeedbackResponseModel(
        isSuccess = true,
        errorMessage = IllegalStateException(""),
        cartId = "123"
    )

    fun buildAddToCartModelResponseFail() = CartFeedbackResponseModel(
        isSuccess = false,
        errorMessage = IllegalStateException("error message "),
        cartId = ""
    )

    fun buildCartUiModel(
        product: PlayProductUiModel.Product,
        action: ProductAction,
        bottomInsetsType: BottomInsetsType,
        isSuccess: Boolean = true,
        errorMessage: Throwable = IllegalStateException(""),
        cartId: String = "123"
    ) = CartFeedbackUiModel(
        isSuccess = isSuccess,
        errorMessage = errorMessage,
        action = action,
        product = product,
        bottomInsetsType = bottomInsetsType,
        cartId = cartId
    )

    /**
     * UI Model
     */
    fun buildVideoPropertyUiModel(
        state: PlayViewerVideoState = PlayViewerVideoState.Play
    ) = VideoPropertyUiModel(state = state)

    fun buildPlayChatUiModel(
        messageId: String = "1",
        userId: String = "1251",
        name: String = "mzennis",
        message: String = "Keren banget fitur ini.",
        isSelfMessage: Boolean = true
    ) = PlayChatUiModel(
        messageId = messageId,
        userId = userId,
        name = name,
        message = message,
        isSelfMessage = isSelfMessage
    )

    fun buildPlayBufferControl(
        minBufferMs: Int = 15000,
        maxBufferMs: Int = 50000,
        bufferForPlaybackMs: Int = 2500,
        bufferForPlaybackAfterRebufferMs: Int = 5000
    ) = PlayBufferControl(
        minBufferMs = minBufferMs,
        maxBufferMs = maxBufferMs,
        bufferForPlaybackMs = bufferForPlaybackMs,
        bufferForPlaybackAfterRebufferMs = bufferForPlaybackAfterRebufferMs
    )

    fun buildProductLineUiModel(
        id: String = "123",
        shopId: String = "567",
        imageUrl: String = "https://tkp.me",
        title: String = "Product laku",
        stock: ProductStock = buildProductStockAvailable(),
        isVariantAvailable: Boolean = true,
        price: ProductPrice = buildOriginalPrice(),
        minQty: Int = 2,
        isFreeShipping: Boolean = true,
        applink: String? = "https://tkp.me",
        isTokoNow: Boolean = false,
        isPinned: Boolean = false,
        isRilisanSpesial: Boolean = false,
        buttons: List<ProductButtonUiModel> = emptyList(),
        isProductNumerationShown: Boolean = false,
        number: String = "0",
        rating: String = "",
        soldQuantity: String = "",
        label: PlayProductUiModel.Product.Label = PlayProductUiModel.Product.Label(rankColors = emptyList(), rankFmt = "", rankType = ""),
    ) = PlayProductUiModel.Product(
        id = id,
        shopId = shopId,
        imageUrl = imageUrl,
        title = title,
        stock = stock,
        isVariantAvailable = isVariantAvailable,
        price = price,
        minQty = minQty,
        isFreeShipping = isFreeShipping,
        applink = applink,
        isTokoNow = isTokoNow,
        isPinned = isPinned,
        isRilisanSpesial = isRilisanSpesial,
        buttons = buttons,
        isNumerationShown = isProductNumerationShown,
        number = number,
        rating = rating,
        soldQuantity = soldQuantity,
        label = label,
    )

    fun buildProductStockAvailable(
        stock: Int = 5
    ) = StockAvailable(stock)

    fun buildOriginalPrice(
        price: String = "Rp120.000",
        priceNumber: Double = 120000.0
    ) = OriginalPrice(
        price = price,
        priceNumber = priceNumber
    )

    fun buildDiscountedPrice(
        originalPrice: String = "Rp120.000",
        discountPercent: Long = 10L,
        discountedPrice: String = "Rp108.000",
        discountedPriceNumber: Double = 108000.0
    ) = DiscountedPrice(
        originalPrice = originalPrice,
        discountPercent = discountPercent,
        discountedPrice = discountedPrice,
        discountedPriceNumber = discountedPriceNumber
    )

    fun buildPlayRoomFreezeEvent(
        title: String = "Freeze",
        message: String = "Kamu kena freeze",
        btnTitle: String = "Defroze",
        btnUrl: String = "https://tkp.me"
    ) = PlayRoomEvent.Freeze(
        title = title,
        message = message,
        btnTitle = btnTitle,
        btnUrl = btnUrl
    )

    fun buildPlayRoomBannedEvent(
        title: String = "Banned",
        message: String = "Kamu kena banned",
        btnTitle: String = "Hiks"
    ) = PlayRoomEvent.Banned(
        title = title,
        message = message,
        btnTitle = btnTitle
    )

    fun buildBottomInsetsMap(
        keyboardState: BottomInsetsState = buildBottomInsetsState(),
        productSheetState: BottomInsetsState = buildBottomInsetsState()
    ) = mapOf(
        BottomInsetsType.Keyboard to keyboardState,
        BottomInsetsType.ProductSheet to productSheetState
    )

    fun buildBottomInsetsState(
        isShown: Boolean = false,
        isPreviousSameState: Boolean = false,
        estimatedInsetsHeight: Int = 250
    ) = if (isShown) {
        BottomInsetsState.Shown(
            estimatedInsetsHeight,
            isPreviousSameState
        )
    } else {
        BottomInsetsState.Hidden(isPreviousSameState)
    }

    fun <T> buildPlayResultLoading(
        showPlaceholder: Boolean = true
    ) = PlayResult.Loading<T>(showPlaceholder)

    fun <T> buildPlayResultFailure(
        error: Throwable = IllegalArgumentException(),
        onRetry: () -> Unit = {}
    ) = PlayResult.Failure<T>(error, onRetry)

    fun <T> buildPlayResultSuccess(
        data: T
    ) = PlayResult.Success(data)

    fun buildShareInfoUiModel(channel: Channel): PlayShareInfoUiModel {
        val fullShareContent = try {
            channel.share.text.replace("${'$'}{url}", channel.share.redirectUrl)
        } catch (e: Throwable) {
            "${channel.share.text}/n${channel.share.redirectUrl}"
        }

        return PlayShareInfoUiModel(
            content = fullShareContent,
            shouldShow = channel.share.isShowButton &&
                channel.share.redirectUrl.isNotBlank() &&
                channel.configuration.active &&
                !channel.configuration.freezed
        )
    }

    fun buildUserReportList(
        reasoningId: Int = 1,
        title: String = "Harga Detail",
        detail: String = "Harga Tidak Wajar",
        submissionData: UserReportOptions.OptionAdditionalField = UserReportOptions.OptionAdditionalField(
            key = "report_reason",
            label = "Detail Laporan",
            max = 255,
            min = 10,
            type = "textarea"
        )
    ): PlayUserReportUiModel.Loaded {
        val userReportOpt = PlayUserReportReasoningUiModel.Reasoning(
            title = title,
            reasoningId = reasoningId,
            detail = detail,
            submissionData = submissionData
        )
        return PlayUserReportUiModel.Loaded(
            listOf(userReportOpt, userReportOpt),
            ResultState.Success
        )
    }

    fun generateResponseSectionGql(
        size: Int = 2,
        shopId: String = "123",
        imageUrl: String = "https://www.tokopedia.com",
        title: String = "Barang Murah",
        isVariantAvailable: Boolean = false,
        minQty: Int = 1,
        isFreeShipping: Boolean = false,
        applink: String? = null,
        gradient: List<String>? = null
    ): ProductSection.Response {
        val data = """
                 {
                  "playGetTagsItemSection": {
                    "sections": [
                      {
                        "type": "other",
                        "title": "",
                        "countdown": {
                          "copy": ""
                        },
                        "background": {
                          "gradient": [],
                          "image_url": ""
                        },
                        "start_time": "1970-01-01T07:00:00+07:00",
                        "end_time": "1970-01-01T07:00:00+07:00",
                        "server_time": "2023-03-21T15:36:25+07:00",
                        "source_id": "0",
                        "products": [
                          {
                            "id": "6925401702",
                            "name": "Hampers 6 Kaleng Cookies Kue Kering",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2023/2/25/916aeea4-a7c5-45f2-b930-2c2b7642120e.jpg",
                            "shop_id": "11757826",
                            "original_price": 195000,
                            "original_price_formatted": "Rp 195.000",
                            "discount": 5,
                            "price": 185250,
                            "price_formatted": "Rp 185.250",
                            "quantity": 4483,
                            "is_variant": true,
                            "is_available": true,
                            "order": 0,
                            "app_link": "tokopedia://product/8174237886",
                            "web_link": "https://www.tokopedia.com/missogura/hampers-6-kaleng-cookies-kue-kering-batch2-17-21mar",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli",
                                "color": "SECONDARY",
                                "button_type": "GCR"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 1,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "8175013631",
                            "name": "Kurma Medjool Cake Cupcake Muffin",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2023/2/25/0a756fc8-5a58-46d6-a049-9f3d7ff85820.jpg",
                            "shop_id": "11757826",
                            "original_price": 65000,
                            "original_price_formatted": "Rp 65.000",
                            "discount": 5,
                            "price": 61750,
                            "price_formatted": "Rp 61.750",
                            "quantity": 8979,
                            "is_variant": true,
                            "is_available": true,
                            "order": 1,
                            "app_link": "tokopedia://product/8175013641",
                            "web_link": "https://www.tokopedia.com/missogura/kurma-medjool-cake-cupcake-muffin-isi-12-batch7-11-15apr-76baf",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli",
                                "color": "SECONDARY",
                                "button_type": "GCR"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 2,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "3325111748",
                            "name": "Ameena Hampers Lebaran Eid Trio Nastar",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2023/2/25/9e0672a0-bc8d-40cb-8b02-c73ed87913c5.jpg",
                            "shop_id": "11757826",
                            "original_price": 65000,
                            "original_price_formatted": "Rp 65.000",
                            "discount": 5,
                            "price": 61750,
                            "price_formatted": "Rp 61.750",
                            "quantity": 8956,
                            "is_variant": true,
                            "is_available": true,
                            "order": 2,
                            "app_link": "tokopedia://product/8160582985",
                            "web_link": "https://www.tokopedia.com/missogura/ameena-hampers-lebaran-eid-trio-nastar-isi-12-batch8-16-20apr",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli",
                                "color": "SECONDARY",
                                "button_type": "GCR"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 3,
                            "rating": "",
                            "sold_quantity": "",
                             "social_proof_rank": "Diminati",
                             "social_proof_tag_color": ["#E02954", "#FF7182"],
                             "social_proof_raw_value": 1,
                             "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "7506341076",
                            "name": "Melted Cookies / Messy Cookies / Cookies Siram",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2023/3/5/6b6ccfaa-602c-43ff-842d-a77832599eb9.jpg",
                            "shop_id": "11757826",
                            "original_price": 60000,
                            "original_price_formatted": "Rp 60.000",
                            "discount": 5,
                            "price": 57000,
                            "price_formatted": "Rp 57.000",
                            "quantity": 8980,
                            "is_variant": true,
                            "is_available": true,
                            "order": 3,
                            "app_link": "tokopedia://product/8319956789",
                            "web_link": "https://www.tokopedia.com/missogura/melted-cookies-messy-cookies-cookies-siram-pandan-kurma-batch2-17-21mar",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli",
                                "color": "SECONDARY",
                                "button_type": "GCR"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 4,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "1953633275",
                            "name": "Seaweed edamame",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/6/26/f181fcc4-e3c6-4dbb-84a8-d2c7cb45dd90.jpg",
                            "shop_id": "11757826",
                            "original_price": 40000,
                            "original_price_formatted": "Rp 40.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "Rp 0",
                            "quantity": 2027,
                            "is_variant": false,
                            "is_available": true,
                            "order": 4,
                            "app_link": "tokopedia://product/1953633275",
                            "web_link": "https://www.tokopedia.com/missogura/seaweed-edamame",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 5,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "8584759533",
                            "name": "KOKO Kookies Kocok / Cookies / Kue Kering",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2023/3/17/d5649a0f-c2bc-4b31-a5d5-043965eac2d6.jpg",
                            "shop_id": "11757826",
                            "original_price": 27000,
                            "original_price_formatted": "Rp 27.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "",
                            "quantity": 1485,
                            "is_variant": true,
                            "is_available": true,
                            "order": 5,
                            "app_link": "tokopedia://product/8584759541",
                            "web_link": "https://www.tokopedia.com/missogura/koko-kookies-kocok-cookies-kue-kering-mie-goreng-788e8",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 6,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "2851476971",
                            "name": "Nastar Ronde",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2022/2/1/2bee065f-f056-4731-953f-253a98752505.jpg",
                            "shop_id": "11757826",
                            "original_price": 60000,
                            "original_price_formatted": "Rp 60.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "",
                            "quantity": 1076,
                            "is_variant": true,
                            "is_available": true,
                            "order": 6,
                            "app_link": "tokopedia://product/2851476977",
                            "web_link": "https://www.tokopedia.com/missogura/nastar-ronde-isi-12",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 7,
                            "rating": "",
                             "sold_quantity": "",
                             "social_proof_rank": "Diminati",
                             "social_proof_tag_color": ["#E02954", "#FF7182"],
                             "social_proof_raw_value": 1,
                             "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "1953652961",
                            "name": "Nastar Klepon",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/6/26/be20e6b4-f9b9-42c2-971f-27fc902448ef.jpg",
                            "shop_id": "11757826",
                            "original_price": 60000,
                            "original_price_formatted": "Rp 60.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "",
                            "quantity": 3471,
                            "is_variant": true,
                            "is_available": true,
                            "order": 7,
                            "app_link": "tokopedia://product/2012242417",
                            "web_link": "https://www.tokopedia.com/missogura/nastar-klepon-isi-12",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 8,
                            "rating": "",
                             "sold_quantity": "",
                             "social_proof_rank": "Diminati",
                             "social_proof_tag_color": ["#E02954", "#FF7182"],
                             "social_proof_raw_value": 1,
                             "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "1953621470",
                            "name": "Greentea tea bag cookies",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/6/26/930957b9-5d2c-4511-9999-48a7c2ef0f20.jpg",
                            "shop_id": "11757826",
                            "original_price": 75000,
                            "original_price_formatted": "Rp 75.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "Rp 0",
                            "quantity": 983,
                            "is_variant": false,
                            "is_available": true,
                            "order": 8,
                            "app_link": "tokopedia://product/1953621470",
                            "web_link": "https://www.tokopedia.com/missogura/greentea-tea-bag-cookies",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 9,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "6924972249",
                            "name": "Rum Kartoshka Cake - Rum Balls",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2022/11/9/d475c2e3-eeb3-4f39-87fe-5b05bd275de6.jpg",
                            "shop_id": "11757826",
                            "original_price": 75000,
                            "original_price_formatted": "Rp 75.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "",
                            "quantity": 134,
                            "is_variant": true,
                            "is_available": true,
                            "order": 9,
                            "app_link": "tokopedia://product/7326019550",
                            "web_link": "https://www.tokopedia.com/missogura/rum-kartoshka-cake-rum-balls-isi-12pc",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 10,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "1953600735",
                            "name": "Earl grey tea bag cookies",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/6/26/67083bf9-1341-4f40-aaae-138be0dd083f.jpg",
                            "shop_id": "11757826",
                            "original_price": 70000,
                            "original_price_formatted": "Rp 70.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "Rp 0",
                            "quantity": 1481,
                            "is_variant": false,
                            "is_available": true,
                            "order": 10,
                            "app_link": "tokopedia://product/1953600735",
                            "web_link": "https://www.tokopedia.com/missogura/earl-grey-tea-bag-cookies",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 11,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "2671545794",
                            "name": "Orange Marmalade Cake Hampers Imlek CNY",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2022/1/5/70c953ea-ed67-4f1e-8bfa-d95a6dea9a7e.jpg",
                            "shop_id": "11757826",
                            "original_price": 65000,
                            "original_price_formatted": "Rp 65.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "Rp 0",
                            "quantity": 1872,
                            "is_variant": false,
                            "is_available": true,
                            "order": 11,
                            "app_link": "tokopedia://product/2671545794",
                            "web_link": "https://www.tokopedia.com/missogura/orange-marmalade-cake-hampers-imlek-cny",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 12,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "1953609394",
                            "name": "Espresso cookies",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/6/26/920f1dd7-a6f7-4eaf-9626-dad9881117b1.jpg",
                            "shop_id": "11757826",
                            "original_price": 45000,
                            "original_price_formatted": "Rp 45.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "",
                            "quantity": 1809,
                            "is_variant": true,
                            "is_available": true,
                            "order": 12,
                            "app_link": "tokopedia://product/2596394887",
                            "web_link": "https://www.tokopedia.com/missogura/espresso-cookies-125gr",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 13,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "1953658710",
                            "name": "Choco Soes",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/6/26/89943743-ea61-4d2f-a8eb-e8a1c618deb2.jpg",
                            "shop_id": "11757826",
                            "original_price": 35000,
                            "original_price_formatted": "Rp 35.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "",
                            "quantity": 656,
                            "is_variant": true,
                            "is_available": true,
                            "order": 13,
                            "app_link": "tokopedia://product/2290364287",
                            "web_link": "https://www.tokopedia.com/missogura/choco-soes-choco",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 14,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "2373468360",
                            "name": "Lavender Cookies - vegan",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/11/4/a5f5770c-ffe1-4776-9e5b-46a59dc13df1.jpg",
                            "shop_id": "11757826",
                            "original_price": 50000,
                            "original_price_formatted": "Rp 50.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "Rp 0",
                            "quantity": 2364,
                            "is_variant": false,
                            "is_available": true,
                            "order": 14,
                            "app_link": "tokopedia://product/2373468360",
                            "web_link": "https://www.tokopedia.com/missogura/lavender-cookies-vegan",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 15,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          }
                        ]
                      }
                    ],
                    "vouchers": [],
                    "config": {
                      "peek_product_count": 15,
                      "title_bottomsheet": "Promo dan produk pilihan"
                    }
                  }
                 }
        """.trimIndent()

        return gson.fromJson(data, ProductSection.Response::class.java)
    }

    fun generateGqlProductNumeration(): ProductSection.Response {
        val data = """
                 {
                  "playGetTagsItemSection": {
                    "sections": [
                      {
                        "type": "other",
                        "title": "",
                        "countdown": {
                          "copy": ""
                        },
                        "background": {
                          "gradient": [],
                          "image_url": ""
                        },
                        "start_time": "1970-01-01T07:00:00+07:00",
                        "end_time": "1970-01-01T07:00:00+07:00",
                        "server_time": "2023-03-21T15:36:25+07:00",
                        "source_id": "0",
                        "products": [
                          {
                            "id": "6925401702",
                            "name": "Hampers 6 Kaleng Cookies Kue Kering",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2023/2/25/916aeea4-a7c5-45f2-b930-2c2b7642120e.jpg",
                            "shop_id": "11757826",
                            "original_price": 195000,
                            "original_price_formatted": "Rp 195.000",
                            "discount": 5,
                            "price": 185250,
                            "price_formatted": "Rp 185.250",
                            "quantity": 4483,
                            "is_variant": true,
                            "is_available": true,
                            "order": 0,
                            "app_link": "tokopedia://product/8174237886",
                            "web_link": "https://www.tokopedia.com/missogura/hampers-6-kaleng-cookies-kue-kering-batch2-17-21mar",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli",
                                "color": "SECONDARY",
                                "button_type": "GCR"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 1,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "8175013631",
                            "name": "Kurma Medjool Cake Cupcake Muffin",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2023/2/25/0a756fc8-5a58-46d6-a049-9f3d7ff85820.jpg",
                            "shop_id": "11757826",
                            "original_price": 65000,
                            "original_price_formatted": "Rp 65.000",
                            "discount": 5,
                            "price": 61750,
                            "price_formatted": "Rp 61.750",
                            "quantity": 8979,
                            "is_variant": true,
                            "is_available": true,
                            "order": 1,
                            "app_link": "tokopedia://product/8175013641",
                            "web_link": "https://www.tokopedia.com/missogura/kurma-medjool-cake-cupcake-muffin-isi-12-batch7-11-15apr-76baf",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli",
                                "color": "SECONDARY",
                                "button_type": "GCR"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 2,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "3325111748",
                            "name": "Ameena Hampers Lebaran Eid Trio Nastar",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2023/2/25/9e0672a0-bc8d-40cb-8b02-c73ed87913c5.jpg",
                            "shop_id": "11757826",
                            "original_price": 65000,
                            "original_price_formatted": "Rp 65.000",
                            "discount": 5,
                            "price": 61750,
                            "price_formatted": "Rp 61.750",
                            "quantity": 8956,
                            "is_variant": true,
                            "is_available": true,
                            "order": 2,
                            "app_link": "tokopedia://product/8160582985",
                            "web_link": "https://www.tokopedia.com/missogura/ameena-hampers-lebaran-eid-trio-nastar-isi-12-batch8-16-20apr",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli",
                                "color": "SECONDARY",
                                "button_type": "GCR"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 3,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "7506341076",
                            "name": "Melted Cookies / Messy Cookies / Cookies Siram",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2023/3/5/6b6ccfaa-602c-43ff-842d-a77832599eb9.jpg",
                            "shop_id": "11757826",
                            "original_price": 60000,
                            "original_price_formatted": "Rp 60.000",
                            "discount": 5,
                            "price": 57000,
                            "price_formatted": "Rp 57.000",
                            "quantity": 8980,
                            "is_variant": true,
                            "is_available": true,
                            "order": 3,
                            "app_link": "tokopedia://product/8319956789",
                            "web_link": "https://www.tokopedia.com/missogura/melted-cookies-messy-cookies-cookies-siram-pandan-kurma-batch2-17-21mar",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli",
                                "color": "SECONDARY",
                                "button_type": "GCR"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 4,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "1953633275",
                            "name": "Seaweed edamame",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/6/26/f181fcc4-e3c6-4dbb-84a8-d2c7cb45dd90.jpg",
                            "shop_id": "11757826",
                            "original_price": 40000,
                            "original_price_formatted": "Rp 40.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "Rp 0",
                            "quantity": 2027,
                            "is_variant": false,
                            "is_available": true,
                            "order": 4,
                            "app_link": "tokopedia://product/1953633275",
                            "web_link": "https://www.tokopedia.com/missogura/seaweed-edamame",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 5,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "8584759533",
                            "name": "KOKO Kookies Kocok / Cookies / Kue Kering",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2023/3/17/d5649a0f-c2bc-4b31-a5d5-043965eac2d6.jpg",
                            "shop_id": "11757826",
                            "original_price": 27000,
                            "original_price_formatted": "Rp 27.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "",
                            "quantity": 1485,
                            "is_variant": true,
                            "is_available": true,
                            "order": 5,
                            "app_link": "tokopedia://product/8584759541",
                            "web_link": "https://www.tokopedia.com/missogura/koko-kookies-kocok-cookies-kue-kering-mie-goreng-788e8",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 6,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "2851476971",
                            "name": "Nastar Ronde",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2022/2/1/2bee065f-f056-4731-953f-253a98752505.jpg",
                            "shop_id": "11757826",
                            "original_price": 60000,
                            "original_price_formatted": "Rp 60.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "",
                            "quantity": 1076,
                            "is_variant": true,
                            "is_available": true,
                            "order": 6,
                            "app_link": "tokopedia://product/2851476977",
                            "web_link": "https://www.tokopedia.com/missogura/nastar-ronde-isi-12",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 7,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "1953652961",
                            "name": "Nastar Klepon",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/6/26/be20e6b4-f9b9-42c2-971f-27fc902448ef.jpg",
                            "shop_id": "11757826",
                            "original_price": 60000,
                            "original_price_formatted": "Rp 60.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "",
                            "quantity": 3471,
                            "is_variant": true,
                            "is_available": true,
                            "order": 7,
                            "app_link": "tokopedia://product/2012242417",
                            "web_link": "https://www.tokopedia.com/missogura/nastar-klepon-isi-12",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 8,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "1953621470",
                            "name": "Greentea tea bag cookies",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/6/26/930957b9-5d2c-4511-9999-48a7c2ef0f20.jpg",
                            "shop_id": "11757826",
                            "original_price": 75000,
                            "original_price_formatted": "Rp 75.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "Rp 0",
                            "quantity": 983,
                            "is_variant": false,
                            "is_available": true,
                            "order": 8,
                            "app_link": "tokopedia://product/1953621470",
                            "web_link": "https://www.tokopedia.com/missogura/greentea-tea-bag-cookies",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 9,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "6924972249",
                            "name": "Rum Kartoshka Cake - Rum Balls",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2022/11/9/d475c2e3-eeb3-4f39-87fe-5b05bd275de6.jpg",
                            "shop_id": "11757826",
                            "original_price": 75000,
                            "original_price_formatted": "Rp 75.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "",
                            "quantity": 134,
                            "is_variant": true,
                            "is_available": true,
                            "order": 9,
                            "app_link": "tokopedia://product/7326019550",
                            "web_link": "https://www.tokopedia.com/missogura/rum-kartoshka-cake-rum-balls-isi-12pc",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 10,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "1953600735",
                            "name": "Earl grey tea bag cookies",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/6/26/67083bf9-1341-4f40-aaae-138be0dd083f.jpg",
                            "shop_id": "11757826",
                            "original_price": 70000,
                            "original_price_formatted": "Rp 70.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "Rp 0",
                            "quantity": 1481,
                            "is_variant": false,
                            "is_available": true,
                            "order": 10,
                            "app_link": "tokopedia://product/1953600735",
                            "web_link": "https://www.tokopedia.com/missogura/earl-grey-tea-bag-cookies",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 11,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "2671545794",
                            "name": "Orange Marmalade Cake Hampers Imlek CNY",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2022/1/5/70c953ea-ed67-4f1e-8bfa-d95a6dea9a7e.jpg",
                            "shop_id": "11757826",
                            "original_price": 65000,
                            "original_price_formatted": "Rp 65.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "Rp 0",
                            "quantity": 1872,
                            "is_variant": false,
                            "is_available": true,
                            "order": 11,
                            "app_link": "tokopedia://product/2671545794",
                            "web_link": "https://www.tokopedia.com/missogura/orange-marmalade-cake-hampers-imlek-cny",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 12,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "1953609394",
                            "name": "Espresso cookies",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/6/26/920f1dd7-a6f7-4eaf-9626-dad9881117b1.jpg",
                            "shop_id": "11757826",
                            "original_price": 45000,
                            "original_price_formatted": "Rp 45.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "",
                            "quantity": 1809,
                            "is_variant": true,
                            "is_available": true,
                            "order": 12,
                            "app_link": "tokopedia://product/2596394887",
                            "web_link": "https://www.tokopedia.com/missogura/espresso-cookies-125gr",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 13,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "1953658710",
                            "name": "Choco Soes",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/6/26/89943743-ea61-4d2f-a8eb-e8a1c618deb2.jpg",
                            "shop_id": "11757826",
                            "original_price": 35000,
                            "original_price_formatted": "Rp 35.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "",
                            "quantity": 656,
                            "is_variant": true,
                            "is_available": true,
                            "order": 13,
                            "app_link": "tokopedia://product/2290364287",
                            "web_link": "https://www.tokopedia.com/missogura/choco-soes-choco",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 14,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          },
                          {
                            "id": "2373468360",
                            "name": "Lavender Cookies - vegan",
                            "image_url": "https://images.tokopedia.net/img/cache/300/VqbcmM/2021/11/4/a5f5770c-ffe1-4776-9e5b-46a59dc13df1.jpg",
                            "shop_id": "11757826",
                            "original_price": 50000,
                            "original_price_formatted": "Rp 50.000",
                            "discount": 0,
                            "price": 0,
                            "price_formatted": "Rp 0",
                            "quantity": 2364,
                            "is_variant": false,
                            "is_available": true,
                            "order": 14,
                            "app_link": "tokopedia://product/2373468360",
                            "web_link": "https://www.tokopedia.com/missogura/lavender-cookies-vegan",
                            "min_quantity": 1,
                            "is_free_shipping": false,
                            "is_toko_now": false,
                            "is_pinned": false,
                            "available_buttons": [
                              {
                                "text": "Beli Langsung",
                                "color": "SECONDARY",
                                "button_type": "OCC"
                              },
                              {
                                "text": "+ Keranjang",
                                "color": "PRIMARY",
                                "button_type": "ATC"
                              }
                            ],
                            "product_number": 15,
                            "rating": "",
                            "sold_quantity": "",
                            "social_proof_rank": "Diminati",
                            "social_proof_tag_color": ["#E02954", "#FF7182"],
                            "social_proof_raw_value": 1,
                            "social_proof_type_value": "diminati"
                          }
                        ]
                      }
                    ],
                    "vouchers": [],
                    "config": {
                      "peek_product_count": 15,
                      "title_bottomsheet": "Promo dan produk pilihan"
                    }
                  }
                 }
        """.trimIndent()

        return gson.fromJson(data, ProductSection.Response::class.java)
    }
}
