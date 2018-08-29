package com.tokopedia.talk.inboxtalk.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by nisie on 8/29/18.
 */

interface InboxTalkContract {

    interface View : CustomerView {

    }

    interface Presenter : CustomerPresenter<View> {

    }
}