package mock

import android.content.Context
import com.tokopedia.entertainment.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class CheckoutEventMockResponse : MockModelConfig(){

    companion object{
        const val KEY_QUERY_PDP_V3 = "event_product_detail_v3"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_PDP_V3,
                getRawString(context, R.raw.event_checkout),
                FIND_BY_CONTAINS)

        return this
    }
}