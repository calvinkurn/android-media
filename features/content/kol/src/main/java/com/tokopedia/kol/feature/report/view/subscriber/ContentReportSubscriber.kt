package com.tokopedia.kol.feature.report.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kol.common.util.debugTrace
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract
import rx.Subscriber

/**
 * @author by milhamj on 15/11/18.
 */
class ContentReportSubscriber(val view: ContentReportContract.View)
     : Subscriber<GraphqlResponse>() {
    override fun onNext(t: GraphqlResponse?) {
        view.hideLoading()
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        e?.debugTrace()
        view.hideLoading()
    }
}