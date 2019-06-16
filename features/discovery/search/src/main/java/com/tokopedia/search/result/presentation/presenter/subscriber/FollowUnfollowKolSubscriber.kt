package com.tokopedia.search.result.presentation.presenter.subscriber

import com.tokopedia.kolcommon.model.FollowResponseModel
import com.tokopedia.search.result.presentation.view.listener.FollowActionListener
import rx.Subscriber

class FollowUnfollowKolSubscriber(
    private val adapterPosition: Int,
    private val followStatus: Boolean,
    private val followActionListener: FollowActionListener
) : Subscriber<FollowResponseModel>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
        followActionListener.onErrorToggleFollow()
    }

    override fun onNext(followResponseModel: FollowResponseModel?) {
        if(followResponseModel == null) {
            followActionListener.onErrorToggleFollow()
            return
        }

        if (followResponseModel.isSuccess) {
            followActionListener.onSuccessToggleFollow(adapterPosition, (!followStatus))
        } else {
            followActionListener.onErrorToggleFollow(followResponseModel.errorMessage ?: "")
        }
    }
}