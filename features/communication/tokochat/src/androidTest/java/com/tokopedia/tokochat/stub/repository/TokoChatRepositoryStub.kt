package com.tokopedia.tokochat.stub.repository

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import retrofit2.Retrofit

class TokoChatRepositoryStub (
    retrofit: Retrofit, context: Context, babbleCourierClient: BabbleCourierClient
): TokoChatRepository(retrofit, context, babbleCourierClient) {

    override fun trackEvent(name: String, properties: Map<String, Any>) {
        //do nothing
    }

    override fun trackPeopleProperty(propertyMap: Map<String, Any>) {
        //do nothing
    }
}
