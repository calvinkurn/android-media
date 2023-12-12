package com.tokopedia.tokopedianow.annotation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse
import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse.GetAnnotationListResponse
import com.tokopedia.tokopedianow.annotation.domain.param.AnnotationType
import com.tokopedia.tokopedianow.annotation.domain.query.GetAnnotationWidgetQuery
import com.tokopedia.tokopedianow.annotation.domain.query.GetAnnotationWidgetQuery.PARAM_ANNOTATION_TYPE
import com.tokopedia.tokopedianow.annotation.domain.query.GetAnnotationWidgetQuery.PARAM_CATEGORY_ID
import com.tokopedia.tokopedianow.annotation.domain.query.GetAnnotationWidgetQuery.PARAM_PAGE_SOURCE
import com.tokopedia.tokopedianow.annotation.domain.query.GetAnnotationWidgetQuery.PARAM_WAREHOUSE_IDS
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetAnnotationWidgetUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) {

    private val graphql by lazy { GraphqlUseCase<TokoNowGetAnnotationListResponse>(graphqlRepository) }

    suspend fun execute(
        categoryId: String,
        warehouseIds: String,
        annotationType: AnnotationType,
        pageSource: String
    ): GetAnnotationListResponse {
        graphql.apply {
            val requestParams = RequestParams().apply {
                putString(PARAM_CATEGORY_ID, categoryId)
                putString(PARAM_WAREHOUSE_IDS, warehouseIds)
                putString(PARAM_ANNOTATION_TYPE, annotationType.name)
                putString(PARAM_PAGE_SOURCE, pageSource)
            }.parameters

            setGraphqlQuery(GetAnnotationWidgetQuery)
            setTypeClass(TokoNowGetAnnotationListResponse::class.java)
            setRequestParams(requestParams)
        }
        val response = graphql.executeOnBackground().response
        val messages = response.header.messages

        return if (messages.isNotEmpty()) {
            throw MessageErrorException(messages.first())
        } else {
            response
        }
    }
}
