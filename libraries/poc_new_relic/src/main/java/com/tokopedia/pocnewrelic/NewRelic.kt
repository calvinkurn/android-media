package com.tokopedia.pocnewrelic

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.pocnewrelic.datasource.NewRelicCloudDataSource
import com.tokopedia.pocnewrelic.remoteconfig.NewRelicRemoteConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class NewRelic private constructor(context: Context) {

    companion object {
        @JvmStatic
        private var instance: NewRelic? = null

        @JvmStatic
        fun getInstance(context: Context): NewRelic {
            var instance = instance
            if (instance == null) {
                instance = createInstance(context)
            }
            return instance
        }

        @JvmStatic
        @Synchronized
        private fun createInstance(context: Context): NewRelic {
            var instance = instance
            if (instance == null) {
                instance = NewRelic(context)
                this.instance = instance
            }
            return instance
        }
    }

    private val newRelicCloudDataSource: NewRelicCloudDataSource = NewRelicCloudDataSource(
            CoroutineDispatchersProvider,
            NewRelicRemoteConfig(FirebaseRemoteConfigImpl(context))
    )

    fun sendData(data: Map<String, Any>) {
        newRelicCloudDataSource.sendData(data)
    }
}