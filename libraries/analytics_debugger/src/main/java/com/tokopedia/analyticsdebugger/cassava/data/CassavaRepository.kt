package com.tokopedia.analyticsdebugger.cassava.data

import com.tokopedia.analyticsdebugger.cassava.data.api.CassavaApi
import com.tokopedia.analyticsdebugger.cassava.data.entity.QueryListEntity
import javax.inject.Inject

/**
 * @author by furqan on 07/04/2021
 */
class CassavaRepository @Inject constructor(private val cassavaApi: CassavaApi) {

    suspend fun getNetworkQueryList(journeyId: Int): QueryListEntity =
            cassavaApi.getQueryList(
                    auth = "",
                    journeyId = journeyId
            ).data

}