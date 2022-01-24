package com.tokopedia.analyticsdebugger.cassava.data

import com.tokopedia.analyticsdebugger.cassava.data.api.CassavaApi
import com.tokopedia.analyticsdebugger.cassava.data.entity.JourneyListEntity
import com.tokopedia.analyticsdebugger.cassava.data.entity.QueryListEntity
import com.tokopedia.analyticsdebugger.cassava.data.entity.ValidationResultEntity
import com.tokopedia.analyticsdebugger.cassava.data.request.ValidationResultRequest
import javax.inject.Inject

/**
 * @author by furqan on 07/04/2021
 */
class CassavaRepository @Inject constructor(private val cassavaApi: CassavaApi) {

    suspend fun getNetworkQueryList(journeyId: String, token: String): QueryListEntity =
        cassavaApi.getQueryList(
            token = token,
            journeyId = journeyId
        ).data

    suspend fun sendValidationResult(
        journeyId: String,
        validationResult: ValidationResultRequest
    ): ValidationResultEntity =
        cassavaApi.postValidationResult(
            journeyId = journeyId,
            validationRequest = validationResult
        )

    suspend fun getNetworkJourneyList(
        token: String,
        journeyName: String = "",
        tribeName: String = ""
    ): List<Pair<String, String>> =
        cassavaApi.getJourneyList(
            token = token,
            journeyName = journeyName,
            tribeName = tribeName
        ).data.map {
            Pair(
                it.id.toString(),
                "${it.tribeName} : ${it.journeyName}"
            )
        }.toList()

}