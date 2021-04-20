package com.tokopedia.analyticsdebugger.cassava.data

import com.tokopedia.analyticsdebugger.cassava.data.api.CassavaApi
import com.tokopedia.analyticsdebugger.cassava.data.api.CassavaUrl
import com.tokopedia.analyticsdebugger.cassava.data.entity.QueryListEntity
import com.tokopedia.analyticsdebugger.cassava.data.entity.ValidationResultEntity
import com.tokopedia.analyticsdebugger.cassava.data.request.ValidationResultRequest
import javax.inject.Inject

/**
 * @author by furqan on 07/04/2021
 */
class CassavaRepository @Inject constructor(private val cassavaApi: CassavaApi) {

    suspend fun getNetworkQueryList(journeyId: String): QueryListEntity =
            cassavaApi.getQueryList(
                    token = CassavaUrl.TOKEN,
                    journeyId = journeyId
            ).data

    suspend fun sendValidationResult(journeyId: String, validationResult: ValidationResultRequest): ValidationResultEntity =
            cassavaApi.postValidationResult(
                    journeyId = journeyId,
                    validationRequest = validationResult
            )

}