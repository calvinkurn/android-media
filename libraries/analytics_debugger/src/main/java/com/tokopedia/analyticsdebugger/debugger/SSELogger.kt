package com.tokopedia.analyticsdebugger.debugger

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.analyticsdebugger.sse.data.local.database.SSELogDatabase
import com.tokopedia.analyticsdebugger.sse.data.repository.SSELogRepositoryImpl
import com.tokopedia.analyticsdebugger.sse.domain.usecase.InsertSSELogUseCase
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogGeneralInfoUiModel
import com.tokopedia.config.GlobalConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * Created By : Jonathan Darwin on November 17, 2021
 */
class SSELogger(context: Context) : WebSocketLogger {

    private val insertSSELogUseCase: InsertSSELogUseCase
    private val dispatchers: CoroutineDispatchers
    private val gson: Gson

    init {
        dispatchers = CoroutineDispatchersProvider
        insertSSELogUseCase = InsertSSELogUseCase(SSELogRepositoryImpl(dispatchers, SSELogDatabase.getInstance(context)))
        gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
    }

    private var generalInfo: SSELogGeneralInfoUiModel? = null

    override fun init(data: String) {
        generalInfo = parseGeneralInfo(data)
    }

    override fun send(event: String, message: String) {
        CoroutineScope(dispatchers.io).launch {
            generalInfo?.let {
                insertSSELogUseCase.setParam(event, beautifyMessage(message), it)
                insertSSELogUseCase.executeOnBackground()
            }
        }
    }

    override fun send(event: String) {
        send(event, "")
    }

    private fun parseGeneralInfo(generalInfo: String): SSELogGeneralInfoUiModel? {
        return try {
            gson.fromJson(generalInfo, SSELogGeneralInfoUiModel::class.java)
        }
        catch (e: Exception) {
            null
        }
    }

    private fun beautifyMessage(message: String): String {
        return if(message.isEmpty()) message
                else gson.toJson(JsonParser.parseString(message))
    }

    companion object {
        private var instance: WebSocketLogger? = null

        @JvmStatic
        fun getInstance(context: Context): WebSocketLogger {
            if (instance == null) {
                instance = if (GlobalConfig.isAllowDebuggingTools()) {
                    SSELogger(context.applicationContext)
                } else {
                    emptyInstance()
                }
            }

            return instance as WebSocketLogger
        }

        private fun emptyInstance(): WebSocketLogger {
            return object: WebSocketLogger {
                override fun init(data: String) {}
                override fun send(event: String, message: String) {}
                override fun send(event: String) {}
            }
        }
    }
}
