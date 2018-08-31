package com.tokopedia.talk.reporttalk.view

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.talk.inboxtalk.listener.InboxTalkContract
import com.tokopedia.talk.inboxtalk.viewmodel.InboxTalkItemViewModel
import com.tokopedia.talk.reporttalk.view.listener.ReportTalkContract
import javax.inject.Inject

/**
 * @author by nisie on 8/30/18.
 */
class ReportTalkPresenter @Inject constructor()
    : BaseDaggerPresenter<ReportTalkContract.View>(),
        ReportTalkContract.Presenter {

    override fun detachView() {
        super.detachView()
    }

}