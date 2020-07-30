package com.tokopedia.discovery2.repository.cpmtopads

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.cpmtopads.CpmTopAdsResponse
import com.tokopedia.topads.sdk.domain.model.CpmModel
import javax.inject.Inject


open class CpmTopAdsGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), CpmTopAdsRepository {

    override suspend fun getCpmTopAdsData(paramsMobile: String): CpmModel? {
        return getGQLData(getGQLString(R.raw.query_cpm_topads_gql),
                CpmTopAdsResponse::class.java, mapOf("params" to paramsMobile)).cpmModelData
    }
}


