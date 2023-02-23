package com.tokopedia.product.manage.feature.suspend.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.StockReminderQuery
import com.tokopedia.product.manage.feature.suspend.data.SuspendReasonDetailResponse
import com.tokopedia.product.manage.feature.suspend.domain.mapper.SuspendReasonMapper
import com.tokopedia.product.manage.feature.suspend.view.uimodel.SuspendReasonUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("SuspendReasonGqlQuery", SuspendReasonUseCase.QUERY)
class SuspendReasonUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val mapper: SuspendReasonMapper
) :
    GraphqlUseCase<SuspendReasonDetailResponse>(graphqlRepository) {

    companion object {
        const val QUERY = """
            query checkProductViolation(${'$'}productID: [String!]!, ${'$'}type: ProductViolationType!, ${'$'}lang: ProductViolationLang!) {
              CheckProductViolation(productID: ${'$'}productID, type: ${'$'}type, lang: ${'$'}lang) {
                data {
                  productID
                  infoToPrevent
                  infoImpact
                  infoReason
                  infoFootNote
                  infoToResolve
                  urlHelpCenter
                }
              }
            }
        """

        private const val PRODUCT_ID_KEY = "productID"
        private const val VIOLATION_TYPE = "type"
        private const val LANG = "lang"

        private fun createRequestParam(productId: String): RequestParams {
            return RequestParams.create().apply {
                putObject(PRODUCT_ID_KEY, listOf(productId))
                putString(VIOLATION_TYPE, "SUSPENSION")
                putString(LANG, "ID")

            }
        }
    }

    init {
        setTypeClass(SuspendReasonDetailResponse::class.java)
        setGraphqlQuery(QUERY)
    }

    suspend fun execute(productId: String): SuspendReasonUiModel {
        setRequestParams(createRequestParam(productId).parameters)
        val response = executeOnBackground()
        return mapper.mapSuspendResponseToUiModel(response)
    }

}