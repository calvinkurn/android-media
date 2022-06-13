package com.tokopedia.analyticsdebugger.debugger

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.analyticsdebugger.websocket.data.local.database.WebSocketLogDatabase
import com.tokopedia.analyticsdebugger.websocket.data.repository.WebSocketLogRepositoryImpl
import com.tokopedia.analyticsdebugger.websocket.domain.usecase.InsertWebSocketLogUseCase
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogGeneralInfoUiModel
import com.tokopedia.config.GlobalConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
class WebSocketLogger(context: Context): RealtimeNetworkLoggerInterface {

    private val insertWebSocketLogUseCase: InsertWebSocketLogUseCase
    private val dispatchers: CoroutineDispatchers
    private val gson: Gson
    private val job: Job
    private val scope: CoroutineScope

    init {
        dispatchers = CoroutineDispatchersProvider
        insertWebSocketLogUseCase = InsertWebSocketLogUseCase(WebSocketLogRepositoryImpl(dispatchers, WebSocketLogDatabase.getInstance(context.applicationContext)))
        gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
        job = Job()
        scope = CoroutineScope(dispatchers.io + job)
    }

    private var generalInfo: WebSocketLogGeneralInfoUiModel? = null

    override fun init(generalInfo: String) {
        this.generalInfo = parseGeneralInfo(generalInfo)
    }

    override fun send(event: String, message: String) {
        scope.launch {
            generalInfo?.let {
                insertWebSocketLogUseCase.setParam(event, beautifyMessage(message), it)
                insertWebSocketLogUseCase.executeOnBackground()
            }
        }
    }

    override fun send(event: String) {
        send(event, "")
    }

    private fun parseGeneralInfo(generalInfo: String): WebSocketLogGeneralInfoUiModel? {
        return try {
            gson.fromJson(generalInfo, WebSocketLogGeneralInfoUiModel::class.java)
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
        private var instance: RealtimeNetworkLoggerInterface? = null

        @JvmStatic
        fun getInstance(context: Context): RealtimeNetworkLoggerInterface {
            if(instance == null) {
                instance = if(GlobalConfig.isAllowDebuggingTools()) WebSocketLogger(context.applicationContext)
                else emptyInstance()
            }
            return instance as RealtimeNetworkLoggerInterface
        }

        private fun emptyInstance(): RealtimeNetworkLoggerInterface {
            return object: RealtimeNetworkLoggerInterface {
                override fun init(generalInfo: String) {

                }

                override fun send(event: String, message: String) {

                }

                override fun send(event: String) {

                }
            }
        }
    }
}