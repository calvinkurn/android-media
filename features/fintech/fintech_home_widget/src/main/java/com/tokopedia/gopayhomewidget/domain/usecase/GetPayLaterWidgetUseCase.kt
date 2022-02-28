package com.tokopedia.gopayhomewidget.domain.usecase

import com.tokopedia.gopayhomewidget.domain.data.GetPayLaterWidgetDataGqlResponse
import com.tokopedia.gopayhomewidget.domain.data.PayLaterWidgetData
import com.tokopedia.gopayhomewidget.domain.query.GQL_QUERY_PAYLATER_WIDGET_DATA
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("GetPaylaterHomeWidgetData", GQL_QUERY_PAYLATER_WIDGET_DATA)
class GetPayLaterWidgetUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
        GraphqlUseCase<GetPayLaterWidgetDataGqlResponse>(graphqlRepository) {

    fun getPayLaterWidgetData(
            onSuccess: (PayLaterWidgetData) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        try {
            this.setTypeClass(GetPayLaterWidgetDataGqlResponse::class.java)
            //todo rohan waiting for native back URL from Product
            this.setRequestParams(getRequestParam(""))
            this.setGraphqlQuery(GetPaylaterHomeWidgetData())
            this.execute(
                    { result ->
                        onSuccess(result.payLaterWidgetData)
                    }, { error ->
                onError(error)
            })
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }


    private fun getRequestParam(backUrl: String): MutableMap<String, Any?> {
        return mutableMapOf(
                REQUEST_PARAMS to mutableMapOf(
                        BACK_URL to backUrl
                )
        )
    }

    companion object {
        const val REQUEST_PARAMS = "request"
        const val BACK_URL = "back_url"
    }

}