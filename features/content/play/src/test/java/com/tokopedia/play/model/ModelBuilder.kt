package com.tokopedia.play.model

import com.google.gson.Gson
import com.tokopedia.play.data.*
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.recom.PlayShareInfoUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.variant_common.model.GetProductVariantResponse


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
            "server_time": "2022-09-13T13:31:38+07:00",
            "source_id": "0",
            "products": [
              {
                "id": "205240239",
                "name": "si tangan besi panas bisa melelehkan baja",
                "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2017/7/28/17227058/17227058_cb279d46-7257-4ec9-b47a-991eb2e261d4_620_372.jpg",
                "shop_id": "1961630",
                "original_price": 131313,
                "original_price_formatted": "Rp 131.313",
                "discount": 20,
                "price": 105050,
                "price_formatted": "Rp 105.050",
                "quantity": 11,
                "is_variant": false,
                "is_available": true,
                "order": 0,
                "app_link": "tokopedia://product/205240239",
                "web_link": "https://www.tokopedia.com/voc123/si-tangan-besi-panas-bisa-melelehkan-baja",
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
                ]
              },
              {
                "id": "233501860",
                "name": "KOL belum digoreng",
                "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2017/11/22/17227058/17227058_bb079da4-06f6-461e-bb5a-3d9f83339625_496_379.jpg",
                "shop_id": "1961630",
                "original_price": 80000,
                "original_price_formatted": "Rp 80.000",
                "discount": 0,
                "price": 0,
                "price_formatted": "",
                "quantity": 20,
                "is_variant": true,
                "is_available": true,
                "order": 1,
                "app_link": "tokopedia://product/1810972910",
                "web_link": "https://www.tokopedia.com/voc123/kol-belum-digoreng-hitam-0",
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
                ]
              }
            ]
          },
          {
            "type": "other",
            "title": "Produk Habis",
            "countdown": {
              "copy": ""
            },
            "background": {
              "gradient": [],
              "image_url": ""
            },
            "start_time": "0001-01-01T00:00:00Z",
            "end_time": "0001-01-01T00:00:00Z",
            "server_time": "2022-09-13T13:31:38+07:00",
            "source_id": "",
            "products": [
              {
                "id": "227295441",
                "name": "Tengkurak",
                "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2017/11/10/17227058/17227058_7ec00048-b473-406e-918a-ec45d3e8e02e_948_1300.jpg",
                "shop_id": "1961630",
                "original_price": 700,
                "original_price_formatted": "Rp 700",
                "discount": 0,
                "price": 0,
                "price_formatted": "Rp 0",
                "quantity": 0,
                "is_variant": false,
                "is_available": false,
                "order": 2,
                "app_link": "tokopedia://product/227295441",
                "web_link": "https://www.tokopedia.com/voc123/tengkurak",
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
                ]
              },
              {
                "id": "193124774",
                "name": "aku ingin begini aku ingin begitu ingin ini ingin itu banyak sekaliiii",
                "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2017/7/7/17227058/17227058_2c250f2d-2fdd-4a85-a570-94fd49730e4f_1071_999.jpg",
                "shop_id": "1961630",
                "original_price": 3123123,
                "original_price_formatted": "Rp 3.123.123",
                "discount": 5,
                "price": 2966967,
                "price_formatted": "Rp 2.966.967",
                "quantity": 0,
                "is_variant": false,
                "is_available": false,
                "order": 3,
                "app_link": "tokopedia://product/193124774",
                "web_link": "https://www.tokopedia.com/voc123/aku-ingin-begini-aku-ingin-begitu-ingin-ini-ingin-itu-banyak-sekaliiii",
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
                ]
              },
              {
                "id": "489530510",
                "name": "sudahbreloom",
                "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2019/6/17/17227058/17227058_d39d0414-e92a-4b0e-8f40-81776ee345db_500_500",
                "shop_id": "1961630",
                "original_price": 200,
                "original_price_formatted": "Rp 200",
                "discount": 0,
                "price": 0,
                "price_formatted": "Rp 0",
                "quantity": 0,
                "is_variant": false,
                "is_available": false,
                "order": 4,
                "app_link": "tokopedia://product/489530510",
                "web_link": "https://www.tokopedia.com/voc123/sudahbreloom",
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
                ]
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
          "title_bottomsheet": "Promo dan produk pilihan"
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
            "server_time": "2022-09-13T13:31:38+07:00",
            "source_id": "0",
            "products": [
              {
                "id": "205240239",
                "name": "si tangan besi panas bisa melelehkan baja",
                "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2017/7/28/17227058/17227058_cb279d46-7257-4ec9-b47a-991eb2e261d4_620_372.jpg",
                "shop_id": "1961630",
                "original_price": 131313,
                "original_price_formatted": "Rp 131.313",
                "discount": 20,
                "price": 105050,
                "price_formatted": "Rp 105.050",
                "quantity": 11,
                "is_variant": false,
                "is_available": true,
                "order": 0,
                "app_link": "tokopedia://product/205240239",
                "web_link": "https://www.tokopedia.com/voc123/si-tangan-besi-panas-bisa-melelehkan-baja",
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
                ]
              },
              {
                "id": "233501860",
                "name": "KOL belum digoreng",
                "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2017/11/22/17227058/17227058_bb079da4-06f6-461e-bb5a-3d9f83339625_496_379.jpg",
                "shop_id": "1961630",
                "original_price": 80000,
                "original_price_formatted": "Rp 80.000",
                "discount": 0,
                "price": 0,
                "price_formatted": "",
                "quantity": 20,
                "is_variant": true,
                "is_available": true,
                "order": 1,
                "app_link": "tokopedia://product/1810972910",
                "web_link": "https://www.tokopedia.com/voc123/kol-belum-digoreng-hitam-0",
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
                ]
              }
            ]
          },
          {
            "type": "other",
            "title": "Produk Habis",
            "countdown": {
              "copy": ""
            },
            "background": {
              "gradient": [],
              "image_url": ""
            },
            "start_time": "0001-01-01T00:00:00Z",
            "end_time": "0001-01-01T00:00:00Z",
            "server_time": "2022-09-13T13:31:38+07:00",
            "source_id": "",
            "products": [
              {
                "id": "227295441",
                "name": "Tengkurak",
                "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2017/11/10/17227058/17227058_7ec00048-b473-406e-918a-ec45d3e8e02e_948_1300.jpg",
                "shop_id": "1961630",
                "original_price": 700,
                "original_price_formatted": "Rp 700",
                "discount": 0,
                "price": 0,
                "price_formatted": "Rp 0",
                "quantity": 0,
                "is_variant": false,
                "is_available": false,
                "order": 2,
                "app_link": "tokopedia://product/227295441",
                "web_link": "https://www.tokopedia.com/voc123/tengkurak",
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
                ]
              },
              {
                "id": "193124774",
                "name": "aku ingin begini aku ingin begitu ingin ini ingin itu banyak sekaliiii",
                "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2017/7/7/17227058/17227058_2c250f2d-2fdd-4a85-a570-94fd49730e4f_1071_999.jpg",
                "shop_id": "1961630",
                "original_price": 3123123,
                "original_price_formatted": "Rp 3.123.123",
                "discount": 5,
                "price": 2966967,
                "price_formatted": "Rp 2.966.967",
                "quantity": 0,
                "is_variant": false,
                "is_available": false,
                "order": 3,
                "app_link": "tokopedia://product/193124774",
                "web_link": "https://www.tokopedia.com/voc123/aku-ingin-begini-aku-ingin-begitu-ingin-ini-ingin-itu-banyak-sekaliiii",
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
                ]
              },
              {
                "id": "489530510",
                "name": "sudahbreloom",
                "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2019/6/17/17227058/17227058_d39d0414-e92a-4b0e-8f40-81776ee345db_500_500",
                "shop_id": "1961630",
                "original_price": 200,
                "original_price_formatted": "Rp 200",
                "discount": 0,
                "price": 0,
                "price_formatted": "Rp 0",
                "quantity": 0,
                "is_variant": false,
                "is_available": false,
                "order": 4,
                "app_link": "tokopedia://product/489530510",
                "web_link": "https://www.tokopedia.com/voc123/sudahbreloom",
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
                ]
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
          "title_bottomsheet": "Promo dan produk pilihan"
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
        }""".trimIndent()

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

    fun buildProductVariant(): GetProductVariantResponse =
        gson.fromJson(productVariant, GetProductVariantResponse::class.java)

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
        productSheetState: BottomInsetsState = buildBottomInsetsState(),
        variantSheetState: BottomInsetsState = buildBottomInsetsState()
    ) = mapOf(
        BottomInsetsType.Keyboard to keyboardState,
        BottomInsetsType.ProductSheet to productSheetState,
        BottomInsetsType.VariantSheet to variantSheetState
    )

    fun buildBottomInsetsState(
        isShown: Boolean = false,
        isPreviousSameState: Boolean = false,
        estimatedInsetsHeight: Int = 250
    ) = if (isShown) BottomInsetsState.Shown(
        estimatedInsetsHeight,
        isPreviousSameState
    ) else BottomInsetsState.Hidden(isPreviousSameState)

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
            shouldShow = channel.share.isShowButton
                    && channel.share.redirectUrl.isNotBlank()
                    && channel.configuration.active
                    && !channel.configuration.freezed
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
        var productList = ""
        for (i in 1..size) {
            productList += """
                {
                    app_link: "$applink",
                    discount: 0,
                    id: $i,
                    image_url: "$imageUrl",
                    is_available: false,
                    is_free_shipping: $isFreeShipping,
                    is_variant: $isVariantAvailable,
                    min_quantity: $minQty,
                    name: "Barang $i",
                    order: 0,
                    original_price: 123,
                    original_price_formatted: "123",
                    price: 0,
                    price_formatted: "",
                    quantity: 0,
                    shop_id: "$shopId",
                    web_link: "https://staging.tokopedia.com/ramayana-qc/ramayana-kemeja-pria-blue-camouflage-raf-07901447"
              }
            """.trimIndent()
            if (i != size) productList += ","
        }

        var sectionList = ""
        for (x in 1..size) {
            sectionList += """{
                type: "active",
                title: "$title $x",
                countdown: {
                    copy: "Berakhir dalam"
                },
                background: {
                    gradient: "$gradient",
                    image_url: "https://via.placeholder.com/150"
                },
                start_time: "2022-01-02T15:04:05Z07:00",
                end_time: "2022-01-02T16:04:05Z07:00",
                server_time: "2022-01-02T15:14:05Z07:00",
                products : [
                    $productList
                 ]
                }
            """.trimIndent()
            if (x != size) sectionList += ","
        }
        val data = """
             {
                "data": {
                      "playGetTagsItemSection":{
                          "sections" : [
                            $sectionList
                          ],
                          "config" : {
                            "peek_product_count" : 15,
                            "title_bottomsheet" : "Promo dan Produk Lainnya"
                          }
                      }
                }
             }
            """.trimIndent()

        return gson.fromJson(data, ProductSection.Response::class.java)
    }
}
