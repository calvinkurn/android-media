package com.tokopedia.product.manage.feature.violation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.violation.data.*
import com.tokopedia.product.manage.feature.violation.domain.mapper.ViolationReasonMapper
import com.tokopedia.product.manage.feature.violation.view.uimodel.ViolationReasonUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ViolationReasonUseCase @Inject constructor(graphqlRepository: GraphqlRepository,
                                                 private val mapper: ViolationReasonMapper) :
        GraphqlUseCase<ViolationReasonDetailResponse>(graphqlRepository) {

    companion object {
        private val QUERY = """
            query getPendingReasonDetail(${'$'}productId: [String]!){
              visionGetPendingReasonDetail(product_id: ${"$"}filter) {
                product_id
                title
                content {
                  description {
                    info
                    detail
                  }
                  resolution {
                    info
                    steps
                  }
                  cta {
                    text
                    link
                  }
                }
              }
            }
        """.trimIndent()

        private const val PRODUCT_ID_KEY = "productId"

        private fun createRequestParam(productId: String): RequestParams {
            return RequestParams.create().apply {
                putObject(PRODUCT_ID_KEY, listOf(productId))
            }
        }
    }

    init {
        setTypeClass(ViolationReasonDetailResponse::class.java)
        setGraphqlQuery(QUERY)
    }

    suspend fun execute(productId: String): ViolationReasonUiModel {
        setRequestParams(createRequestParam(productId).parameters)
        val response = executeOnBackground()
        return mapper.mapViolationResponseToUiModel(response)
    }

}