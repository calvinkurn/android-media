package mock

import android.content.Context
import com.tokopedia.entertainment.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class PDPTicketEventMockResponse : MockModelConfig(){

    companion object{
        const val KEY_QUERY_PDP_V3 = "event_product_detail_v3"
        const val KEY_TRAVEL_HOLIDAY = "TravelGetHoliday"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_PDP_V3,
                getRawString(context, R.raw.event_pdp_ticket),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_TRAVEL_HOLIDAY,
                getRawString(context, R.raw.event_travel_holiday),
                FIND_BY_CONTAINS)
        return this
    }
}