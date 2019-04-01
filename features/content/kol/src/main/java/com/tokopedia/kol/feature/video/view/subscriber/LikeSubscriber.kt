package com.tokopedia.kol.feature.video.view.subscriber

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage
import com.tokopedia.kol.common.network.GraphqlErrorHandler
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import rx.Subscriber

/**
 * @author by yfsx on 26/03/19.
 */
class LikeSubscriber constructor(
        private val view: KolPostListener.View.Like?,
        private val rowNumber: Int)
    : Subscriber<Boolean>() {


    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.onLikeKolError(
                GraphqlErrorHandler.getErrorMessage(view.context, e)
        )
    }

    override fun onNext(isSuccess: Boolean?) {
        if (view != null) {
            if (isSuccess!!) {
                view.onLikeKolSuccess(rowNumber, 0)
            } else {
                view.onLikeKolError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
            }
        }
    }
}