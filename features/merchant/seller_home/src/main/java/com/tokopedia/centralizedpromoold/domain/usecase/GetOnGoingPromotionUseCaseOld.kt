package com.tokopedia.centralizedpromoold.domain.usecase

import com.tokopedia.centralizedpromoold.domain.model.GetPromotionListResponseWrapperOld
import com.tokopedia.centralizedpromoold.domain.usecase.GetOnGoingPromotionGqlQuery
import com.tokopedia.centralizedpromoold.view.model.OnGoingPromoListUiModelOld
import com.tokopedia.centralizedpromoold.domain.mapper.OnGoingPromotionMapperOld
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.usecase.BaseGqlUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetOnGoingPromotionGqlQuery", GetOnGoingPromotionUseCaseOld.QUERY)
class GetOnGoingPromotionUseCaseOld @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val onGoingPromotionMapperOld: OnGoingPromotionMapperOld
) : BaseGqlUseCase<OnGoingPromoListUiModelOld>() {

    override suspend fun executeOnBackground(): OnGoingPromoListUiModelOld {
        val gqlRequest = GraphqlRequest(
            GetOnGoingPromotionGqlQuery(),
            GetPromotionListResponseWrapperOld::class.java,
            params.parameters
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest))

        val errors = gqlResponse.getError(GetPromotionListResponseWrapperOld::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetPromotionListResponseWrapperOld>()
            return onGoingPromotionMapperOld.mapDomainDataModelToUiDataModel(data.response)
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