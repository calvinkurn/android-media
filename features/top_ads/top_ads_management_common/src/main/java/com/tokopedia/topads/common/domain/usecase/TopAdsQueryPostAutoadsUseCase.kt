package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.model.AutoAdsParam
import com.tokopedia.topads.common.domain.model.TopAdsAutoAdsModel
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsV2
import com.tokopedia.topads.common.domain.mapper.TopAdsAutoAdsMapper.mapToDomain
import com.tokopedia.topads.common.domain.query.GetTopadsPostAutoAdsV2
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TopAdsQueryPostAutoadsUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<TopAdsAutoAdsV2.Response>(graphqlRepository) {

    init {
        setTypeClass(TopAdsAutoAdsV2.Response::class.java)
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD)
                .build()
        )
        setGraphqlQuery(GetTopadsPostAutoAdsV2)
    }

    fun executeQuery(param: AutoAdsParam, block: (Result<TopAdsAutoAdsModel>) -> Unit) {
        setParam(param)

        execute(onSuccess = { data ->
            val error = data.autoAds.error.firstOrNull()
            block.invoke(
                if (error != null) {
                    Fail(Throwable(error.detail))
                } else {
                    Success(data = data.autoAds.data.mapToDomain)
                }
            )
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun setParam(input: AutoAdsParam) {
        val param = mapOf("input" to input.input)
        setRequestParams(param)
    }
}
