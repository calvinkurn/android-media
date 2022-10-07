package com.tokopedia.watch.ordersummary.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.watch.ordersummary.model.DataKeyParameterModel
import com.tokopedia.watch.ordersummary.model.SummaryDataModel
import rx.Observable
import rx.functions.Func1

class GetSummaryUseCase(
    private val graphqlUseCase: GraphqlUseCase,
    private val summaryDataModelMapper: Func1<GraphqlResponse?, SummaryDataModel>
): UseCase<SummaryDataModel>() {
    override fun createObservable(p0: RequestParams?): Observable<SummaryDataModel> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(mutableListOf(
            createSummaryDataGqlRequest()
        ))

        val gqlOrderSummaryObservable = graphqlUseCase
            .createObservable(RequestParams.EMPTY)
            .map(summaryDataModelMapper)

        return gqlOrderSummaryObservable
    }

    @GqlQuery("SummaryCardWidgetDataQuery", SUMMARY_CARD_WIDGET_DATA_QUERY)
    private fun createSummaryDataGqlRequest(): GraphqlRequest {
        return GraphqlRequest(
            SummaryCardWidgetDataQuery.GQL_QUERY,
            SummaryDataModel::class.java,
            createParams().parameters
        )
    }

    companion object {
        private const val PARAM_KEY = "dataKeys"
        private const val SUMMARY_CARD_WIDGET_DATA_QUERY = """
            query SummaryCardWidgetData(${'$'}dataKeys : [dataKey!]!) {
              fetchCardWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  value
                  state
                  description
                  descriptionSecondary
                  error
                  errorMsg
                  showWidget
                }
              }
            }
        """

        fun createParams(): RequestParams {
            val input = listOf(
                DataKeyParameterModel(key = DataKeyParameterModel.KEY_NEW_ORDER),
                DataKeyParameterModel(key = DataKeyParameterModel.KEY_READY_TO_SHIP),
                DataKeyParameterModel(key = DataKeyParameterModel.KEY_UNREAD_CHAT),
            )
            return RequestParams.create().apply {
                putObject(PARAM_KEY, input)
            }
        }
    }
}