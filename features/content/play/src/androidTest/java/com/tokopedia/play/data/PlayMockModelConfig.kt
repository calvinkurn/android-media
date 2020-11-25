package com.tokopedia.play.data

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig


/**
 * Created by mzennis on 24/08/20.
 *
 * mock response for calculating Page Load Time
 */
class PlayMockModelConfig : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY,
                RESPONSE_MOCK_CHANNEL_DETAIL,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_SHOP_INFO,
                RESPONSE_MOCK_SHOP_INFO,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_TOTAL_LIKE,
                RESPONSE_MOCK_TOTAL_LIKE,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_IS_LIKED,
                RESPONSE_MOCK_IS_LIKED,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_TOTAL_CART,
                RESPONSE_MOCK_TOTAL_CART,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_TOTAL_CART,
                RESPONSE_MOCK_TOTAL_CART,
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_PINNED_PRODUCT,
                RESPONSE_MOCK_PINNED_PRODUCT,
                FIND_BY_CONTAINS)
        return this
    }

    companion object  {
        private const val KEY_QUERY = "GetPlayChannelDetail"
        private const val RESPONSE_MOCK_CHANNEL_DETAIL = """
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
                      "id": "6496952",
                      "type": "shop",
                      "name": "RAVPower Official Store",
                      "thumbnail_url": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2019/12/26/6496952/6496952_9cd123f1-53fa-4a14-ae74-25aaafe3e982.png",
                      "badge_url": "https://ecs7.tokopedia.net/img/official_store/badge_os.png"
                    },
                    "video": {
                      "id": "6453",
                      "orientation": "vertical",
                      "type": "vod",
                      "cover_url": "https://ecs7.tokopedia.net/img/jJtrdn/2020/8/10/d8bff2c4-7ff9-4482-9e71-3c62ae25c826.jpg",
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
    }
}
