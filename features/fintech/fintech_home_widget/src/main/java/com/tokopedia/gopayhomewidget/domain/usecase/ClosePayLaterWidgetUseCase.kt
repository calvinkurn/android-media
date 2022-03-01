package com.tokopedia.gopayhomewidget.domain.usecase

import com.tokopedia.gopayhomewidget.domain.data.PayLaterCloseSuccessResponse
import com.tokopedia.gopayhomewidget.domain.data.PayLaterHomeWidgetCloseResponse
import com.tokopedia.gopayhomewidget.domain.query.GQL_QUERY_PAYLATER_WIDGET_CLOSE
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("ClosePaylaterHomeWidgetData", GQL_QUERY_PAYLATER_WIDGET_CLOSE)
class ClosePayLaterWidgetUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
        GraphqlUseCase<PayLaterHomeWidgetCloseResponse>(graphqlRepository) {

    fun getPayLaterWidgetCloseData(
            onSuccess: (PayLaterCloseSuccessResponse) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        try {
            this.setTypeClass(PayLaterHomeWidgetCloseResponse::class.java)
            this.setGraphqlQuery(ClosePaylaterHomeWidgetData())
            this.execute(
                    { result ->
                        onSuccess(result.paylaterCloseSuccessData)
                    }, { error ->
                onError(error)
            })
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }


}