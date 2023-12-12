package com.tokopedia.tokopedianow.test.dispatcher

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockWebserverDispatcher : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        return MockResponse().setResponseCode(200).setBody("")
    }
}
