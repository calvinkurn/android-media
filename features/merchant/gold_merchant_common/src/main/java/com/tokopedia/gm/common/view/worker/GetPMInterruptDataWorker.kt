package com.tokopedia.gm.common.view.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.config.GlobalConfig
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.domain.interactor.GetPMInterruptDataUseCase
import com.tokopedia.gm.common.domain.mapper.GetPMInterruptDataMapper
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created By @ilhamsuaib on 08/04/21
 */

class GetPMInterruptDataWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    private val getPMInterruptDataUseCase: GetPMInterruptDataUseCase by lazy {
        val gqlRepository = GraphqlInteractor.getInstance().graphqlRepository
        val mapper = GetPMInterruptDataMapper()
        GetPMInterruptDataUseCase(gqlRepository, mapper)
    }
    private val userSession: UserSessionInterface by lazy {
        UserSession(appContext)
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                getPMInterruptDataUseCase.params = GetPMInterruptDataUseCase.createParams(
                        shopId = userSession.shopId,
                        source = PMConstant.PM_SETTING_INFO_SOURCE
                )
                val result = getPMInterruptDataUseCase.executeOnBackground()
                val outputData = getOutputData(result)
                Result.success(outputData)
            } catch (e: Exception) {
                logToCrashlytic(e)
                Result.failure()
            }
        }
    }

    private fun getOutputData(result: PowerMerchantInterruptUiModel): Data {
        val gson = Gson()
        val json = gson.toJson(result)
        val map: Map<String, Any> = gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
        val outputData = Data.Builder()
        outputData.putAll(map)
        return outputData.build()
    }

    private fun logToCrashlytic(throwable: Throwable) {
        if (!GlobalConfig.isAllowDebuggingTools()) {
            val message = "GetPMInterruptDataWorker : ${throwable.localizedMessage}"
            FirebaseCrashlytics.getInstance().recordException(MessageErrorException(message))
        } else {
            throwable.printStackTrace()
        }
    }
}