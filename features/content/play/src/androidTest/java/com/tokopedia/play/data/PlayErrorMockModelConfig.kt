package com.tokopedia.play.data

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig


/**
 * Created by mzennis on 03/09/20.
 */
class PlayErrorMockModelConfig : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY,
                RESPONSE_MOCK_CHANNEL_DETAIL,
                FIND_BY_CONTAINS)
        return this
    }

    companion object  {
        private const val KEY_QUERY = "GetPlayChannelDetail"
        private const val RESPONSE_MOCK_CHANNEL_DETAIL = """
            {
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
            }
        """
    }
}
