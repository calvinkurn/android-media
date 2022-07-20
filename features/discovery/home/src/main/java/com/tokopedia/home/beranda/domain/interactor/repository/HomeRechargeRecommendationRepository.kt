package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.di.module.query.RechargeRecommendationQuery
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by resakemal on 2020-03-09
 */

class HomeRechargeRecommendationRepository @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<RechargeRecommendation.Response>)
    : UseCase<RechargeRecommendation>(), HomeRepository<RechargeRecommendation> {
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(RechargeRecommendation.Response::class.java)
    }
    companion object {
        const val PARAM_TYPE = "type"
        const val DEFAULT_TYPE = 1

        const val NULL_RESPONSE = "null response"
    }

    private var params: RequestParams = RequestParams.create()

    override suspend fun executeOnBackground(): RechargeRecommendation {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(RechargeRecommendationQuery())
        graphqlUseCase.setRequestParams(params.parameters)
        val response = graphqlUseCase.executeOnBackground().response

        if (response != null) return response
        else throw (MessageErrorException(NULL_RESPONSE))
    }

    fun setParams(type: Int = DEFAULT_TYPE) {
        params.parameters.clear()
        params.putInt(PARAM_TYPE, type)
    }

    override suspend fun getRemoteData(bundle: Bundle): RechargeRecommendation {
        setParams()
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): RechargeRecommendation {
        setParams()
        return RechargeRecommendation()
    }
}