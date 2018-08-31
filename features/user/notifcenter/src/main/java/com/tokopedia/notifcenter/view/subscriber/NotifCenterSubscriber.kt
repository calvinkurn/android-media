package com.tokopedia.notifcenter.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.notifcenter.view.NotifCenterContract
import rx.Subscriber

/**
 * @author by milhamj on 31/08/18.
 */
class NotifCenterSubscriber(view: NotifCenterContract.View) : Subscriber<GraphqlResponse>() {

    override fun onError(e: Throwable?) {

    }

    override fun onCompleted() {

    }

    override fun onNext(t: GraphqlResponse?) {

    }
}