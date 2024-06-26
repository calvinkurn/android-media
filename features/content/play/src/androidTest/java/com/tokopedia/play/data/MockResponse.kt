package com.tokopedia.play.data


/**
 * Created by mzennis on 04/09/20.
 */

const val KEY_QUERY = "GetPlayChannelDetail"
const val KEY_QUERY_CHANNEL_DETAILS = "playGetChannelDetailsWithRecom"
const val KEY_QUERY_SHOP_INFO = "getShopInfo"
const val KEY_QUERY_PINNED_PRODUCT = "playGetTagsItem"
const val KEY_QUERY_PRODUCT_VARIANT = "getProductVariant"
const val KEY_QUERY_TOTAL_LIKE = "feedGetLikeContent"
const val KEY_QUERY_IS_LIKED = "feedGetIsLikePost"
const val KEY_QUERY_TOTAL_CART = "update_cart_counter"
const val KEY_MUTATION_FOLLOW_SHOP = "followShop"
const val KEY_MUTATION_LIKE_CHANNEL = "do_like_kol_post"
const val KEY_MUTATION_ADD_TO_CART = "add_to_cart"

/**
 * get channel detail
 * type live
 * vertical
 * with shop info
 * with pinned message (without pinned product)
 * with quick reply
 * with container chat
 */
const val RESPONSE_MOCK_CHANNEL_DETAIL_LIVE = """
            [{
              "data": {
                "playGetChannelDetails": {
                  "data": {
                    "id": "10708",
                    "title": "Powerbank Mini Ngecas Kilat",
                    "description": "",
                    "start_time": "2020-08-11T12:30:00+07:00",
                    "end_time": "2020-08-11T13:00:00+07:00",
                    "is_live": true,
                    "partner": {
                      "id": "6496952",
                      "type": "shop",
                      "name": "RAVPower Official Store",
                      "thumbnail_url": "https://images.tokopedia.net/img/cache/215-square/shops-1/2019/12/26/6496952/6496952_9cd123f1-53fa-4a14-ae74-25aaafe3e982.png",
                      "badge_url": "https://images.tokopedia.net/img/official_store/badge_os.png"
                    },
                    "video": {
                      "id": "6453",
                      "orientation": "vertical",
                      "type": "live",
                      "cover_url": "https://images.tokopedia.net/img/jJtrdn/2020/8/10/d8bff2c4-7ff9-4482-9e71-3c62ae25c826.jpg",
                      "stream_source": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                      "autoplay": true,
                      "buffer_control": {
                        "max_buffer_in_seconds": 18,
                        "min_buffer_in_seconds": 3,
                        "buffer_for_playback": 2,
                        "buffer_for_playback_after_rebuffer": 2
                      }
                    },
                    "pinned_message": {
                      "id": "123",
                      "title": "Ada promo menarik buat belanja barang pilihan kami",
                      "message": "",
                      "redirect_url": "tokopedia://shop/3598808"
                    },
                    "quick_replies": [
                      "Halo",
                      "❤ ❤ ❤",
                      "Berapa harganya?",
                      "Bagus!",
                      "Mau beli dong"
                    ],
                    "configurations": {
                      "ping_interval": 10000,
                      "max_chars": 200,
                      "max_retries": 5,
                      "min_reconnect_delay": 5000,
                      "show_cart": false,
                      "show_pinned_product": false,
                      "published": false,
                      "active": true,
                      "freezed": false,
                      "has_promo": false,
                      "feeds_like_params": {
                        "content_type": 29,
                        "content_id": "10708",
                        "like_type": 3
                      },
                      "channel_freeze_screen": {
                        "category": "channel",
                        "title": "%s Telah Berakhir",
                        "description": "Yuk lanjut nonton berbagai video menarik lainnya di Tokopedia Play!",
                        "button_text": "Cek Channel Lain",
                        "button_app_link": "tokopedia://webview?titlebar=false&url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F"
                      },
                      "channel_exit_message": {
                        "title": "Yakin ingin Keluar? Vote aja dulu",
                        "message": "Saat ini masih ada Voter berhadiah di Group Chat. Yuk vote dan menangkan hadiahnya!"
                      },
                      "channel_banned_message": {
                        "title": "Anda diblokir admin",
                        "message": "Anda diblokir oleh admin karena melanggar syarat dan ketentuan Channel, sehingga tidak dapat melihat konten ini.",
                        "button_text": "OK"
                      },
                      "pinned_product_config": {
                        "pin_title": "Ada promo menarik buat belanja barang pilihan kami",
                        "bottom_sheet_title": "Barang & Promo Pilihan"
                      },
                      "room_background": {
                        "image_url": ""
                      }
                    },
                    "stats": {
                      "view": {
                        "value": "8697",
                        "formatted": "8.7 rb"
                      }
                    },
                    "app_link": "tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fupdate",
                    "web_link": "https://www.tokopedia.com/play/update"
                  }
                }
              }
            }]
        """

/**
 * get channel detail
 * vod
 * horizontal
 * with pinned product (with / without variant)
 * with vouchers
 */
const val RESPONSE_MOCK_CHANNEL_DETAIL_VOD = """
            [{
              "data": {
                "playGetChannelDetails": {
                  "data": {
                    "id": "10708",
                    "title": "Powerbank Mini Ngecas Kilat",
                    "description": "",
                    "start_time": "2020-08-11T12:30:00+07:00",
                    "end_time": "2020-08-11T13:00:00+07:00",
                    "is_live": false,
                    "partner": {
                      "id": "41129297",
                      "type": "tokopedia",
                      "name": "Tokopedia Play",
                      "thumbnail_url": "https://images.tokopedia.net/img/cache/100-square/user-1/2019/5/27/41129297/41129297_c075146d-2b07-4c7f-a4c6-fbd42d26d7ef.png",
                      "badge_url": ""
                    },
                    "video": {
                      "id": "6453",
                      "orientation": "horizontal",
                      "type": "vod",
                      "cover_url": "https://images.tokopedia.net/img/jJtrdn/2020/8/10/d8bff2c4-7ff9-4482-9e71-3c62ae25c826.jpg",
                      "stream_source": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                      "autoplay": true,
                      "buffer_control": {
                        "max_buffer_in_seconds": 18,
                        "min_buffer_in_seconds": 3,
                        "buffer_for_playback": 2,
                        "buffer_for_playback_after_rebuffer": 2
                      }
                    },
                    "pinned_message": {
                      "id": "",
                      "title": "",
                      "message": "",
                      "redirect_url": ""
                    },
                    "quick_replies": [
                      "Halo",
                      "❤ ❤ ❤",
                      "Berapa harganya?",
                      "Bagus!",
                      "Mau beli dong"
                    ],
                    "configurations": {
                      "ping_interval": 10000,
                      "max_chars": 200,
                      "max_retries": 5,
                      "min_reconnect_delay": 5000,
                      "show_cart": true,
                      "show_pinned_product": true,
                      "published": false,
                      "active": true,
                      "freezed": false,
                      "has_promo": false,
                      "feeds_like_params": {
                        "content_type": 29,
                        "content_id": "10708",
                        "like_type": 3
                      },
                      "channel_freeze_screen": {
                        "category": "channel",
                        "title": "%s Telah Berakhir",
                        "description": "Yuk lanjut nonton berbagai video menarik lainnya di Tokopedia Play!",
                        "button_text": "Cek Channel Lain",
                        "button_app_link": "tokopedia://webview?titlebar=false&url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F"
                      },
                      "channel_exit_message": {
                        "title": "Yakin ingin Keluar? Vote aja dulu",
                        "message": "Saat ini masih ada Voter berhadiah di Group Chat. Yuk vote dan menangkan hadiahnya!"
                      },
                      "channel_banned_message": {
                        "title": "Anda diblokir admin",
                        "message": "Anda diblokir oleh admin karena melanggar syarat dan ketentuan Channel, sehingga tidak dapat melihat konten ini.",
                        "button_text": "OK"
                      },
                      "pinned_product_config": {
                        "pin_title": "Ada promo menarik buat belanja barang pilihan kami",
                        "bottom_sheet_title": "Barang & Promo Pilihan"
                      },
                      "room_background": {
                        "image_url": ""
                      }
                    },
                    "stats": {
                      "view": {
                        "value": "8697",
                        "formatted": "8.7 rb"
                      }
                    },
                    "app_link": "tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fupdate",
                    "web_link": "https://www.tokopedia.com/play/update"
                  }
                }
              }
            }]
        """

/**
 * get channel detail error
 */
const val RESPONSE_MOCK_CHANNEL_DETAIL_ERROR = """
            [{
              "errors": [
                {
                  "message": "too much request",
                  "path": [
                    "playGetChannelDetails"
                  ],
                  "extensions": {
                    "developerMessage": "too much request",
                    "timestamp": "2020-09-03 11:42:53.430468486 +0700 WIB m=+2502.416277421"
                  }
                }
              ],
              "data": null
            }]
        """

/**
 * product with & without variant, > 5 vouchers
 */
const val RESPONSE_MOCK_PINNED_PRODUCT = """
            [{
              "data": {
                "playGetTagsItem": {
                  "products": [
                    {
                      "id": "632305096",
                      "name": "RAVPower Mini Powerbank 10000mAH with PD18W + QC3.0 Black [RP-PB194]",
                      "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/11/72048583/72048583_6847377e-ad8b-4fac-b451-25f013b73b63_1000_1000",
                      "shop_id": "6496952",
                      "original_price": 799000,
                      "original_price_formatted": "Rp 799.000",
                      "discount": 50,
                      "price": 799000,
                      "price_formatted": "Rp 399.000",
                      "quantity": 95,
                      "is_variant": false,
                      "is_available": true,
                      "order": 0,
                      "app_link": "tokopedia://product/632305096",
                      "web_link": "https://www.tokopedia.com/ravpowerofficial/ravpower-mini-powerbank-10000mah-with-pd18w-qc3-0-black-rp-pb194",
                      "min_quantity": 1,
                      "is_free_shipping": false
                    },
                    {
                      "id": "15240013",
                      "name": "Indomie Soto Lamongan",
                      "image_url": "https://images.tokopedia.net/img/cache/700/product-1/2018/7/3/5511658/5511658_081f12a8-2229-4062-87d6-a405f17d5c90_500_500.jpg",
                      "shop_id": "479887",
                      "original_price": 60000,
                      "original_price_formatted": "Rp 60.000",
                      "discount": 0,
                      "price": 0,
                      "price_formatted": "",
                      "quantity": 9988,
                      "is_variant": true,
                      "is_available": true,
                      "order": 0,
                      "app_link": "tokopedia://product/15240013",
                      "web_link": "https://staging.tokopedia.com/hahastag/indomie-soto-lamongan",
                      "min_quantity": 1,
                      "is_free_shipping": false
                    }
                  ],
                  "vouchers": [{
                      "voucher_id": "12",
                      "voucher_name": "test date",
                      "shop_id": "105407",
                      "title": " ",
                      "subtitle": "min. pembelian ",
                      "voucher_type": 1,
                      "voucher_image": "https://images.tokopedia.net/img/attachment/2018/10/4/5480066/5480066_4a86d259-d8ce-4501-a1d8-17803320bc35",
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
                      "voucher_image": "https://images.tokopedia.net/img/attachment/2020/2/17/5479551/5479551_eb6e74a3-1ad9-4fd8-a17f-19d3197bb869",
                      "voucher_image_square": "https://images.tokopedia.net/img/attachment/2020/2/17/5479551/5479551_c3171bd3-1b75-4b46-a451-9228273072ca",
                      "voucher_quota": 100,
                      "voucher_finish_time": "2020-03-17T00:30:00Z"
                    },
                    {
                      "voucher_id": "1235",
                      "voucher_name": "ini cashback",
                      "shop_id": "479155",
                      "title": " ",
                      "subtitle": "min. pembelian ",
                      "voucher_type": 3,
                      "voucher_image": "https://images.tokopedia.net/img/attachment/2018/10/15/5480709/5480709_318c7508-710f-4fb7-a55a-794730de70fb",
                      "voucher_image_square": "",
                      "voucher_quota": 100,
                      "voucher_finish_time": "2018-12-05T23:30:00Z"
                    },
                    {
                      "voucher_id": "1231",
                      "voucher_name": "ini cashback",
                      "shop_id": "479155",
                      "title": " ",
                      "subtitle": "min. pembelian ",
                      "voucher_type": 3,
                      "voucher_image": "https://images.tokopedia.net/img/attachment/2018/10/15/5480709/5480709_318c7508-710f-4fb7-a55a-794730de70fb",
                      "voucher_image_square": "",
                      "voucher_quota": 100,
                      "voucher_finish_time": "2018-12-05T23:30:00Z"
                    },
                    {
                      "voucher_id": "1234",
                      "voucher_name": "ini cashback",
                      "shop_id": "479155",
                      "title": " ",
                      "subtitle": "min. pembelian ",
                      "voucher_type": 3,
                      "voucher_image": "https://images.tokopedia.net/img/attachment/2018/10/15/5480709/5480709_318c7508-710f-4fb7-a55a-794730de70fb",
                      "voucher_image_square": "",
                      "voucher_quota": 100,
                      "voucher_finish_time": "2018-12-05T23:30:00Z"
                    }]
                }
              }
            }]
        """

/**
 * get product variant
 */
const val RESPONSE_MOCK_PRODUCT_VARIANT = """
            [{
              "data": {
                "getProductVariant": {
                  "parentID": "745647988",
                  "defaultChild": "745647991",
                  "sizeChart": "",
                  "alwaysAvailable": false,
                  "stock": 47,
                  "variant": [
                    {
                      "name": "warna",
                      "identifier": "colour",
                      "unitName": "",
                      "position": 1,
                      "variantID": "1",
                      "variantUnitID": 0,
                      "productVariantID": "15125086",
                      "option": [
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
                          "value": "Cokelat",
                          "hex": "#8b4513",
                          "productVariantOptionID": 47372629,
                          "variantUnitValueID": 16,
                          "picture": {
                            "url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860",
                            "url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8c9f1db3-0bdf-45e6-987d-5299acbb0b8a_860_860"
                          }
                        },
                        {
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
                          "value": "Biru Muda",
                          "hex": "#8ad1e8",
                          "productVariantOptionID": 47372628,
                          "variantUnitValueID": 6,
                          "picture": {
                            "url": "https://images.tokopedia.net/img/cache/700/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105",
                            "url200": "https://images.tokopedia.net/img/cache/200-square/product-1/2020/3/18/82764734/82764734_8536ec76-938b-4f02-ac1b-5f0047921813_1105_1105"
                          }
                        }
                      ]
                    }
                  ],
                  "children": [
                    {
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
                        "stock": 6,
                        "isLimitedStock": false,
                        "stockWording": "Stok <b>tersisa &lt;10, </b> beli segera!",
                        "stockWordingHTML": "Stok <b>tersisa &lt;10, </b> beli segera!",
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
                        "stock": 10,
                        "isLimitedStock": false,
                        "stockWording": "Stok tinggal &lt;20, beli segera!",
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
                        "stock": 10,
                        "isLimitedStock": false,
                        "stockWording": "Stok tinggal &lt;20, beli segera!",
                        "stockWordingHTML": "Stok tinggal &lt;20, beli segera!",
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
                        "stock": 1,
                        "isLimitedStock": false,
                        "stockWording": "<b style='color:red'>Stok terakhir,</b> beli sekarang!",
                        "stockWordingHTML": "<b style='color:red'>Stok terakhir,</b> beli sekarang!",
                        "otherVariantStock": "available",
                        "minimumOrder": 1,
                        "maximumOrder": 0,
                        "isBuyable": false,
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
                        "stock": 10,
                        "isLimitedStock": false,
                        "stockWording": "Stok tinggal &lt;20, beli segera!",
                        "stockWordingHTML": "Stok tinggal &lt;20, beli segera!",
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
                        "stock": 10,
                        "isLimitedStock": false,
                        "stockWording": "Stok tinggal &lt;20, beli segera!",
                        "stockWordingHTML": "Stok tinggal &lt;20, beli segera!",
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
            }]
        """

/**
 * get shop info
 */
const val RESPONSE_MOCK_SHOP_INFO = """
            [{
              "data": {
                "shopInfoByID": {
                  "result": [
                    {
                      "shopCore": {
                        "name": "RAVPower Official Store",
                        "shopID": "6496952"
                      },
                      "favoriteData": {
                        "totalFavorite": 3347,
                        "alreadyFavorited": 0
                      }
                    }
                  ],
                  "error": {
                    "message": ""
                  }
                }
              }
            }]
        """

/**
 * follow shop
 */
const val RESPONSE_MOCK_SHOP_FOLLOW = """
            [{
              "data": {
                "followShop": {
                  "success": true,
                  "message": "Berhasil follow"
                }
              }
            }]
        """

/**
 * get total like
 */
const val RESPONSE_MOCK_TOTAL_LIKE = """
    [{
      "data": {
        "feedGetLikeContent": {
          "error": "",
          "data": {
            "like": {
              "fmt": "10rb",
              "value": 10000
            }
          }
        }
      }
    }]
"""

/**
 * get is liked
 */
const val RESPONSE_MOCK_IS_LIKED = """
    [{
      "data": {
        "feedGetIsLikePost": {
          "error": "",
          "data": {
            "isLike": false
          }
        }
      }
    }]
"""

/**
 * get total cart
 */
const val RESPONSE_MOCK_TOTAL_CART = """
    [{
      "data": {
        "update_cart_counter": {
          "count": 4
        }
      }
    }]
"""

/**
 * channel like
 * */
const val RESPONSE_MOCK_LIKE_CHANNEL = """
    [{
      "data": {
        "do_like_kol_post": {
          "error": "",
          "data": {
            "success": 1
          }
        }
      }
    }]
"""

/**
 * add to cart
 * */
const val RESPONSE_MOCK_ADD_TO_CART = """
    [{
	"data": {
		"add_to_cart": {
			"error_message": ["Produk berhasil dimasukkan ke Keranjang Belanja"],
			"status": "OK",
			"data": {
				"success": 1,
				"cart_id": "1829424101",
				"product_id": 589831712,
				"quantity": 1,
				"notes": "",
				"shop_id": 603254,
				"customer_id": 1680384,
				"warehouse_id": 3943988,
				"tracker_attribution": "",
				"tracker_list_name": "",
				"uc_ut_param": "",
				"is_trade_in": false,
				"message": ["Produk berhasil dimasukkan ke Keranjang Belanja"]
			},
			"error_reporter": {
				"eligible": false,
				"texts": {
					"submit_title": "Barang belum bisa dimasukkan ke keranjang",
					"submit_description": "Ayo beri tahu kami tentang gangguan ini biar bisa segera diperbaiki!",
					"submit_button": "Laporkan Gangguan",
					"cancel_button": "Batal"
				}
			}
		}
	}
}]
"""

const val RESPONSE_MOCK_CHANNEL_DETAILS = """
    [
   {
      "data":{
         "playGetChannelDetailsWithRecom":{
            "meta":{
               "cursor":"CJ57IhAIGRIMWzkwOCw2Mzc0ODVdKg1iTV9zN2hYOjxXIzY0"
            },
            "data":[
               {
                  "id":"15774",
                  "title":"Test - Android Performance Test",
                  "description":"",
                  "cover_url":"https://images.tokopedia.net/img/jJtrdn/2020/12/16/4944cbeb-dc7f-400c-a825-20242f88fefd.png?b\\u003dUMKT%40{.T^hE34p%25MIuIT^%2Bt5D%25WEV[S5nhs%2B",
                  "start_time":"2020-12-15T00:00:00+07:00",
                  "is_live":false,
                  "partner":{
                     "id":"1959733",
                     "type":"shop",
                     "name":"BUKAN Toko Kuma ~!@#${'$'}%^\u0026amp;(){}-",
                     "thumbnail_url":"https://images.tokopedia.net/img/cache/215-square/shops-1/2017/4/6/1959733/1959733_263ef181-52db-415c-80c9-1a7c45a465a3.png",
                     "badge_url":"https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power Merchant Pro.png",
                     "app_link":"tokopedia://shop/1959733",
                     "web_link":"https://www.tokopedia.com/bukantokokuma"
                  },
                  "video":{
                     "id":"15689",
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
                     "id":"4614",
                     "title":"Test Pinned",
                     "redirect_url":"tokopedia://play/15774"
                  },
                  "quick_replies":[
                     
                  ],
                  "configurations":{
                     "welcome_format":{
                        "type":"self_join",
                        "copy":"{{name}} \u003cb\u003eikut nonton\u003c/b\u003e",
                        "background_color":"#AD009F92",
                        "icon":"https://images.tokopedia.net/img/playassets/join.png"
                     },
                     "real_time_notif":{
                        "lifespan":800
                     },
                     "show_cart":false,
                     "show_pinned_product":false,
                     "active":true,
                     "freezed":false,
                     "has_promo":true,
                     "reminder":{
                        "is_set":false
                     },
                     "channel_freeze_screen":{
                        "title":"%s Telah Berakhir",
                        "description":"Yuk lanjut nonton berbagai video menarik lainnya di Tokopedia Play!",
                        "button_text":"Cek Channel Lain",
                        "button_app_link":"tokopedia://webview?pull_to_refresh\u003dtrue\u0026titlebar\u003dfalse\u0026autoplay\u003dtrue\u0026url\u003dhttps%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F"
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
                        "content_id":"15774",
                        "like_type":3
                     },
                     "room_background":{
                        "image_url":""
                     },
                     "multiple_like":[
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FFF94D63"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FF00AA5B"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FF46D8F1"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FF9342ED"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FFFFC400"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FFF94D63"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FF00AA5B"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FF46D8F1"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FF9342ED"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FFFFC400"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FFF94D63"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FF00AA5B"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FF46D8F1"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FF9342ED"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FFFFC400"
                        }
                     ],
                     "has_follow_button":true,
                     "empty_bottom_sheet":{
                        "copy_text_header":"Barang pilihan telah habis",
                        "copy_text_body":"Wah, barang \u0026 promo pilihan sudah habis. Yuk cek barang menarik lainnya di toko ini!",
                        "copy_text_redirect_button":"Ke Halaman Toko",
                        "image_url":"https://images.tokopedia.net/img/play/bottomsheet/empty/Product Search Not Found.png"
                     }
                  },
                  "app_link":"tokopedia://play/15774",
                  "web_link":"https://www.tokopedia.com/play/channel/15774",
                  "share":{
                     "text":"Udah pada nonton \"Test - Android Performance Test\" di Tokopedia Play? Ayo nonton bareng~ soalnya ini seru banget!\n",
                     "redirect_url":"https://www.tokopedia.com/play/channel/15774?titlebar\\u003dfalse",
                     "use_short_url":false,
                     "meta_title":"Tonton BUKAN Toko Kuma ~!@#${'$'}%^\u0026amp;(){}- di Tokopedia Play",
                     "meta_description":"Aku punya obat anti-bosen buatmu. Ayo nonton BUKAN Toko Kuma ~!@#${'$'}%^\u0026amp;*(){}- di Tokopedia Play!",
                     "is_show_button":true
                  },
                  "air_time":"WATCH_AGAIN"
               },
               {
                  "id":"638087",
                  "title":"🌻22.9ATASAN WANITA KEKINIAN",
                  "description":"",
                  "cover_url":"https://images.tokopedia.net/img/jJtrdn/2022/9/23/aa6b45aa-50a6-4ec5-92f8-13e571aa957b.jpg?b\\u003dUGH_i9n%24%25KWBr.D*D%25of~Wt7Nft7ISxtt7NG",
                  "start_time":"2022-09-23T15:02:00+07:00",
                  "is_live":true,
                  "partner":{
                     "id":"961442",
                     "type":"shop",
                     "name":"Vice Shop Official",
                     "thumbnail_url":"https://images.tokopedia.net/img/cache/215-square/shops-1/2021/4/7/961442/961442_6e9fb5f0-4194-4cc9-afff-7f2de02e0eb6.jpg",
                     "badge_url":"https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power Merchant Pro.png",
                     "app_link":"tokopedia://shop/961442",
                     "web_link":"https://www.tokopedia.com/viceshop"
                  },
                  "video":{
                     "id":"969782",
                     "orientation":"vertical",
                     "type":"live",
                     "stream_source":"https://live-stream.tokopedia.net/live/v0.2/play_20220923150159_005f17bf-3b16-11ed-976b-42010a2946ba/live/abr.m3u8",
                     "autoplay":true,
                     "buffer_control":{
                        "max_buffer_in_seconds":18,
                        "min_buffer_in_seconds":3,
                        "buffer_for_playback":2,
                        "buffer_for_playback_after_rebuffer":2
                     }
                  },
                  "pinned_message":{
                     "id":"119362",
                     "title":"cancel 1x-max 3 orang baru-tanya belakang 1/1❤️",
                     "redirect_url":""
                  },
                  "quick_replies":[
                     "Spill promo hari ini min!",
                     "Gratis ongkir kemana aja?",
                     "Dikirim darimana?",
                     "Jelasin variannya dong!"
                  ],
                  "configurations":{
                     "welcome_format":{
                        "type":"self_join",
                        "copy":"{{name}} \u003cb\u003eikut nonton\u003c/b\u003e",
                        "background_color":"#AD009F92",
                        "icon":"https://images.tokopedia.net/img/playassets/join.png"
                     },
                     "real_time_notif":{
                        "lifespan":800
                     },
                     "show_cart":true,
                     "show_pinned_product":true,
                     "active":true,
                     "freezed":false,
                     "has_promo":true,
                     "reminder":{
                        "is_set":false
                     },
                     "channel_freeze_screen":{
                        "title":"%s Telah Berakhir",
                        "description":"Yuk lanjut nonton berbagai video menarik lainnya di Tokopedia Play!",
                        "button_text":"Cek Channel Lain",
                        "button_app_link":"tokopedia://webview?pull_to_refresh\u003dtrue\u0026titlebar\u003dfalse\u0026autoplay\u003dtrue\u0026url\u003dhttps%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F"
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
                        "content_id":"638087",
                        "like_type":2
                     },
                     "room_background":{
                        "image_url":""
                     },
                     "multiple_like":[
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FFF94D63"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FF00AA5B"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FF46D8F1"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FF9342ED"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FFFFC400"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FFF94D63"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FF00AA5B"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FF46D8F1"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FF9342ED"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FFFFC400"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FFF94D63"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FF00AA5B"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FF46D8F1"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FF9342ED"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FFFFC400"
                        }
                     ],
                     "has_follow_button":true,
                     "empty_bottom_sheet":{
                        "copy_text_header":"Barang pilihan telah habis",
                        "copy_text_body":"Wah, barang \u0026 promo pilihan sudah habis. Yuk cek barang menarik lainnya di toko ini!",
                        "copy_text_redirect_button":"Ke Halaman Toko",
                        "image_url":"https://images.tokopedia.net/img/play/bottomsheet/empty/Product Search Not Found.png"
                     }
                  },
                  "app_link":"tokopedia://play/638087",
                  "web_link":"https://www.tokopedia.com/play/channel/638087",
                  "share":{
                     "text":"Udah pada nonton \"🌻22.9ATASAN WANITA KEKINIAN\" di Tokopedia Play? Ayo nonton bareng~ soalnya ini seru banget!",
                     "redirect_url":"https://www.tokopedia.com/play/channel/638087?titlebar\\u003dfalse",
                     "use_short_url":false,
                     "meta_title":"Tonton Vice Shop Official di Tokopedia Play",
                     "meta_description":"Aku punya obat anti-bosen buatmu. Ayo nonton Vice Shop Official di Tokopedia Play!",
                     "is_show_button":true
                  },
                  "air_time":"LIVE"
               },
               {
                  "id":"638206",
                  "title":"LONG OUTER X RAJUT SULTAN🥰",
                  "description":"",
                  "cover_url":"https://images.tokopedia.net/img/jJtrdn/2022/9/23/b8b52f7d-786b-456c-9573-546742bf1730.jpg?b\\u003dUYHeE}~pS%23x]4nROozWVtlxuofNGX9Rkt6xu",
                  "start_time":"2022-09-23T15:30:24+07:00",
                  "is_live":true,
                  "partner":{
                     "id":"12525390",
                     "type":"shop",
                     "name":"oteweshopid",
                     "thumbnail_url":"https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/12/8/9804ea71-612a-49bb-89ce-3afd2bf89a14.jpg",
                     "badge_url":"https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power Merchant Pro.png",
                     "app_link":"tokopedia://shop/12525390",
                     "web_link":"https://www.tokopedia.com/oteweshopid"
                  },
                  "video":{
                     "id":"969942",
                     "orientation":"vertical",
                     "type":"live",
                     "stream_source":"https://chat.tokopedia.com/live/api/v1/livestream/adaptive.m3u8?id\\u003df661d83e-3b19-11ed-978f-42010a2946ba",
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
                     "Spill promo hari ini min!",
                     "Gratis ongkir kemana aja?",
                     "Dikirim darimana?",
                     "Jelasin variannya dong!"
                  ],
                  "configurations":{
                     "welcome_format":{
                        "type":"self_join",
                        "copy":"{{name}} \u003cb\u003eikut nonton\u003c/b\u003e",
                        "background_color":"#AD009F92",
                        "icon":"https://images.tokopedia.net/img/playassets/join.png"
                     },
                     "real_time_notif":{
                        "lifespan":800
                     },
                     "show_cart":true,
                     "show_pinned_product":true,
                     "active":true,
                     "freezed":false,
                     "has_promo":false,
                     "reminder":{
                        "is_set":false
                     },
                     "channel_freeze_screen":{
                        "title":"%s Telah Berakhir",
                        "description":"Yuk lanjut nonton berbagai video menarik lainnya di Tokopedia Play!",
                        "button_text":"Cek Channel Lain",
                        "button_app_link":"tokopedia://webview?pull_to_refresh\u003dtrue\u0026titlebar\u003dfalse\u0026autoplay\u003dtrue\u0026url\u003dhttps%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F"
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
                        "content_id":"638206",
                        "like_type":2
                     },
                     "room_background":{
                        "image_url":""
                     },
                     "multiple_like":[
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FFF94D63"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FF00AA5B"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FF46D8F1"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FF9342ED"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/thumb_filled.png",
                           "background_color":"#FFFFC400"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FFF94D63"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FF00AA5B"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FF46D8F1"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FF9342ED"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/star_filled.png",
                           "background_color":"#FFFFC400"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FFF94D63"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FF00AA5B"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FF46D8F1"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FF9342ED"
                        },
                        {
                           "icon":"https://images.tokopedia.net/img/playassets/multiplelike/heart_filled.png",
                           "background_color":"#FFFFC400"
                        }
                     ],
                     "has_follow_button":true,
                     "empty_bottom_sheet":{
                        "copy_text_header":"Barang pilihan telah habis",
                        "copy_text_body":"Wah, barang \u0026 promo pilihan sudah habis. Yuk cek barang menarik lainnya di toko ini!",
                        "copy_text_redirect_button":"Ke Halaman Toko",
                        "image_url":"https://images.tokopedia.net/img/play/bottomsheet/empty/Product Search Not Found.png"
                     }
                  },
                  "app_link":"tokopedia://play/638206",
                  "web_link":"https://www.tokopedia.com/play/channel/638206",
                  "share":{
                     "text":"Udah pada nonton \"LONG OUTER X RAJUT SULTAN🥰\" di Tokopedia Play? Ayo nonton bareng~ soalnya ini seru banget!\n",
                     "redirect_url":"https://www.tokopedia.com/play/channel/638206?titlebar\\u003dfalse",
                     "use_short_url":false,
                     "meta_title":"Tonton oteweshopid di Tokopedia Play",
                     "meta_description":"Aku punya obat anti-bosen buatmu. Ayo nonton oteweshopid di Tokopedia Play!",
                     "is_show_button":true
                  },
                  "air_time":"LIVE"
               }
            ]
         }
      }
   }
]
"""
