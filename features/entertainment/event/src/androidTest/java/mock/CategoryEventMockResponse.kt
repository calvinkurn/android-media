package mock

import android.content.Context
import com.tokopedia.entertainment.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class CategoryEventMockResponse : MockModelConfig(){

    companion object{
        const val KEY_EVENT_CHILD = "event_search"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_EVENT_CHILD,
                InstrumentationMockHelper.getRawString(context, R.raw.event_category),
                MockModelConfig.FIND_BY_CONTAINS)

        return this
    }
}