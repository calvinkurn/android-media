package com.tokopedia.discovery2.repository.cpmtopads

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.data.cpmtopads.CpmTopAdsResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_CPM
import com.tokopedia.topads.sdk.domain.model.CpmModel


open class CpmTopAdsGQLRepository : BaseRepository(), CpmTopAdsRepository {

    override suspend fun getCpmTopAdsData(paramsMobile: String): CpmModel? {
        return getGQLData(GQL_CPM,
                CpmTopAdsResponse::class.java, mapOf("params" to paramsMobile)).cpmModelData
    }
}


