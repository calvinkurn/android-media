package com.tokopedia.groupchat.room.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.groupchat.room.view.viewmodel.VideoStreamViewModel

/**
 * @author : Steven 19/06/19
 */
interface PlayActivityContract {

    interface View: CustomerView {

    }

    interface Presenter: CustomerPresenter<View> {
    }
}