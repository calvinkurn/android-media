package com.tokopedia.interestpick.view.subscriber

import android.text.TextUtils
import com.tokopedia.config.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.interestpick.data.pojo.FeedInterestUserUpdate
import com.tokopedia.interestpick.data.pojo.UpdateInterestData
import com.tokopedia.interestpick.view.listener.InterestPickContract
import rx.Subscriber

/**
 * @author by milhamj on 10/09/18.
 */
class UpdateInterestSubscriber(val view: InterestPickContract.View): Subscriber<GraphqlResponse>() {
    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e?.printStackTrace()
        }
        view.hideLoading()
        view.onErrorUpdateInterest(ErrorHandler.getErrorMessage(view.getContext(), e))
    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        view.hideLoading()
        graphqlResponse?.let {
            val updateInterestData: UpdateInterestData = it.getData(UpdateInterestData::class.java)
            updateInterestData.feedInterestUserUpdate
        }?.let {
            if (!TextUtils.isEmpty(it.error)) {
                view.onErrorUpdateInterest(it.error)
                return
            } else if (!it.success) {
                RuntimeException()
            }

            view.onSuccessUpdateInterest()
        }
    }
}