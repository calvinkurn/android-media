package com.tokopedia.profile.view.subscriber

import com.tokopedia.config.GlobalConfig
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.profile.view.listener.ProfileContract
import rx.Subscriber

/**
 * @author by milhamj on 10/15/18.
 */
class GetProfilePostSubscriber(private val view: ProfileContract.View)
    : Subscriber<DynamicFeedDomainModel>() {

    override fun onNext(t: DynamicFeedDomainModel?) {
        if (t == null) {
            view.showGetListError(RuntimeException())
            return
        }
        view.onSuccessGetProfilePost(t.postList, t.cursor)
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e?.printStackTrace()
        }
        view.showGetListError(e)
    }
}