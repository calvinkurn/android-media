package com.tokopedia.analyticsdebugger.debugger

import android.content.Context

class ServerLogLogger private constructor(private val context: Context) : ServerLogLoggerInterface {

    override fun putServerLoggerEvent(data: Any) {
        //noop
    }

    override fun openActivity() {
        //noop
    }

    companion object {

        private var instance: ServerLogLoggerInterface? = null

        @JvmStatic
        fun getInstance(context: Context): ServerLogLoggerInterface {
            if (instance == null) {
                instance = emptyInstance()
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
