package com.tokopedia.pocnewrelic

import android.app.Application
import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.pocnewrelic.datasource.NewRelicDataSource
import com.tokopedia.pocnewrelic.remoteconfig.NewRelicRemoteConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class NewRelic private constructor(context: Context) {

    companion object {
        @JvmStatic
        private var instance: NewRelic? = null

        @JvmStatic
        fun getInstance(application: Application): NewRelic {
            var instance = instance
            if (instance == null) {
                instance = createInstance(application.applicationContext)
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

    private val newRelicDataSource: NewRelicDataSource = NewRelicDataSource(
            CoroutineDispatchersProvider,
            NewRelicRemoteConfig(FirebaseRemoteConfigImpl(context))
    )

    fun sendData(data: Map<String, Any>) {
        newRelicDataSource.sendData(data)
    }
}