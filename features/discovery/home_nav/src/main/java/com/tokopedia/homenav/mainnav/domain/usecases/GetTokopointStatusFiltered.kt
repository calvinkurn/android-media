package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.tokopoint.TokopointsStatusFilteredPojo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Lukas on 3/1/21.
 */
class GetTokopointStatusFiltered @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<TokopointsStatusFilteredPojo>
) : UseCase<Result<TokopointsStatusFilteredPojo>>() {
    var params: RequestParams = RequestParams.EMPTY

    init {
        val query = """
            {
              tokopointsStatusFiltered(filterKeys: ["points"], pointsExternalCurrency: "IDR", source: "globalMenu"){
                statusFilteredData {
                  points {
                    iconImageURL
                    pointsAmount
                    pointsAmountStr
                    externalCurrencyAmount
                    externalCurrencyAmountStr
                  }
                }
              }
            }
        """.trimIndent()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(TokopointsStatusFilteredPojo::class.java)
    }

    override suspend fun executeOnBackground(): Result<TokopointsStatusFilteredPojo> {
        return try{
            graphqlUseCase.setRequestParams(params.parameters)
            val data = graphqlUseCase.executeOnBackground()
            return Success(data)
        } catch (e: Exception) {
            Fail(e)
        }
    }
}