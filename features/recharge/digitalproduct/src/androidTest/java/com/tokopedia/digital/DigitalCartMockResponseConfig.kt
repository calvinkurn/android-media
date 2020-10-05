package com.tokopedia.digital

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

import com.tokopedia.digital.test.R

class DigitalCartMockResponseConfig: MockModelConfig() {

    companion object {
        const val KEY_QUERY_CART = "{\"data\":{\"attributes\":{\"device_id\":5,\"fields\":[{\"name\":\"client_number\",\"value\":\"087855813456\"}],\"identifier\":{\"device_token\":\"\",\"os_type\":\"1\",\"user_id\":\"108956738\"},\"instant_checkout\":false,\"ip_address\":\"10.0.2.15\",\"is_reseller\":false,\"is_thankyou_native\":true,\"is_thankyou_native_new\":true,\"product_id\":30,\"show_subscribe_flag\":true,\"user_agent\":\"Android Tokopedia Application/com.tokopedia.customerappp v.3.90 (Google Android SDK built for x86; Android; API_24; Version7.0) \",\"user_id\":108956738},\"type\":\"add_cart\"}}"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_CART,
                InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_cart),
                FIND_BY_CONTAINS)
        return this
    }
}