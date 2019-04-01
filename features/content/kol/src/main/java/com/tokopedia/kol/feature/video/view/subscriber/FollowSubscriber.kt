package com.tokopedia.kol.feature.video.view.subscriber

import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kol.feature.post.data.pojo.FollowKolQuery
import com.tokopedia.kol.feature.video.view.listener.VideoDetailContract
import com.tokopedia.network.constant.ErrorNetMessage
import rx.Subscriber


/**
 * @author by milhamj on 10/10/18.
 */
class FollowSubscriber(private val view: VideoDetailContract.View) : Subscriber<GraphqlResponse>() {

    companion object {
        const val FOLLOW_SUCCESS = 1
    }

    override fun onCompleted() {

    }

    override fun onError(throwable: Throwable?) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            throwable?.printStackTrace()
        }
        view.onErrorFollowKol(
                ErrorHandler.getErrorMessage(view.getContext(), throwable)
        )
    }

    override fun onNext(response: GraphqlResponse) {
        val query: FollowKolQuery? = response.getData(FollowKolQuery::class.java)

        if (query == null) {
            onError(RuntimeException())
            return
        }
        if (!TextUtils.isEmpty(query.data.error)) {
            view.onErrorFollowKol(query.data.error)
            return
        }

        val isSuccess = query.data.data.status == FOLLOW_SUCCESS
        if (isSuccess) {
            view.onSuccessFollowKol()
        } else {
            view.onErrorFollowKol(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
        }
    }
}