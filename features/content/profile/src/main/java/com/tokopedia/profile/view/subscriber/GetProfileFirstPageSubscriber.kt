package com.tokopedia.profile.view.subscriber

import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.viewmodel.ProfileFirstPageViewModel
import rx.Subscriber

/**
 * @author by milhamj on 9/21/18.
 */
class GetProfileFirstPageSubscriber(val view: ProfileContract.View)
    : Subscriber<ProfileFirstPageViewModel>() {
    override fun onNext(t: ProfileFirstPageViewModel) {
        view.onSuccessGetProfileFirstPage(t,"")
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