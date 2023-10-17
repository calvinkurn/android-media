package com.tokopedia.sellerorder.detail.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.detail.data.model.SomDetailIncomeDetailRequest
import com.tokopedia.sellerorder.detail.data.model.SomDetailIncomeDetailResponse
import com.tokopedia.sellerorder.detail.domain.mapper.GetFeeTransparencyFeeMapper
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeUiModelWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

const val GET_SOM_INCOME_DETAIL_QUERY = """
    query GetSOMIncomeDetail(${'$'}input: SOMIncomeDetailRequest!) {
      get_som_income_detail(input: ${'$'}input) {
        title
        sections {
          key
          label
          sub_label
          value
          value_raw
          attributes {
            tooltip {
              title
              value
            }
            data {
              type:__typename
              ... on SOMIncomeDetailAttributeIconData {
                icon_url
                icon_url_dark
              }
              ... on SOMIncomeDetailAttributeLabelData {
                level
                label
              }
            }
          }
          components {
            key
            label
            sub_label
            value
            value_raw
            attributes {
              tooltip {
                title
                value
              }
              data {
                type:__typename
                ... on SOMIncomeDetailAttributeIconData {
                  icon_url
                  icon_url_dark
                }
                ... on SOMIncomeDetailAttributeLabelData {
                  level
                  label
                }
              }
            }
            type
          }
        }
        summary {
          key
          label
          sub_label
          value
          value_raw
          attributes {
            tooltip {
              title
              value
            }
            data {
              type:__typename
              ... on SOMIncomeDetailAttributeIconData {
                icon_url
                icon_url_dark
              }
              ... on SOMIncomeDetailAttributeLabelData {
                level
                label
              }
            }
          }
          state
          note
        }
      }
    }
"""

@GqlQuery("GetSomIncomeDetailQuery", GET_SOM_INCOME_DETAIL_QUERY)
class SomGetFeeTransparencyUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<SomDetailIncomeDetailResponse>,
    private val getFeeTransparencyFeeMapper: GetFeeTransparencyFeeMapper
) {
    init {
        useCase.setGraphqlQuery(GetSomIncomeDetailQuery())
        useCase.setTypeClass(SomDetailIncomeDetailResponse::class.java)
    }

    suspend fun execute(
        orderId: String,
    ): TransparencyFeeUiModelWrapper {
        useCase.setRequestParams(createRequestParams(orderId))
        val response = useCase.executeOnBackground()
        return getFeeTransparencyFeeMapper.map(requireNotNull(response.getSomIncomeDetail))
    }

    private fun createRequestParams(orderId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putObject(PARAM_INPUT, SomDetailIncomeDetailRequest(orderId.toLongOrZero()))
        }.parameters
    }

    companion object {
        private const val PARAM_INPUT = "input"
    }
}
