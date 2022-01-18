package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberData
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.digital_product_detail.data.model.param.FavoriteNumberParam
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(GetRechargeCatalogUseCase.QUERY_NAME, GetRechargeCatalogUseCase.QUERY)
class GetRechargeFavoriteNumberUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<TopupBillsSeamlessFavNumberData>(graphqlRepository) {
    
    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): TopupBillsSeamlessFavNumberData {
        val gqlRequest = GraphqlRequest(QUERY, TopupBillsSeamlessFavNumberData::class.java, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest))

        val error = gqlResponse.getError(TopupBillsSeamlessFavNumberData::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(TopupBillsSeamlessFavNumberData::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    fun createSeamlessFavoriteNumberParams(categoryIds: List<String>): RequestParams {
        var paramSource = if (categoryIds.contains(TopupBillsViewModel.CATEGORY_ID_PASCABAYAR.toString()))
            TopupBillsViewModel.FAVORITE_NUMBER_PARAM_SOURCE_POSTPAID else TopupBillsViewModel.FAVORITE_NUMBER_PARAM_SOURCE_PREPAID

        return RequestParams.create().apply {
            putObject(FAVORITE_NUMBER_PARAM_FIELDS, FavoriteNumberParam(
                source = paramSource,
                categoryIds = categoryIds,
                minLastTransaction = "",
                minTotalTransaction = "",
                servicePlanType = "",
                subscription = false,
                limit = FAVORITE_NUMBER_LIMIT
            ))
        }
    }

    companion object {
        const val QUERY_NAME = "GetRechargeFavoriteNumberUseCaseQuery"
        const val QUERY = """
        query rechargeFetchFavoriteNumber(${'$'}fields: RechargeFavoriteNumberListRequest!) {
          rechargeFetchFavoriteNumber(fields:${'$'}fields) {
            client_number
            operator_id
            product_id
            category_id
            list {
              client_number
              operator_id
              product_id
              category_id
              label
              icon_url
            }
          }
        }
        """

        const val FAVORITE_NUMBER_PARAM_FIELDS = "fields"
        const val FAVORITE_NUMBER_LIMIT = 10
    }
}