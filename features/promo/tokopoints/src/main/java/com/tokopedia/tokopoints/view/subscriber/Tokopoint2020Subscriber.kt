package com.tokopedia.tokopoints.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopoints.notification.model.TokoPointDetailEntity
import com.tokopedia.tokopoints.view.contract.TokoPointsHomeContract
import rx.Subscriber

class Tokopoint2020Subscriber(val view: TokoPointsHomeContract.View) : Subscriber<GraphqlResponse>() {

    override fun onError(e: Throwable?) {
    }

    override fun onCompleted() {

    }

    override fun onNext(graphqlResponse: GraphqlResponse) {

        val tokoPointDetailEntity = graphqlResponse.getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java)
        tokoPointDetailEntity?.let {
            view.showTokopoint2020(it.tokoPoints.popupNotif)
        }
    }
}