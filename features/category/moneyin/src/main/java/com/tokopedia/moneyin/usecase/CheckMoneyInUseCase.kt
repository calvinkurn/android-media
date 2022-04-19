package com.tokopedia.moneyin.usecase

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.model.ValidateTradePDP
import com.tokopedia.common_tradein.utils.TradeInUtils
import com.tokopedia.moneyin.MoneyinConstants.MONEYIN
import com.tokopedia.moneyin.repository.MoneyInRepository
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class CheckMoneyInUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val repository: MoneyInRepository,
        userSession: UserSessionInterface) {

    val fcmDeviceId: String = userSession.deviceId

    fun createRequestParams(modelId: Int, tradeInParams: TradeInParams, userId: String): HashMap<String, Any> {
        if (tradeInParams.deviceId == null || tradeInParams.deviceId == fcmDeviceId)
            tradeInParams.deviceId = TradeInUtils.getDeviceId(context)
        try{
            tradeInParams.userId = userId.toInt()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            tradeInParams.userId = 0
        }
        tradeInParams.tradeInType = MONEYIN
        tradeInParams.modelID = modelId
        val variables = HashMap<String, Any>()
        variables["params"] = tradeInParams
        return variables
    }

    private fun getQuery(resources: Resources?): String {
        return GraphqlHelper.loadRawString(resources, com.tokopedia.common_tradein.R.raw.gql_validate_tradein)
    }

    suspend fun checkMoneyIn(resources: Resources?, modelId: Int, tradeInParams: TradeInParams, userId: String): ValidateTradePDP {
        val variables = createRequestParams(modelId, tradeInParams, userId)
        return repository.getGQLData(getQuery(resources), ValidateTradePDP::class.java, variables) as ValidateTradePDP
    }
}