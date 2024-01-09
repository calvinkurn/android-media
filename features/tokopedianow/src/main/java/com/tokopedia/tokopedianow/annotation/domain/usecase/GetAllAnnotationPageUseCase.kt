package com.tokopedia.tokopedianow.annotation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse
import com.tokopedia.tokopedianow.annotation.domain.param.AnnotationPageSource
import com.tokopedia.tokopedianow.annotation.domain.param.AnnotationType
import com.tokopedia.tokopedianow.annotation.domain.query.GetAllAnnotationPageQuery
import com.tokopedia.tokopedianow.annotation.domain.query.GetAllAnnotationPageQuery.PARAM_ANNOTATION_TYPE
import com.tokopedia.tokopedianow.annotation.domain.query.GetAllAnnotationPageQuery.PARAM_CATEGORY_ID
import com.tokopedia.tokopedianow.annotation.domain.query.GetAllAnnotationPageQuery.PARAM_PAGE_LAST_ID
import com.tokopedia.tokopedianow.annotation.domain.query.GetAllAnnotationPageQuery.PARAM_PAGE_SOURCE
import com.tokopedia.tokopedianow.annotation.domain.query.GetAllAnnotationPageQuery.PARAM_WAREHOUSES
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper.mapToWarehouses
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetAllAnnotationPageUseCase @Inject constructor(
    private val addressData: TokoNowLocalAddress,
    graphqlRepository: GraphqlRepository
) {

    private val graphql by lazy { GraphqlUseCase<TokoNowGetAnnotationListResponse>(graphqlRepository) }

    suspend fun execute(
        categoryId: String,
        annotationType: AnnotationType,
        pageLastId: String
    ): TokoNowGetAnnotationListResponse.GetAnnotationListResponse {
        graphql.apply {
            val requestParams = RequestParams().apply {
                putString(PARAM_CATEGORY_ID, categoryId)
                putString(PARAM_WAREHOUSES, mapToWarehouses(addressData.getAddressData()))
                putString(PARAM_ANNOTATION_TYPE, annotationType.name)
                putString(PARAM_PAGE_LAST_ID, pageLastId)
                putString(PARAM_PAGE_SOURCE, AnnotationPageSource.ALL_ANNOTATION.name)
            }.parameters

            setGraphqlQuery(GetAllAnnotationPageQuery)
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
