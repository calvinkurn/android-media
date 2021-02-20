package com.tokopedia.profile.view.subscriber

import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.profile.view.listener.ProfileContract
import rx.Subscriber


/**
 * @author by milhamj on 10/10/18.
 */
class FollowSubscriber(private val view: ProfileContract.View) : Subscriber<GraphqlResponse>() {

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
                ErrorHandler.getErrorMessage(view.context, throwable)
        )
    }

    override fun onNext(response: GraphqlResponse) {
        val query: FollowKolQuery? = response.getData(FollowKolQuery::class.java)

        if (query == null) {
            onError(RuntimeException())
            return
        }
        if (query.data.error.isNotEmpty()) {
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