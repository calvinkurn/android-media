package com.tokopedia.talk_old.reporttalk.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.talk_old.reporttalk.view.viewmodel.TalkReportOptionViewModel

/**
 * @author by nisie on 8/30/18.
 */
interface ReportTalkContract {

    interface View : CustomerView {
        fun showLoadingFull()
        fun hideLoadingFull()
        fun onErrorReportTalk(errorMessage: String)
        fun getContext(): Context?
        fun onSuccessReportTalk()


    }

    interface Presenter : CustomerPresenter<View> {
        fun reportTalk(talkId: String, shopId: String, productId: String, otherReason: String,
                       selectedOption: TalkReportOptionViewModel)

        fun reportCommentTalk(talkId: String, shopId: String, productId: String,
                              commentId: String, otherReason: String,
                              selectedOption: TalkReportOptionViewModel)

    }
}