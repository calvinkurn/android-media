package com.tokopedia.tokopedianow.annotation.domain.usecase

import com.tokopedia.abstraction.common.data.model.response.Header
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse
import com.tokopedia.tokopedianow.annotation.domain.query.GetAllAnnotationPageQuery
import com.tokopedia.tokopedianow.annotation.domain.query.GetAllAnnotationPageQuery.PARAM_ANNOTATION_TYPE
import com.tokopedia.tokopedianow.annotation.domain.query.GetAllAnnotationPageQuery.PARAM_CATEGORY_ID
import com.tokopedia.tokopedianow.annotation.domain.query.GetAllAnnotationPageQuery.PARAM_PAGE_SOURCE
import com.tokopedia.tokopedianow.annotation.domain.query.GetAllAnnotationPageQuery.PARAM_WAREHOUSE_IDS
import com.tokopedia.tokopedianow.annotation.domain.query.GetAllAnnotationPageQuery.VALUE_PAGE_SOURCE
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetAllAnnotationPageUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) {

    private val graphql by lazy { GraphqlUseCase<TokoNowGetAnnotationListResponse>(graphqlRepository) }

    suspend fun execute(
        categoryId: String,
        warehouseIds: String,
        annotationType: String
    ): TokoNowGetAnnotationListResponse.GetAnnotationListResponse {
        graphql.apply {
            val requestParams = RequestParams().apply {
                putString(PARAM_CATEGORY_ID, categoryId)
                putString(PARAM_WAREHOUSE_IDS, warehouseIds)
                putString(PARAM_ANNOTATION_TYPE, annotationType)
                putString(PARAM_PAGE_SOURCE, VALUE_PAGE_SOURCE)
            }.parameters

            setGraphqlQuery(GetAllAnnotationPageQuery)
            setTypeClass(TokoNowGetAnnotationListResponse::class.java)
            setRequestParams(requestParams)
        }
//        val response = graphql.executeOnBackground().response
//        val messages = response.header.messages

//        return if (messages.isNotEmpty()) {
//            throw MessageErrorException(messages.first())
//        } else {
//            response
//        }
        return TokoNowGetAnnotationListResponse.GetAnnotationListResponse(
            header =  Header(

            ),
            annotationHeader = TokoNowGetAnnotationListResponse.AnnotationHeaderResponse(
                title = "Semua brand mana"
            ),
            annotationList = listOf(
                TokoNowGetAnnotationListResponse.AnnotationListResponse(
                    annotationID = "1234",
                    name = "Biore",
                    imageURL = "https://images.tokopedia.net/img/android/res/singleDpi/catalog_entry_point_background.png",
                    appLink = "tokopedia://discovery/now-annotation?category_id=123&annotation_id=11"
                ),
                TokoNowGetAnnotationListResponse.AnnotationListResponse(
                    annotationID = "1234",
                    name = "Pantene",
                    imageURL = "https://images.tokopedia.net/img/android/res/singleDpi/tradein_exchange_advantage_3.png",
                    appLink = "tokopedia://discovery/now-annotation?category_id=123&annotation_id=11"
                ),
                TokoNowGetAnnotationListResponse.AnnotationListResponse(
                    annotationID = "1234",
                    name = "Lux",
                    imageURL = "https://images.tokopedia.net/img/android/res/singleDpi/catalog_library_logo.png",
                    appLink = "tokopedia://discovery/now-annotation?category_id=123&annotation_id=11"
                ),
                TokoNowGetAnnotationListResponse.AnnotationListResponse(
                    annotationID = "1234",
                    name = "Dethol",
                    imageURL = "https://images.tokopedia.net/img/PYbRbC/2021/1/28/e6a29f96-c463-4960-91ce-90ddf8678567.png",
                    appLink = "tokopedia://discovery/now-annotation?category_id=123&annotation_id=11"
                ),
                TokoNowGetAnnotationListResponse.AnnotationListResponse(
                    annotationID = "1234",
                    name = "Biore",
                    imageURL = "https://images.tokopedia.net/img/android/res/singleDpi/catalog_entry_point_background.png",
                    appLink = "tokopedia://discovery/now-annotation?category_id=123&annotation_id=11"
                ),
                TokoNowGetAnnotationListResponse.AnnotationListResponse(
                    annotationID = "1234",
                    name = "Pantene",
                    imageURL = "https://images.tokopedia.net/img/android/res/singleDpi/tradein_exchange_advantage_3.png",
                    appLink = "tokopedia://discovery/now-annotation?category_id=123&annotation_id=11"
                ),
                TokoNowGetAnnotationListResponse.AnnotationListResponse(
                    annotationID = "1234",
                    name = "Lux",
                    imageURL = "https://images.tokopedia.net/img/android/res/singleDpi/catalog_library_logo.png",
                    appLink = "tokopedia://discovery/now-annotation?category_id=123&annotation_id=11"
                ),
                TokoNowGetAnnotationListResponse.AnnotationListResponse(
                    annotationID = "1234",
                    name = "Dethol",
                    imageURL = "https://images.tokopedia.net/img/PYbRbC/2021/1/28/e6a29f96-c463-4960-91ce-90ddf8678567.png",
                    appLink = "tokopedia://discovery/now-annotation?category_id=123&annotation_id=11"
                )
            )
        )
    }
}
