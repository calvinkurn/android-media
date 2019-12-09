package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.GqlRecentViewResponse
import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import com.tokopedia.graphql.data.model.GraphqlResponse

import rx.Subscriber

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

class GetRecentViewSubscriber(private val view: ICartListView?,
                              private val presenter: ICartListPresenter) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.setHasTriedToLoadRecentView()
        view?.stopAllCartPerformanceTrace()
    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        if (view != null) {
            if (graphqlResponse?.getData<Any>(GqlRecentViewResponse::class.java) != null) {
                val gqlRecentViewResponse = graphqlResponse.getData<GqlRecentViewResponse>(GqlRecentViewResponse::class.java)
                if (gqlRecentViewResponse.gqlRecentView?.recentViewList?.size ?: 0 > 0) {
                    view.renderRecentView(gqlRecentViewResponse.gqlRecentView?.recentViewList)
                }
            }
            view.setHasTriedToLoadRecentView()
            view.stopAllCartPerformanceTrace()
        }
    }

}
