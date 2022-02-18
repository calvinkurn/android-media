package com.tokopedia.play.model

import com.google.gson.Gson
import com.tokopedia.play.data.*
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.recom.PlayShareInfoUiModel
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.model.PlayBufferControl
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
              "sourceID": "23676",
              "type": "active",
              "title": "RE ACTIVE",
              "countdown": {
                "copy": "Berakhir dalam"
              },
              "background": {
                "gradient": [],
                "imageUrl": "https://images.tokopedia.net/img/playasset/Ornament%20Left%20-%20420px.png"
              },
              "startTime": "2022-02-16T18:32:00+07:00",
              "endTime": "2022-02-17T18:02:00+07:00",
              "serverTime": "2022-02-17T10:13:36+07:00",
              "products": [
                {
                  "ID": "14286844",
                  "Name": "IBO3",
                  "ImageUrl": "https://ecs7.tokopedia.net/img/cache/700/product-1/2017/4/3/5510248/5510248_1fada4fe-8444-4911-b3e0-b70b54b119b6_1500_946.jpg",
                  "ShopID": "479541",
                  "OriginalPrice": 11000,
                  "OriginalPriceFormatted": "Rp 11.000",
                  "Discount": 2,
                  "Price": 10780,
                  "PriceFormatted": "Rp 10.780",
                  "Quantity": 100,
                  "QuantityRender": {
                    "show": false,
                    "copy": "",
                    "color": ""
                  },
                  "IsVariant": false,
                  "IsAvailable": true,
                  "Order": 2,
                  "AppLink": "tokopedia://product/14286844",
                  "WebLink": "https://staging.tokopedia.com/ituajakak/ibo3",
                  "MinQuantity": 1,
                  "IsFreeShipping": false
                },
                {
                  "ID": "14286886",
                  "Name": "Amida's Hyakuren",
                  "ImageUrl": "https://ecs7.tokopedia.net/img/cache/700/product-1/2017/4/3/5510248/5510248_d9d8103d-95b4-4cb7-96bd-485710f5e1e6_640_404.jpg",
                  "ShopID": "479541",
                  "OriginalPrice": 125000,
                  "OriginalPriceFormatted": "Rp 125.000",
                  "Discount": 3,
                  "Price": 121250,
                  "PriceFormatted": "Rp 121.250",
                  "Quantity": 100,
                  "QuantityRender": {
                    "show": false,
                    "copy": "",
                    "color": ""
                  },
                  "IsVariant": false,
                  "IsAvailable": true,
                  "Order": 3,
                  "AppLink": "tokopedia://product/14286886",
                  "WebLink": "https://staging.tokopedia.com/ituajakak/amidas-hyakuren",
                  "MinQuantity": 1,
                  "IsFreeShipping": false
                }
              ]
            },
            {
              "sourceID": "0",
              "type": "other",
              "title": "Produk Lainnya",
              "countdown": {
                "copy": ""
              },
              "background": {
                "gradient": [],
                "imageUrl": ""
              },
              "startTime": "1970-01-01T07:00:00+07:00",
              "endTime": "1970-01-01T07:00:00+07:00",
              "serverTime": "2022-02-17T10:13:36+07:00",
              "products": [
                {
                  "ID": "14288512",
                  "Name": "Gandum Bael",
                  "ImageUrl": "https://ecs7.tokopedia.net/img/cache/700/product-1/2017/4/10/5510248/5510248_f43d3d8e-3a2a-4147-ba3c-a6b82c60e168_774_1200.jpg",
                  "ShopID": "479541",
                  "OriginalPrice": 123213,
                  "OriginalPriceFormatted": "Rp 123.213",
                  "Discount": 0,
                  "Price": 0,
                  "PriceFormatted": "Rp 0",
                  "Quantity": 10000,
                  "QuantityRender": {
                    "show": false,
                    "copy": "",
                    "color": ""
                  },
                  "IsVariant": false,
                  "IsAvailable": true,
                  "Order": 0,
                  "AppLink": "tokopedia://product/14288512",
                  "WebLink": "https://staging.tokopedia.com/ituajakak/gandum-bael",
                  "MinQuantity": 1,
                  "IsFreeShipping": false
                },
                {
                  "ID": "14288322",
                  "Name": "RECONGUISTA *6*",
                  "ImageUrl": "https://ecs7.tokopedia.net/img/cache/700/product-1/2017/4/6/5510248/5510248_30997a9b-c6f8-40cf-885c-44ed2bfd2a8e_800_505.jpg",
                  "ShopID": "479541",
                  "OriginalPrice": 1111111,
                  "OriginalPriceFormatted": "Rp 1.111.111",
                  "Discount": 0,
                  "Price": 0,
                  "PriceFormatted": "Rp 0",
                  "Quantity": 10000,
                  "QuantityRender": {
                    "show": false,
                    "copy": "",
                    "color": ""
                  },
                  "IsVariant": false,
                  "IsAvailable": true,
                  "Order": 1,
                  "AppLink": "tokopedia://product/14288322",
                  "WebLink": "https://staging.tokopedia.com/ituajakak/reconguista-6",
                  "MinQuantity": 1,
                  "IsFreeShipping": false
                }
              ]
            },
            {
              "sourceID": "23674",
              "type": "upcoming",
              "title": "Rilisan Spesial",
              "countdown": {
                "copy": "Dimulai dalam"
              },
              "background": {
                "gradient": [],
                "imageUrl": ""
              },
              "startTime": "2022-02-19T18:25:00+07:00",
              "endTime": "2022-02-19T18:55:00+07:00",
              "serverTime": "2022-02-17T10:13:36+07:00",
              "products": [
                {
                  "ID": "14286891",
                  "Name": "Gusion",
                  "ImageUrl": "https://ecs7.tokopedia.net/img/cache/700/product-1/2017/4/3/5510248/5510248_34dd0a6d-ea93-47ae-a04a-42a32752f138_2048_1291.jpg",
                  "ShopID": "479541",
                  "OriginalPrice": 100000,
                  "OriginalPriceFormatted": "Rp 100.000",
                  "Discount": 0,
                  "Price": 0,
                  "PriceFormatted": "Rp 0",
                  "Quantity": 0,
                  "QuantityRender": {
                    "show": false,
                    "copy": "",
                    "color": ""
                  },
                  "IsVariant": false,
                  "IsAvailable": false,
                  "Order": 5,
                  "AppLink": "tokopedia://product/14286891",
                  "WebLink": "https://staging.tokopedia.com/ituajakak/gusion",
                  "MinQuantity": 1,
                  "IsFreeShipping": false
                },
                {
                  "ID": "14286893",
                  "Name": "Man Rodi",
                  "ImageUrl": "https://ecs7.tokopedia.net/img/cache/700/product-1/2017/4/3/5510248/5510248_0533af43-539a-4445-b8f5-eb6c41aff395_2048_1292.jpg",
                  "ShopID": "479541",
                  "OriginalPrice": 80000,
                  "OriginalPriceFormatted": "Rp 80.000",
                  "Discount": 0,
                  "Price": 0,
                  "PriceFormatted": "Rp 0",
                  "Quantity": 0,
                  "QuantityRender": {
                    "show": false,
                    "copy": "",
                    "color": ""
                  },
                  "IsVariant": false,
                  "IsAvailable": false,
                  "Order": 6,
                  "AppLink": "tokopedia://product/14286893",
                  "WebLink": "https://staging.tokopedia.com/ituajakak/man-rodi",
                  "MinQuantity": 1,
                  "IsFreeShipping": false
                }
              ]
            },
            {
              "sourceID": "0",
              "type": "other",
              "title": "Produk Habis",
              "countdown": {
                "copy": ""
              },
              "background": {
                "gradient": [],
                "imageUrl": ""
              },
              "startTime": "1970-01-01T07:00:00+07:00",
              "endTime": "1970-01-01T07:00:00+07:00",
              "serverTime": "2022-02-17T10:13:36+07:00",
              "products": [
                {
                  "ID": "14286895",
                  "Name": "Graze Custom",
                  "ImageUrl": "https://ecs7.tokopedia.net/img/cache/700/product-1/2017/4/3/5510248/5510248_d0630ab7-4f66-45c6-be27-a1177ac72c66_1500_946.jpg",
                  "ShopID": "479541",
                  "OriginalPrice": 1212,
                  "OriginalPriceFormatted": "Rp 1.212",
                  "Discount": 0,
                  "Price": 0,
                  "PriceFormatted": "Rp 0",
                  "Quantity": 0,
                  "QuantityRender": {
                    "show": false,
                    "copy": "",
                    "color": ""
                  },
                  "IsVariant": false,
                  "IsAvailable": false,
                  "Order": 4,
                  "AppLink": "tokopedia://product/14286895",
                  "WebLink": "https://staging.tokopedia.com/ituajakak/graze-custom",
                  "MinQuantity": 1,
                  "IsFreeShipping": false
                }
              ]
            }
          ],
          "vouchers": [
            {
              "ID": "9541",
              "Name": "rintihanhatiini",
              "ShopID": "479541",
              "Title": "Cashback 20rb",
              "Subtitle": "min. pembelian 100rb",
              "VoucherType": 3,
              "VoucherImage": "https://images-staging.tokopedia.net/img/BTJGre/2022/2/15/bf02129d-32f2-45aa-bedd-6fec960c950e.jpg",
              "VoucherImageSquare": "https://images-staging.tokopedia.net/img/nNLhqY/2022/2/15/99a9866d-44eb-49ac-a4f1-79dbacaf6bc8.jpg",
              "VoucherQuota": 25,
              "VoucherFinishTime": "2022-03-16T00:30:00Z",
              "VoucherCode": "ITUAL5I01B",
              "IsHighlighted": false,
              "IsVoucherCopyable": false,
              "IsPrivate": false
            }
          ],
          "config": {
            "peek_product_count": 15,
            "title_bottomsheet": "Promo dan Produk Lainnya"
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
        						"url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420",
        						"url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420"
        					}
        				},
        				{
        					"value": "Biru",
        					"hex": "#1d6cbb",
        					"productVariantOptionID": 47372625,
        					"variantUnitValueID": 5,
        					"picture": {
        						"url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491",
        						"url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491"
        					}
        				},
        				{
        					"value": "Hijau",
        					"hex": "#006400",
        					"productVariantOptionID": 47372626,
        					"variantUnitValueID": 18,
        					"picture": {
        						"url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450",
        						"url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450"
        					}
        				},
        				{
        					"value": "Hitam",
        					"hex": "#000000",
        					"productVariantOptionID": 47372627,
        					"variantUnitValueID": 2,
        					"picture": {
        						"url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000",
        						"url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000"
        					}
        				},
        				{
        					"value": "Biru Muda",
        					"hex": "#8ad1e8",
        					"productVariantOptionID": 47372628,
        					"variantUnitValueID": 6,
        					"picture": {
        						"url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105",
        						"url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105"
        					}
        				},
        				{
        					"value": "Cokelat",
        					"hex": "#8b4513",
        					"productVariantOptionID": 47372629,
        					"variantUnitValueID": 16,
        					"picture": {
        						"url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860",
        						"url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860"
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
        					"url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420",
        					"url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420"
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
        					"url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491",
        					"url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491"
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
        					"url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450",
        					"url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450"
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
        					"url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000",
        					"url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000"
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
        					"url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105",
        					"url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105"
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
        					"url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860",
        					"url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860"
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
       "image_url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/20/82764734/82764734_5759a072-79f9-4b66-8cf8-65c0e94721e3_779_779",
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

    fun buildChannel(): ChannelDetailsWithRecomResponse = gson.fromJson(channelJsonWithRecom, ChannelDetailsWithRecomResponse::class.java)

    fun buildSocketCredential(): SocketCredential = gson.fromJson(socketCredential, SocketCredential::class.java)

    fun buildChannelWithShop(): ChannelDetailsWithRecomResponse = gson.fromJson(channelJsonWithRecom, ChannelDetailsWithRecomResponse::class.java)

    fun buildShopInfo(): ShopInfo = gson.fromJson(shopInfoJson, ShopInfo::class.java)

    fun buildNewChat(): PlayChat = gson.fromJson(newChatJson, PlayChat::class.java)

    fun buildTotalLike(): TotalLikeContent.Response = gson.fromJson(totalLikeCount, TotalLikeContent.Response::class.java)

    fun buildIsLike(): IsLikedContent.Data = gson.fromJson(isLike, IsLikedContent.Data::class.java)

    fun buildProductTagging(): ProductSection = gson.fromJson(channelTagItemsJson, ProductSection::class.java)

    fun buildProductVariant(): GetProductVariantResponse = gson.fromJson(productVariant, GetProductVariantResponse::class.java)

    fun buildProduct(): Product = gson.fromJson(product, Product::class.java)

    fun buildAddToCartModelResponseSuccess() =  CartFeedbackResponseModel(
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

    fun buildVariantSheetUiModel(
            product: PlayProductUiModel.Product = buildProductLineUiModel(),
            action: ProductAction = ProductAction.Buy,
            parentVariant: ProductVariant? = null,
            stockWording: String? = "Stok tersedia",
            listOfVariantCategory: List<VariantCategory> = emptyList(),
            mapOfSelectedVariants: MutableMap<String, String> = mutableMapOf()
    ) = VariantSheetUiModel(
            product = product,
            action = action,
            parentVariant = parentVariant,
            stockWording = stockWording,
            listOfVariantCategory = listOfVariantCategory,
            mapOfSelectedVariants = mapOfSelectedVariants
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
            applink: String? = "https://tkp.me"
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
            applink = applink
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
            discountPercent: Int = 10,
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
    ) = if (isShown) BottomInsetsState.Shown(estimatedInsetsHeight, isPreviousSameState) else BottomInsetsState.Hidden(isPreviousSameState)

    fun <T>buildPlayResultLoading(
            showPlaceholder: Boolean = true
    ) = PlayResult.Loading<T>(showPlaceholder)

    fun <T>buildPlayResultFailure(
            error: Throwable = IllegalArgumentException(),
            onRetry: () -> Unit = {}
    ) = PlayResult.Failure<T>(error, onRetry)

    fun <T>buildPlayResultSuccess(
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
    ) : PlayUserReportUiModel.Loaded{
        val userReportOpt = PlayUserReportReasoningUiModel.Reasoning(
            title = title, reasoningId = reasoningId, detail = detail, submissionData = submissionData
        )
        return PlayUserReportUiModel.Loaded(listOf(userReportOpt, userReportOpt))
    }
}