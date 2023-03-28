package com.tokopedia.centralizedpromo.domain.usecase

import com.tokopedia.centralizedpromo.domain.mapper.OnGoingPromotionMapper
import com.tokopedia.centralizedpromo.domain.model.GetPromotionListResponseWrapper
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoListUiModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetOnGoingPromotionGqlQuery", GetOnGoingPromotionUseCase.QUERY)
class GetOnGoingPromotionUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val onGoingPromotionMapper: OnGoingPromotionMapper
) : BaseGqlUseCase<OnGoingPromoListUiModel>() {

    override suspend fun executeOnBackground(): OnGoingPromoListUiModel {
        val gqlRequest = GraphqlRequest(
            GetOnGoingPromotionGqlQuery(),
            GetPromotionListResponseWrapper::class.java,
            params.parameters
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest))

        val errors = gqlResponse.getError(GetPromotionListResponseWrapper::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetPromotionListResponseWrapper>()
            return onGoingPromotionMapper.mapDomainDataModelToUiDataModel(data.response)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        const val QUERY = """
            query MerchantPromotionGetPromotionList(${'$'}showEmpty: Boolean!) {
              MerchantPromotionGetPromotionList(ShowEmptyCount: ${'$'}showEmpty) {
                header {
                  process_time
                  message
                  reason
                  error_code
                }
                data {
                  title
                  list {
                    title
                    status {
                      text
                      count
                      url
                      mobile_url
                    }
                    footer {
                      text
                      url
                      mobile_url
                    }
                  }
                }
              }
            }
        """

        private const val SHOW_EMPTY_COUNT = "showEmpty"

        fun getRequestParams(showEmpty: Boolean): RequestParams {
            return RequestParams.create().apply {
                putBoolean(SHOW_EMPTY_COUNT, showEmpty)
            }
        }
    }
}
