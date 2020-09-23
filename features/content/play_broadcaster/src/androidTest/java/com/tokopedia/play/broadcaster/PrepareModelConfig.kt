package com.tokopedia.play.broadcaster

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig


/**
 * Created by mzennis on 16/09/20.
 */
class PrepareModelConfig : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY,
                RESPONSE_MOCK_DRAFT_CHANNEL,
                FIND_BY_CONTAINS)
        return this
    }

    companion object {
        private const val KEY_QUERY = "broadcasterGetShopConfig"
        private const val RESPONSE_MOCK_DRAFT_CHANNEL = """
            [{
              "data": {
                "broadcasterGetShopConfig": {
                  "streamAllowed": true,
                  "config": "{\"draft_channel\":1,\"max_duration_sec\":1800,\"max_duration_desc\":\"30 menit\",\"max_tagged_product\":20,\"min_tagged_product\":1,\"max_tagged_product_desc\":\"cuma boleh 20\",\"max_pause_duration_sec\":300,\"countdown_sec\":3,\"max_title_length\":37}"
                }
              }
            }]
        """

        private const val RESPONSE_MOCK_PAUSED_CHANNEL = """
            [{
              "data": {
                "broadcasterGetShopConfig": {
                  "streamAllowed": true,
                  "config": "{\"paused_channel\":1,\"max_duration_sec\":1800,\"max_duration_desc\":\"30 menit\",\"max_tagged_product\":20,\"min_tagged_product\":1,\"max_tagged_product_desc\":\"cuma boleh 20\",\"max_pause_duration_sec\":300,\"countdown_sec\":3,\"max_title_length\":37}"
                }
              }
            }]
        """

        private const val RESPONSE_MOCK_CHANNEL_ERROR = """
            [{
              "errors": [
                {
                  "message": "your session has expired, please login again",
                  "path": [
                    "broadcasterGetShopConfig"
                  ],
                  "extensions": {
                    "developerMessage": "your session has expired, please login again",
                    "timestamp": "2020-09-21 17:53:31.024534507 +0700 WIB m=+2073.641702339"
                  }
                }
              ],
              "data": null
            }]
        """
    }
}