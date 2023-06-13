package com.tokopedia.analyticsdebugger.debugger.ws

import android.content.Context
import com.google.gson.*
import com.tokopedia.analyticsdebugger.debugger.BaseWebSocketLogger
import com.tokopedia.analyticsdebugger.websocket.domain.param.InsertWebSocketLogParam
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.info.TopchatUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogPageSource
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class TopchatWebSocketLogger constructor(mContext: Context) : BaseWebSocketLogger(mContext) {

    private var detail: TopchatUiModel? = null

    override fun init(data: String) {
        detail = gson
            .registerTypeAdapter(TopchatUiModel::class.java, TopchatHeaderSerializer())
            .create()
            .parseDetailInfo(data)
    }

    override fun send(event: String, message: String) {
        launch {
            val param = InsertWebSocketLogParam(
                pageSource = WebSocketLogPageSource.TOPCHAT,
                info = WebSocketLogUiModel(
                    event = event,
                    message = message.jsonHumanized(),
                    topchat = detail ?: TopchatUiModel()
                )
            )

            insertWebSocketLogUseCase(param)
        }
    }
}

internal class TopchatHeaderSerializer : JsonDeserializer<TopchatUiModel> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): TopchatUiModel {
        val jsonObject = json?.asJsonObject

        val headerObject = jsonObject?.get("header")

        val header = if (headerObject != null) {
            GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(headerObject)
        } else ""

        return TopchatUiModel(
            source = jsonObject?.get("source")?.asString?: "",
            code = jsonObject?.get("code")?.asString?: "",
            messageId = jsonObject?.get("messageId")?.asString?: "",
            header = header
        )
    }
}
