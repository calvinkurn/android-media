package com.tokopedia.tradein.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.model.ValidateTradePDP
import com.tokopedia.common_tradein.utils.TradeInUtils
import com.tokopedia.tradein.R
import com.tokopedia.tradein.repository.TradeInRepository
import com.tokopedia.tradein.view.viewcontrollers.BaseTradeInActivity.TRADEIN_MONEYIN
import com.tokopedia.user.session.UserSession
import java.util.HashMap
import javax.inject.Inject

class CheckMoneyInUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val repository: TradeInRepository) {

    val fcmDeviceId = UserSession(context).deviceId

    fun createRequestParams(modelId: Int, tradeInParams: TradeInParams, userId: String): HashMap<String, Any> {
        if (tradeInParams.deviceId == null || tradeInParams.deviceId == fcmDeviceId)
            tradeInParams.deviceId = TradeInUtils.getDeviceId(context)
        try{
            tradeInParams.userId = userId.toInt()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            tradeInParams.userId = 0
        }
        tradeInParams.tradeInType = TRADEIN_MONEYIN
        tradeInParams.modelID = modelId
        val variables = HashMap<String, Any>()
        variables["params"] = tradeInParams
        return variables
    }

    private fun getQuery(): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.common_tradein.R.raw.gql_validate_tradein)
    }

    suspend fun checkMoneyIn(modelId: Int, tradeInParams: TradeInParams, userId: String): ValidateTradePDP {
        val variables = createRequestParams(modelId, tradeInParams, userId)
        return repository.getGQLData(getQuery(), ValidateTradePDP::class.java, variables) as ValidateTradePDP
    }
}