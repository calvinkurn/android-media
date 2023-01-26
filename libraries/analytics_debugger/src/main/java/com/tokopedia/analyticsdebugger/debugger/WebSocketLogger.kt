package com.tokopedia.analyticsdebugger.debugger

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.tokopedia.analyticsdebugger.websocket.data.local.database.WebSocketLogDatabase
import com.tokopedia.analyticsdebugger.websocket.data.repository.PlayWebSocketLogRepositoryImpl
import com.tokopedia.analyticsdebugger.websocket.data.repository.TopchatWebSocketLogRepositoryImpl
import com.tokopedia.analyticsdebugger.websocket.domain.usecase.InsertWebSocketLogUseCase
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.PlayWebSocketLogGeneralInfoUiModel
import com.tokopedia.config.GlobalConfig
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */

interface WebSocketLogger {
    fun init(data: String)
    fun send(event: String, message: String)
    fun send(event: String)
}

abstract class BaseWebSocketLogger(val context: Context) : WebSocketLogger, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    var insertWebSocketLogUseCase: InsertWebSocketLogUseCase

    protected var gson: Gson = GsonBuilder()
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .create()

    init {
        val db = WebSocketLogDatabase.getInstance(context.applicationContext)

        insertWebSocketLogUseCase = InsertWebSocketLogUseCase(
            PlayWebSocketLogRepositoryImpl(db),
            TopchatWebSocketLogRepositoryImpl(db)
        )
    }

    override fun send(event: String) {
        send(event, "")
    }

    protected open fun beautifyMessage(message: String): String {
        return if (message.isEmpty()) message else gson.toJson(JsonParser.parseString(message))
    }
}
