package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.model.AutoAdsParam
import com.tokopedia.topads.common.data.response.TopAdsAutoAds
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsData
import com.tokopedia.topads.common.domain.usecase.TopAdsQueryPostAutoadsUseCase.Companion.QUERY
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

@GqlQuery("TopAdsQueryPostAutoads", QUERY)
class TopAdsQueryPostAutoadsUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<TopAdsAutoAds.Response>(graphqlRepository) {

    init {
        setTypeClass(TopAdsAutoAds.Response::class.java)
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD)
                .build()
        )
        setGraphqlQuery(TopAdsQueryPostAutoads())
    }

    fun executeQuery(param: AutoAdsParam, block: (Result<TopAdsAutoAdsData>) -> Unit) {
        setParam(param)

        execute(onSuccess = { data ->
            val error = data.autoAds.error.firstOrNull()
            block.invoke(
                if (error != null) {
                    Fail(Throwable(error.detail))
                } else {
                    Success(data = data.autoAds.data)
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

    companion object {
        const val QUERY = """
            mutation topAdsPostAutoAdsV2(${'$'}input: TopAdsPostAutoAdsParamV2!) {
              topAdsPostAutoAdsV2(input: ${'$'}input) {
                data {
                  shopID
                  status
                  statusDesc
                  dailyUsage
                  dailyBudget
                  info {
                    reason
                    message
                  }
                }
                errors {
                  code
                  detail
                  object {
                    type
                    text
                  }
                  title
                }
              }
            }
        """
    }
}