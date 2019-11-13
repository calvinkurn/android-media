package com.tokopedia.kol.feature.video.view.subscriber

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.kolcommon.util.GraphqlErrorHandler
import com.tokopedia.kol.feature.video.view.listener.VideoDetailContract
import rx.Subscriber

/**
 * @author by yfsx on 26/03/19.
 */
class VideoDetailSubscriber constructor(private val view : VideoDetailContract.View)
    : Subscriber<DynamicFeedDomainModel>() {

    override fun onNext(t: DynamicFeedDomainModel?) {
        if (t == null) {
            view.onErrorGetVideoDetail(RuntimeException().toString())
        }
        view.onSuccessGetVideoDetail(t!!.postList)
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        e?.let {
            view.onErrorGetVideoDetail(
                    GraphqlErrorHandler.getErrorMessage(view.androidContext, it))
        }
    }
}