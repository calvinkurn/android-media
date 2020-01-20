package com.tokopedia.scanner.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.scanner.R
import com.tokopedia.scanner.domain.model.VerificationResponse
import javax.inject.Inject

/**
 * Created by rival on 29/10/19.
 */

class ScannerUseCase @Inject constructor(
        resource: Resources,
        repository: GraphqlRepository
) : GraphqlUseCase<VerificationResponse>(repository) {

    init {
        val query = GraphqlHelper.loadRawString(resource, R.raw.query_qr)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).apply {
            setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`())
        }.build()

        if (!query.isNullOrEmpty()) {
            setGraphqlQuery(query)
            setTypeClass(VerificationResponse::class.java)
            setCacheStrategy(cacheStrategy)
        }
    }

    fun setParams(campaignId: String, isNotApp: Boolean) {
        val params = mutableMapOf(
                PARAMS_CAMPAIGN_ID to campaignId,
                PARAMS_IS_NOT_APP to isNotApp
        )

        setRequestParams(params)
    }

    companion object {
        private const val PARAMS_CAMPAIGN_ID = "campaignId"
        private const val PARAMS_IS_NOT_APP = "isNotApp"
    }
}