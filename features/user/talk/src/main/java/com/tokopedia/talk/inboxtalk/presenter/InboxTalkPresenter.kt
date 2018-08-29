package com.tokopedia.talk.inboxtalk.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.talk.inboxtalk.listener.InboxTalkContract

/**
 * @author by nisie on 8/29/18.
 */
class InboxTalkPresenter : BaseDaggerPresenter<InboxTalkContract.View>(),
        InboxTalkContract.Presenter {

}