package com.tokopedia.inbox.universalinbox.stub.data.repository

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import retrofit2.Retrofit

class TokoChatRepositoryStub(
    retrofit: Retrofit,
    context: Context,
    babbleCourierClient: BabbleCourierClient,
    remoteConfig: RemoteConfig
) : TokoChatRepository(retrofit, context, babbleCourierClient, remoteConfig) {

    override fun trackEvent(name: String, properties: Map<String, Any>) {
        // do nothing
    }

    override fun trackPeopleProperty(propertyMap: Map<String, Any>) {
        // do nothing
    }
}
