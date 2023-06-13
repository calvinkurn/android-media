package com.tokopedia.analyticsdebugger.debugger

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.analyticsdebugger.websocket.data.local.database.WebSocketLogDatabase
import com.tokopedia.analyticsdebugger.websocket.data.repository.PlayWebSocketLogRepositoryImpl
import com.tokopedia.analyticsdebugger.websocket.data.repository.TopchatWebSocketLogRepositoryImpl
import com.tokopedia.analyticsdebugger.websocket.domain.usecase.InsertWebSocketLogUseCase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */

interface WebSocketLogger {
    fun init(data: String)
    fun send(event: String)
    fun send(event: String, message: String)
}

abstract class BaseWebSocketLogger(val context: Context) : WebSocketLogger, CoroutineScope {

    var insertWebSocketLogUseCase: InsertWebSocketLogUseCase
        private set

    private val dispatchers = CoroutineDispatchersProvider

    protected val gson = GsonBuilder()
        .disableHtmlEscaping()
        .setPrettyPrinting()

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatchers.io

    init {
        val db = WebSocketLogDatabase.getInstance(context)

        insertWebSocketLogUseCase = InsertWebSocketLogUseCase(
            PlayWebSocketLogRepositoryImpl(db),
            TopchatWebSocketLogRepositoryImpl(db),
            dispatchers
        )
    }

    override fun send(event: String) {
        send(event, "")
    }

    /**
     * Encode the data object into json string
     */
    protected open fun String.jsonHumanized(): String {
        return if (this.isEmpty()) {
            this
        } else {
            gson.create().toJson(JsonParser.parseString(this))
        }
    }

    /**
     * Util to decode from the string to gson object.
     *
     * @param data
     */
    inline fun <reified T> Gson.parseDetailInfo(data: String): T? {
        return try {
            fromJson(data, T::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
