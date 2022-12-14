package com.tokopedia.watch.orderlist.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.watch.orderlist.model.OrderListModel
import rx.functions.Func1

class OrderListMapper: Func1<GraphqlResponse?, OrderListModel> {
    override fun call(graphqlResponse: GraphqlResponse?): OrderListModel {
        graphqlResponse ?: return OrderListModel()

        return OrderListModel(
            graphqlResponse.getData<OrderListModel>(OrderListModel::class.java).orderList
        )
    }
}