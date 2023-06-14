package com.tokopedia.inbox.universalinbox.stub.data.response

import com.tokopedia.inbox.universalinbox.data.response.counter.UniversalInboxCounterWrapperResponse
import com.tokopedia.inbox.universalinbox.data.response.widget.UniversalInboxWidgetWrapperResponse

object GqlResponseStub {

    lateinit var counterResponse: ResponseStub<UniversalInboxCounterWrapperResponse>
    lateinit var widgetMetaResponse: ResponseStub<UniversalInboxWidgetWrapperResponse>

    init {
        reset()
    }

    fun reset() {
        counterResponse = ResponseStub(
            filePath = "counter/success_get_counter.json",
            type = UniversalInboxCounterWrapperResponse::class.java,
            query = "query GetAllCounter",
            isError = false
        )

        widgetMetaResponse = ResponseStub(
            filePath = "widget/success_get_widget_meta.json",
            type = UniversalInboxWidgetWrapperResponse::class.java,
            query = "query GetChatInboxWidgetMeta",
            isError = false
        )
    }
}
