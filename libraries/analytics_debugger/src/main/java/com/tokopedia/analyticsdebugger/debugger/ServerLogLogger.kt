package com.tokopedia.analyticsdebugger.debugger

import android.content.Context
import com.google.gson.GsonBuilder
import com.tokopedia.analyticsdebugger.database.ServerLogDB
import com.tokopedia.analyticsdebugger.debugger.data.source.ServerLogDBSource
import com.tokopedia.analyticsdebugger.debugger.ui.activity.ServerLogDebuggerActivity
import com.tokopedia.config.GlobalConfig
import rx.Subscriber
import rx.schedulers.Schedulers

class ServerLogLogger private constructor(private val context: Context) : ServerLogLoggerInterface {

    private val serverLogDBSource: ServerLogDBSource = ServerLogDBSource(context)

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

    override fun putServerLoggerEvent(data: Any) {
        try {
            val serverLogDB = ServerLogDB()
            serverLogDB.data = prettify(data)
            serverLogDB.timestamp = System.currentTimeMillis()
            serverLogDBSource.insertAll(serverLogDB)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber())
        } catch (ignored: Exception) {
        }
    }

    private fun prettify(obj: Any): String? {
        return try {
            val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().setLenient().create()
            gson.toJson(obj)
        } catch (e: Exception) {
            obj.toString()
        }
    }

    override fun openActivity() {
        context.startActivity(ServerLogDebuggerActivity.newInstance(context))
    }

    companion object {

        private var instance: ServerLogLoggerInterface? = null

        @JvmStatic
        fun getInstance(context: Context): ServerLogLoggerInterface {
            if (instance == null) {
                instance = if (GlobalConfig.isAllowDebuggingTools()!!) {
                    ServerLogLogger(context)
                } else {
                    emptyInstance()
                }
            }

            return instance as ServerLogLoggerInterface
        }

        private fun emptyInstance(): ServerLogLoggerInterface {
            return object : ServerLogLoggerInterface {
                override fun putServerLoggerEvent(data: Any) {
                    // noop
                }

                override fun openActivity() {
                    // noop
                }

            }
        }
    }

}
