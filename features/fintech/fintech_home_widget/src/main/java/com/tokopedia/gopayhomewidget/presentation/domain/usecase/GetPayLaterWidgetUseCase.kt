package com.tokopedia.gopayhomewidget.presentation.domain.usecase

import com.tokopedia.gopayhomewidget.presentation.domain.data.GetPayLaterWidgetDataGqlResponse
import com.tokopedia.gopayhomewidget.presentation.domain.query.GQL_QUERY_PAYLATER_WIDGET_DATA
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("GetPaylaterHomeWidgetData", GQL_QUERY_PAYLATER_WIDGET_DATA)
class GetPayLaterWidgetUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<GetPayLaterWidgetDataGqlResponse>(graphqlRepository) {

    fun getPayLaterWidgetData(
        onSuccess: (GetPayLaterWidgetDataGqlResponse) -> Unit,
        onError: (Throwable) -> Unit,
        isForceRefresh: Boolean
    ) {
        try {
            this.setTypeClass(GetPayLaterWidgetDataGqlResponse::class.java)
            this.setGraphqlQuery(GetPaylaterHomeWidgetData())
            this.execute(
                { result ->

                }, { error ->
                    onError(error)
                })
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }
}