package com.tokopedia.topchat.stub.chatlist.usecase

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatlist.domain.pojo.operational_insight.ShopChatMetricResponse
import com.tokopedia.topchat.chatlist.domain.usecase.GetOperationalInsightUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class GetOperationalInsightUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
): GetOperationalInsightUseCase(repository, dispatcher) {

    var response: ShopChatMetricResponse = ShopChatMetricResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var isError = false
        set(value) {
            if (value) {
                repository.createErrorMapResult(response::class.java, "Oops!")
            }
            field = value
        }

    private var defaultShopChatMetricJson = "chat_list_operational_insight/success_get_shop_chat_ticker.json"

    fun getShopMetricResponse(
        isShowing: Boolean,
        isMaintain: Boolean
    ): ShopChatMetricResponse {
        return alterResponseOf(defaultShopChatMetricJson) { json ->
            toggleShowTicker(json, isShowing)
            toggleIsMaintainTicker(json, isMaintain)
        }
    }

    private fun toggleShowTicker(json: JsonObject, isShowing: Boolean) {
        val response = json.getAsJsonObject(GET_SHOP_CHAT_TICKER)
        response.addProperty(SHOW_TICKER, isShowing)
    }

    private fun toggleIsMaintainTicker(json: JsonObject, isMaintain: Boolean) {
        val response = json.getAsJsonObject(GET_SHOP_CHAT_TICKER)
        response.addProperty(IS_MAINTAIN, isMaintain)
    }

    private fun alterResponseOf(
        responsePath: String,
        altercation: (JsonObject) -> Unit
    ): ShopChatMetricResponse {
        val responseObj: JsonObject = AndroidFileUtil.parse(
            responsePath, JsonObject::class.java
        )
        altercation(responseObj)
        return CommonUtil.fromJson(
            responseObj.toString(), ShopChatMetricResponse::class.java
        )
    }

    companion object {
        private const val GET_SHOP_CHAT_TICKER = "GetShopChatTicker"
        private const val SHOW_TICKER = "ShowTicker"
        private const val IS_MAINTAIN = "IsMaintain"
    }
}