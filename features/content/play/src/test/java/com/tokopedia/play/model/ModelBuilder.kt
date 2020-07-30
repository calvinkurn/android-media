package com.tokopedia.play.model

import com.google.android.exoplayer2.ExoPlayer
import com.google.gson.Gson
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.play.data.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.variant_common.model.ProductDetailVariantCommonResponse
import com.tokopedia.variant_common.model.ProductVariantCommon
import com.tokopedia.variant_common.model.VariantCategory
import io.mockk.mockk


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

    private val channelWithShopJson = """
    {
      "gc_token": "",
      "partner_type": 1,
      "partner_id": 2900759,
      "channel_id": 2315,
      "title": "Test Channel Dengan Shop",
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
          "shop_id": "479887",
          "original_price": 60000,
          "original_price_formatted": "Rp 60.000",
          "discount": 0,
          "price": 0,
          "price_formatted": "",
          "quantity": 9988,
          "is_variant": false,
          "is_available": false,
          "order": 0,
          "app_link": "tokopedia://product/15240013",
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

    private val parentVariant = """
        {
            "parentID": 745647988,
            "defaultChild": 745647992,
            "sizeChart": "",
            "alwaysAvailable": false,
            "stock": 52,
            "variant": [
              {
                "productVariantID": 15125086,
                "variantID": 1,
                "variantUnitID": 0,
                "name": "warna",
                "identifier": "colour",
                "unitName": "",
                "position": 1,
                "option": [
                  {
                    "productVariantOptionID": 47372624,
                    "variantUnitValueID": 9,
                    "value": "Merah",
                    "hex": "#ff0016",
                    "picture": {
                      "url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420",
                      "url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420"
                    }
                  },
                  {
                    "productVariantOptionID": 47372625,
                    "variantUnitValueID": 5,
                    "value": "Biru",
                    "hex": "#1d6cbb",
                    "picture": {
                      "url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491",
                      "url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491"
                    }
                  },
                  {
                    "productVariantOptionID": 47372626,
                    "variantUnitValueID": 18,
                    "value": "Hijau",
                    "hex": "#006400",
                    "picture": {
                      "url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450",
                      "url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450"
                    }
                  },
                  {
                    "productVariantOptionID": 47372627,
                    "variantUnitValueID": 2,
                    "value": "Hitam",
                    "hex": "#000000",
                    "picture": {
                      "url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000",
                      "url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000"
                    }
                  },
                  {
                    "productVariantOptionID": 47372628,
                    "variantUnitValueID": 6,
                    "value": "Biru Muda",
                    "hex": "#8ad1e8",
                    "picture": {
                      "url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105",
                      "url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105"
                    }
                  },
                  {
                    "productVariantOptionID": 47372629,
                    "variantUnitValueID": 16,
                    "value": "Cokelat",
                    "hex": "#8b4513",
                    "picture": {
                      "url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860",
                      "url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860"
                    }
                  }
                ]
              }
            ],
            "children": [
              {
                "productID": 745647991,
                "price": 110000.0,
                "priceFmt": "Rp 110.000",
                "sku": "",
                "stock": {
                  "stock": 0,
                  "isBuyable": true,
                  "alwaysAvailable": false,
                  "isLimitedStock": false,
                  "stockWording": "Stok tersisa \u003c5, beli segera!",
                  "stockWordingHTML": "Stok \u003cb style\u003d\u0027color:red\u0027\u003etersisa \u0026lt;5,\u003c/b\u003e beli segera!",
                  "otherVariantStock": "available",
                  "minimumOrder": 1,
                  "maximumOrder": 0
                },
                "optionID": [
                  47372624
                ],
                "productName": "starterpokemonberdasarkanwarna - Merah",
                "productURL": "https://www.tokopedia.com/ostactical/starterpokemonberdasarkanwarna-merah",
                "picture": {
                  "url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420",
                  "url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420"
                },
                "campaignInfo": {
                  "getStockPercentageInt": 0,
                  "campaignID": "0",
                  "isActive": false,
                  "originalPrice": 0.0,
                  "originalPriceFmt": "",
                  "discountPercentage": 0.0,
                  "discountPrice": 0.0,
                  "discountPriceFmt": "",
                  "campaignType": 0,
                  "campaignTypeName": "",
                  "startDate": "",
                  "endDate": "",
                  "stock": 0,
                  "isAppsOnly": false,
                  "appLinks": ""
                },
                "isWishlist": false,
                "isCOD": false
              },
              {
                "productID": 745647992,
                "price": 120000.0,
                "priceFmt": "Rp 120.000",
                "sku": "",
                "stock": {
                  "stock": 0,
                  "isBuyable": true,
                  "alwaysAvailable": false,
                  "isLimitedStock": false,
                  "stockWording": "Stok tinggal \u003c20, beli segera!",
                  "stockWordingHTML": "Stok tinggal \u0026lt;20, beli segera!",
                  "otherVariantStock": "available",
                  "minimumOrder": 1,
                  "maximumOrder": 0
                },
                "optionID": [
                  47372625
                ],
                "productName": "starterpokemonberdasarkanwarna - Biru",
                "productURL": "https://www.tokopedia.com/ostactical/starterpokemonberdasarkanwarna-biru",
                "picture": {
                  "url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491",
                  "url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491"
                },
                "campaignInfo": {
                  "getStockPercentageInt": 0,
                  "campaignID": "0",
                  "isActive": false,
                  "originalPrice": 0.0,
                  "originalPriceFmt": "",
                  "discountPercentage": 0.0,
                  "discountPrice": 0.0,
                  "discountPriceFmt": "",
                  "campaignType": 0,
                  "campaignTypeName": "",
                  "startDate": "",
                  "endDate": "",
                  "stock": 0,
                  "isAppsOnly": false,
                  "appLinks": ""
                },
                "isWishlist": false,
                "isCOD": false
              },
              {
                "productID": 745647993,
                "price": 130000.0,
                "priceFmt": "Rp 130.000",
                "sku": "",
                "stock": {
                  "stock": 0,
                  "isBuyable": false,
                  "alwaysAvailable": false,
                  "isLimitedStock": false,
                  "stockWording": "Tersedia Untuk Varian Lain",
                  "stockWordingHTML": "Tersedia Untuk Varian Lain",
                  "otherVariantStock": "available",
                  "minimumOrder": 1,
                  "maximumOrder": 0
                },
                "optionID": [
                  47372626
                ],
                "productName": "starterpokemonberdasarkanwarna - Hijau",
                "productURL": "https://www.tokopedia.com/ostactical/starterpokemonberdasarkanwarna-hijau",
                "picture": {
                  "url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450",
                  "url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450"
                },
                "campaignInfo": {
                  "getStockPercentageInt": 0,
                  "campaignID": "0",
                  "isActive": false,
                  "originalPrice": 0.0,
                  "originalPriceFmt": "",
                  "discountPercentage": 0.0,
                  "discountPrice": 0.0,
                  "discountPriceFmt": "",
                  "campaignType": 0,
                  "campaignTypeName": "",
                  "startDate": "",
                  "endDate": "",
                  "stock": 0,
                  "isAppsOnly": false,
                  "appLinks": ""
                },
                "isWishlist": false,
                "isCOD": false
              },
              {
                "productID": 745647994,
                "price": 300000.0,
                "priceFmt": "Rp 300.000",
                "sku": "",
                "stock": {
                  "stock": 0,
                  "isBuyable": true,
                  "alwaysAvailable": false,
                  "isLimitedStock": false,
                  "stockWording": "Stok tinggal \u003c20, beli segera!",
                  "stockWordingHTML": "Stok tinggal \u0026lt;20, beli segera!",
                  "otherVariantStock": "available",
                  "minimumOrder": 1,
                  "maximumOrder": 0
                },
                "optionID": [
                  47372627
                ],
                "productName": "starterpokemonberdasarkanwarna - Hitam",
                "productURL": "https://www.tokopedia.com/ostactical/starterpokemonberdasarkanwarna-hitam",
                "picture": {
                  "url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000",
                  "url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000"
                },
                "campaignInfo": {
                  "getStockPercentageInt": 0,
                  "campaignID": "0",
                  "isActive": false,
                  "originalPrice": 0.0,
                  "originalPriceFmt": "",
                  "discountPercentage": 0.0,
                  "discountPrice": 0.0,
                  "discountPriceFmt": "",
                  "campaignType": 0,
                  "campaignTypeName": "",
                  "startDate": "",
                  "endDate": "",
                  "stock": 0,
                  "isAppsOnly": false,
                  "appLinks": ""
                },
                "isWishlist": false,
                "isCOD": false
              },
              {
                "productID": 745647995,
                "price": 500000.0,
                "priceFmt": "Rp 500.000",
                "sku": "",
                "stock": {
                  "stock": 0,
                  "isBuyable": false,
                  "alwaysAvailable": false,
                  "isLimitedStock": false,
                  "stockWording": "Tersedia Untuk Varian Lain",
                  "stockWordingHTML": "Tersedia Untuk Varian Lain",
                  "otherVariantStock": "available",
                  "minimumOrder": 1,
                  "maximumOrder": 0
                },
                "optionID": [
                  47372628
                ],
                "productName": "starterpokemonberdasarkanwarna - Biru Muda",
                "productURL": "https://www.tokopedia.com/ostactical/starterpokemonberdasarkanwarna-biru-muda",
                "picture": {
                  "url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105",
                  "url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105"
                },
                "campaignInfo": {
                  "getStockPercentageInt": 0,
                  "campaignID": "0",
                  "isActive": false,
                  "originalPrice": 0.0,
                  "originalPriceFmt": "",
                  "discountPercentage": 0.0,
                  "discountPrice": 0.0,
                  "discountPriceFmt": "",
                  "campaignType": 0,
                  "campaignTypeName": "",
                  "startDate": "",
                  "endDate": "",
                  "stock": 0,
                  "isAppsOnly": false,
                  "appLinks": ""
                },
                "isWishlist": false,
                "isCOD": false
              },
              {
                "productID": 745647996,
                "price": 190000.0,
                "priceFmt": "Rp 190.000",
                "sku": "",
                "stock": {
                  "stock": 0,
                  "isBuyable": false,
                  "alwaysAvailable": false,
                  "isLimitedStock": false,
                  "stockWording": "Tersedia Untuk Varian Lain",
                  "stockWordingHTML": "Tersedia Untuk Varian Lain",
                  "otherVariantStock": "available",
                  "minimumOrder": 1,
                  "maximumOrder": 0
                },
                "optionID": [
                  47372629
                ],
                "productName": "starterpokemonberdasarkanwarna - Cokelat",
                "productURL": "https://www.tokopedia.com/ostactical/starterpokemonberdasarkanwarna-cokelat",
                "picture": {
                  "url": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860",
                  "url200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860"
                },
                "campaignInfo": {
                  "getStockPercentageInt": 0,
                  "campaignID": "0",
                  "isActive": false,
                  "originalPrice": 0.0,
                  "originalPriceFmt": "",
                  "discountPercentage": 0.0,
                  "discountPrice": 0.0,
                  "discountPriceFmt": "",
                  "campaignType": 0,
                  "campaignTypeName": "",
                  "startDate": "",
                  "endDate": "",
                  "stock": 0,
                  "isAppsOnly": false,
                  "appLinks": ""
                },
                "isWishlist": false,
                "isCOD": false
              }
            ]
          }
    """.trimIndent()

    private val listOfVariantCategory = """
        [
            {
              "name": "warna",
              "identifier": "colour",
              "variantGuideline": "",
              "hasCustomImage": true,
              "selectedValue": "",
              "isLeaf": true,
              "variantOptions": [
                {
                  "variantId": 47372624,
                  "currentState": 0,
                  "variantHex": "#ff0016",
                  "variantName": "Merah",
                  "image200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420",
                  "imageOriginal": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_027a3f2c-f1a6-4353-be2f-ccbae8ede018_420_420",
                  "isBuyable": false,
                  "stock": 0,
                  "variantOptionIdentifier": "colour",
                  "variantCategoryKey": "15125086",
                  "selectedStockWording": "",
                  "level": 0,
                  "flashSale": false,
                  "hasCustomImages": true
                },
                {
                  "variantId": 47372625,
                  "currentState": 0,
                  "variantHex": "#1d6cbb",
                  "variantName": "Biru",
                  "image200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491",
                  "imageOriginal": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_3d3dda2b-b9b7-4303-a560-e72b925f1f1f_491_491",
                  "isBuyable": false,
                  "stock": 0,
                  "variantOptionIdentifier": "colour",
                  "variantCategoryKey": "15125086",
                  "selectedStockWording": "",
                  "level": 0,
                  "flashSale": false,
                  "hasCustomImages": true
                },
                {
                  "variantId": 47372626,
                  "currentState": -1,
                  "variantHex": "#006400",
                  "variantName": "Hijau",
                  "image200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450",
                  "imageOriginal": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_fd079472-0bd7-4fbd-abf0-1e1767bc0503_450_450",
                  "isBuyable": false,
                  "stock": 0,
                  "variantOptionIdentifier": "colour",
                  "variantCategoryKey": "15125086",
                  "selectedStockWording": "",
                  "level": 0,
                  "flashSale": false,
                  "hasCustomImages": true
                },
                {
                  "variantId": 47372627,
                  "currentState": 0,
                  "variantHex": "#000000",
                  "variantName": "Hitam",
                  "image200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000",
                  "imageOriginal": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_b1cbe38a-e398-4818-bc11-eee776cf5dc6_1000_1000",
                  "isBuyable": false,
                  "stock": 0,
                  "variantOptionIdentifier": "colour",
                  "variantCategoryKey": "15125086",
                  "selectedStockWording": "",
                  "level": 0,
                  "flashSale": false,
                  "hasCustomImages": true
                },
                {
                  "variantId": 47372628,
                  "currentState": -1,
                  "variantHex": "#8ad1e8",
                  "variantName": "Biru Muda",
                  "image200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105",
                  "imageOriginal": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105",
                  "isBuyable": false,
                  "stock": 0,
                  "variantOptionIdentifier": "colour",
                  "variantCategoryKey": "15125086",
                  "selectedStockWording": "",
                  "level": 0,
                  "flashSale": false,
                  "hasCustomImages": true
                },
                {
                  "variantId": 47372629,
                  "currentState": -1,
                  "variantHex": "#8b4513",
                  "variantName": "Cokelat",
                  "image200": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860",
                  "imageOriginal": "https://ecs7.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860",
                  "isBuyable": false,
                  "stock": 0,
                  "variantOptionIdentifier": "colour",
                  "variantCategoryKey": "15125086",
                  "selectedStockWording": "",
                  "level": 0,
                  "flashSale": false,
                  "hasCustomImages": true
                }
              ]
            }
          ]
    """.trimIndent()

    fun buildChannel() = gson.fromJson(channelJson, Channel::class.java)

    fun buildChannelWithShop() = gson.fromJson(channelWithShopJson, Channel::class.java)

    fun buildShopInfo() = gson.fromJson(shopInfoJson, ShopInfo::class.java)

    fun buildNewChat() = gson.fromJson(newChatJson, PlayChat::class.java)

    fun buildTotalLike() = gson.fromJson(totalLikeCount, TotalLikeContent.Data::class.java)

    fun buildIsLike() = gson.fromJson(isLike, IsLikedContent.Data::class.java)

    fun buildProductTagging() = gson.fromJson(channelTagItemsJson, ProductTagging::class.java)

    fun buildProductVariant() = gson.fromJson(productVariant, ProductDetailVariantCommonResponse::class.java)

    fun buildProduct() = gson.fromJson(product, Product::class.java)

    fun buildAddToCartModelResponseSuccess() = AddToCartDataModel(data = DataModel(cartId = "123", success = 1))
    fun buildAddToCartModelResponseFail() = AddToCartDataModel(
            errorMessage = arrayListOf("error message"),
            data = DataModel(cartId = "", success = 0)
    )

    fun buildCartUiModel(
            product: ProductLineUiModel,
            action: ProductAction,
            bottomInsetsType: BottomInsetsType,
            isSuccess: Boolean = true,
            errorMessage: String = "",
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
    fun buildStateHelperUiModel(
        shouldShowPinned: Boolean = true,
        channelType: PlayChannelType = PlayChannelType.Live,
        videoPlayer: VideoPlayerUiModel = YouTube("absbrb"),
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = buildBottomInsetsMap(),
        screenOrientation: ScreenOrientation = ScreenOrientation.Portrait,
        videoOrientation: VideoOrientation = VideoOrientation.Vertical,
        videoState: PlayVideoState = PlayVideoState.Playing
    ) = StateHelperUiModel(
            shouldShowPinned = shouldShowPinned,
            channelType = channelType,
            videoPlayer = videoPlayer,
            bottomInsets = bottomInsets,
            screenOrientation = screenOrientation,
            videoOrientation = videoOrientation,
            videoState = videoState
    )

    fun buildChannelInfoUiModel(
            id: String = "1230",
            title: String = "Channel live",
            description: String = "Ini Channel live",
            partnerId: Long = 123151,
            partnerType: PartnerType = PartnerType.Admin,
            moderatorName: String = "Lisa",
            contentId: Int = 1412,
            contentType: Int = 2,
            likeType: Int = 1,
            isShowCart: Boolean = true
    ) = ChannelInfoUiModel(id, title, description, partnerId, partnerType,
            moderatorName, contentId, contentType, likeType, isShowCart)

    fun buildVideoPropertyUiModel(
            state: PlayVideoState = PlayVideoState.Playing
    ) = VideoPropertyUiModel(state = state)

    fun buildVideoStreamUiModel(
            uriString: String = "https://tkp.me",
            channelType: PlayChannelType = PlayChannelType.Live,
            isActive: Boolean = true,
            orientation: VideoOrientation = VideoOrientation.Vertical,
            backgroundUrl: String = "https://tkp.me"
    ) = VideoStreamUiModel(
            uriString = uriString,
            channelType = channelType,
            isActive = isActive,
            orientation = orientation,
            backgroundUrl = backgroundUrl
    )

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

    fun buildTotalLikeUiModel(
            totalLike: Int = 1200,
            totalLikeFormatted: String = "1.2k"
    ) = TotalLikeUiModel(
            totalLike = totalLike,
            totalLikeFormatted = totalLikeFormatted
    )

    fun buildTotalViewUiModel(
            totalView: String = "1.5k"
    ) = TotalViewUiModel(
            totalView = totalView
    )

    fun buildLikeStateUiModel(
            isLiked: Boolean = true,
            fromNetwork: Boolean = false
    ) = LikeStateUiModel(
            isLiked = isLiked,
            fromNetwork = fromNetwork
    )

    fun buildPartnerInfoUiModel(
            id: Long = 10213,
            name: String = "Partner",
            type: PartnerType = PartnerType.Shop,
            isFollowed: Boolean = false,
            isFollowable: Boolean = true
    ) = PartnerInfoUiModel(
            id = id,
            name = name,
            type = type,
            isFollowed = isFollowed,
            isFollowable = isFollowable
    )

    fun buildPinnedMessageUiModel(
            applink: String? = "https://tkp.me",
            partnerName: String = "Admin",
            title: String = "message"
    ) = PinnedMessageUiModel(
            applink = applink,
            partnerName = partnerName,
            title = title
    )

    fun buildPinnedProductUiModel(
            partnerName: String = "Admin",
            title: String = "message",
            isPromo: Boolean = false
    ) = PinnedProductUiModel(
            partnerName = partnerName,
            title = title,
            isPromo = isPromo
    )

    fun buildPinnedRemoveUiModel() = PinnedRemoveUiModel

    fun buildQuickReplyUiModel(
            quickReplyList: List<String> = listOf("Keren", "UwU")
    ) = QuickReplyUiModel(
            quickReplyList = quickReplyList
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

    fun buildGeneralVideoUiModel(exoPlayer: ExoPlayer = mockk()) = General(exoPlayer)

    fun buildYouTubeVideoUiModel(videoId: String = "abacac") = YouTube(videoId)

    fun buildCartUiModel(
            isShow: Boolean = true,
            count: Int = 1
    ) = CartUiModel(
            isShow = isShow,
            count = count
    )

    fun buildProductSheetUiModel(
            title: String = "Yeaya",
            partnerId: Long = 1234L,
            voucherList: List<PlayVoucherUiModel> = emptyList(),
            productList: List<PlayProductUiModel> = emptyList()
    ) = ProductSheetUiModel(
            title = title,
            partnerId = partnerId,
            voucherList = voucherList,
            productList = productList
    )

    fun buildVariantSheetUiModel(
            product: ProductLineUiModel = buildProductLineUiModel(),
            action: ProductAction = ProductAction.Buy,
            parentVariant: ProductVariantCommon? = null,
            stockWording: String? = "Stok tersedia",
            listOfVariantCategory: List<VariantCategory> = emptyList(),
            mapOfSelectedVariants: MutableMap<String, Int> = mutableMapOf()
    ) = VariantSheetUiModel(
            product = product,
            action = action,
            parentVariant = parentVariant,
            stockWording = stockWording,
            listOfVariantCategory = listOfVariantCategory,
            mapOfSelectedVariants = mapOfSelectedVariants
    )

    fun buildMerchantVoucherUiModel(
            type: MerchantVoucherType = MerchantVoucherType.Discount,
            title: String = "Diskon gedean",
            description: String = "wowaw"
    ) = MerchantVoucherUiModel(
            type = type,
            title = title,
            description = description
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
    ) = ProductLineUiModel(
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
            priceNumber: Long = 120000
    ) = OriginalPrice(
            price = price,
            priceNumber = priceNumber
    )

    fun buildDiscountedPrice(
            originalPrice: String = "Rp120.000",
            discountPercent: Int = 10,
            discountedPrice: String = "Rp108.000",
            discountedPriceNumber: Long = 108000
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
}