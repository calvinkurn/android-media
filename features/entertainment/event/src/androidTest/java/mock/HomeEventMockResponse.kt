package mock

import android.content.Context
import com.tokopedia.entertainment.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class HomeEventMockResponse : MockModelConfig(){

    companion object{
        const val KEY_EVENT_CHILD = "event_child_category"
        const val KEY_EVENT_LOCATION = "event_location_search"
        const val KEY_EVENT_HOME = "event_home"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_EVENT_CHILD,
                getRawString(context, R.raw.event_home),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_EVENT_LOCATION,
                getRawString(context, R.raw.event_home),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_EVENT_HOME,
                getRawString(context, R.raw.event_home),
                FIND_BY_CONTAINS
        )
        return this
    }
}