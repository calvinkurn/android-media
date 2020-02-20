package com.tokopedia.kol.feature.video.view.subscriber

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage
import com.tokopedia.kolcommon.util.GraphqlErrorHandler
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import rx.Subscriber

/**
 * @author by yfsx on 26/03/19.
 */
class LikeSubscriber constructor(
        private val view: KolPostLikeListener?,
        private val rowNumber: Int,
        private val action: LikeKolPostUseCase.LikeKolPostAction)
    : Subscriber<Boolean>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.onLikeKolError(
                GraphqlErrorHandler.getErrorMessage(view.androidContext, e)
        )
    }

    override fun onNext(isSuccess: Boolean?) {
        if (view != null) {
            if (isSuccess!!) {
                view.onLikeKolSuccess(rowNumber, action)
            } else {
                view.onLikeKolError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
            }
        }
    }
}