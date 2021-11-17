package com.tokopedia.cmhomewidget.domain.usecase

import com.tokopedia.cmhomewidget.constants.GQL_GET_HTDW_DATA
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetDataResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository

import javax.inject.Inject

@GqlQuery("GetHTDWData", GQL_GET_HTDW_DATA)
class CMHomeWidgetDataUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<CMHomeWidgetDataResponse>(graphqlRepository) {


    fun getPayLaterProductDetails(
        onSuccess: (CMHomeWidgetDataResponse) -> Unit,
        onError: (Throwable) -> Unit,
        amount: Long
    ) {
        try {
            this.setTypeClass(CMHomeWidgetDataResponse::class.java)
            this.setGraphqlQuery(GetHTDWData.GQL_QUERY)
            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }
}