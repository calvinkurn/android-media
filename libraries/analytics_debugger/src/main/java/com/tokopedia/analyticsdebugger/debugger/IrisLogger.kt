package com.tokopedia.analyticsdebugger.debugger

import android.content.Context

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.analyticsdebugger.database.IrisSaveLogDB
import com.tokopedia.analyticsdebugger.database.IrisSendLogDB
import com.tokopedia.analyticsdebugger.debugger.data.source.IrisSaveLogDBSource
import com.tokopedia.analyticsdebugger.debugger.data.source.IrisSendLogDBSource
import com.tokopedia.analyticsdebugger.debugger.ui.activity.AnalyticsIrisSaveDebuggerActivity
import com.tokopedia.analyticsdebugger.debugger.ui.activity.AnalyticsIrisSendDebuggerActivity
import com.tokopedia.config.GlobalConfig

import rx.Subscriber
import rx.schedulers.Schedulers

class IrisLogger private constructor(private val context: Context) : IrisLoggerInterface {

    private val irisSaveLogDBSource: IrisSaveLogDBSource
    private val irisSendLogDBSource: IrisSendLogDBSource

    init {
        this.irisSendLogDBSource = IrisSendLogDBSource(context)
        this.irisSaveLogDBSource = IrisSaveLogDBSource(context)
    }

    private fun defaultSubscriber(): Subscriber<in Boolean> {
        return object : Subscriber<Boolean>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(aBoolean: Boolean?) {
                // no-op
            }
        }
    }

    override fun putSaveIrisEvent(data: String) {
        try {
            val irisSaveLogDB = IrisSaveLogDB()
            irisSaveLogDB.data = prettify(data)
            irisSaveLogDB.timestamp = System.currentTimeMillis()
            irisSaveLogDBSource.insertAll(irisSaveLogDB)
                    .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber())
        } catch (ignored: Exception) {
        }

    }

    override fun putSendIrisEvent(data: String, rowCount: Int) {
        try {
            val irisSendLogDB = IrisSendLogDB()
            irisSendLogDB.data = rowCount.toString() + " - " + prettify(data)
            irisSendLogDB.timestamp = System.currentTimeMillis()
            irisSendLogDBSource.insertAll(irisSendLogDB, rowCount)
                    .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber())
        } catch (ignored: Exception) {
        }

    }

    private fun prettify(jsonString: String): String? {
        try {
            val jsonObject = JsonParser().parse(jsonString).asJsonObject
            val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().setLenient().create()
            return gson.toJson(jsonObject)
        } catch (e: Exception) {
            return jsonString
        }

    }

    override fun openSaveActivity() {
        context.startActivity(AnalyticsIrisSaveDebuggerActivity.newInstance(context))
    }

    override fun openSendActivity() {
        context.startActivity(AnalyticsIrisSendDebuggerActivity.newInstance(context))
    }

    companion object {

        private var instance: IrisLoggerInterface? = null

        @JvmStatic
        fun getInstance(context: Context): IrisLoggerInterface {
            if (instance == null) {
                if (GlobalConfig.isAllowDebuggingTools()!!) {
                    instance = IrisLogger(context)
                } else {
                    instance = emptyInstance()
                }
            }

            return instance as IrisLoggerInterface
        }

        private fun emptyInstance(): IrisLoggerInterface {
            return object : IrisLoggerInterface {

                override fun putSendIrisEvent(data: String, rowCount: Int) {

                }

                override fun putSaveIrisEvent(data: String) {

                }

                override fun openSaveActivity() {

                }

                override fun openSendActivity() {

                }
            }
        }
    }
}
