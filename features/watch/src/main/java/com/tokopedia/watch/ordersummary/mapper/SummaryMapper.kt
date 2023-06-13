package com.tokopedia.watch.ordersummary.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.watch.ordersummary.model.SummaryDataModel
import rx.functions.Func1

class SummaryMapper: Func1<GraphqlResponse?, SummaryDataModel> {
    override fun call(graphqlResponse: GraphqlResponse?): SummaryDataModel {
        graphqlResponse ?: return SummaryDataModel()

        return SummaryDataModel(
            graphqlResponse.getData<SummaryDataModel>(SummaryDataModel::class.java).data
        )
    }
}